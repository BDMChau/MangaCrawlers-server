package serverapi.tables.user_tables.user.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FriendController {
    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    public Map getUserAttribute(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        Map user = (HashMap) req.getAttribute("user");
        return user;
    }


}
