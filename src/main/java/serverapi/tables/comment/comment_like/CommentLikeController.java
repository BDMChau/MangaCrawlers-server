package serverapi.tables.comment.comment_like;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.api.Response;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CacheConfig(cacheNames = {"user"})
public class CommentLikeController {
    public final CommentLikeService commentLikeService;

    public CommentLikeController(CommentLikeService commentLikeService) {
        this.commentLikeService = commentLikeService;
    }

    public Map getUserAttribute(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        return (Map) req.getAttribute("user");
    }

    @PostMapping("/check_user_like")
    public ResponseEntity checkUserLike(@RequestBody Map data, ServletRequest request) {
        String comment_id = String.valueOf(data.get("comment_id"));

        Long userID;
        Long commentID = 0L;
        String sUserId = getUserAttribute(request).get("user_id").toString();
        if (sUserId.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "User empty!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        userID = Long.parseLong(sUserId);
        if (!comment_id.isEmpty()) {
            commentID = Long.parseLong(comment_id);
        }

        return commentLikeService.checkUserLike(userID, commentID);
    }



    @PostMapping("/add_like")
    public ResponseEntity addLike(@RequestBody Map data, ServletRequest request) {
        String comment_id = String.valueOf(data.get("comment_id"));

        Long userID = 0L;
        Long commentID = 0L;
        String sUserId = getUserAttribute(request).get("user_id").toString();
        if (!sUserId.isEmpty()) {
            userID = Long.parseLong(sUserId);
        }
        if (!comment_id.isEmpty()) {
            commentID = Long.parseLong(comment_id);
        }

        return commentLikeService.addLike(userID, commentID);
    }

    @PostMapping("/unlike")
    public ResponseEntity unLike(@RequestBody Map data, ServletRequest request) {
        String comment_id = String.valueOf(data.get("comment_id"));

        Long userID = 0L;
        Long commentID = 0L;
        String sUserId = getUserAttribute(request).get("user_id").toString();
        if (!sUserId.isEmpty()) {
            userID = Long.parseLong(sUserId);
        }
        if (!comment_id.isEmpty()) {
            commentID = Long.parseLong(comment_id);
        }

        return commentLikeService.unLike(userID, commentID);
    }
}
