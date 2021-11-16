package serverapi.tables.comment.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.query.dtos.features.CommentDTOs.CommentDTOs;
import serverapi.tables.comment.pojo.CommentPOJO;

import java.util.List;

@RestController
@RequestMapping("/api/user_unauth")
@CacheConfig(cacheNames = {"user_unauth"})
public class CommentControllerUnAuth {
    private final CommentService commentService;

    @Autowired
    public CommentControllerUnAuth(
            CommentService commentService) {
        this.commentService = commentService;
    }

    // target_title: String
    // target_id: String
    // from: int
    // amount: int
    @GetMapping("/get_comments")
    public ResponseEntity getComments(CommentPOJO commentPOJO) {
        int from = commentPOJO.getFrom();
        int amount = commentPOJO.getAmount();
        String targetTitle = commentPOJO.getTarget_title();
        String target_id = commentPOJO.getTarget_id();
        Long targetID = 0L;
        if (!target_id.equals("")) {
            targetID = Long.parseLong(target_id);
        }
        return commentService.getComments(from, amount, targetTitle, targetID);
    }

    // comment_id: String
    // from: int
    // amount: int
    @GetMapping("/get_comments_child")
    public ResponseEntity getCommentsChild(CommentPOJO commentPOJO) {
        int from = commentPOJO.getFrom();
        int amount = commentPOJO.getAmount();
        String comment_id = commentPOJO.getComment_id();
        Long commentID = 0L;
        if (!comment_id.equals("")) {
            commentID = Long.parseLong(comment_id);
        }

        return commentService.getCommentsChild(from, amount, commentID);
    }


}
