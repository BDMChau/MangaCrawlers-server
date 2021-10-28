package serverapi.tables.user_tables.friend_request_status;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverapi.query.repository.user.FriendRequestRepos;
import serverapi.socket.message.SocketMessage;
import serverapi.tables.user_tables.user.User;

import java.util.Calendar;
import java.util.TimeZone;

@Service
public class FriendRequestStatusService {


    private final FriendRequestRepos friendRequestRepos;

    @Autowired
    public FriendRequestStatusService(FriendRequestRepos friendRequestRepos) {
        this.friendRequestRepos = friendRequestRepos;
    }


    ////////////////////////// usable //////////////////////////
    public void saveNew(User sender, User reciever){
        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        FriendRequestStatus friendRequestStatus = new FriendRequestStatus();
        friendRequestStatus.setStatus(false);
        friendRequestStatus.setUser(sender);
        friendRequestStatus.setTo_user(reciever);
        friendRequestStatus.setTime_accepted(currentTime);

        friendRequestRepos.save(friendRequestStatus);
    }
}
