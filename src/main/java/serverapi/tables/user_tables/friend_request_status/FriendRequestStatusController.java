package serverapi.tables.user_tables.friend_request_status;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/friend_request")
@CacheConfig(cacheNames = {"friend_request"})
public class FriendRequestStatusController {


//    @PostMapping("/get_friends_user")
//    public ResponseEntity getFriendsUser(ServletRequest request, @RequestBody Map data) {
//        String StrUserId = getUserAttribute(request).get("user_id").toString();
//        Long userId = Long.parseLong(StrUserId);
//        Integer fromPos = (Integer) data.get("from");
//
//        return notificationService.getListNotifications(userId, fromPos);
//    }
}
