package serverapi.query.dtos.tables;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentTreesDTO {

    private Long user_id;
    private String user_name;
    private String user_avatar;

    private Long manga_id;

    private Long chapter_id;
    private String chapter_name;
    private Calendar created_at;

    private Long manga_comment_id;
    private Calendar manga_comment_time;
    private String manga_comment_content;
    private Long to_user_id;
    private String to_user_name;
    private String to_user_avatar;

    private Long manga_comment_relation_id;
    private Long parent_id;
    private Long child_id;
    private String level;

    private Long manga_comment_image_id;
    private String image_url;

    List<CommentTreesDTO> comments_level02 = new ArrayList<>();

    /**
     * Constructor for
     */
}
