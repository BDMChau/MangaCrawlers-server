package serverapi.tables.user_tables.user.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import serverapi.api.Response;
import serverapi.query.dtos.tables.FieldsCreateMangaDTO;
import serverapi.tables.manga_tables.manga.pojo.MangaPOJO;
import serverapi.tables.manga_tables.manga.pojo.RatingPOJO;
import serverapi.tables.user_tables.user.pojo.TransGroupPOJO;
import serverapi.tables.user_tables.user.pojo.UserPOJO;
import serverapi.utils.UserHelpers;

import javax.servlet.ServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Map;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/api/user")
@CacheConfig(cacheNames = {"user"})
public class UserController {
    private static final String fileNameDefault = "/static/media/8031DF085D7DBABC0F4B3651081CE70ED84622AE9305200F2FC1D789C95CF06F.9960248d.svg";
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserHelpers userHelpers = new UserHelpers();



    ///////////////////////////////// Users parts //////////////////////////////

    @Cacheable(value = "historymangas", key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @GetMapping("/gethistorymanga")
    public ResponseEntity GetReadingHistory(ServletRequest request) {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long user_id = Long.parseLong(StrUserId);

        return userService.GetReadingHistory(user_id);
    }


    // update reading history of user by chapters in a manga
//    @CacheEvict(allEntries = true, value = {"historymangas"})
    @CacheEvict(value = {"historymangas"}, key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @PutMapping("/updatereadinghistory")
    public ResponseEntity updateReadingHistory(@RequestBody UserPOJO userPOJO, ServletRequest request) {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);
        Long mangaId = Long.parseLong(userPOJO.getManga_id());
        Long chapterId = Long.parseLong(userPOJO.getChapter_id());

        return userService.updateReadingHistory(userId, mangaId, chapterId);
    }

    @CacheEvict(value = {"historymangas"}, key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @PostMapping("/remove_history_manga")
    public ResponseEntity removeHistoryManga(ServletRequest request, @RequestBody Map data) {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        Long mangaId = Long.parseLong(String.valueOf(data.get("manga_id")));

        return userService.removeHistoryManga(userId, mangaId);
    }


    @Cacheable(value = "followingmangas", key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @GetMapping("/getfollowingmangas")
    public ResponseEntity getFollowingMangas(ServletRequest request) {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return userService.getFollowingMangas(userId);
    }



    ///Add manga to list user's follows
//    @CacheEvict(allEntries = true, value = {"followingmangas"})
    @CacheEvict(value = {"followingmangas"}, key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @PostMapping("/addfollowingmanga")
    public ResponseEntity addFollowingMangas(@RequestBody MangaPOJO mangaPOJO, ServletRequest request) {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);
        Long mangaId = Long.parseLong(mangaPOJO.getManga_id());

        return userService.addFollowManga(mangaId, userId);
    }


    ///Delete manga following from user's follows( Unfollow)
//    @CacheEvict(allEntries = true, value = {"followswingmangas"})
    @CacheEvict(value = {"followingmangas"}, key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @DeleteMapping("/deletefollowingmanga")
    public ResponseEntity deleteFollowingMangas(@RequestBody MangaPOJO mangaPOJO, ServletRequest request) {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        Long mangaId = Long.parseLong(mangaPOJO.getManga_id());

        return userService.deleteFollowManga(mangaId, userId);
    }

    @PostMapping("/check_following_manga")
    public ResponseEntity checkIsFollowingManga(ServletRequest request, @RequestBody Map data) {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        Long mangaId = Long.parseLong(String.valueOf(data.get("manga_id")));

        return userService.checkIsFollowingManga(userId, mangaId);
    }

    @CacheEvict(value = {"mangapage"}, key = "#ratingPOJO.getManga_id()")
    @PutMapping("/ratingmanga")
    public ResponseEntity ratingManga(@RequestBody RatingPOJO ratingPOJO, ServletRequest request) {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);
        Long mangaId = Long.parseLong(ratingPOJO.getManga_id());
        Float newValue = Float.parseFloat(ratingPOJO.getValue());

        return userService.ratingManga(userId, mangaId, newValue);

    }


    @PutMapping("/updateavatar")
    public ResponseEntity updateAvatar(
            ServletRequest request,
            @RequestParam(required = false) MultipartFile file
    ) throws IOException, ParseException {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        String[] splitedFileName = file.getOriginalFilename().split(Pattern.quote("."));
        String fileName = splitedFileName[0];
        byte[] fileBytes = file.getBytes();


        return userService.updateAvatar(fileName, fileBytes, userId);
    }


    @DeleteMapping("/removeavatar")
    public ResponseEntity removeAvatar(ServletRequest request) throws IOException {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return userService.removeAvatar(userId);
    }


    @GetMapping("/get_friend_requests")
    public ResponseEntity getFriendRequests(ServletRequest request, @RequestParam int from, @RequestParam int amount) throws IOException {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return userService.getFriendRequests(userId, from, amount);
    }



    @PostMapping("/searchusers")
    public ResponseEntity searchUsers(@RequestBody Map data) {
        String valToSearch = (String) data.get("value");
        int key = (int) data.get("key"); // search with: 1 is email, 2 is name

        return userService.searchUsers(valToSearch, key);
    }


    @PutMapping("/update_description")
    public ResponseEntity updateDescription(ServletRequest request, @RequestBody Map data) throws IOException {
        String description = String.valueOf(data.get("user_desc"));
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return userService.updateDescription(userId, description);
    }

    ////////////////////////// Translation Group parts /////////////////////////////
//    @CacheEvict(allEntries = true, value = {"allmangas", "transGroupInfo", "mangaInfoUploadPage"})
//    @CacheEvict(allEntries = true, value = {"allmangas", "transGroupInfo", "mangaInfoUploadPage"})
//    @CacheEvict(value = {"mangaInfoUploadPage"}, key = "{#request.getAttribute(\"user\").get(\"user_id\"), #manga_id}")
    @PostMapping("/uploadchapterimgs")
    public ResponseEntity uploadChapterImgs(
            ServletRequest request,
            @RequestParam(required = false) MultipartFile[] files,
            @RequestParam(required = false) Integer manga_id,
            @RequestParam(required = false) Integer chapter_id,
            @RequestParam(required = false) String chapter_name,
            @RequestParam(required = false) boolean is_create
    ) throws IOException, ParseException {
        String strUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(strUserId);

        String strTransGrId = String.valueOf(userHelpers.getUserAttribute(request).get("user_transgroup_id"));

        for (MultipartFile file : files) {
            System.err.println(file.getOriginalFilename());
        }

        Long mangaId = Long.parseLong(String.valueOf(manga_id));
        Long chapterId = Long.parseLong(String.valueOf(chapter_id));
        String chapterName = chapter_name;
        boolean isCreate = is_create;

        return userService.uploadChapterImgs(userId, strTransGrId, mangaId, chapterId, chapterName, files, isCreate);
    }

    @CacheEvict(allEntries = true, value = {"transGroupInfo"})
    @PostMapping("/accept_to_join_team")
    public ResponseEntity acceptToJoinTeam(ServletRequest request, @RequestBody TransGroupPOJO transGroupPOJO) throws NoSuchAlgorithmException {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);
        Long transGroupId = Long.parseLong(transGroupPOJO.getTransgroup_id());

        return userService.acceptToJoinTeam(userId, transGroupId);
    }


    @PostMapping("/signuptransgroup")
    public ResponseEntity signUpTransGroup(ServletRequest request, @RequestBody TransGroupPOJO transGroupPOJO) throws NoSuchAlgorithmException {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        if (Boolean.FALSE.equals(transGroupPOJO.isValid())) {
            Map<String, String> error = Map.of("err", "Missing credentials!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        String groupName = transGroupPOJO.getGroup_name();
        String groupDesc = transGroupPOJO.getGroup_desc();


        return userService.signUpTransGroup(userId, groupName, groupDesc);
    }


    @Cacheable(value = "transGroupInfo", key = "{#request.getAttribute(\"user\").get(\"user_id\"), #request.getAttribute(\"user\").get(\"user_transgroup_id\")}")
    @PostMapping("/gettransgroupinfo")
    public ResponseEntity getTransGroupInfo(ServletRequest request, @RequestBody TransGroupPOJO transGroupPOJO) {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        if (userHelpers.getUserAttribute(request).get("user_transgroup_id") == null) {
            Map<String, String> error = Map.of("err", "Login again before visit this page, thank you!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(),
                    HttpStatus.ACCEPTED);
        }
        Long transGroupId = Long.parseLong(userHelpers.getUserAttribute(request).get("user_transgroup_id").toString());


        return userService.getTransGroupInfo(userId, transGroupId);
    }


    //    @CacheEvict(allEntries = true, value = {"transGroupInfo"})
    @CacheEvict(value = {"transGroupInfo"}, key = "{#request.getAttribute(\"user\").get(\"user_id\"), #request.getAttribute(\"user\").get(\"user_transgroup_id\")}")
    @DeleteMapping("/deletemanga")
    public ResponseEntity deleteManga(@RequestBody TransGroupPOJO transGroupPOJO, ServletRequest request) {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userID = Long.parseLong(StrUserId);

        Long mangaId = Long.parseLong(transGroupPOJO.getManga_id().toString());

        Long transGroupId = Long.parseLong(transGroupPOJO.getTransgroup_id());

        return userService.deleteManga(userID, mangaId, transGroupId);
    }

    @CacheEvict(value = {"transGroupInfo"}, key = "{#request.getAttribute(\"user\").get(\"user_id\"), #request.getAttribute(\"user\").get(\"user_transgroup_id\")}")
    @DeleteMapping("/deletetransgroup")
    public ResponseEntity deleteTransGroup(@RequestBody TransGroupPOJO transGroupPOJO, ServletRequest request) {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        Long transGroupId = Long.parseLong(transGroupPOJO.getTransgroup_id());
        System.err.println("transgr_id to delete" + transGroupId);

        return userService.deletetransGroup(userId, transGroupId);
    }


    @CacheEvict(value = {"transGroupInfo"}, key = "{#request.getAttribute(\"user\").get(\"user_id\"), #request.getAttribute(\"user\").get(\"user_transgroup_id\")}")
    @DeleteMapping("/remove_member")
    public ResponseEntity removeMember(ServletRequest request, @RequestBody TransGroupPOJO transGroupPOJO) {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        Long memberId = Long.parseLong(transGroupPOJO.getMember_id());

        return userService.removeMember(userId, memberId);
    }


//    @GetMapping("/checkroletransgroup")
//    public ResponseEntity checkRoleTransGroup(ServletRequest request) {
//        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
//        Long userId = Long.parseLong(StrUserId);
//
//        String StrTransGroupId = getUserAttribute(request).get("transgroup_id").toString();
//        if(StrTransGroupId.equals (null))
//        {
//            Map<String, Object> err = Map.of(
//                    "err", "User not in transgroup!"
//
//            );
//            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
//                    HttpStatus.BAD_REQUEST);
//        }
//        Long transGroupId = Long.parseLong(StrTransGroupId);
//
//        return userService.checkRoleTransGroup (userId, transGroupId);
//    }


//    @Cacheable(value = "mangaInfoUploadPage", key = "{#request.getAttribute(\"user\").get(\"user_id\"), #transGroupPOJO.getManga_id()}")
    @PostMapping("/getmangainfo")
    public ResponseEntity getMangaInfoUploadPage(ServletRequest request, @RequestBody TransGroupPOJO transGroupPOJO) {
        if (userHelpers.getUserAttribute(request).get("user_transgroup_id") == null) {
            Map<String, String> error = Map.of("err", "Login again before visit this page, thank you!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(),
                    HttpStatus.ACCEPTED);
        }
        Long transGroupId = Long.parseLong(userHelpers.getUserAttribute(request).get("user_transgroup_id").toString());

        Long mangaId = Long.parseLong(transGroupPOJO.getManga_id().toString());

        return userService.getMangaInfoUploadPage(transGroupId, mangaId);
    }


    @PostMapping("/addnewprojectmangafields")
    @Transactional
    public ResponseEntity addNewProjectMangaFields(
            ServletRequest request,
            @RequestBody FieldsCreateMangaDTO fieldsCreateMangaDTO
    ) throws IOException {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        if (userHelpers.getUserAttribute(request).get("user_transgroup_id") == null) {
            Map<String, String> error = Map.of("err", "Login again before visit this page|");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(),
                    HttpStatus.ACCEPTED);
        }
        Long transGrId = Long.parseLong(userHelpers.getUserAttribute(request).get("user_transgroup_id").toString());


        if (fieldsCreateMangaDTO.isFieldsEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "Miss fields!"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        return userService.addNewProjectMangaFields(userId, transGrId, fieldsCreateMangaDTO);
    }


    @CacheEvict(allEntries = true, value = {"transGroupInfo", "allmangas"}) //allmangas is in admin service
    @PostMapping("/addnewprojectmangathumbnail")
    public ResponseEntity addNewProjectMangaThumbnail(
            ServletRequest request,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "manga_id", required = false) Integer manga_id
    ) throws IOException {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);
        String StrTransGrId = userHelpers.getUserAttribute(request).get("user_transgroup_id").toString();
        Long transGrId = Long.parseLong(StrTransGrId);

        if (file == null && manga_id != 0) {
            Map<String, Object> err = Map.of(
                    "err", "Miss fields!"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        Long mangaId = Long.parseLong(String.valueOf(manga_id));
        return userService.addNewProjectMangaThumbnail(userId, transGrId, file, mangaId);
    }

}






