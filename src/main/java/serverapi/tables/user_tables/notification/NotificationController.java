package serverapi.tables.user_tables.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.tables.user_tables.notification.NotificationService;
import serverapi.tables.user_tables.user.UserService;

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

}
