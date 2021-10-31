package serverapi.tables.user_tables.friend_request_status;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverapi.query.repository.user.FriendRequestRepos;
import serverapi.socket.message.SocketMessage;
import serverapi.tables.user_tables.user.User;

import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class FriendRequestStatusService {


    private final FriendRequestRepos friendRequestRepos;

    @Autowired
    public FriendRequestStatusService(FriendRequestRepos friendRequestRepos) {
        this.friendRequestRepos = friendRequestRepos;
    }


    ////////////////////////// usable //////////////////////////
    public void updateDeclineReq(Long senderID, Long receiverID) {
        FriendRequestStatus friendRequestStatus = friendRequestRepos.getFriendStatus(senderID, receiverID).get();

        friendRequestStatus.setStatus(false);
        friendRequestRepos.saveAndFlush(friendRequestStatus);
    }

    public void saveNew(User sender, User reciever) {
        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        FriendRequestStatus friendRequestStatus = new FriendRequestStatus();
        friendRequestStatus.setStatus(true);
        friendRequestStatus.setUser(sender);
        friendRequestStatus.setTo_user(reciever);

        friendRequestRepos.save(friendRequestStatus);
    }
}
