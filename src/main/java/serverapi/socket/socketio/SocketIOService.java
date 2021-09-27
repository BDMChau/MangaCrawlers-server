package serverapi.socket.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverapi.socket.MySocketService;
import serverapi.socket.message.EventsName;
import serverapi.socket.message.SocketMessage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SocketIOService implements ISocketIOService {
    private EventsName EVENTs_NAME = new EventsName();
    private static Map<Object, SocketIOClient> clientMap = new ConcurrentHashMap<>();
    private SocketMessage socketMessage = new SocketMessage();

    private final MySocketService mySocketService;

    @Autowired
    public SocketIOService(MySocketService mySocketService) {
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


        ///////////// listen event from client /////////////
        socketIOServer.addEventListener(EVENTs_NAME.getUPDATE_SOCKETID(), Map.class, (client, data, ackSender) -> {
            if (data.get("user_id") == null) {
                stop();
                return;
            }
            Long userId = Long.parseLong(String.valueOf(data.get("user_id")));
            UUID sessionId = client.getSessionId();

            mySocketService.updateSocketId(userId, sessionId);
        });


        socketIOServer.addEventListener(EVENTs_NAME.getSPECIFIC_USERS(), Map.class, (client, data, ackSender) -> {
            List usersIdentification = (List) data.get("users_identification"); // an user_identification can be String user_email or Integer user_id
            String message = String.valueOf(data.get("message"));

            SocketIOClient senderClient = client;
            Collection<SocketIOClient> clients = socketIOServer.getAllClients();

            socketMessage.setUsersIdentification(usersIdentification);
            socketMessage.setMessage(message);
            pushMessageToUsersExceptSender(socketMessage, clients, senderClient);
        });


        socketIOServer.addEventListener(EVENTs_NAME.getALL_USERS(), Map.class, (client, data, ackSender) -> {
            String message = String.valueOf(data.get("message"));

            SocketIOClient senderClient = client;
            Collection<SocketIOClient> clients = socketIOServer.getAllClients();

            socketMessage.setMessage(message);
            pushMessageToAllUsersExceptSender(socketMessage, clients, senderClient);
        });




        socketIOServer.addDisconnectListener(client -> {
            client.disconnect();
            socketMessage.getUsersIdentification().forEach(identification -> {
                clientMap.remove(identification);
            });
        });


        socketIOServer.start();
    }



    ////////////////////////////////// prepare before call method in MySocketService class /////////////////////////////////////
    public void pushMessageToUsersExceptSender(SocketMessage socketMessage, Collection<SocketIOClient> clients, SocketIOClient senderClient) {
        mySocketService.setSocketMessage(socketMessage);
        mySocketService.setAllClients(clients);
        mySocketService.setSenderClient(senderClient);

        mySocketService.pushMessageToUsersExceptSender();
    }


    public void pushMessageToAllUsersExceptSender(SocketMessage socketMessage, Collection<SocketIOClient> clients, SocketIOClient senderClient) {
        mySocketService.setSocketMessage(socketMessage);
        mySocketService.setAllClients(clients);
        mySocketService.setSenderClient(senderClient);

        mySocketService.pushMessageToAllUsersExceptSender();
    }



    ///////////////// stuffs //////////////////
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
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



//        new Thread(() -> {
//            int i = 0;
//            while (true) {
//                try {
//                    Thread.sleep(3000);
//                    socketIOServer.getBroadcastOperations().sendEvent("sendToAllClients", "Broadcast message "); // send to all clients

//        Collection<SocketIOClient> clients = server.getAllClients();
//        for (SocketIOClient client : clients) {
//            if(client.getSessionId().equals(senderClient.getSessionId()) == false){ // send all clients except sender
//                client.sendEvent(Events.modifyTrain, modifiedTrain);
//            }
//        }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
