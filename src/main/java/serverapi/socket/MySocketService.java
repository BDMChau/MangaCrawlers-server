package serverapi.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverapi.query.dtos.tables.NotificationDTO;
import serverapi.query.repository.user.UserRepos;
import serverapi.socket.message.EventsName;
import serverapi.socket.message.SocketMessage;
import serverapi.tables.user_tables.friend_request_status.FriendRequestStatus;
import serverapi.tables.user_tables.friend_request_status.FriendRequestStatusService;
import serverapi.tables.user_tables.notification.NotificationService;
import serverapi.tables.user_tables.user.User;
import serverapi.tables.user_tables.user.friend.FriendService;

import java.util.*;


@Setter
@NoArgsConstructor
@Service
public class MySocketService {
    private EventsName EVENTs_NAME = new EventsName();

    private SocketMessage socketMessage;
    private SocketIOClient senderClient;
    private Collection<SocketIOClient> allClients;

    private UserRepos userRepos;

    private NotificationService notificationService;
    private FriendRequestStatusService friendRequestStatusService;
    private FriendService friendService;

    @Autowired
    public MySocketService(UserRepos userRepos, NotificationService notificationService, FriendRequestStatusService friendRequestStatusService, FriendService friendService) {
        this.notificationService = notificationService;
        this.userRepos = userRepos;
        this.friendRequestStatusService = friendRequestStatusService;
        this.friendService = friendService;
    }


    ////////////////////////////////////////////////////////
    public void updateSocketId(Long userId, UUID sessionId) {
        Optional<User> userOptional = userRepos.findById(userId);
        if (!userOptional.isEmpty()) {
            User user = userOptional.get();

            if (String.valueOf(sessionId).equals("00000000-0000-0000-0000-000000000000")) {
                user.setSocket_session_id(null);
            } else {
                user.setSocket_session_id(sessionId);
            }

            userRepos.saveAndFlush(user);
            System.err.println("Updated socket sessionId of user_id: " + userId);
        }
    }


    public void pushMessageToUsersExceptSender() {
        List listToUser = socketMessage.getListTo();

        listToUser.forEach(toUserVal -> {
            String identify_type = toUserVal.getClass().getName();
            int notification_type = socketMessage.getType();

            User receiver = getReciever(identify_type, toUserVal);
            if (receiver == null) {
                senderClient.sendEvent(EVENTs_NAME.getSEND_FAILED(), "Failed!");
                return;
            };

            Long senderId = socketMessage.getUserId();
            User sender = userRepos.findById(senderId).get();

            // check allow to send friend request
            if (identify_type.equals("java.lang.Integer") && socketMessage.getObjData().get("target_title").equals("user")) {
                Long receiverId = Long.parseLong(String.valueOf(toUserVal));

                int checkStatus = friendService.checkStatus(senderId, receiverId);
                if (checkStatus != 0) {
                    System.err.println("checkStatus  failed");
                    senderClient.sendEvent(EVENTs_NAME.getSEND_FAILED(), "Failed!");
                    return;
                }
            }


            handleNotificationType(notification_type, receiver, sender);

            NotificationDTO dataToSend = notificationService.saveNew(receiver, socketMessage);
            if (dataToSend == null) {
                System.err.println("datasToSend null");
                senderClient.sendEvent(EVENTs_NAME.getSEND_FAILED(), "failed");
                return;
            }

            sendToUsersExceptSender(dataToSend);
        });
    }


    public void pushMessageToAllUsersExceptSender() {
        Object message = socketMessage.getMessage();

        sendAll(message);
    }


    /////////////////////////////// services ///////////////////////////////
    private void handleNotificationType(int notificationType, User receiver, User sender) {
        if (notificationType == 2) {

            friendRequestStatusService.saveNew(sender, receiver);
        }
    }

    public User getReciever(String identify_type, Object toUserVal) {
        Optional<User> userOptional = Optional.empty();
        if (identify_type.equals("java.lang.String")) {
            String userEmail = String.valueOf(toUserVal);
            userOptional = userRepos.findByEmail(userEmail);

        } else if (identify_type.equals("java.lang.Integer")) {
            Long userId = Long.parseLong(String.valueOf(toUserVal));
            userOptional = userRepos.findById(userId);
        }

        if (userOptional.isEmpty()) return null;

        return userOptional.get();
    }

    ////////////////////////// sender //////////////////////////////
    private void sendToUsersExceptSender(NotificationDTO dataToSend) {
        String receiverName = dataToSend.getReceiver_name();

        if (receiverName.isEmpty()) {
            senderClient.sendEvent(EVENTs_NAME.getSEND_FAILED(), "failed");

        } else {
            UUID receiver_sessionId = dataToSend.getReceiver_socket_id();

            for (SocketIOClient client : allClients) {
                if (!client.getSessionId().equals(senderClient.getSessionId())) { // except sender
                    if (client.getSessionId().equals(receiver_sessionId)) {
                        client.sendEvent(EVENTs_NAME.getFROM_SERVER_TO_SPECIFIC_USERS(), dataToSend);
                    }
                }
            }

            senderClient.sendEvent(EVENTs_NAME.getSEND_OK(), "okkkkkk");
        }
    }


    private void sendAll(Object message) {
        for (SocketIOClient client : allClients) {
            if (client.getSessionId().equals(senderClient.getSessionId()) == false) {
                client.sendEvent(EVENTs_NAME.getFROM_SERVER_TO_SPECIFIC_USERS(), message);
            }
        }
    }

}
