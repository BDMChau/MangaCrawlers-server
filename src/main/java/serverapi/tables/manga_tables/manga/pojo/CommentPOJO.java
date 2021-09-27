package serverapi.tables.manga_tables.manga.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class CommentPOJO {

    private String to_user_id;
    private String manga_id;
    private String chapter_id;
    private String manga_comment_id;
    private String manga_comment_content;
    private MultipartFile image_url;
    private String level;
    private String parent_id;

    private int from;
    private int amount;

}
