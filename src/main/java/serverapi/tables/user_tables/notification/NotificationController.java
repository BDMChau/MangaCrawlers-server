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
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);
        Integer fromPos = (Integer) data.get("from");

        return notificationService.getListNotifications(userId, fromPos);
    }


    @PostMapping("/update_viewed")
    public ResponseEntity updateToViewed(ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return notificationService.updateToViewed(userId);
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
}
