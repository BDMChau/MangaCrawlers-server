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

    private UserService userService;

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
        System.out.println(getUserAttribute(request));


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






}
