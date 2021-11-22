package serverapi.tables.user_tables.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/notification")
@CacheConfig(cacheNames = {"notification"})
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public Map getUserAttribute(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        Map user = (HashMap) req.getAttribute("user");
        return user;
    }


    /////////////////////////////////////////////
    @PostMapping("/get_list_notification")
    public ResponseEntity getListNotifications(ServletRequest request, @RequestBody Map data) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(strUserId);
        Integer fromPos = (Integer) data.get("from");

        return notificationService.getListNotifications(userId, fromPos);
    }


    @PostMapping("/viewed_all")
    public ResponseEntity updateAllToViewed(ServletRequest request) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(strUserId);

        return notificationService.updateAllToViewed(userId);
    }

    @PostMapping("/update_viewed")
    public ResponseEntity updateToViewed(ServletRequest request,  @RequestBody Map data) {
        Long notificationId = Long.parseLong(String.valueOf(data.get("notification_id")));

        return notificationService.updateToViewed(notificationId);
    }

    @PostMapping("/update_interacted")
    public ResponseEntity updateToInteracted(@RequestBody Map data) {
        Long notificationId = Long.parseLong(String.valueOf(data.get("notification_id")));

        // 1: delete,
        // 2: accept join team,
        // 3: accept friend request
        int action = (int) data.get("action");

        return notificationService.updateToInteracted(notificationId, action);
    }


    @PostMapping("/update_deleted")
    public ResponseEntity updateToDeleted(@RequestBody Map data) {
        Long notificationId = Long.parseLong(String.valueOf(data.get("notification_id")));

        // 1: delete,
        // 2: accept join team,
        // 3: accept friend request
        int action = (int) data.get("action");

        return notificationService.updateToDeleted(notificationId, action);
    }


    // without notification_id
    @PostMapping("/update_notification_friend_req")
    public ResponseEntity updateFriendReq(ServletRequest request, @RequestBody Map data) {
        String strUserId = getUserAttribute(request).get("user_id").toString();

        Long fromUserId = Long.parseLong((strUserId));
        Long toUserId = Long.parseLong(String.valueOf(data.get("to_user_id")));
        String targetTitle = String.valueOf(data.get("target_title"));

        // 1: delete,
        // 2: accept join team,
        // 3: accept friend request
        int action = (int) data.get("action");

        // 1: set delete to true
        // 2: set interact to true
        int type = (int) data.get("type");

        // 1: sender update
        // 2: receiver update
        int cmdFrom = (int) data.get("cmd_from");

        return notificationService.updateFriendReq(fromUserId, toUserId, targetTitle, action, type, cmdFrom);
    }
}
