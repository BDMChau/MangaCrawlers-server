package serverapi.socket02;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SocketIOService implements ISocketIOService {
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

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
            System.out.println("Client: " + getIpByClient(client) + " Connected!");

            client.sendEvent("newMessage", "You're connected successfully...");


            System.out.println("hello");
            String userId = getParamsByClient(client);
            System.out.println(userId);
            if (userId != null) {
                clientMap.put(userId, client);
                pushMessageToUser(userId, "hello");

            }
        });


        socketIOServer.addDisconnectListener(client -> {
            String clientIp = getIpByClient(client);
            String userId = getParamsByClient(client);
            System.out.println(userId);
            if (userId != null) {
                clientMap.remove(userId);
                client.disconnect();
            }
        });


        socketIOServer.addEventListener("newMessage", String.class, (client, data, ackSender) -> {
            String clientIp = getIpByClient(client);
        });

        socketIOServer.start();


        new Thread(() -> {
            int i = 0;
            while (true) {
                try {
                    Thread.sleep(3000);
                    socketIOServer.getBroadcastOperations().sendEvent("myBroadcast", "Broadcast message ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    @Override
    public void pushMessageToUser(String userId, String msgContent) {
        SocketIOClient client = clientMap.get(userId);
        if (client != null) {
            client.sendEvent("newMessage", "You're connected ebdfb...");
        }
    }



    ///////////////////////////////////////////

    private String getParamsByClient(SocketIOClient client) {
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> userIdList = params.get("userId");
        if (!CollectionUtils.isEmpty(userIdList)) {
            return userIdList.get(0);
        }
        return null;
    }

    // Get the connected client ip address
    private String getIpByClient(SocketIOClient client) {
        String sa = client.getRemoteAddress().toString();
        String clientIp = sa.substring(1, sa.indexOf(":"));
        return clientIp;
    }

}
