package serverapi.socket;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverapi.query.repository.user.UserRepos;
import serverapi.socket.message.SocketMessage;
import serverapi.tables.user_tables.user.User;

import java.util.Optional;

@Setter
@NoArgsConstructor
@Service
public class MySocketService{
    private SocketIOClient client;
    private SocketMessage socketMessage;

    private UserRepos userRepos;

    @Autowired
    public MySocketService(UserRepos userRepos){
        this.userRepos = userRepos;
    }


    ////////////////////////////////////////////////////////

    public void test(){
        Long userId = Long.parseLong(String.valueOf(socketMessage.getUserId()));
        Optional<User> user = userRepos.findById(userId);
        System.out.println(user);


        System.out.println(socketMessage.getMessage());
        client.sendEvent("newMessageToClient", "Heyyyyyyyyy client");
    }
}
