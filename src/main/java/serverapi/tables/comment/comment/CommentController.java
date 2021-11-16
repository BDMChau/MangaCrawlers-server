package serverapi.tables.comment.comment;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    UserHelpers userHelpers;
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
    public ResponseEntity addCommentManga(@Valid CommentPOJO commentPOJO, ServletRequest request) throws IOException {
        String target_title = commentPOJO.getTarget_title();
        String content = commentPOJO.getComment_content();
        String stickerUrl = commentPOJO.getSticker_url();
        //targetID
        String target_id = commentPOJO.getTarget_id();
        Long targetID = 0L;
        if(!target_id.equals("")){targetID = Long.parseLong(target_id);}
        // userID
        String strUserID = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userID = Long.parseLong(strUserID);
        // image
        MultipartFile image = commentPOJO.getImage();
        if (Objects.equals(image.getOriginalFilename(), fileNameDefault)) {image = null;}
        //parentID
        String parent_id = commentPOJO.getParent_id();
        Long parentID = 0L;
        if (!parent_id.equals("")) {parentID = Long.parseLong(commentPOJO.getParent_id());}
        // toUsersID
        List<String> to_users = commentPOJO.getTo_users_id();
        List<Long> toUsers = new ArrayList<>();
        if (!to_users.isEmpty()) {
            to_users.forEach(item -> {
                Long to_user = Long.parseLong(item);
                toUsers.add(to_user);
            });
        }

        return commentService.addCommentManga(target_title, targetID, parentID, userID, toUsers, content, image, stickerUrl);
    }
}
