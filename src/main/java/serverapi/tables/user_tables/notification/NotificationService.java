package serverapi.tables.user_tables.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.helpers.OffsetBasedPageRequest;
import serverapi.query.dtos.tables.NotificationDTO;
import serverapi.query.repository.user.NotificationRepos;
import serverapi.query.repository.user.NotificationTypesRepos;
import serverapi.query.repository.user.UserRepos;
import serverapi.socket.message.SocketMessage;
import serverapi.tables.user_tables.friend_request_status.FriendRequestStatusService;
import serverapi.tables.user_tables.notification.notification_types.NotificationTypes;
import serverapi.tables.user_tables.notification.notifications.Notifications;
import serverapi.tables.user_tables.user.User;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class NotificationService {
    private final UserRepos userRepos;
    private final NotificationRepos notificationRepos;
    private final NotificationTypesRepos notificationTypesRepos;
    private final FriendRequestStatusService friendRequestStatusService;

    @Autowired
    public NotificationService(UserRepos userRepos, NotificationRepos notificationRepos,
                               NotificationTypesRepos notificationTypesRepos, FriendRequestStatusService friendRequestStatusService) {
        this.userRepos = userRepos;
        this.notificationRepos = notificationRepos;
        this.notificationTypesRepos = notificationTypesRepos;
        this.friendRequestStatusService = friendRequestStatusService;
    }


    @Transactional
    protected ResponseEntity updateToViewed(Long userId) {
        List<Notifications> notificationsList = notificationRepos.getAllByUserId(userId);

        notificationsList.forEach(notification -> {
            notification.setIs_viewed(true);
            notificationRepos.saveAndFlush(notification);
        });


        Map<String, Object> msg = Map.of("msg", "updated viewed notifications");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    protected ResponseEntity updateToInteracted(Long notificationId, int action) {
        Notifications notification = notificationRepos.findById(notificationId).get();

        if (action == 1)
            handleUpdateTargetUnit(notification.getFrom_user().getUser_id(), notification.getTarget_id(), notification.getTarget_title());

        notification.setIs_interacted(true);
        notification.setIs_viewed(true);
        notificationRepos.saveAndFlush(notification);

        Map<String, Object> msg = Map.of("msg", "updated interacted notification");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    protected ResponseEntity updateToDeleted(Long notificationId, int action) {
        Notifications notification = notificationRepos.findById(notificationId).get();

        if (action == 1)
            handleUpdateTargetUnit(notification.getFrom_user().getUser_id(), notification.getTarget_id(), notification.getTarget_title());

        notification.setIs_delete(true);
        notificationRepos.saveAndFlush(notification);

        Map<String, Object> msg = Map.of("msg", "deleted notification");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    protected ResponseEntity updateFriendReq(Long senderId, Long recieverId, String targetTitle, int action, int type, int cmdFrom) {
        Optional<Notifications> notificationsOptional = Optional.empty();
        if (cmdFrom == 1)
            notificationsOptional = notificationRepos.getFriendReqByTargetTitleUser(senderId, recieverId, targetTitle, 2);
        else if (cmdFrom == 2)
            notificationsOptional = notificationRepos.getFriendReqByTargetTitleUser(recieverId, senderId, targetTitle, 2);


        if (notificationsOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "cannot delete");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }
        Notifications notification = notificationsOptional.get();

        if (action == 1)
            handleUpdateTargetUnit(notification.getFrom_user().getUser_id(), notification.getTarget_id(), notification.getTarget_title());

        if (type == 1) notification.setIs_delete(true);
        else if (type == 2) {
            notification.setIs_interacted(true);
            notification.setIs_viewed(true);
        }


        notificationRepos.saveAndFlush(notification);

        Map<String, Object> msg = Map.of("msg", "deleted notification");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    protected ResponseEntity getListNotifications(Long userId, Integer fromPos) {
        Pageable pageable = new OffsetBasedPageRequest(fromPos, 5);
        List<NotificationDTO> notificationsList = notificationRepos.getAllByUserId(userId, pageable);

        Map<String, Object> msg = Map.of(
                "msg", "get list notifications OK",
                "notifications_list", notificationsList,
                "fromRow", fromPos + 5
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    ////////////////////////////////////////////// usable //////////////////////////////////////////////
    private void handleUpdateTargetUnit(Long fromUserId, Long targetId, String targetTitle) {
        if (targetTitle.equals("user")) {
            Long toUserId = targetId;

            friendRequestStatusService.updateDeclineReq(fromUserId, toUserId);
        }
    }


    public void updateInteractedById(Long notificationId) {
        Notifications notification = notificationRepos.findById(notificationId).get();

        notification.setIs_interacted(true);
        notification.setIs_viewed(true);
        notificationRepos.saveAndFlush(notification);
    }


    ////////////////////////////////////////////// for socket //////////////////////////////////////////////

    /**
     * @param receiver:      user receiver
     * @param socketMessage: data to save to database
     */
    public NotificationDTO saveNew(User receiver, SocketMessage socketMessage) {
        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        User sender = userRepos.findById(socketMessage.getUserId()).get();

        String imgUrl = socketMessage.getImage_url();
        if (socketMessage.getImage_url().equals("null")) imgUrl = null;

        NotificationTypes notificationTypes = new NotificationTypes();
        Notifications notifications = new Notifications();

        notificationTypes.setType(socketMessage.getType());

        notifications.setNotification_type(notificationTypes);
        notifications.setContent(socketMessage.getMessage());
        notifications.setImage_url(imgUrl);
        notifications.setFrom_user(sender);
        notifications.setTo_user(receiver);
        notifications.setIs_viewed(false);
        notifications.setIs_interacted(false);
        notifications.setIs_delete(false);
        notifications.setCreated_at(currentTime);


        if (socketMessage.getObjData().get("target_id").equals("") || !socketMessage.getObjData().get("target_title").equals("")) {
            Long targetId = Long.parseLong(String.valueOf(socketMessage.getObjData().get("target_id")));
            String targetTitle = String.valueOf(socketMessage.getObjData().get("target_title"));
            Long toUserId = receiver.getUser_id();

            Boolean isExisted = checkIsExisted(targetTitle, targetId, toUserId);
            if (isExisted) return null;


            notifications.setTarget_id(targetId);
            notifications.setTarget_title(targetTitle);
        }

        notificationTypesRepos.saveAndFlush(notificationTypes);
        notificationRepos.saveAndFlush(notifications);


        NotificationDTO dataToSend = new NotificationDTO();
        dataToSend.setNotification_id(notifications.getNotification_id());
        dataToSend.setNotification_content(notifications.getContent());
        dataToSend.setImage_url(notifications.getImage_url());
        dataToSend.setCreated_at(notifications.getCreated_at());
        dataToSend.setTarget_id(notifications.getTarget_id());
        dataToSend.setTarget_title(notifications.getTarget_title());
        dataToSend.setIs_viewed(notifications.getIs_viewed());
        dataToSend.setIs_interacted(notifications.getIs_interacted());

        dataToSend.setNotification_type_id(notificationTypes.getNotification_type_id());
        dataToSend.setNotification_type(notificationTypes.getType());

        dataToSend.setSender_id(sender.getUser_id());
        dataToSend.setSender_name(sender.getUser_name());

        dataToSend.setReceiver_id(receiver.getUser_id());
        dataToSend.setReceiver_name(receiver.getUser_name());
        dataToSend.setReceiver_socket_id(receiver.getSocket_session_id());

        return dataToSend;
    }


    //////////////////// HELPERS ////////////////////
    Boolean checkIsExisted(String targetTitle, Long targetId, Long toUserId) {
        if (targetTitle.equals("user")) {
            List<Notifications> isExisted = notificationRepos.findByTargetTitleUserAndNotInteract(targetId);
            if (!isExisted.isEmpty()) return true;

        } else if (targetTitle.equals("transgroup")) {
            List<Notifications> isExisted = notificationRepos.findByTargetTitleTransGroupAndNotInteract(targetId, toUserId);
            if (!isExisted.isEmpty()) return true;
        }

        return false;
    }

}
