package serverapi.tables.user_tables.notification;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.helpers.OffsetBasedPageRequest;
import serverapi.query.repository.user.NotificationRepos;
import serverapi.query.repository.user.NotificationTypesRepos;
import serverapi.query.repository.user.UserRepos;
import serverapi.socket.message.SocketMessage;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.user_tables.notification.notification_types.NotificationTypes;
import serverapi.tables.user_tables.notification.notifications.Notifications;
import serverapi.tables.user_tables.user.User;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class NotificationService {
    private Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    private final UserRepos userRepos;
    private final NotificationRepos notificationRepos;
    private final NotificationTypesRepos notificationTypesRepos;

    @Autowired
    public NotificationService(UserRepos userRepos, NotificationRepos notificationRepos, NotificationTypesRepos notificationTypesRepos){
        this.userRepos = userRepos;
        this.notificationRepos = notificationRepos;
        this.notificationTypesRepos = notificationTypesRepos;
    }



    @Transactional
    public ResponseEntity updateToViewed(Long userId){
        List<Notifications> notificationsList = notificationRepos.getAllByUserId(userId);

        notificationsList.forEach(notification ->{
            notification.setIs_viewed(true);
            notificationRepos.saveAndFlush(notification);
        });


        Map<String, Object> msg = Map.of("msg", "updated viewed notifications");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity getListNotifications(Long userId, Integer fromPos){
        System.err.println(fromPos);
        Pageable pageable = new OffsetBasedPageRequest(fromPos, 5);
        List<Notifications> notificationsList = notificationRepos.getListByUserId(userId, pageable);


        Map<String, Object> msg = Map.of(
                "msg", "get list notifications OK",
                "notifications_list", notificationsList
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }




    /////////////////////// for socket ///////////////////////

    /**
     *
     * @param uID: can be String userEmail or Long userId
     * @param socketMessage: data to save to database
     */
    public Map saveNew(Object uID, SocketMessage socketMessage, Optional<User> userOptional){
        if(userOptional.isEmpty()){
            return Collections.emptyMap();
        }
        User user = userOptional.get();
        User sender = userRepos.findById(socketMessage.getUserId()).get();

        NotificationTypes notificationTypes = new NotificationTypes();
        Notifications notifications = new Notifications();

        notificationTypes.setType(socketMessage.getType());

        notifications.setNotification_type(notificationTypes);
        notifications.setContent(socketMessage.getMessage());
        notifications.setImage_url(String.valueOf(socketMessage.getObjData().get("image")));
        notifications.setFrom_user(sender);
        notifications.setTo_user(user);
        notifications.setIs_viewed(false);
        notifications.setCreated_at(currentTime);

        notificationTypesRepos.saveAndFlush(notificationTypes);
        notificationRepos.saveAndFlush(notifications);

        return Map.<String, Object>of("err", "Group is existed!");
    }

}
