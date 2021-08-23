package serverapi.socket;

import io.socket.emitter.Emitter;
import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoSocket;
import io.socket.engineio.server.EngineIoWebSocket;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

@WebServlet(value = "/socket.io/*", asyncSupported = true)
public class SocketIoServlet extends HttpServlet {

    private final EngineIoServer mEngineIoServer = new EngineIoServer();
    private final SocketIoServer mSocketIoServer =new SocketIoServer();
    private final EngineIoWebSocket socket;

    public SocketIoServlet(EngineIoWebSocket socket) {
        this.socket = socket;
    }

    protected void service() throws IOException {
        mEngineIoServer.handleWebSocket(socket);

        mEngineIoServer.on("connection", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                EngineIoSocket socket = (EngineIoSocket) args[0];
                System.out.println(socket);
                System.out.println("???");
            }
        });
    }
}
