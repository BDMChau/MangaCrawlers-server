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
import serverapi.tables.manga_tables.manga.pojo.FriendPOJO;

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

    @GetMapping("/get_list_friends")
    public ResponseEntity getListFriends(@RequestParam int from, int amount, ServletRequest request) {
        Long userID = 0L;
        String sUserId = getUserAttribute(request).get("user_id").toString();
        if (!sUserId.isEmpty()) {
            userID = Long.parseLong(sUserId);
        }

        return friendService.getListFriends(userID, from, amount);
    }

    @PostMapping("/unfriend")
    public ResponseEntity unFriend(ServletRequest request, @RequestBody FriendPOJO friendPOJO) {
        String sUserId = getUserAttribute(request).get("user_id").toString();
        Long toUserID = 0L;
        List<FriendDTO> listFriends = friendPOJO.getListFriends();

        if (sUserId.isEmpty() || friendPOJO.getTo_user_id().isEmpty()) {
            Map<String, Object> err = Map.of("err", "User or target user is empty!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }
        Long userID = Long.parseLong(sUserId);
        toUserID = Long.parseLong(friendPOJO.getTo_user_id());

        return friendService.unfriend(userID, toUserID, listFriends);
    }

    @PostMapping("/add_friend")
    public ResponseEntity addFriend(ServletRequest request, @RequestBody FriendPOJO friendPOJO) {
        String sUserId = getUserAttribute(request).get("user_id").toString();
        Long toUserID = 0l;
        Long userID = 0L;

        if (sUserId.isEmpty() || friendPOJO.getTo_user_id().isEmpty()) {
            Map<String, Object> err = Map.of("err", "User or target user is empty!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }
        userID = Long.parseLong(sUserId);
        toUserID = Long.parseLong(friendPOJO.getTo_user_id());

        return friendService.addFriend(userID, toUserID);
    }

    @PostMapping("/check_status")
    public ResponseEntity checkStatus(ServletRequest request, @RequestBody FriendPOJO friendPOJO) {
        String sUserId = getUserAttribute(request).get("user_id").toString();
        if (sUserId.isEmpty() || friendPOJO.getTo_user_id().isEmpty() || friendPOJO.getStatus_id().isEmpty() || friendPOJO.getStatus_id().equals("")) {
            Map<String, Object> err = Map.of("err", "Missing credential!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }
        Long senderID = Long.parseLong(sUserId);
        Long receiverID = Long.parseLong(friendPOJO.getTo_user_id());

        int checkStatus = friendService.checkStatus(senderID, receiverID);
        String exportCheck = switch (checkStatus) {
            case 0 -> "Add friend";
            case 1 -> "Pending";
            case 2 -> "Friend";
            case 3 -> "accept friend";
            default -> "";
        };

        if (exportCheck.equals("")) {
            Map<String, Object> err = Map.of("err", "Error when check status!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
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