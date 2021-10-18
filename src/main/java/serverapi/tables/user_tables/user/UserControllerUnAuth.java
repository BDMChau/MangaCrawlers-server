package serverapi.tables.user_tables.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/user_unauth")
@CacheConfig(cacheNames = {"user_unauth"})
public class UserControllerUnAuth {

    private final UserService userService;

    @Autowired
    public UserControllerUnAuth(UserService userService) {
        this.userService = userService;
    }


    ///////////////////////////////// Users parts //////////////////////////////
    @Cacheable(value = "userinfo", key = "#data.get(\"user_id\")")
    @PostMapping("/get_userinfo")
    public ResponseEntity GetReadingHistory(@RequestBody Map data) {
        Long userId = Long.parseLong(String.valueOf(data.get("user_id")));

        return userService.getUserInfo(userId);
    }
}