package serverapi.tables.user_tables.user.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CacheConfig(cacheNames = {"user"})
public class FriendController {

    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    public Map getUserAttribute(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        Map user = (HashMap) req.getAttribute("user");
        return user;
    }

    @PostMapping("/get_list_friends")
    public ResponseEntity getListFriends(@RequestParam int from, int amount, ServletRequest request) {
        Long userID = 0L;
        String sUserId = getUserAttribute(request).get("user_id").toString();
        if(!sUserId.isEmpty()){
            userID = Long.parseLong(sUserId);
        }

        return friendService.getListFriends(userID, from, amount);
    }
}