package serverapi.tables.comment.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import serverapi.query.dtos.features.CommentDTOs.CommentDTOs;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CommentPOJO {

    private List<String> to_users_id = new ArrayList<>();
    private String manga_id;
    private String chapter_id;
    private String post_id;
    private String comment_id;
    private String comment_content;
    private MultipartFile image;
    private String sticker_url;
    private String level;
    private String parent_id;
    private int key;
    private String target_title;
    private String target_id;

    private List<CommentDTOs> comments;
    private int from;
    private int amount;

}
