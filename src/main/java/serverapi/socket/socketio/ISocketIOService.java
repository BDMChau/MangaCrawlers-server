package serverapi.socket.socketio;

import serverapi.socket.message.SocketMessage;

public interface ISocketIOService {
    /**
     * Start Services
     */
    void start();

    /**
     * Out of Service
     */
    void stop();

    /**
     * Push information to specified client
     *
     * @param socketMessage: SocketMessage class to receive message from client
     */
    void pushMessageToUser(SocketMessage socketMessage);
}