package serverapi.Tables.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import serverapi.Tables.Manga.POJO.CommentPOJO;
import serverapi.Tables.Manga.POJO.MangaPOJO;
import serverapi.Tables.Manga.POJO.RatingPOJO;
import serverapi.Tables.User.POJO.UserPOJO;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
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


    ////////////comment parts
    @PostMapping("/addcommentchapter")
    public ResponseEntity addCommentChapter(@RequestBody CommentPOJO commentPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);
        Long chapterId = Long.parseLong(commentPOJO.getChapter_id());
        String content = commentPOJO.getChaptercmt_content();

        return userService.addCommentChapter(chapterId, userId, content);
    }


//////////////////////// Admin parts ////////////////////////////

    @Cacheable(value = "allmangas", key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @GetMapping("/getallmangas")
    public ResponseEntity getAllMangas(ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return userService.getAllMangas(userId);
    }


    @Cacheable(value = "allusers", key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @GetMapping("/getallusers")
    public ResponseEntity getAllUsers(ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return userService.getAllUsers(userId);
    }


    @CacheEvict(allEntries = true, value = {"allusers"})
    @PutMapping("/deprecateuser")
    public ResponseEntity deprecateUser(@RequestBody UserPOJO userPOJO, ServletRequest request) {

        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(StrUserId);

        Long userId = Long.parseLong(userPOJO.getUser_id());

        return userService.deprecateUser(userId, adminId);
    }


    // Delete User by userId
    @CacheEvict(allEntries = true, value = {"allusers"})
    @DeleteMapping("/deleteuser")
    public ResponseEntity deleteUser(@RequestBody UserPOJO userPOJO, ServletRequest request) {

        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(StrUserId);

        Long userId = Long.parseLong(userPOJO.getUser_id());

        return userService.deleteUser(userId, adminId);
    }


}
