package serverapi.Tables.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import serverapi.Api.Response;
import serverapi.Tables.Manga.POJO.CommentPOJO;
import serverapi.Tables.Manga.POJO.MangaPOJO;
import serverapi.Tables.Manga.POJO.RatingPOJO;
import serverapi.Tables.User.POJO.FieldsCreateMangaDTO;
import serverapi.Tables.User.POJO.TransGroupPOJO;
import serverapi.Tables.User.POJO.UserPOJO;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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


    @PostMapping("/addcommentchapter")
    public ResponseEntity addCommentChapter(@RequestBody CommentPOJO commentPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);
        Long chapterId = Long.parseLong(commentPOJO.getChapter_id());
        String content = commentPOJO.getChaptercmt_content();

        return userService.addCommentChapter(chapterId, userId, content);
    }


    ////////////////////////// Translation Group parts /////////////////////////////
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


    @Cacheable(value = "TransGroupInfo", key = "{#request.getAttribute(\"user\").get(\"user_id\"), #request.getAttribute(\"user\").get(\"user_transgroup_id\")}")
    @PostMapping("/gettransgroupinfo")
    public ResponseEntity getTransGroupInfo(ServletRequest request, @RequestBody TransGroupPOJO transGroupPOJO) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);
        System.err.println("user_transgroup_id: " + getUserAttribute(request).get("user_transgroup_id"));
        System.err.println("userID: " + userId);


        if (getUserAttribute(request).get("user_transgroup_id") == null) {
            Map<String, String> error = Map.of("err", "Login again before visit this page, thank you!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(),
                    HttpStatus.ACCEPTED);
        }
        Long transGroupId = Long.parseLong(getUserAttribute(request).get("user_transgroup_id").toString());


        return userService.getTransGroupInfo(userId, transGroupId);
    }


    @Cacheable(value = "TransGroupInfo", key = "{#request.getAttribute(\"user\").get(\"user_id\"), #transGroupPOJO.getManga_id()}")
    @PostMapping("/getmangainfo")
    public ResponseEntity getMangaInfo(ServletRequest request, @RequestBody TransGroupPOJO transGroupPOJO) {
        if (getUserAttribute(request).get("user_transgroup_id") == null) {
            Map<String, String> error = Map.of("err", "Login again before visit this page, thank you!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(),
                    HttpStatus.ACCEPTED);
        }
        Long transGroupId = Long.parseLong(getUserAttribute(request).get("user_transgroup_id").toString());

        Long mangaId = Long.parseLong(transGroupPOJO.getManga_id().toString());

        return userService.getMangaInfo(transGroupId, mangaId);
    }

    @DeleteMapping("/deletemanga")
    public ResponseEntity deleteManga(@RequestBody TransGroupPOJO transGroupPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userID = Long.parseLong(StrUserId);

        Long mangaId = Long.parseLong (transGroupPOJO.getManga_id ().toString ());

        Long transGroupId = Long.parseLong (transGroupPOJO.getTransgroup_id ());

        return userService.deleteManga(userID, mangaId,transGroupId);
    }

    ////////////////////////// transgroup
    @DeleteMapping("/deletetransgroup")
    public ResponseEntity deleteTransGroup(@RequestBody TransGroupPOJO transGroupPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        Long transGroupId = Long.parseLong (transGroupPOJO.getTransgroup_id ());

        return userService.deletetransGroup (userId, transGroupId);
    }


    @PostMapping("/addnewprojectmangafields")
    @Transactional
    public ResponseEntity addNewProjectMangaFields(
            ServletRequest request,
            @RequestBody FieldsCreateMangaDTO fieldsCreateMangaDTO
    ) throws IOException {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        if (getUserAttribute(request).get("transgroup_id") == null) {
            Map<String, String> error = Map.of("err", "Login again before visit this page, thank you!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(),
                    HttpStatus.ACCEPTED);
        }
        Long transGrId = Long.parseLong(getUserAttribute(request).get("transgroup_id").toString());


        if (fieldsCreateMangaDTO.isFieldsEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "Miss fields!"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        return userService.addNewProjectMangaFields(userId, transGrId, fieldsCreateMangaDTO);
    }


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
