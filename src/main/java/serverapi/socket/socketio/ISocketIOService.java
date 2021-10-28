package serverapi.socket.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import serverapi.socket.message.SocketMessage;

import java.util.Collection;

public interface ISocketIOService {
    /**
     * Start Services
     */
    void start();

    /**
     * Out of Service
     */
    void stop();


    void pushMessageToUsersExceptSender(SocketMessage socketMessage, Collection<SocketIOClient> clients , SocketIOClient senderClient);

    void pushMessageToAllUsersExceptSender(SocketMessage socketMessage, Collection<SocketIOClient> clients , SocketIOClient senderClient);
}