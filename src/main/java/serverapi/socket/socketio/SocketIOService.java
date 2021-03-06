package serverapi.socket.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverapi.helpers.ReadJSONFileAndGetValue;
import serverapi.socket.MySocketService;
import serverapi.socket.message.EventsName;
import serverapi.socket.message.SocketMessage;
import serverapi.tables.user_tables.user.User;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SocketIOService implements ISocketIOService {
    private static String imgNotification_default = "https://res.cloudinary.com/mangacrawlers/image/upload/v1632847306/notification_imgs/default/notification.svg";
    private final EventsName EVENTs_NAME = new EventsName();
    private static final Map<Object, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    List<User> usersConnected = new ArrayList<>();

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

            SocketIOClient senderClient = client;
            Collection<SocketIOClient> clients = socketIOServer.getAllClients();

            if (data.get("is_disconnect").equals(true)) {
                mySocketService.updateSocketId(userId, new UUID(0L, 0L));

                SocketMessage socketMessage = new SocketMessage();
                socketMessage.setUserId(userId);
                socketMessage.setMessage("notify_onl_off");
                pushMessageToUsersExceptSender(socketMessage, clients, senderClient);
                return;
            }

            mySocketService.updateSocketId(userId, sessionId);

            SocketMessage socketMessage = new SocketMessage();
            socketMessage.setUserId(userId);
            socketMessage.setMessage("notify_onl_off");
            pushMessageToUsersExceptSender(socketMessage, clients, senderClient);
        });


        socketIOServer.addEventListener(EVENTs_NAME.getSPECIFIC_USERS(), Map.class, (client, data, ackSender) -> {
            try {
                Integer type = (Integer) data.get("type");
                String message = String.valueOf(data.get("message"));
                Long userId = Long.parseLong(String.valueOf(data.get("user_id")));
                List listTo = (List) data.get("list_to"); // can be String user_email or Integer user_id
                Map objData = (Map) data.get("obj_data");
                String imageUrl = String.valueOf(data.get("image_url"));
                if (imageUrl.equals("")) imageUrl = imgNotification_default;


                SocketIOClient senderClient = client;
                Collection<SocketIOClient> clients = socketIOServer.getAllClients();

                usersConnected = listTo;

                SocketMessage socketMessage = new SocketMessage();
                socketMessage.setType(type);
                socketMessage.setUserId(userId);
                socketMessage.setListTo(listTo);
                socketMessage.setMessage(message);
                socketMessage.setImage_url(imageUrl);
                socketMessage.setObjData(objData);

                pushMessageToUsersExceptSender(socketMessage, clients, senderClient);
            } catch (Exception ex) {
                System.err.println("socket err: " + ex);
            }
        });


        socketIOServer.addDisconnectListener(client -> {
            client.disconnect();
            if (!usersConnected.isEmpty()) {
                usersConnected.forEach(clientMap::remove);
            }
        });


        socketIOServer.start();
    }


    ////////////////////////////////// prepare before call method in MySocketService class /////////////////////////////////////
    public void pushMessageToUsersExceptSender(SocketMessage socketMessage, Collection<SocketIOClient> clients, SocketIOClient senderClient) {
        mySocketService.setSocketMessage(socketMessage);
        mySocketService.setAllClients(clients);
        mySocketService.setSenderClient(senderClient);

        String targetTitle = "";
        if (socketMessage.getObjData() != null) targetTitle = (String) socketMessage.getObjData().get("target_title");

        if (socketMessage.getMessage().equals("notify_onl_off")) {
            mySocketService.notifyFriendsWhenUserOnline();

        } else if (targetTitle.contains("comment")) { // can be comment_post or comment_manga
            mySocketService.notifyTaggedUsers();

        } else if (targetTitle.equals("post_new")) {
            mySocketService.notifyFriends();

        } else {
            // friend request
            mySocketService.pushMessageToUsersExceptSender();
        }
    }


    public void pushMessageToAllUsersExceptSender(SocketMessage socketMessage, Collection<SocketIOClient> clients, SocketIOClient senderClient) {
        mySocketService.setSocketMessage(socketMessage);
        mySocketService.setAllClients(clients);
        mySocketService.setSenderClient(senderClient);

        mySocketService.pushMessageToAllUsersExceptSender();
    }


    ///////////////// stuffs //////////////////
    private String getImageDefault(String objKey) {
        ReadJSONFileAndGetValue readJSONFileAndGetValue = new ReadJSONFileAndGetValue("src/main/java/serverapi/utils/img_notify.json", objKey);
        readJSONFileAndGetValue.read();

        return readJSONFileAndGetValue.getValue();
    }


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
