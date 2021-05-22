package serverapi.Tables.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.Tables.Manga.MangaService;
import serverapi.Tables.User.POJO.UserPOJO;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
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

    @GetMapping("/test")
    public String getUser(ServletRequest request) {
        System.out.println("dasdasdasd"+getUserAttribute(request).get ("user_id"));



        return "Get user route";
    }

    @GetMapping("/gethistorymanga")
    public ResponseEntity findUserByReadingHistory(ServletRequest request){
        String text = getUserAttribute(request).get("user_id").toString();

        Long user_id = Long.parseLong(text);


        return userService.GetUserByReadingHistory(user_id);

    }

    @PutMapping("/updatetimehistory")
    public ResponseEntity updatetime(@RequestBody UserPOJO userPOJO,ServletRequest request){
        String text = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(text);
        Long mangaId = Long.parseLong(userPOJO.getManga_id());
        Long chapterId = Long.parseLong(userPOJO.getChapter_id());

        return userService.updatetime(userId,mangaId,userPOJO,chapterId);


    }





    @GetMapping("/getfollowingmangas")
    public ResponseEntity getFollowingMangas(ServletRequest request) {

       String text= getUserAttribute(request).get ("user_id").toString ();
       Long userId = Long.parseLong (text);

        return userService.getFollowManga (userId);
    }

    @DeleteMapping("/deletefollowingmangas")
    public ResponseEntity deleteFollowingMangas(@RequestBody MangaPOJO mangaPOJO, ServletRequest request) {

        String text= getUserAttribute(request).get ("user_id").toString ();
        Long userId = Long.parseLong (text);
        Long mangaId = Long.parseLong(mangaPOJO.getManga_id());

        return userService.deleteFollowManga (mangaId,userId);
    }

    @PostMapping("/addfollowingmangas")
    public ResponseEntity addFollowingMangas(@RequestBody MangaPOJO mangaPOJO, ServletRequest request) {

        String text= getUserAttribute(request).get ("user_id").toString ();
        Long userId = Long.parseLong (text);
        Long mangaId = Long.parseLong(mangaPOJO.getManga_id());

        return userService.addFollowManga (mangaId,userId);
    }

}
