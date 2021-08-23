package serverapi.socket;

import org.springframework.context.annotation.Configuration;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class ApplicationServerConfig implements ServerApplicationConfig {

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
        final HashSet<ServerEndpointConfig> result = new HashSet<>();
        result.add(ServerEndpointConfig.Builder
                .create(EngineIoEndpoint.class, "/socket.io/*")
                .build());

        return result;
    }

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
        return null;
    }

}
