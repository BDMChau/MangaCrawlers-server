package serverapi.tables.user_tables.user.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.api.Response;
import serverapi.query.dtos.features.FriendDTO;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CacheConfig(cacheNames = {"user"})
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

    @Cacheable(value = "listfriends", key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @GetMapping("/get_list_friends")
    public ResponseEntity getListFriends(@RequestParam int from, int amount, ServletRequest request) {
        Long userID = 0L;
        String sUserId = getUserAttribute(request).get("user_id").toString();
        if (!sUserId.isEmpty()) {
            userID = Long.parseLong(sUserId);
        }

        return friendService.getListFriends(userID, from, amount);
    }

    @CacheEvict(value = {"listfriends"}, key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @PostMapping("/unfriend")
    public ResponseEntity unFriend(ServletRequest request, @RequestBody String to_user_id, List<FriendDTO> listFriends) {
        String sUserId = getUserAttribute(request).get("user_id").toString();
        if (sUserId.isEmpty() || to_user_id.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "User or target user is empty!"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        Long userID = Long.parseLong(sUserId);
        Long toUserID = Long.parseLong(to_user_id);

        return friendService.unFriend(userID, toUserID, listFriends);
    }

    @PostMapping("/check_status")
    public ResponseEntity checkStatus(ServletRequest request, @RequestBody String to_user_id, String status_id) {
        String sUserId = getUserAttribute(request).get("user_id").toString();
        if (sUserId.isEmpty() || to_user_id.isEmpty() || status_id.isEmpty() || status_id.equals("")) {
            Map<String, Object> msg = Map.of(
                    "msg", "Missing credential!"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        Long senderID = Long.parseLong(sUserId);
        Long receiverID = Long.parseLong(to_user_id);
        Long statusID = Long.parseLong(status_id);

        int checkStatus = friendService.checkStatus(senderID, receiverID);
        String exportCheck = switch (checkStatus) {
            case 0 -> "Add friend";
            case 1 -> "Pending";
            case 2 -> "Friend";
            default -> "";
        };

        if (exportCheck.equals("")) {
            Map<String, Object> msg = Map.of("err", "Error when check status!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Check status successfully!",
                "status_number", checkStatus,
                "status", exportCheck
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }
}