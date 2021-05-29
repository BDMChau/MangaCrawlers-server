package serverapi.Tables.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Query.DTO.*;
import serverapi.Query.Repository.*;
import serverapi.SharedServices.CloudinaryUploader;
import serverapi.StaticFiles.UserAvatarCollection;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.FollowingManga.FollowingManga;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.Manga.POJO.RatingPOJO;
import serverapi.Tables.RatingManga.RatingManga;
import serverapi.Tables.ReadingHistory.ReadingHistory;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public UserService(MangaRepos mangaRepository, FollowingRepos followingRepos, UserRepos userRepos, ReadingHistoryRepos readingHistoryRepos, ChapterRepos chapterRepos, ChapterCommentsRepos chapterCommentsRepos, RatingMangaRepos ratingMangaRepos) {
        this.mangaRepository = mangaRepository;
        this.followingRepos = followingRepos;
        this.userRepos = userRepos;
        this.readingHistoryRepos = readingHistoryRepos;
        this.chapterRepos = chapterRepos;
        this.chapterCommentsRepos = chapterCommentsRepos;
        this.ratingMangaRepos = ratingMangaRepos;
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


    public ResponseEntity getChapterComments(Long userId) {

        List<ChapterCommentsDTO> chapterCommentsDTOList = chapterCommentsRepos.getCommentsByUserId(userId);

        if (chapterCommentsDTOList.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "No comment found!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get chapter comment successfully!",
                "mangas", chapterCommentsDTOList,
                "user_id", userId

        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
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


    public ResponseEntity deleteUser(Long userId) {
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


    public ResponseEntity deprecateUser(Long userId) {
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


    public ResponseEntity DeleteFollowsUsersByUserId(Long userId) {
        List<FollowingDTO> followingDTOList = followingRepos.findByUserId(userId);
        Optional<User> userOptional = userRepos.findById(userId);

        if (userOptional.isEmpty()) {

            Map<String, Object> msg = Map.of(
                    "msg", "user not found!"

            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        if (followingDTOList.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "No Follows!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        User user = userOptional.get();
        followingRepos.deleteAllFollowByUserId(user);

        Map<String, Object> msg = Map.of(
                "msg", "delete all user's follows successfully!",
                "user deleted info: ", user
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity DeleteHitoriesUsersByUserId(Long userId) {

        List<UserReadingHistoryDTO> userReadingHistoryDTOList = readingHistoryRepos.GetHistoriesByUserId(userId);
        Optional<User> userOptional = userRepos.findById(userId);

        if (userOptional.isEmpty()) {

            Map<String, Object> msg = Map.of(
                    "msg", "user not found!"

            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        if (userReadingHistoryDTOList.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "No history found!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        User user = userOptional.get();
        readingHistoryRepos.deleteAllHistoryByUserId(user);

        Map<String, Object> msg = Map.of(
                "msg", "delete all user's histories successfully!",
                "user deleted info: ", user
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity DeleteCommentsUsersByUserId(Long userId) {

        List<ChapterCommentsDTO> chapterCommentsDTOList = chapterCommentsRepos.getCommentsByUserId(userId);
        Optional<User> userOptional = userRepos.findById(userId);

        if (userOptional.isEmpty()) {

            Map<String, Object> msg = Map.of(
                    "msg", "user not found!"

            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);

        }
        if (chapterCommentsDTOList.isEmpty()) {

            Map<String, Object> msg = Map.of("msg", "No comment found!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        User user = userOptional.get();
        chapterCommentsRepos.deleteAllCommentsByUserId(user);

        Map<String, Object> msg = Map.of(
                "msg", "delete all user's comments successfully!",
                "user deleted info: ", user
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

    public ResponseEntity getAllMangas(Long userId) {
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

        List<Manga> mangas = mangaRepository.findAll();
        if (mangas.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "Empty mangas!",
                    "mangas", mangas
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }
        mangas.forEach(manga ->{
            manga.getAuthor().getAuthor_name();
            System.out.println(manga.getChapters().size());
        });


        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas successfully!",
                "users", mangas
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

    public ResponseEntity ratingManga(Long userId, Long mangaId,Float value, RatingPOJO ratingPOJO){

        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "User not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
        if (mangaOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "No mangas!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }

        List<RatingMangaDTO> ratingMangaDTOS = ratingMangaRepos.ratingManga(userId);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        ratingMangaDTOS.forEach(item->{
            if(item.getManga_id().equals(mangaId)){

                atomicBoolean.set(true);

                Long ratingMangaId = item.getRatingmanga_id();


                Optional<RatingManga> ratingMangaOptional = ratingMangaRepos.findById(ratingMangaId);
                RatingManga ratingManga = ratingMangaOptional.get();

                User user = userOptional.get();
                Manga manga = mangaOptional.get();

                System.out.println("aaaaaaaaaa"+value);
                ratingManga.setValue(value);
                System.out.println("shshshshs:"+value);
                ratingManga.setManga(manga);

                ratingMangaRepos.save(ratingManga);
            }

        });
        if (atomicBoolean.get() == true) {
            Map<String, Object> msg = Map.of(
                    "msg", "Update ratingmanga successfully!"

            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

        }

        Manga manga = mangaOptional.get();
        System.out.println("ra cai gi:"+manga);

        User user = userOptional.get();

        RatingManga rating = new RatingManga();

        rating.setManga(manga);

        rating.setUser(user);
        rating.setValue(value);

      ratingMangaRepos.save(rating);

        Map<String, Object> msg = Map.of(
                "msg", "Rating manga successfully"
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }

    public ResponseEntity averageStar(){
        List<AverageStarDTO> ratingMangas = ratingMangaRepos.avgRatingManga();

        ratingMangas.forEach(item->{
            Long mangaId = item.getManga_id();

           float stars =  (float)item.getStar();
           int starDiv = (int) (stars/1);
            System.out.println("stardiv"+starDiv);
           float starMod = stars % 1;
            System.out.println("starmod"+starMod);

            if(starMod > 0 && starMod < 0.5){
                starMod = 0;
            }
            else if(starMod > 0.5 && starMod <= 1){
                starMod = 1;
            }
            else if(starMod == 0.5){

                starMod =0.5F;
            }

           float roundoff = starDiv + starMod;

           Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
            Manga manga = mangaOptional.get();
            manga.setStars(roundoff);


            mangaRepository.saveAndFlush(manga);


        });
        Map<String, Object> msg = Map.of(
                "msg", "Average stars manga successfully"
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);

    }



}

