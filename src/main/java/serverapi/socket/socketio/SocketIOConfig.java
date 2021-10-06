package serverapi.socket.socketio;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {
    private String host = System.getenv("HOST_PRODUCTION02");
    private Integer port = 4445;
    private int bossCount = 1;
    private int workCount = 100;
    private boolean allowCustomRequests = true;
    private int upgradeTimeout = 1000000;
    private int pingTimeout = 6000000;
    private int pingInterval = 250000;
    private int maxFramePayloadLength = 1000000;


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

        System.out.println("Socket server initialized at port: " + port);
        return new SocketIOServer(config);
    }

}
