package serverapi.socket02;

import com.corundumstudio.socketio.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class SocketIOOService {

    @Bean
    void start(){
        System.err.println("////////////////////////////////////");

        Configuration config = new Configuration();
        config.setPort(4444);
        config.setHostname("localhost");
        SocketIOOServer server = new SocketIOOServer(config);

        server.start();
        server.addConnectListener(client ->{
            System.err.println(client);

            client.sendEvent("newMessage", "AAAAAAAAAAAAAA");

            System.err.println("client");
        });

        System.err.println("////////////////////////////////////");
    }


}
