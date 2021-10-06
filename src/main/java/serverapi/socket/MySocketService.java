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
import serverapi.tables.user_tables.notification.NotificationService;
import serverapi.tables.user_tables.user.User;

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

    @Autowired
    public MySocketService(UserRepos userRepos, NotificationService notificationService) {
        this.notificationService = notificationService;
        this.userRepos = userRepos;
    }


    ////////////////////////////////////////////////////////
    public void updateSocketId(Long userId, UUID sessionId) {
        Optional<User> userOptional = userRepos.findById(userId);
        if (!userOptional.isEmpty()) {
            User user = userOptional.get();
            user.setSocket_session_id(sessionId);

            userRepos.saveAndFlush(user);
            System.err.println("Updated socket sessionId of user_id: " + userId);
        }
    }


    public void pushMessageToUsersExceptSender() {
        List listToUser = socketMessage.getListTo();

        listToUser.forEach(userVal -> {
            String type = userVal.getClass().getName();

            NotificationDTO dataToSend = notificationService.saveNew(type, userVal, socketMessage);
            if (dataToSend != null) {
                sendToUsersExceptSender(dataToSend);
            }

        });
    }


    public void pushMessageToAllUsersExceptSender() {
        Object message = socketMessage.getMessage();

        sendAll(message);
    }


    ////////////////////////////////////////////////////////
    private void sendToUsersExceptSender(NotificationDTO dataToSend) {
        String receiverName = dataToSend.getReceiver_name();

        if (receiverName.isEmpty()) {
            senderClient.sendEvent(EVENTs_NAME.getSEND_FAILED(), "failed");

        } else {
            UUID receiver_sessionId = dataToSend.getReceiver_socket_id();

            for (SocketIOClient client : allClients) {
                if (!client.getSessionId().equals(senderClient.getSessionId())) {
                    if (client.getSessionId().equals(receiver_sessionId)) {
                        client.sendEvent(EVENTs_NAME.getFROM_SERVER_TO_SPECIFIC_USERS(), dataToSend);
                    }
                }
            }

            senderClient.sendEvent(EVENTs_NAME.getSEND_OK(), "ok");
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