package serverapi.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverapi.query.repository.user.UserRepos;
import serverapi.socket.message.EventsName;
import serverapi.socket.message.SocketMessage;
import serverapi.tables.user_tables.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Setter
@NoArgsConstructor
@Service
public class MySocketService {
    private EventsName EVENTs_NAME = new EventsName();

    private SocketMessage socketMessage;
    private SocketIOClient senderClient;
    private Collection<SocketIOClient> allClients;


    private UserRepos userRepos;

    @Autowired
    public MySocketService(UserRepos userRepos, SocketIOServer socketIOServer) {
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
        List identificationList = socketMessage.getUsersIdentification();
        Object message = socketMessage.getMessage();

        identificationList.forEach(identification -> {
            String type = identification.getClass().getName();

            if (type.equals("java.lang.String")) {
                String userEmail = String.valueOf(identification);
                Optional<User> userOptional = userRepos.findByEmail(userEmail);

                sendToUsersExceptSender(userOptional, message);
            } else if (type.equals("java.lang.Integer")) {
                Long userId = Long.parseLong(String.valueOf(identification));
                Optional<User> userOptional = userRepos.findById(userId);

                sendToUsersExceptSender(userOptional, message);
            }
        });
    }


    public void pushMessageToAllUsersExceptSender() {
        Object message = socketMessage.getMessage();

        sendAll(message);
    }


    ////////////////////////////////////////////////////////
    private void sendToUsersExceptSender(Optional<User> userOptional, Object message) {
        if (userOptional.isEmpty()) {
            senderClient.sendEvent(EVENTs_NAME.getSEND_FAILED(), "failed");

        } else {
            UUID sessionId = userOptional.get().getSocket_session_id();

            for (SocketIOClient client : allClients) {
                if (!client.getSessionId().equals(senderClient.getSessionId())) {
                    if (client.getSessionId().equals(sessionId)) {
                        client.sendEvent(EVENTs_NAME.getFROM_SERVER_TO_SPECIFIC_USERS(), message);
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
