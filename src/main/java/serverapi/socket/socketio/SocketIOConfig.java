package serverapi.socket.socketio;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("socket")
public class SocketIOConfig {
//    private String host = System.getenv("HOST_PRODUCTION02");
    private final String host = "localhost";
    private final Integer port = 4445;
    private final int bossCount = 1;
    private final int workCount = 100;
    private final boolean allowCustomRequests = true;
    private final int upgradeTimeout = 1000000;
    private final int pingTimeout = 6000000;
    private final int pingInterval = 250000;
    private final int maxFramePayloadLength = 1000000;


    @Bean
    public SocketIOServer socketIOServer() {
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setSocketConfig(socketConfig);
        config.setHostname(host);
        config.setPort(port);
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);
        config.setMaxFramePayloadLength(maxFramePayloadLength);

        System.out.println("Socket server is initialized at port: " + port);
        return new SocketIOServer(config);
    }

}
