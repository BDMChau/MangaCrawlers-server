package serverapi.Tables.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Helpers.RoundNumber;
import serverapi.Query.DTO.AuthorMangaDTO;
import serverapi.Query.DTO.CommentExportDTO;
import serverapi.Query.DTO.FollowingDTO;
import serverapi.Query.DTO.UserReadingHistoryDTO;
import serverapi.Query.Repository.*;
import serverapi.SharedServices.CloudinaryUploader;
import serverapi.StaticFiles.UserAvatarCollection;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.ChapterComments.ChapterComments;
import serverapi.Tables.FollowingManga.FollowingManga;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.RatingManga.RatingManga;
import serverapi.Tables.ReadingHistory.ReadingHistory;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserService {
    private final MangaRepos mangaRepository;
    private final FollowingRepos followingRepos;
    private final UserRepos userRepos;
    private final ReadingHistoryRepos readingHistoryRepos;
    private final ChapterRepos chapterRepos;
    private final ChapterCommentsRepos chapterCommentsRepos;
    private final RatingMangaRepos ratingMangaRepos;

    @Autowired
    CacheManager cacheManager;


    @Autowired
    public UserService(MangaRepos mangaRepository, FollowingRepos followingRepos, UserRepos userRepos,
                       ReadingHistoryRepos readingHistoryRepos, ChapterRepos chapterRepos,
                       ChapterCommentsRepos chapterCommentsRepos, RatingMangaRepos ratingMangaRepos) {
        this.mangaRepository = mangaRepository;
        this.followingRepos = followingRepos;
        this.userRepos = userRepos;
        this.readingHistoryRepos = readingHistoryRepos;
        this.chapterRepos = chapterRepos;
        this.chapterCommentsRepos = chapterCommentsRepos;
        this.ratingMangaRepos = ratingMangaRepos;
    }


    //////////////////////History reading manga parts
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


    ////////////////////// Following manga parts
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


    //////////////////////Comment parts//////////////////////
    public ResponseEntity addCommentChapter(Long chapterId, Long userId, String content) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Calendar timeUpdated = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        Optional<Chapter> chapterOptional = chapterRepos.findById(chapterId);

        //check chapter exit
        if (chapterOptional.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "Empty chapters!"
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }
        Chapter chapter = chapterOptional.get();

        Optional<User> userOptional = userRepos.findById(userId);

        ///check user exit
        if (userOptional.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "Empty user!"
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }
        User user = userOptional.get();

        ChapterComments chapterComments = new ChapterComments();
        chapterComments.setChapter(chapter);
        chapterComments.setUser(user);
        chapterComments.setChaptercmt_content(content);
        chapterComments.setChaptercmt_time(timeUpdated);
        chapterCommentsRepos.save(chapterComments);

        CommentExportDTO commentExportDTO = new CommentExportDTO();

        commentExportDTO.setChapter_id(chapterComments.getChapter().getChapter_id());
        commentExportDTO.setChapter_name(chapterComments.getChapter().getChapter_name());
        commentExportDTO.setCreatedAt(chapterComments.getChapter().getCreatedAt());

        commentExportDTO.setChaptercmt_id(chapterComments.getChaptercmt_id());
        commentExportDTO.setChaptercmt_content(chapterComments.getChaptercmt_content());
        commentExportDTO.setChaptercmt_time(chapterComments.getChaptercmt_time());

        commentExportDTO.setUser_id(chapterComments.getUser().getUser_id());
        commentExportDTO.setUser_email(chapterComments.getUser().getUser_email());
        commentExportDTO.setUser_name(chapterComments.getUser().getUser_name());
        commentExportDTO.setUser_avatar(chapterComments.getUser().getUser_avatar());


        Map<String, Object> msg = Map.of(
                "msg", "add comment successfully!",
                "comment_info", commentExportDTO

        );
        return new ResponseEntity<>(new Response(201, HttpStatus.CREATED, msg).toJSON(), HttpStatus.CREATED);


    }


    ///////////////// Rating manga part
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
                total.updateAndGet(v -> new Float((float) (v + oneRatingManga.getValue())));
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
                total.updateAndGet(v -> new Float((float) (v + ratingManga.getValue())));
            });


            averageResult = total.get() / ratingMangaList02.size();

            roundedResult = new RoundNumber().roundRatingManga(averageResult);

            manga.setStars(roundedResult);
            mangaRepository.save(manga);
        }

        // evict single cache
        cacheManager.getCache("mangaPage").evict(mangaId.toString());
