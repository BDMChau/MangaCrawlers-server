package serverapi.tables.user_tables.user;

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
import serverapi.tables.manga_tables.manga.pojo.CommentPOJO;
import serverapi.tables.manga_tables.manga.pojo.MangaPOJO;
import serverapi.tables.manga_tables.manga.pojo.RatingPOJO;
import serverapi.tables.user_tables.user.pojo.TransGroupPOJO;
import serverapi.tables.user_tables.user.pojo.UserPOJO;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/api/user")
@CacheConfig(cacheNames = {"user"})
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    public Map getUserAttribute(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        Map user = (HashMap) req.getAttribute("user");
        return user;
    }


    ///////////////////////////////// Users parts //////////////////////////////

    @Cacheable(value = "historymangas", key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @GetMapping("/gethistorymanga")
    public ResponseEntity GetReadingHistory(ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long user_id = Long.parseLong(StrUserId);

        return userService.GetReadingHistory(user_id);
    }


    // update reading history of user by chapters in a manga
    @CacheEvict(allEntries = true, value = {"historymangas"})
    @PutMapping("/updatereadinghistory")
    public ResponseEntity updateReadingHistory(@RequestBody UserPOJO userPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);
        Long mangaId = Long.parseLong(userPOJO.getManga_id());
        Long chapterId = Long.parseLong(userPOJO.getChapter_id());

        return userService.updateReadingHistory(userId, mangaId, chapterId);
    }


    @Cacheable(value = "followingmangas", key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @GetMapping("/getfollowingmangas")
    public ResponseEntity getFollowingMangas(ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return userService.getFollowingMangas(userId);
    }


    ///Add manga to list user's follows
    @CacheEvict(allEntries = true, value = {"followingmangas"})
    @PostMapping("/addfollowingmanga")
    public ResponseEntity addFollowingMangas(@RequestBody MangaPOJO mangaPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);
        Long mangaId = Long.parseLong(mangaPOJO.getManga_id());

        return userService.addFollowManga(mangaId, userId);
    }


    ///Delete manga following from user's follows( Unfollow)
    @CacheEvict(allEntries = true, value = {"followingmangas"})
    @DeleteMapping("/deletefollowingmanga")
    public ResponseEntity deleteFollowingMangas(@RequestBody MangaPOJO mangaPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        Long mangaId = Long.parseLong(mangaPOJO.getManga_id());

        return userService.deleteFollowManga(mangaId, userId);
    }


    @PutMapping("/ratingmanga")
    public ResponseEntity ratingManga(@RequestBody RatingPOJO ratingPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
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
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        String[] splitedFileName = file.getOriginalFilename().split(Pattern.quote("."));
        String fileName = splitedFileName[0];
        byte[] fileBytes = file.getBytes();


        return userService.updateAvatar(fileName, fileBytes, userId);
    }


    @DeleteMapping("/removeavatar")
    public ResponseEntity removeAvatar(ServletRequest request) throws IOException {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return userService.removeAvatar(userId);
    }



    @PostMapping("/addcommentmanga")
    public ResponseEntity addCommentManga(@Valid CommentPOJO commentPOJO, ServletRequest request) throws IOException {

        /**
         * Declare variables
         */
        Long mangaID;
        Long chapterID = 0L;
        Long parentID = 0L;

        String strUserID = getUserAttribute(request).get("user_id").toString();
        String content = commentPOJO.getManga_comment_content();

        MultipartFile imageUrl = commentPOJO.getImage_url();

        List<String> to_usersString = commentPOJO.getTo_user_id();
        List<Long> to_users = new ArrayList<>();

        /**
         * Format variables necessary include: mangaID, UserID, chapterID, parentID, toUserID
         */
        Long userID = Long.parseLong(strUserID);
        mangaID = Long.parseLong(commentPOJO.getManga_id());

        //toUserID
        if (!to_usersString.isEmpty()) {

            to_usersString.forEach(item ->{

                Long to_user = Long.parseLong(item);
                to_users.add(to_user);
            });
        }

        //ChapterID
        if (!commentPOJO.getChapter_id().equals("")) {

            chapterID = Long.parseLong(commentPOJO.getChapter_id());
        }

        //parentID
        if(!commentPOJO.getParent_id().equals("")){

            parentID = Long.parseLong(commentPOJO.getParent_id());
        }
        return userService.addCommentManga(to_users, userID, mangaID, chapterID, content, imageUrl, parentID);
    }


    @PostMapping("/updatecomment")
    public ResponseEntity updateComment(@Valid CommentPOJO commentPOJO, ServletRequest request) throws IOException {

        /**
         * Initialize variable
         */
        Long userID = 0L;
        Long commentID = 0L;


        String content = commentPOJO.getManga_comment_content();
        String strUserID = getUserAttribute(request).get("user_id").toString();

        MultipartFile imageUrl = commentPOJO.getImage_url();

        List<String> to_usersString = commentPOJO.getTo_user_id();
        List<Long> toUsers = new ArrayList<>();

        /**
         * Assign variable
         */
        if(!strUserID.isEmpty()){

            userID = Long.parseLong(strUserID);
        }

        if(!commentPOJO.getManga_comment_id().equals("")){

            commentID = Long.parseLong(commentPOJO.getManga_comment_id());
        }

        //toUserID
        if (!to_usersString.isEmpty()) {

            to_usersString.forEach(item ->{

                Long to_user = Long.parseLong(item);
                toUsers.add(to_user);
            });
        }


        System.err.println("line 215");
        return userService.updateComment(userID, toUsers, commentID, content, imageUrl);
    }

    @PostMapping("/deletecomment")
    public ResponseEntity deleteComment(@RequestBody CommentPOJO commentPOJO, ServletRequest request) {

        /**
         * Declare variables
         */
        String strUserID = getUserAttribute(request).get("user_id").toString();
        /**
         * Format variable necessary
         */
        Long userID = Long.parseLong(strUserID);
        Long formatCommentID = Long.parseLong(commentPOJO.getManga_comment_id());

        return userService.deleteComment(userID, formatCommentID);
    }

    @PostMapping("/searchusers")
    public ResponseEntity searchUsers(@RequestBody Map data) {
        String valToSearch = (String) data.get("value");

        return userService.searchUsers(valToSearch);
    }

    ////////////////////////// Translation Group parts /////////////////////////////
    @CacheEvict(allEntries = true, value = {"allmangas", "transGroupInfo", "mangaInfoUploadPage"})
    @PostMapping("/uploadchapterimgs")
    public ResponseEntity uploadChapterImgs(
            ServletRequest request,
            @RequestParam(required = false) MultipartFile[] files,
            @RequestParam(required = false) Integer manga_id,
            @RequestParam(required = false) String chapter_name
    ) throws IOException, ParseException {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        for (MultipartFile file : files) {
            System.err.println(file.getOriginalFilename());
        }

        Long mangaId = Long.parseLong(String.valueOf(manga_id));
        String chapterName = chapter_name;
        System.err.println("chapterName: " + chapterName);
        return userService.uploadChapterImgs(userId, mangaId, chapterName, files);
    }


    @PostMapping("/signuptransgroup")
    public ResponseEntity signUpTransGroup(ServletRequest request, @RequestBody TransGroupPOJO transGroupPOJO) throws NoSuchAlgorithmException {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
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
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        if (getUserAttribute(request).get("user_transgroup_id") == null) {
            Map<String, String> error = Map.of("err", "Login again before visit this page, thank you!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(),
                    HttpStatus.ACCEPTED);
        }
        Long transGroupId = Long.parseLong(getUserAttribute(request).get("user_transgroup_id").toString());


        return userService.getTransGroupInfo(userId, transGroupId);
    }


    @Cacheable(value = "mangaInfoUploadPage", key = "{#request.getAttribute(\"user\").get(\"user_id\"), #transGroupPOJO.getManga_id()}")
    @PostMapping("/getmangainfo")
    public ResponseEntity getMangaInfoUploadPage(ServletRequest request, @RequestBody TransGroupPOJO transGroupPOJO) {
        if (getUserAttribute(request).get("user_transgroup_id") == null) {
            Map<String, String> error = Map.of("err", "Login again before visit this page, thank you!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(),
                    HttpStatus.ACCEPTED);
        }
        Long transGroupId = Long.parseLong(getUserAttribute(request).get("user_transgroup_id").toString());

        Long mangaId = Long.parseLong(transGroupPOJO.getManga_id().toString());

        return userService.getMangaInfoUploadPage(transGroupId, mangaId);
    }


    @CacheEvict(allEntries = true, value = {"transGroupInfo"})
    @DeleteMapping("/deletemanga")
    public ResponseEntity deleteManga(@RequestBody TransGroupPOJO transGroupPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userID = Long.parseLong(StrUserId);

        Long mangaId = Long.parseLong(transGroupPOJO.getManga_id().toString());

        Long transGroupId = Long.parseLong(transGroupPOJO.getTransgroup_id());

        return userService.deleteManga(userID, mangaId, transGroupId);
    }


    @DeleteMapping("/deletetransgroup")
    public ResponseEntity deleteTransGroup(@RequestBody TransGroupPOJO transGroupPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        Long transGroupId = Long.parseLong(transGroupPOJO.getTransgroup_id());
        System.err.println("acascacacacs" + transGroupId);
        return userService.deletetransGroup(userId, transGroupId);
    }

//    @GetMapping("/checkroletransgroup")
//    public ResponseEntity checkRoleTransGroup(ServletRequest request) {
//        String StrUserId = getUserAttribute(request).get("user_id").toString();
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


    @PostMapping("/addnewprojectmangafields")
    @Transactional
    public ResponseEntity addNewProjectMangaFields(
            ServletRequest request,
            @RequestBody FieldsCreateMangaDTO fieldsCreateMangaDTO
    ) throws IOException {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        if (getUserAttribute(request).get("user_transgroup_id") == null) {
            Map<String, String> error = Map.of("err", "Login again before visit this page|");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(),
                    HttpStatus.ACCEPTED);
        }
        Long transGrId = Long.parseLong(getUserAttribute(request).get("user_transgroup_id").toString());


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
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);
        String StrTransGrId = getUserAttribute(request).get("user_transgroup_id").toString();
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
