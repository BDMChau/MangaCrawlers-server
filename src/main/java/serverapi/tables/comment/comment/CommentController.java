package serverapi.tables.comment.comment;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import serverapi.tables.comment.pojo.CommentPOJO;
import serverapi.utils.UserHelpers;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
@CacheConfig(cacheNames = {"user"})
public class CommentController {
    private final CommentService commentService;
    private final UserHelpers userHelpers = new UserHelpers();
    private static final String fileNameDefault = "/static/media/8031DF085D7DBABC0F4B3651081CE70ED84622AE9305200F2FC1D789C95CF06F.9960248d.svg";


    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // target_title: String
    // content: String
    // stickerUrl: String
    // target_id: String
    // image: multipartFile
    // parent_id: String
    // to_users_id: Long array list
    @PostMapping("/add_comment")
    public ResponseEntity addComment(@Valid CommentPOJO commentPOJO, ServletRequest request) throws IOException {
        String target_title = commentPOJO.getTarget_title();
        String content = commentPOJO.getComment_content();
        String stickerUrl = commentPOJO.getSticker_url();
        //targetID
        String target_id = commentPOJO.getTarget_id();
        Long targetID = 0L;
        if (!target_id.equals("")) {
            targetID = Long.parseLong(target_id);
        }
        // userID
        String strUserID = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userID = Long.parseLong(strUserID);
        // image
        MultipartFile image = commentPOJO.getImage();
        if (Objects.equals(image.getOriginalFilename(), fileNameDefault)) {
            image = null;
        }
        //parentID
        String parent_id = commentPOJO.getParent_id();
        Long parentID = 0L;
        if (!parent_id.equals("")) {
            parentID = Long.parseLong(commentPOJO.getParent_id());
        }
        // toUsersID
        List<String> to_users_id = commentPOJO.getTo_users_id();
        List<Long> toUsersID = new ArrayList<>();
        if (!to_users_id.isEmpty()) {
            to_users_id.forEach(item -> {
                Long to_user = Long.parseLong(item);
                toUsersID.add(to_user);
            });
        }

        return commentService.addComment(target_title, targetID, parentID, userID, toUsersID, content, image, stickerUrl);
    }


    // content: String
    // stickerUrl: String
    // image: multipartFile
    // comment_id: String
    // to_users_id: Long array list
    @PutMapping("/update_comment")
    public ResponseEntity updateComment(@Valid CommentPOJO commentPOJO, ServletRequest request) throws IOException {
        // content, sticker, image
        String content = commentPOJO.getComment_content();
        String stickerUrl = commentPOJO.getSticker_url();
        MultipartFile image = commentPOJO.getImage();
        if (Objects.equals(image.getOriginalFilename(), fileNameDefault)) {
            image = null;
        }
        // user_id
        String user_id = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userID = Long.parseLong(user_id);
        //comment_id
        String comment_id = commentPOJO.getComment_id();
        Long commentID = 0L;
        if(!comment_id.equals("")){
            commentID = Long.parseLong(comment_id);
        }
        //to_user_id
        List<String> to_users_id = commentPOJO.getTo_users_id();
        List<Long> toUsersID = new ArrayList<>();
        if (!to_users_id.isEmpty()) {
            to_users_id.forEach(item -> {
                Long to_user = Long.parseLong(item);
                toUsersID.add(to_user);
            });
        }

        return commentService.updateComment(userID, toUsersID, commentID, content, image, stickerUrl);
    }

    // comment_id: String
    @DeleteMapping("/delete_comment")
    public ResponseEntity deleteComment(@RequestBody CommentPOJO commentPOJO, ServletRequest request) {
        String strUserID = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userID = Long.parseLong(strUserID);

        String comment_id = commentPOJO.getComment_id();
        Long commentID = 0L;
        if (!comment_id.equals("")) {
            commentID = Long.parseLong(comment_id);
        }

        return commentService.deleteComment(userID, commentID);
    }
}