//        new CacheService().evictSingleCacheValue("mangaPage", mangaId.toString());

        Map<String, Object> msg = Map.of(
                "msg", "Rating manga successfully",
                "manga", Map.of("stars", roundedResult)
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }


////////////////////////// Admin parts  /////////////////////////

    //////////// Interact with users
    public ResponseEntity deleteUser(Long userId, Long adminId) {
        Optional<User> adminOptional = userRepos.findById(adminId);
        if (adminOptional.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "Missing creadential to access this resource"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User admin = adminOptional.get();

        Boolean isAdmin = admin.getUser_isAdmin();
        if (Boolean.FALSE.equals(isAdmin)) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allowed to access this resource!"
            );
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }
        //get user info
        Optional<User> userOptional = userRepos.findById(userId);

        if (userOptional.isEmpty()) {

            Map<String, Object> err = Map.of(
                    "err", "User not found!"

            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();
        userRepos.delete(user);

        List<User> users = userRepos.findAll();

        Comparator<User> compareById = (User u1, User u2) -> u1.getUser_id().compareTo(u2.getUser_id());
        Collections.sort(users, compareById); // sort users by id

        Map<String, Object> msg = Map.of(
                "msg", "delete user successfully!",
                "users", users
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }


    public ResponseEntity deprecateUser(Long userId, Long adminId) {
        Optional<User> adminOptional = userRepos.findById(adminId);
        if (adminOptional.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "Missing creadential to access this resource"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User admin = adminOptional.get();

        Boolean isAdmin = admin.getUser_isAdmin();
        if (Boolean.FALSE.equals(isAdmin)) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allowed to access this resource!"
            );
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }
        //get user info
        Optional<User> userOptional = userRepos.findById(userId);

        if (userOptional.isEmpty()) {

            Map<String, Object> err = Map.of(
                    "err", "User not found!"

            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();
        user.setUser_isVerified(false);
        userRepos.save(user);

        List<User> users = userRepos.findAll();

        Comparator<User> compareById = (User u1, User u2) -> u1.getUser_id().compareTo(u2.getUser_id());
        Collections.sort(users, compareById); // sort users by id

        Map<String, Object> msg = Map.of(
                "msg", "Deprecate user successfully!",
                "users", users
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }


    public ResponseEntity getAllUsers(Long userId) {
        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "Missing creadential to access this resource"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();

        Boolean isAdmin = user.getUser_isAdmin();
        if (Boolean.FALSE.equals(isAdmin)) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allowed to access this resource!"
            );
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        List<User> users = userRepos.findAll();
        if (users.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "Empty users!",
                    "users", users
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get all users successfully!",
                "users", users
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
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
        Map cloudinaryResponse = cloudinaryUploader.uploadImg(fileBytes, fileName, "users_avatar");

//        String createdAt = (String) cloudinaryResponse.get("created_at");
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
//        Date date = dateFormat.parse(createdAt);
//        System.out.println(date.getTime());

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


    /////Interact with mangas
    public ResponseEntity getAllMangas(Long userId) {
        Optional<User> userOptional = userRepos.findById(userId);
        User user = userOptional.get();
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "Missing creadential to access this resource"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        Boolean isAdmin = user.getUser_isAdmin();
        if (Boolean.FALSE.equals(isAdmin)) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allowed to access this resource!"
            );
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        List<AuthorMangaDTO> mangas = mangaRepository.getAllMangasInfo();

        if (mangas.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "Empty mangas!",
                    "mangas", mangas
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }
        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas successfully!",
                "mangas", mangas
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


}

