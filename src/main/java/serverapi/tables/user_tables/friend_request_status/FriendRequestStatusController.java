package serverapi.tables.user_tables.friend_request_status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serverapi.api.Response;
import serverapi.tables.user_tables.notification.NotificationService;
import serverapi.utils.UserHelpers;

import javax.servlet.ServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/friend_request")
@CacheConfig(cacheNames = {"friend_request"})
public class FriendRequestStatusController {

    private final FriendRequestStatusService friendRequestStatusService;

    @Autowired
    public FriendRequestStatusController(FriendRequestStatusService friendRequestStatusService) {
        this.friendRequestStatusService = friendRequestStatusService;
    }

    UserHelpers userHelpers = new UserHelpers();


    @PostMapping("/update_to_false")
    public ResponseEntity getFriendsUser(ServletRequest request, @RequestBody Map data) {
        String strUserId = userHelpers.getUserAttribute(request).get("user_id").toString();

        Long fromUserId = Long.parseLong(strUserId);
        Long toUserId = Long.parseLong(String.valueOf(data.get("to_user_id")));

        Boolean isUpdated = friendRequestStatusService.updateDeclineReq(fromUserId, toUserId);
        if (!isUpdated) {
            Map<String, Object> err = Map.of("err", "update failed");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> msg = Map.of("msg", "updated status to false");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
