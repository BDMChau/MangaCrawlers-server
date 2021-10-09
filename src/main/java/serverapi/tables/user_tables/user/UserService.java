package serverapi.tables.user_tables.user;


import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import serverapi.api.Response;
import serverapi.configuration.cache.CacheService;
import serverapi.helpers.RoundNumber;
import serverapi.helpers.UserAvatarCollection;
import serverapi.query.dtos.features.MangaCommentDTOs.CommentTagsDTO;
import serverapi.query.dtos.features.MangaCommentDTOs.CommentTreesDTO;
import serverapi.query.dtos.features.MangaCommentDTOs.MangaCommentDTOs;
import serverapi.query.dtos.features.SearchCriteriaDTO;
import serverapi.query.dtos.tables.FieldsCreateMangaDTO;
import serverapi.query.dtos.tables.FollowingDTO;
import serverapi.query.dtos.tables.MangaChapterDTO;
import serverapi.query.dtos.tables.UserReadingHistoryDTO;
import serverapi.query.repository.manga.*;
import serverapi.query.repository.manga.comment.*;
import serverapi.query.repository.user.*;
import serverapi.query.specification.Specificationn;
import serverapi.sharing_services.CloudinaryUploader;
import serverapi.tables.manga_tables.author.Author;
import serverapi.tables.manga_tables.chapter.Chapter;
import serverapi.tables.manga_tables.genre.Genre;
import serverapi.tables.manga_tables.image_chapter.ImageChapter;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.manga_tables.manga_comment.manga_comment_images.CommentImages;
import serverapi.tables.manga_tables.manga_comment.manga_comment_relations.CommentRelations;
import serverapi.tables.manga_tables.manga_comment.manga_comment_tags.CommentTags;
import serverapi.tables.manga_tables.manga_comment.manga_comments.MangaComments;
import serverapi.tables.manga_tables.manga_genre.MangaGenre;
import serverapi.tables.manga_tables.rating_manga.RatingManga;
import serverapi.tables.user_tables.following_manga.FollowingManga;
import serverapi.tables.user_tables.reading_history.ReadingHistory;
import serverapi.tables.user_tables.trans_group.TransGroup;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class UserService {
    private Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    private final MangaRepos mangaRepository;
    private final FollowingRepos followingRepos;
    private final UserRepos userRepos;
    private final ReadingHistoryRepos readingHistoryRepos;
    private final ChapterRepos chapterRepos;
    private final MangaCommentsRepos mangaCommentsRepos;
    private final RatingMangaRepos ratingMangaRepos;
    private final TransGroupRepos transGroupRepos;
    private final GenreRepos genreRepos;
    private final MangaGenreRepos mangaGenreRepos;
    private final AuthorRepos authorRepos;
    private final ImgChapterRepos imgChapterRepos;
    private final CommentRelationRepos commentRelationRepos;
    private final CommentImageRepos commentImageRepos;
    private final CommentTagsRepos commentTagsRepos;
    private final CommentLikesRepos commentLikesRepos;

    @Autowired
    CacheService cacheService;

    CloudinaryUploader cloudinaryUploader = CloudinaryUploader.getInstance();

    @Autowired
    public UserService(MangaRepos mangaRepository, FollowingRepos followingRepos, UserRepos userRepos,
                       ReadingHistoryRepos readingHistoryRepos, ChapterRepos chapterRepos,
                       MangaCommentsRepos mangaCommentsRepos, RatingMangaRepos ratingMangaRepos,
                       TransGroupRepos transGroupRepos, GenreRepos genreRepos, MangaGenreRepos mangaGenreRepos,
                       AuthorRepos authorRepos, ImgChapterRepos imgChapterRepos, CommentRelationRepos commentRelationRepos,
                       CommentImageRepos commentImageRepos, CommentTagsRepos commentTagsRepos, CommentLikesRepos commentLikesRepos) {
        this.mangaRepository = mangaRepository;
        this.followingRepos = followingRepos;
        this.userRepos = userRepos;
        this.readingHistoryRepos = readingHistoryRepos;
        this.chapterRepos = chapterRepos;
        this.mangaCommentsRepos = mangaCommentsRepos;
        this.ratingMangaRepos = ratingMangaRepos;
        this.transGroupRepos = transGroupRepos;
        this.genreRepos = genreRepos;
        this.mangaGenreRepos = mangaGenreRepos;
        this.authorRepos = authorRepos;
        this.imgChapterRepos = imgChapterRepos;
        this.commentRelationRepos = commentRelationRepos;
        this.commentImageRepos = commentImageRepos;
        this.commentTagsRepos = commentTagsRepos;
        this.commentLikesRepos = commentLikesRepos;
    }


    private boolean isLeader(User user, TransGroup transGroup) {
        if (!user.getTransgroup().equals(transGroup)) {
            return false;
        } else if (!user.getUser_email().equals(transGroup.getTransgroup_email())) {
            return false;
        }

        return true;
    }

    //////////////////////////////////// User parts ///////////////////////////////////////////
    public ResponseEntity GetReadingHistory(Long userId) {
        List<UserReadingHistoryDTO> readingHistoryDTO = readingHistoryRepos.GetHistoriesByUserId(userId);
        readingHistoryDTO.sort(Comparator.comparing(UserReadingHistoryDTO::getReading_History_time).reversed());


        Map<String, Object> msg = Map.of(
                "msg", "Get reading history mangas successfully!",
                "mangas", readingHistoryDTO
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity updateReadingHistory(Long userId, Long mangaId, Long chapterId) {

        List<UserReadingHistoryDTO> readingHistoryDTO = readingHistoryRepos.GetHistoriesByUserId(userId);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        readingHistoryDTO.forEach(item -> {
            if (item.getManga_id().equals(mangaId)) {
                atomicBoolean.set(true);

                Long readingHistoryId = item.getReadingHistory_id();

                Optional<ReadingHistory> readingHistoryOptional = readingHistoryRepos.findById(readingHistoryId);
                ReadingHistory readingHistory = readingHistoryOptional.get();


                Optional<Chapter> chapterOptional = chapterRepos.findById(chapterId);
                Chapter chapter = chapterOptional.get();


                Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
                Manga manga = mangaOptional.get();

                readingHistory.setChapter(chapter);
                readingHistory.setReading_history_time(currentTime);

                readingHistoryRepos.save(readingHistory);

            }
        });

        if (atomicBoolean.get() == true) {
            Map<String, Object> msg = Map.of(
                    "msg", "Update reading history successfully!"

            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

        }

        Optional<Chapter> chapterOptional = chapterRepos.findById(chapterId);
        Chapter chapter = chapterOptional.get();

        Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
        Manga manga = mangaOptional.get();

        Optional<User> userOptional = userRepos.findById(userId);
        User user = userOptional.get();

        ReadingHistory readingHistory = new ReadingHistory();

        readingHistory.setUser(user);
        readingHistory.setManga(manga);
        readingHistory.setChapter(chapter);
        readingHistory.setReading_history_time(currentTime);

        readingHistoryRepos.save(readingHistory);

        Map<String, Object> msg = Map.of(
                "msg", "Add reading history successfully!"

        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity getFollowingMangas(Long UserId) {

        List<FollowingDTO> followingDTOList = followingRepos.findByUserId(UserId);

        if (followingDTOList.isEmpty()) {

            Map<String, Object> msg = Map.of("msg", "No manga follow found!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        Map<String, Object> msg = Map.of(
                "msg", "Get following mangas successfully!",
                "mangas", followingDTOList,
                "user_id", UserId

        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity addFollowManga(Long mangaId, Long userId) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        List<FollowingDTO> follows = followingRepos.findByUserId(userId);

        if (follows.isEmpty()) {
            Optional<User> userOptional = userRepos.findById(userId);
            User user = userOptional.get();

            Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
            Manga manga = mangaOptional.get();

            FollowingManga followingManga = new FollowingManga();
            followingManga.setUser(user);
            followingManga.setManga(manga);

            followingRepos.save(followingManga);

            Map<String, Object> msg = Map.of(
                    "msg", "add Follow successfully!"

            );
            return new ResponseEntity<>(new Response(201, HttpStatus.CREATED, msg).toJSON(), HttpStatus.CREATED);

        } else {
            follows.forEach(item -> {
                if (item.getManga_id().equals(mangaId)) {

                    atomicBoolean.set(true);
                }
            });
            if (atomicBoolean.get() == true) {
                Map<String, Object> err = Map.of("err", "Cannot ADD to database!");
                return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                        HttpStatus.BAD_REQUEST);

            } else {
                Optional<User> userOptional = userRepos.findById(userId);
                User user = userOptional.get();

                Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
                Manga manga = mangaOptional.get();

                FollowingManga followingManga = new FollowingManga();
                followingManga.setUser(user);
                followingManga.setManga(manga);

                followingRepos.save(followingManga);

                Map<String, Object> msg = Map.of(
                        "msg", "add follow successfully!"
                );
                return new ResponseEntity<>(new Response(201, HttpStatus.CREATED, msg).toJSON(), HttpStatus.CREATED);

            }

        }
    }

    public ResponseEntity deleteFollowManga(Long mangaId, Long userId) {
        List<FollowingDTO> Follow = followingRepos.findByUserId(userId);
        System.out.println("mangaID" + mangaId);
        System.out.println("userID" + userId);

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        if (Follow.isEmpty()) {
            Map<String, Object> err = Map.of("err", "No following manga to delete!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);

        } else {

            Follow.forEach(item -> {
                System.out.println(item.getManga_id());
                System.out.println(item.getManga_id().equals(mangaId));
                if (item.getManga_id().equals(mangaId)) {
                    Long followId = item.getFollowId();

                    System.out.println("follow id: " + followId);
                    followingRepos.deleteById(followId);
                    atomicBoolean.set(true);
                }
            });
            if (atomicBoolean.get() == true) {
                Map<String, Object> msg = Map.of(
                        "msg", "Delete following manga successfully!"
                );
                return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
            }

            Map<String, Object> err = Map.of("err", "Cannot delete");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }
    }

    /////////////////////////////////////// comment /////////////////////////////////

    public ResponseEntity addCommentManga(List<Long> toUsersID, Long userID, Long mangaID, Long chapterID,
                                          String content, MultipartFile image,
                                          Long parentID) throws IOException {

        /**
         * Check variable
         */
        Calendar timeUpdated = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Optional<Chapter> chapterOptional = chapterRepos.findById(chapterID);
        Optional<Manga> mangaOptional = mangaRepository.findById(mangaID);
        Optional<User> userOptional = userRepos.findById(userID);


        Chapter chapter = null;
        if (!chapterOptional.isEmpty()) {
            chapter = chapterOptional.get();
        }

        if (mangaOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "Manga not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        Manga manga = mangaOptional.get();


        if (userOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "User not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();
        User toUser = user;
        if (!toUsersID.isEmpty()) {
            Optional<User> toUserOptional = userRepos.findById(toUsersID.get(0));

            if (!toUserOptional.isEmpty()) {
                toUser = toUserOptional.get();
            }

        } else {
            toUsersID.add(toUser.getUser_id());
        }

        /*---------------------------------------------------------------------------------------------------------*/
        /* Add comment */
        MangaComments mangaComments = new MangaComments();

        mangaComments.setManga(manga);
        mangaComments.setChapter(chapter); // if chapter null >> set null
        mangaComments.setUser(user);
        mangaComments.setManga_comment_content(content);
        mangaComments.setManga_comment_time(timeUpdated);
        mangaComments.setIs_deprecated(false);
        mangaCommentsRepos.saveAndFlush(mangaComments);

        /**
         * Add tags
         */
        // Declare off_set
        int offSet = 0;
        List<CommentTagsDTO> commentTags = new ArrayList<>();
        for (int i = 0; i < toUsersID.size(); i++) {

            Optional<User> userTagOptional = userRepos.findById(toUsersID.get(i));

            if (!userTagOptional.isEmpty()) {

                User userTag = userTagOptional.get();

                CommentTags commentTag = new CommentTags();

                commentTag.setManga_comment(mangaComments);
                commentTag.setUser(userTag);
                commentTag.setOff_set(offSet);

                commentTagsRepos.saveAndFlush(commentTag);

                // for export
                CommentTagsDTO commentTagsDTO = new CommentTagsDTO();

                commentTagsDTO.setManga_comment_id(mangaComments.getManga_comment_id());
                commentTagsDTO.setManga_comment_tag_id(commentTag.getManga_comment_tag_id());

                commentTagsDTO.setUser_id(commentTag.getUser().getUser_id());
                commentTagsDTO.setUser_avatar(commentTag.getUser().getUser_avatar());
                commentTagsDTO.setUser_name(commentTag.getUser().getUser_name());

                commentTags.add(commentTagsDTO);

                offSet++;
            }
        }
        /**
         *  Add comment images
         */

        String image_url = null;
        System.err.println(image != null);
        if (!image.isEmpty()) {

            Map cloudinaryResponse = cloudinaryUploader.uploadImg(
                    image.getBytes(),
                    manga.getManga_name(),
                    "user_comment_images",
                    false

            );
            String securedUrl = (String) cloudinaryResponse.get("secure_url");

            CommentImages commentImages = new CommentImages();

            commentImages.setImage_url(securedUrl);
            commentImages.setManga_comment(mangaComments);
            commentImageRepos.saveAndFlush(commentImages);
            image_url = securedUrl;
        }

        /**
         * Add manga_comment_relations
         */

        if (parentID == 0L) {

            parentID = mangaComments.getManga_comment_id();
        }

        Optional<MangaComments> parentOptional = mangaCommentsRepos.findById(parentID);
        MangaComments parent = parentOptional.get();

        /**
         * Checking level
         */
        String level = "0";

        if (mangaComments.getManga_comment_id().equals(parent.getManga_comment_id())) {

            level = "0";
        } else {

            if (toUser.getUser_id().equals(parent.getUser().getUser_id())) {

                level = "1";
            } else {

                level = "2";
            }
        }

        System.err.println("level " + level);
        CommentRelations commentRelations = new CommentRelations();

        commentRelations.setChild_id(mangaComments);
        commentRelations.setParent_id(parent);
        commentRelations.setLevel(level);
        commentRelationRepos.save(commentRelations);

        /**
         * Response
         */
        MangaCommentDTOs exportComment = new MangaCommentDTOs();

        exportComment.setTo_users(commentTags);

        exportComment.setUser_id(user.getUser_id());
        exportComment.setUser_name(user.getUser_name());
        exportComment.setUser_avatar(user.getUser_avatar());

        exportComment.setManga_id(manga.getManga_id());

        if (chapter != null) {

            exportComment.setChapter_id(chapter.getChapter_id());
            exportComment.setChapter_name(chapter.getChapter_name());
            exportComment.setCreated_at(chapter.getCreated_at());
        }

        exportComment.setManga_comment_id(mangaComments.getManga_comment_id());
        exportComment.setManga_comment_time(mangaComments.getManga_comment_time());
        exportComment.setManga_comment_content(mangaComments.getManga_comment_content());

        exportComment.setParent_id(parent.getManga_comment_id());

        exportComment.setImage_url(image_url);

        Map<String, Object> msg = Map.of(
                "msg", "Add comment successfully!",
                "comment_information", exportComment
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.CREATED, msg).toJSON(), HttpStatus.CREATED);
    }

    public ResponseEntity updateComment(Long userID, List<Long> toUsersID, Long commentID, String commentContent, MultipartFile imageUrl) throws IOException {

        /**
         * Initialize variable
         */
        Optional<User> userOptional = userRepos.findById(userID);
        Optional<MangaComments> mangaCommentsOptional = mangaCommentsRepos.findById(commentID);

        if (userOptional.isEmpty() || mangaCommentsOptional.isEmpty()) {

            Map<String, Object> msg = Map.of("msg", "User or comment not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        MangaComments mangaComments = mangaCommentsOptional.get();
        User user = userOptional.get();

        // Get current image in this comment
        Optional<CommentImages> commentImagesOptional = commentImageRepos.getCommentImagesByManga_comment(commentID);

        String currentImageUrl = "";
        CommentImages currentCommentImages = null;
        if (!commentImagesOptional.isEmpty()) {

            currentImageUrl = commentImagesOptional.get().getImage_url();
            currentCommentImages = commentImagesOptional.get();
        }

        /**
         * if commentContent && imagesUrl = ""
         * update comment will become delete comment by set isDeprecated = true;
         */
        if (commentContent.equals("") && imageUrl.isEmpty()) {

            mangaComments.setIs_deprecated(true);
            mangaCommentsRepos.save(mangaComments);

            Map<String, Object> msg = Map.of(
                    "msg", "Delete comment successfully!"
            );

            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }

        //Check input tags
        List<User> toUsersInput = new ArrayList<>();
        for (int i = 0; i < toUsersID.size(); i++) {

            Optional<User> userTagOptional = userRepos.findById(toUsersID.get(i));

            if (!userTagOptional.isEmpty()) {
                // Add user tags into list toUsersInput
                User userTag = userTagOptional.get();
                toUsersInput.add(userTag);

            } else {
                Map<String, Object> msg = Map.of(
                        "msg", "Invalid user tag!"
                );
                return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
            }
        }

        // Check offSet is equal position's to_userID input
        List<CommentTags> currentTags;

        // Export tags
        List<CommentTagsDTO> listExportTags = new ArrayList<>();
        if (!toUsersInput.isEmpty()) {

            currentTags = commentTagsRepos.getListCommentTags(commentID);

            if (!currentTags.isEmpty()) {

                // Check size between currenTags and inputTags
                int length = 0;
                if (toUsersInput.size() > currentTags.size()) {

                    // Get length
                    length = toUsersInput.size() - currentTags.size();
                }

                // Chang currentTags into inputTags
                for (CommentTags currentTag : currentTags) {

                    for (int i = 0; i < toUsersInput.size(); i++) {

                        if (currentTag.getOff_set() == i) {

                            if (!currentTag.getUser().getUser_id().equals(toUsersInput.get(i).getUser_id())) {

                                currentTag.setUser(toUsersInput.get(i));
                                commentTagsRepos.saveAndFlush(currentTag);

                                listExportTags = exportTags(listExportTags, mangaComments, currentTag);
                                break;
                            }
                        }
                    }
                }
                // Add new tag if currentTags.size < inputTags.size
                if (length > 0) {

                    // Add new tags
                    int offSet = 0;

                    for (int i = length; i < toUsersInput.size(); i++) {

                        CommentTags commentTag = new CommentTags();

                        commentTag.setManga_comment(mangaComments);
                        commentTag.setUser(toUsersInput.get(i));
                        commentTag.setOff_set(offSet);

                        commentTagsRepos.saveAndFlush(commentTag);

                        // for export
                        listExportTags = exportTags(listExportTags, mangaComments, commentTag);

                        offSet++;
                    }
                }
            }
        }

        // Check imagesUrl and set if it not as same as sub_imagesUrl
        Optional<Manga> mangaOptional = mangaRepository.findById(mangaComments.getManga().getManga_id());
        Manga manga = null;
        String exportUrl = "";
        if (!mangaOptional.isEmpty()) {
            manga = mangaOptional.get();
        }

        if (!imageUrl.isEmpty()) {
            if (!imageUrl.equals(currentImageUrl)) {
                Map cloudinaryResponse = cloudinaryUploader.uploadImg(
                        imageUrl.getBytes(),
                        manga.getManga_name(),
                        "user_comment_images",
                        false
                );

                // Get cloudinary url
                String securedUrl = (String) cloudinaryResponse.get("secure_url");

                // For comment export
                exportUrl = securedUrl;

                // If current comment is exist, change current imageUrl into inputUrl( cloudinaryUrl)
                if (currentCommentImages != null) {

                    currentCommentImages.setImage_url(securedUrl);
                    commentImageRepos.saveAndFlush(currentCommentImages);

                } else {

                    CommentImages image = new CommentImages();

                    image.setImage_url(securedUrl);
                    commentImageRepos.saveAndFlush(image);
                }
            }
        }

        mangaComments.setManga_comment_content(commentContent);
        mangaCommentsRepos.saveAndFlush(mangaComments);

        // Response
        MangaCommentDTOs exportComment = new MangaCommentDTOs();

        exportComment.setTo_users(listExportTags);

        exportComment.setUser_id(user.getUser_id());
        exportComment.setUser_name(user.getUser_name());
        exportComment.setUser_avatar(user.getUser_avatar());

        exportComment.setManga_id(manga.getManga_id());

        if (mangaComments.getChapter() != null) {

            Optional<Chapter> chapterOptional = chapterRepos.findById(mangaComments.getChapter().getChapter_id());

            Chapter chapter = !chapterOptional.isEmpty() ? chapterOptional.get() : null;

            exportComment.setChapter_id(chapter.getChapter_id());
            exportComment.setChapter_name(chapter.getChapter_name());
            exportComment.setCreated_at(chapter.getCreated_at());
        }
        exportComment.setManga_comment_id(mangaComments.getManga_comment_id());
        exportComment.setManga_comment_time(mangaComments.getManga_comment_time());
        exportComment.setManga_comment_content(mangaComments.getManga_comment_content());

        exportComment.setImage_url(exportUrl);

        Map<String, Object> msg = Map.of(
                "msg", "Update comment successfully!",
                "comment_information", exportComment
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity deleteComment(Long userID, Long commentID, List<MangaCommentDTOs> comments) {
        System.err.println(comments);
        /**
         * Declare variable
         */
        Optional<User> userOptional = userRepos.findById(userID);
        Optional<MangaComments> mangaCommentsOptional = mangaCommentsRepos.findById(commentID);

        if (userOptional.isEmpty() || mangaCommentsOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "Empty user or comment!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();

        // Check if user is not the owner
        MangaComments mangaComments = mangaCommentsOptional.get();
        if(!mangaComments.getUser().equals(user)){

            Map<String, Object> msg = Map.of("msg", "Don't have permission!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        // Set deprecate this comment
        mangaComments.setIs_deprecated(true);
        mangaCommentsRepos.saveAndFlush(mangaComments);

        // Set flag
        Boolean isDeleted = false;

        // Get cmtsToRemove
        List<MangaCommentDTOs> cmtsToRes = comments;

        for (int i = 0; i < comments.size(); i++) {

            if (isDeleted.equals(false)) {

                Long cmt00Id = comments.get(i).getManga_comment_id();
            
                if (cmt00Id.equals(commentID)) {

                    cmtsToRes.remove(i);
                    break;

                } else {
                    for (int j = 0; j < comments.get(i).getComments_level_01().size(); j++) {

                        if (isDeleted.equals(false)) {

                            Long cmt01Id = comments.get(i).getComments_level_01().get(j).getManga_comment_id();


                            if (cmt01Id.equals(commentID)) {


                                cmtsToRes.get(i).getComments_level_01().remove(j);
                                isDeleted = true;
                                break;

                            } else {

                                List<CommentTreesDTO> cmtsLv02 = comments.get(i).getComments_level_01().get(j).getComments_level_02();

                                for (int k = 0; k < cmtsLv02.size(); k++) {

                                    Long cmt02Id = comments.get(i).getComments_level_01().get(j).getComments_level_02().get(k).getManga_comment_id();

                                    if (cmt02Id.equals(commentID)) {

                                        cmtsToRes.get(i).getComments_level_01().get(j).getComments_level_02().remove(k);
                                        isDeleted = true;

                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        Map<String, Object> msg = Map.of(
                "msg", "Delete comment successfully!",
                "comments", cmtsToRes
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity ratingManga(Long userId, Long mangaId, Float newValue) {
        Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
        Manga manga = mangaOptional.get();


        Optional<User> userOptional = userRepos.findById(userId);
        User user = userOptional.get();

        float averageResult = 0;
        AtomicReference<Float> total = new AtomicReference<>((float) 0);
        float roundedResult = 0;


        Long ratingMangaId = null;
        List<RatingManga> ratingMangaList = ratingMangaRepos.findAllByMangaId(mangaId);
        for (RatingManga ratingManga : ratingMangaList) {
            if (ratingManga.getUser().getUser_id().equals(userId)) {
                ratingMangaId = ratingManga.getRatingmanga_id();
                break;
            }
        }

        if (ratingMangaId == null) {
            ratingMangaList.forEach(oneRatingManga -> {
                total.updateAndGet(v -> (float) (v + oneRatingManga.getValue()));
            });

            averageResult = (total.get() + newValue) / (ratingMangaList.size() + 1);

            roundedResult = new RoundNumber().roundRatingManga(averageResult);

            manga.setStars(roundedResult);
            mangaRepository.save(manga);

            RatingManga ratingManga = new RatingManga();
            ratingManga.setManga(manga);
            ratingManga.setUser(user);
            ratingManga.setValue(newValue);
            ratingMangaRepos.save(ratingManga);

        } else {
            Optional<RatingManga> existedRatingMangaOptionNal = ratingMangaRepos.findById(ratingMangaId);
            RatingManga existedRatingManga = existedRatingMangaOptionNal.get();

            existedRatingManga.setValue(newValue);
            ratingMangaRepos.saveAndFlush(existedRatingManga);

            List<RatingManga> ratingMangaList02 = ratingMangaRepos.findAllByMangaId(mangaId);
            ratingMangaList02.forEach(ratingManga -> {
                total.updateAndGet(v -> (float) (v + ratingManga.getValue()));
            });


            averageResult = total.get() / ratingMangaList02.size();

            roundedResult = new RoundNumber().roundRatingManga(averageResult);

            manga.setStars(roundedResult);
            mangaRepository.save(manga);
        }

        // evict single cache
        cacheService.evictSingleCacheValue("mangaPage", mangaId.toString());

        Map<String, Object> msg = Map.of(
                "msg", "Rating manga successfully",
                "manga", Map.of("stars", roundedResult)
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }


    public ResponseEntity updateAvatar(String fileName, byte[] fileBytes, Long userId) throws IOException,
            ParseException {
        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "User not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();


        // delete previous avatar on cloudinary
        String publicIdAvatar = user.getAvatar_public_id_cloudinary();
        if (publicIdAvatar != null) {
            cloudinaryUploader.deleteImg(publicIdAvatar);
        }

        // upload new avatar to cloudinary
        Map cloudinaryResponse = cloudinaryUploader.uploadImg(fileBytes, fileName, "users_avatar", false);


        user.setAvatar_public_id_cloudinary((String) cloudinaryResponse.get("public_id"));
        user.setUser_avatar((String) cloudinaryResponse.get("secure_url")); // secure_url is https, url is http

        userRepos.save(user);

        Map<String, Object> msg = Map.of(
                "msg", "Update avatar successfully!",
                "avatar_url", user.getUser_avatar()
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity removeAvatar(Long userId) throws IOException {
        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "User not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();

        String publicIdAvatar = user.getAvatar_public_id_cloudinary();
        if (publicIdAvatar == null) {
            Map<String, Object> err = Map.of("err", "Avatar has already set to default!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(),
                    HttpStatus.ACCEPTED);
        } else {
            Map responseFromCloudinary = cloudinaryUploader.deleteImg(publicIdAvatar);
        }


        UserAvatarCollection userAvatarCollection = new UserAvatarCollection();
        Boolean isAdmin = user.getUser_isAdmin();
        if (Boolean.FALSE.equals(isAdmin)) {
            user.setUser_avatar(userAvatarCollection.getAvatar_member());
        } else {
            user.setUser_avatar(userAvatarCollection.getAvatar_admin());
        }
        user.setAvatar_public_id_cloudinary(null);

        userRepos.save(user);

        Map<String, Object> msg = Map.of(
                "msg", "Remove avatar successfully!",
                "avatar_url", user.getUser_avatar()
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }


    public ResponseEntity searchUsers(String valToSearch, int key) {
        Specificationn.SearchingUsers searchingUsers = null;
        if(key == 1){
            // search by email
            Specificationn specificationn = new Specificationn(new SearchCriteriaDTO("user_email", ":", valToSearch));
            searchingUsers = specificationn.new SearchingUsers();
        } else if(key == 2){
            // search by name
            Specificationn specificationn = new Specificationn(new SearchCriteriaDTO("user_name", ":", valToSearch));
            searchingUsers = specificationn.new SearchingUsers();
        }

        List<User> searchingResults = userRepos.findAll(searchingUsers);

        if (searchingResults.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "No user!",
                    "data", searchingResults
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get search results successfully!",
                "data", searchingResults
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    /////////////// Translation Group parts //////////////
    public ResponseEntity uploadChapterImgs(
            Long userId,
            String strTransGrId,
            Long mangaId,
            String chapterName,
            @RequestParam(required = false) MultipartFile[] files
    ) throws IOException, ParseException {
        cacheService.evictSingleCacheValue("transGroupInfo", userId.toString() + strTransGrId);

        Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
        if (mangaOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Manga not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        Manga manga = mangaOptional.get();

        Chapter chapter = new Chapter();
        chapter.setChapter_name(chapterName);
        chapter.setCreated_at(currentTime);
        chapter.setManga(manga);
        chapterRepos.saveAndFlush(chapter);


        String folderName = manga.getManga_name() + "/" + manga.getManga_name() + "_" + chapterName;
        for (MultipartFile file : files) {
            Map responseFromCloudinary = cloudinaryUploader.uploadImg(file.getBytes(),
                    file.getOriginalFilename(), "/transgroup_upload/" + folderName, true);
            if (responseFromCloudinary.get("name") == "Error") {
                Map<String, Object> err = Map.of(
                        "err", "One or many files have been failed when uploading!",
                        "err2", "Maximum size for one file is 10MB"
                );
                return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                        HttpStatus.BAD_REQUEST);
            }
            System.err.println(responseFromCloudinary);
            String securedUrl = (String) responseFromCloudinary.get("secure_url");
            String publicId = (String) responseFromCloudinary.get("public_id");

            // format date
            // String created_atFromCloudinary = (String) responseFromCloudinary.get("created_at");
            // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
            // Date date = dateFormat.parse(created_atFromCloudinary);
            // Calendar created_at = dateFormat.getCalendar();


            ImageChapter imageChapter = new ImageChapter();
            imageChapter.setImgchapter_url(securedUrl);
            imageChapter.setImgchapter_public_id_cloudinary(publicId);
            imageChapter.setChapter(chapter);
            imgChapterRepos.saveAndFlush(imageChapter);
        }


        Map<String, Object> msg = Map.of("msg", "Get all mangas successfully!");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity acceptToJoinTeam(Long userId, Long transGroupId) {
        User user = userRepos.findById(userId).get();
        TransGroup transGroup = transGroupRepos.findById(transGroupId).get();

        TransGroup isJoinedBefore = user.getTransgroup();
        Hibernate.initialize(isJoinedBefore);
        if (isJoinedBefore != null) {
            System.err.println("Existed a team: " + isJoinedBefore.getTransgroup_id());
            Map<String, Object> err = Map.of("err", "You were in a team!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }

        user.setTransgroup(transGroup);
        userRepos.saveAndFlush(user);

        Map<String, Object> msg = Map.of(
                "msg", "Joine this team OK!",
                "transgroup_id", transGroup.getTransgroup_id()
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity signUpTransGroup(Long userId, String groupName, String groupDesc) {
        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "User is not exist!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();


        Optional<TransGroup> transGroupOptional = transGroupRepos.findByEmail(user.getUser_email());
        if (transGroupOptional.isPresent()) {
            Map<String, Object> err = Map.of("err", "Group is existed!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(),
                    HttpStatus.ACCEPTED);
        }

        TransGroup transGroup = new TransGroup();
        transGroup.setIs_deprecated(false);
        transGroup.setTransgroup_name(groupName);
        transGroup.setTransgroup_email(user.getUser_email());
        transGroup.setCreated_at(currentTime);
        if (groupDesc.isEmpty()) {
            transGroup.setTransgroup_desc("");
        } else {
            transGroup.setTransgroup_desc(groupDesc);
        }
        transGroupRepos.saveAndFlush(transGroup);

        user.setTransgroup(transGroup);
        userRepos.save(user);

        Map<String, Object> msg = Map.of(
                "msg", "Register new translation group successfully",
                "transgroup_id", transGroup.getTransgroup_id()
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity getTransGroupInfo(Long userId, Long transGroupId) {
        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "msg", "User is not exist!"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        Optional<TransGroup> transGroupOptional = transGroupRepos.findById(transGroupId);

        if (transGroupOptional.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "No translation group!"
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }
        TransGroup transGroup = transGroupOptional.get();

        List<User> listUsers = (List<User>) transGroup.getUsers();
        Hibernate.initialize(listUsers);

        List<MangaChapterDTO> mangaList = new HelpingUser(mangaRepository, chapterRepos).getMangaList(transGroupId);


        Map<String, Object> msg = Map.of(
                "msg", "Get translation group info successfully!",
                "trans_group", transGroup,
                "list_manga", mangaList,
                "list_user", listUsers
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity getMangaInfoUploadPage(Long transGroupId, Long mangaId) {
        Optional<TransGroup> transGroupOptional = transGroupRepos.findById(transGroupId);
        if (transGroupOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Trans group not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        TransGroup transGroup = transGroupOptional.get();

        // check exist manga in this trans group
        Boolean isExistdManga = false;
        List<Manga> listManga = (List<Manga>) transGroup.getMangas();
        for (int i = 0; i < listManga.size(); i++) {
            if (listManga.get(i).getManga_id().equals(mangaId)) {
                isExistdManga = true;
                break;
            }
        }

        if (isExistdManga) {
            Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
            Manga manga = mangaOptional.get();
            String authorName = manga.getAuthor().getAuthor_name();
            List<Chapter> listChapter = manga.getChapters();
            listChapter.forEach(chapter -> chapter.getChapter_name());

            Map<String, Object> msg = Map.of(
                    "msg", "Get manga info successfully!",
                    "manga", manga,
                    "chapters", listChapter,
                    "author_name", authorName
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                    HttpStatus.OK);
        }


        Map<String, Object> err = Map.of("err", "Manga not found!");
        return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(),
                HttpStatus.ACCEPTED);
    }


    @Transactional
    public ResponseEntity deleteManga(Long userId, Long mangaId, Long transGroupId) {
        Optional<TransGroup> transGroupOptional = transGroupRepos.findById(transGroupId);
        if (transGroupOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Transgroup not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        TransGroup transGroup = transGroupOptional.get();


        //////
        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "User not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();

        Boolean isleader = isLeader(user, transGroup);
        if (!isleader) {
            Map<String, Object> err = Map.of("err", "You are not allowed to access this resource!");
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        //check manga deleted is empty
        Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
        if (mangaOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "manga not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        Manga manga = mangaOptional.get();

        mangaRepository.delete(manga);

        // get remaining mangaList to return
        List<MangaChapterDTO> mangaList = mangaRepository.getLatestChapterFromManga();
        if (mangaList.isEmpty()) {
            Map<String, Object> err = Map.of("err", "List manga not found!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(),
                    HttpStatus.ACCEPTED);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Delete manga successfully!",
                "manga_id", mangaId
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity removeMember(Long userId, Long memberId) {
        User user = userRepos.findById(userId).get();
        TransGroup transGroup = user.getTransgroup();
        Hibernate.initialize(transGroup);

        if (!user.getUser_email().equals(transGroup.getTransgroup_email())) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allow to do this action!",
                    "err_code", 1
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }

        User member = userRepos.findById(memberId).get();
        TransGroup memberTransGr = member.getTransgroup();
        Hibernate.initialize(memberTransGr);

        if (!transGroup.getTransgroup_id().equals(memberTransGr.getTransgroup_id())) {
            Map<String, Object> err = Map.of("err", "Cannot remove member!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }

        if (member.getUser_email().equals(transGroup.getTransgroup_email())) {
            Map<String, Object> err = Map.of(
                    "err", "You cannot remove a leader!",
                    "err_code", 2
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }

        member.setTransgroup(null);
        userRepos.saveAndFlush(member);


        Map<String, Object> msg = Map.of(
                "msg", "Removed member!",
                "member_id", memberId,
                "member_name", member.getUser_name(),
                "transGroup_id", transGroup.getTransgroup_id()
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity deletetransGroup(Long userId, Long transGroupId) {
        Optional<TransGroup> transGroupOptional = transGroupRepos.findById(transGroupId);
        if (transGroupOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Transgroup not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }
        TransGroup transGroup = transGroupOptional.get();

        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "User not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();


        Boolean isleader = isLeader(user, transGroup);
        if (!isleader) {
            Map<String, Object> err = Map.of("err", "You are not allowed in this action!");
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(), HttpStatus.FORBIDDEN);
        }

        /// delete
        List<User> userList = (List<User>) transGroup.getUsers();
        List<Manga> mangaList = (List<Manga>) transGroup.getMangas();

        // set relational of children to null
        userList.forEach(user1 -> {
            user1.setTransgroup(null);
            userRepos.saveAndFlush(user1);
        });

        mangaList.forEach(manga -> {
            manga.setTransgroup(null);
            mangaRepository.save(manga);
        });

        transGroup.setIs_deprecated(true);
        transGroupRepos.save(transGroup);

        Map<String, Object> msg = Map.of("msg", "Delete trans team successfully!");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity addNewProjectMangaFields(Long userId, Long transGrId,
                                                   FieldsCreateMangaDTO fieldsCreateMangaDTO) throws IOException {
        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "user not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();
        Long transgroupId = user.getTransgroup().getTransgroup_id();


        // translation group
        Optional<TransGroup> transGroupOptional = transGroupRepos.findById(transGrId);
        if (transGroupOptional.isPresent()) {
            TransGroup transGroup = transGroupOptional.get();
            List<Manga> mangaList = (List<Manga>) transGroup.getMangas();

            List<Manga> isExistedManga = mangaList.stream()
                    .filter(manga -> manga.getManga_name().equals(fieldsCreateMangaDTO.getMangaName()))
                    .collect(Collectors.toList());

            if (!isExistedManga.isEmpty()) {
                Map<String, Object> err = Map.of("err", "This manga is existed! With manga's name");
                return new ResponseEntity<>(new Response(202, HttpStatus.OK, err).toJSON(),
                        HttpStatus.OK);
            }
            System.err.println("01");
        }
        TransGroup transGroup = transGroupOptional.get();


        // set author
        Author author = new Author();
        author.setAuthor_name(fieldsCreateMangaDTO.getAuthor());
        authorRepos.saveAndFlush(author);
        System.err.println("02");

        // set manga
        Manga manga = new Manga();
        manga.setManga_name(fieldsCreateMangaDTO.getMangaName());
        manga.setStars(Float.intBitsToFloat(fieldsCreateMangaDTO.getRating()));
        manga.setViews(Long.valueOf(fieldsCreateMangaDTO.getViews()));
        manga.setDate_publications(fieldsCreateMangaDTO.getPublicationYear());
        manga.setStatus(fieldsCreateMangaDTO.getStatus());
        manga.setDescription(fieldsCreateMangaDTO.getDescription());
        manga.setAuthor(author);
        manga.setTransgroup(transGroup);
        manga.setCreated_at(currentTime);
        mangaRepository.saveAndFlush(manga);
        System.err.println("03");

        // set mangaGenre
        List<Long> listGenreId = new ArrayList<>();
        fieldsCreateMangaDTO.getGenres().forEach(genreId -> {
            listGenreId.add(Long.parseLong(genreId));
        });
        List<Genre> genres = genreRepos.findAllById(listGenreId);

        MangaGenre mangaGenre = new MangaGenre();
        genres.forEach(genre -> {
            mangaGenre.setGenre(genre);
        });
        mangaGenre.setManga(manga);
        mangaGenreRepos.saveAndFlush(mangaGenre);
        System.err.println("04");


        Map<String, Object> msg = Map.of(
                "msg", "Added fields, go to next step to add thumbnails image",
                "manga_id", manga.getManga_id()
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }


    public ResponseEntity addNewProjectMangaThumbnail(Long userId, Long transGrId, MultipartFile file, Long mangaId) throws IOException {
        Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
        if (mangaOptional.isEmpty()) {
            return null;
        }
        Manga manga = mangaOptional.get();


        Map cloudinaryResponse = cloudinaryUploader.uploadImg(
                file.getBytes(),
                manga.getManga_name(),
                "transGroup_manga_upload",
                false
        );
        String securedUrl = (String) cloudinaryResponse.get("secure_url");

        manga.setThumbnail(securedUrl);
        mangaRepository.saveAndFlush(manga);

        Map<String, Object> msg = Map.of(
                "msg", "Added manga's thumbnail , add new manga successfully",
                "manga", manga
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);

    }


    ////////////////////////////////helper

    public List<CommentTagsDTO> exportTags(List<CommentTagsDTO> listTags, MangaComments mangaComment, CommentTags commentTag) {

        CommentTagsDTO commentTagsDTO = new CommentTagsDTO();

        commentTagsDTO.setManga_comment_id(mangaComment.getManga_comment_id());
        commentTagsDTO.setManga_comment_tag_id(commentTag.getManga_comment_tag_id());

        commentTagsDTO.setUser_id(commentTag.getUser().getUser_id());
        commentTagsDTO.setUser_avatar(commentTag.getUser().getUser_avatar());
        commentTagsDTO.setUser_name(commentTag.getUser().getUser_name());

        listTags.add(commentTagsDTO);

        return listTags;
    }


}

