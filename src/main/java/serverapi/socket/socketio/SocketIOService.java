package serverapi.socket.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverapi.socket.MySocketService;
import serverapi.socket.message.SocketMessage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SocketIOService implements ISocketIOService {
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();
    private SocketMessage socketMessage = new SocketMessage();

    private final MySocketService mySocketService;

    @Autowired
    public SocketIOService(MySocketService mySocketService){
        this.mySocketService = mySocketService;
    }

    @Autowired
    private SocketIOServer socketIOServer;



    @PostConstruct
    private void autoStartup() {
        start();
    }


    @PreDestroy
    private void autoStop() {
        stop();
    }

    @Override
    public void start() {
        socketIOServer.addConnectListener(client -> {
            // handle new connection
        });


        // listen event from client
        socketIOServer.addEventListener("sendMessageFromClient", Map.class, (client, data, ackSender) -> {
            String clientIp = getIpByClient(client);

            String userId = String.valueOf(data.get("userId"));
            String message = String.valueOf(data.get("message"));

            socketMessage.setUserId(userId);
            socketMessage.setMessage(message);

            if (!socketMessage.getUserId().isEmpty()) {
                clientMap.put(socketMessage.getUserId(), client);
                pushMessageToUser(socketMessage);
            }
        });


        socketIOServer.addDisconnectListener(client -> {
            String clientIp = getIpByClient(client);
            if (socketMessage.getUserId() != null) {
                clientMap.remove(socketMessage.getUserId());
                client.disconnect();
                System.err.println(socketMessage.getUserId() + " Disconnected!");
            }
        });

        socketIOServer.start();
//
//
//        new Thread(() -> {
//            int i = 0;
//            while (true) {
//                try {
//                    Thread.sleep(3000);
//                    socketIOServer.getBroadcastOperations().sendEvent("myBroadcast", "Broadcast message ");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }


    ///////////////////////////////////////////
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }


    public void pushMessageToUser(SocketMessage socketMessage) {
        SocketIOClient client = clientMap.get(socketMessage.getUserId());
        if (client != null) {
            mySocketService.setSocketMessage(socketMessage);
            mySocketService.setClient(client);

            mySocketService.test();

        }
    }



    private String getParamsByClient(SocketIOClient client) {
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();

        return null;
    }

    // Get the connected client ip address
    private String getIpByClient(SocketIOClient client) {
        String sa = client.getRemoteAddress().toString();
        String clientIp = sa.substring(1, sa.indexOf(":"));
        return clientIp;
    }

}
