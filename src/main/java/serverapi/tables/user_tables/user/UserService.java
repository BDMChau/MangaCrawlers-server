package serverapi.tables.user_tables.user;


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
import serverapi.query.dtos.features.SearchCriteriaDTO;
import serverapi.query.dtos.tables.*;
import serverapi.query.repository.manga.*;
import serverapi.query.repository.user.*;
import serverapi.query.specification.Specificationn;
import serverapi.sharing_services.CloudinaryUploader;
import serverapi.tables.manga_tables.author.Author;
import serverapi.tables.manga_tables.chapter.Chapter;
import serverapi.tables.manga_tables.genre.Genre;
import serverapi.tables.manga_tables.image_chapter.ImageChapter;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.manga_tables.manga_comment_images.CommentImages;
import serverapi.tables.manga_tables.manga_comment_relations.CommentRelations;
import serverapi.tables.manga_tables.manga_comments.MangaComments;
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
    @Autowired
    CacheService cacheService;


    @Autowired
    public UserService(MangaRepos mangaRepository, FollowingRepos followingRepos, UserRepos userRepos,
                       ReadingHistoryRepos readingHistoryRepos, ChapterRepos chapterRepos,
                       MangaCommentsRepos mangaCommentsRepos, RatingMangaRepos ratingMangaRepos,
                       TransGroupRepos transGroupRepos, GenreRepos genreRepos, MangaGenreRepos mangaGenreRepos,
                       AuthorRepos authorRepos, ImgChapterRepos imgChapterRepos, CommentRelationRepos commentRelationRepos, CommentImageRepos commentImageRepos) {
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
        Calendar updatetime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
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
                readingHistory.setReading_history_time(updatetime);

                readingHistoryRepos.save(readingHistory);

            }
        });

        if (atomicBoolean.get() == true) {
            Map<String, Object> msg = Map.of(
                    "msg", "Update readinghistory successfully!"

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
        readingHistory.setReading_history_time(updatetime);

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


    public ResponseEntity addCommentManga(Long toUserID, Long userID, Long mangaID, Long chapterID,
                                          String content, String level, MultipartFile imageUrl,
                                          Long parentID) throws IOException {

        /**
         * Check variable
         */
        Calendar timeUpdated = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Optional<Chapter> chapterOptional = chapterRepos.findById(chapterID);
        Optional<Manga> mangaOptional = mangaRepository.findById(mangaID);
        Optional<User> userOptional = userRepos.findById(userID);
        Optional<User> toUserOptional = userRepos.findById(toUserID);

        Chapter chapter = null;
        if (!chapterOptional.isEmpty()) {
            chapter = chapterOptional.get();
        }

        if (mangaOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "Empty manga!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }
        Manga manga = mangaOptional.get();


        if (userOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "Empty user!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }
        User user = userOptional.get();

        User toUser = user;
        if (!toUserOptional.isEmpty()) {
            toUser = toUserOptional.get();
        }

        if (level == null || level.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "level cannot be null!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }
        /*---------------------------------------------------------------------------------------------------------*/
        /* Add comment */
        MangaComments mangaComments = new MangaComments();

        mangaComments.setManga(manga);
        mangaComments.setChapter(chapter); // if chapter null >> set null
        mangaComments.setUser(user);
        mangaComments.setTo_user(toUser);
        mangaComments.setManga_comment_content(content);
        mangaComments.setManga_comment_time(timeUpdated);
        mangaCommentsRepos.saveAndFlush(mangaComments);

        if (parentID == 0L) {
            parentID = mangaComments.getManga_comment_id();
        }
        Optional<MangaComments> parentOptional = mangaCommentsRepos.findById(parentID);
        MangaComments parent = parentOptional.get();

        CommentRelations commentRelations = new CommentRelations();

        commentRelations.setChild_id(mangaComments);
        commentRelations.setParent_id(parent);
        commentRelations.setLevel(level);
        commentRelationRepos.saveAndFlush(commentRelations);

        String image_url = null;
        if (imageUrl != null) {

            CloudinaryUploader cloudinaryUploader = new CloudinaryUploader();
            Map cloudinaryResponse = cloudinaryUploader.uploadImg(
                    imageUrl.getBytes(),
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
         * Response
         */
        MangaCommentDTOs exportComment = new MangaCommentDTOs();

        exportComment.setTo_user_id(toUser.getUser_id());
        exportComment.setTo_user_name(toUser.getUser_name());
        exportComment.setTo_user_avatar(toUser.getUser_avatar());

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
                "msg", "add comment successfully!",
                "comment_information", exportComment
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.CREATED, msg).toJSON(), HttpStatus.CREATED);
    }

    public ResponseEntity updateComment(Long userID, Long toUserID, Long commentID, String commentContent, MultipartFile imageUrl) {
        /**
         * Declare variable
         */
        Optional<User> userOptional = userRepos.findById(userID);
        Optional<MangaComments> mangaCommentsOptional = mangaCommentsRepos.findById(commentID);
        Optional<User> toUserOptional = userRepos.findById(toUserID);

        if (userOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "user cannot be null!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }

        if (mangaCommentsOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "comment cannot be null!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }
        MangaComments mangaComments = mangaCommentsOptional.get();

        User toUser = null;
        if (!toUserOptional.isEmpty()) {
            toUser = toUserOptional.get();
        }


        /**
         * Update comment
         */

        // if tagging someone
        if (toUser != null) {
            mangaComments.setTo_user(toUser);
        }

        Optional<CommentImages> commentImagesOptional = commentImageRepos.getCommentImagesByManga_comment(commentID);
        String sub_imageUrl = "";
        if (!commentImagesOptional.isEmpty()) {
            CommentImages commentImages = commentImagesOptional.get();
            sub_imageUrl = commentImages.getImage_url();
        }


        // if content is "", update comment will become delete comment
        if (commentContent.equals("")) {
            if (imageUrl.equals("")) {
                mangaComments.setIs_deprecated(true);
                mangaCommentsRepos.save(mangaComments);

                Map<String, Object> msg = Map.of(
                        "msg", "delete comment successfully!"
                );
                return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
            }
        }
        if (imageUrl != null) {
            if (!imageUrl.equals(sub_imageUrl) && !imageUrl.equals("")) {
                CloudinaryUploader cloudinaryUploader = new CloudinaryUploader();
                try {
                    Map cloudinaryResponse = cloudinaryUploader.deleteImg(sub_imageUrl);
                } catch (IOException e) {
                    System.err.println("error line 471" + e);
                    e.printStackTrace();
                }

            }
            mangaCommentsRepos.delete(mangaComments);
            Map<String, Object> msg = Map.of(
                    "msg", "delete comment successfully!"
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }
        System.err.println("line 459");
        // check to import images or delete images
//        if(imageUrl != null){
//
//            CloudinaryUploader cloudinaryUploader = new CloudinaryUploader();
//            Map cloudinaryResponse = cloudinaryUploader.uploadImg(
//                    imageUrl.getBytes(),
//                    manga.getManga_name(),
//                    "user_comment_images",
//                    false
//
//            );
//            String securedUrl = (String) cloudinaryResponse.get("secure_url");
//
//            CommentImages commentImages = new CommentImages();
//
//            commentImages.setImage_url(securedUrl);
//            commentImages.setManga_comment(mangaComments);
//            commentImageRepos.saveAndFlush(commentImages);
//            image_url = securedUrl;
//        }

        mangaComments.setManga_comment_content(commentContent);
        mangaCommentsRepos.save(mangaComments);

        Map<String, Object> msg = Map.of(
                "msg", "update comment successfully!",
                "comment_information", mangaComments
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity deleteComment(Long userID, Long commentID) {

        /**
         * Declare variable
         */
        Optional<User> userOptional = userRepos.findById(userID);
        Optional<MangaComments> mangaCommentsOptional = mangaCommentsRepos.findById(commentID);

        if (userOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "user cannot be null!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }

        if (mangaCommentsOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "comment cannot be null!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }
        MangaComments mangaComments = mangaCommentsOptional.get();
        /**
         * Update comment
         */
        mangaCommentsRepos.delete(mangaComments);

        Map<String, Object> msg = Map.of(
                "msg", "delete comment successfully!"
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

        CloudinaryUploader cloudinaryUploader = new CloudinaryUploader();

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
            Map responseFromCloudinary = new CloudinaryUploader().deleteImg(publicIdAvatar);
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


    public ResponseEntity searchUsers(String valToSearch){
        // search by email
        Specificationn specificationn = new Specificationn(new SearchCriteriaDTO("user_email", ":", valToSearch));
        Specificationn.SearchingUsers searchingUsers = specificationn.new SearchingUsers();

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
            Long mangaId,
            String chapterName,
            @RequestParam(required = false) MultipartFile[] files
    ) throws IOException, ParseException {
        Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
        if (mangaOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Manga not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        Manga manga = mangaOptional.get();

        Chapter chapter = new Chapter();
        chapter.setChapter_name(chapterName);
        chapter.setCreated_at(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
        chapter.setManga(manga);
        chapterRepos.saveAndFlush(chapter);


        String folderName = manga.getManga_name() + "/" + manga.getManga_name() + "_" + chapterName;
        for (MultipartFile file : files) {
            Map responseFromCloudinary = new CloudinaryUploader().uploadImg(file.getBytes(),
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
//            imageChapter.setcreated_at(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
            imageChapter.setImgchapter_public_id_cloudinary(publicId);
            imageChapter.setChapter(chapter);
            imgChapterRepos.saveAndFlush(imageChapter);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas successfully!"
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity signUpTransGroup(Long userId, String groupName, String groupDesc) {
        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "User is not exist!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();


        Optional<TransGroup> transGroupOptional = transGroupRepos.findByEmail(user.getUser_email());
        if (transGroupOptional.isPresent()) {
            Map<String, Object> err = Map.of("err", "Group is existed!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(),
                    HttpStatus.ACCEPTED);
        }

        TransGroup transGroup = new TransGroup();
        transGroup.setTransgroup_name(groupName);
        transGroup.setTransgroup_email(user.getUser_email());
        transGroup.setCreated_at(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
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

        List<MangaChapterDTO> mangaList = new HelpingUser(mangaRepository, chapterRepos).getMangaList(transGroupId);
        System.err.println("mangaList");
        System.err.println(mangaList);
        List<UserTransGroupDTO> listUsers = userRepos.getUsersTransGroup(transGroupId);


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
            Map<String, Object> err = Map.of("err", "You are not allowed to access this resource!");
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
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

        transGroupRepos.save(transGroup);

        Map<String, Object> msg = Map.of("msg", "Delete transgroup successfully!");
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
        manga.setCreated_at(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
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


        CloudinaryUploader cloudinaryUploader = new CloudinaryUploader();
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


}

