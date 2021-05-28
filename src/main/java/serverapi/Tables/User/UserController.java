package serverapi.Tables.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import serverapi.Tables.Manga.POJO.MangaPOJO;
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

//////////////////////History parts//////////////////////

    @Cacheable(value = "historymangas", key = "#root.method")
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




//////////////////////Follow parts//////////////////////

    ///Get user's follows
    @Cacheable(value = "followingmangas", key = "#root.method")
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


//////////////////////comment parts//////////////////////





//////////////////////Admin parts//////////////////////

    //////Interact with users

    //Set user's verify = false
    @PutMapping("/deprecateuser")
    public ResponseEntity deprecateUser(@RequestBody UserPOJO userPOJO, ServletRequest request) {

        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(StrUserId);

        Long userId = Long.parseLong(userPOJO.getUser_id());

        return userService.deprecateUser(userId, adminId);
    }

    //Get all users
    @Cacheable(value = "allusers", key = "#root.method")
    @GetMapping("/getallusers")
    public ResponseEntity getAllUsers(ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return userService.getAllUsers(userId);
    }

    // Delete User by userId
    @DeleteMapping("/deleteuser")
    public ResponseEntity deleteUser(@RequestBody UserPOJO userPOJO, ServletRequest request) {

        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(StrUserId);

        Long userId = Long.parseLong(userPOJO.getUser_id());

        return userService.deleteUser(userId, adminId);
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


    /////Interact with mangas

    //Get all mangas in admin page
    //    @Cacheable(value = "allmangas", key = "#root.method")
    @GetMapping("/getallmangas")
    public ResponseEntity getAllMangas(ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return userService.getAllMangas(userId);
    }


}
