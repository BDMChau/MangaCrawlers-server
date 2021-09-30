package serverapi.tables.user_tables.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverapi.query.repository.user.NotificationRepos;
import serverapi.query.repository.user.NotificationTypesRepos;
import serverapi.query.repository.user.UserRepos;
import serverapi.socket.message.SocketMessage;
import serverapi.tables.user_tables.notification.notification_types.NotificationTypes;
import serverapi.tables.user_tables.notification.notifications.Notifications;
import serverapi.tables.user_tables.user.User;

import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

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



    /**
     *
     * @param uID: can be String userEmail or Long userId
     * @param socketMessage: data to save to database
     */
    public void saveNew(Object uID, SocketMessage socketMessage, Optional<User> userOptional){
        if(userOptional.isEmpty()){
            return;
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
    }

}
