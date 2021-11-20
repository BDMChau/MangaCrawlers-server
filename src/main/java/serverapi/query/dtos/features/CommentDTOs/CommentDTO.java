package serverapi.query.dtos.features.CommentDTOs;

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
public class CommentDTO {

    private Long user_id;
    private String user_name;
    private String user_avatar;

    private Long manga_id;

    private Long chapter_id;
    private String chapter_name;
    private Calendar created_at;

    private Long post_id;
    private String post_content;

    private Long comment_id;
    private Calendar comment_time;
    private String comment_content;
    private int count_like;
    private boolean is_deprecated;

    private List<CommentTagsDTO> to_users = new ArrayList<>();

    private Long comment_relation_id;
    private Long parent_id;
    private Long child_id;
    private String level;
    private Long target_id;

    private String title;

    private Long count_comments_child;

    private Long comment_image_id;
    private String image_url;

    private Long user_id_is_liked;
    private String is_liked;

    // For comment level 0
    public CommentDTO(Long count_comments_child,
                      Long user_id, String user_name, String user_avatar,
                      Long comment_id, Calendar comment_time, String comment_content, int count_like, boolean is_deprecated,
                      Long parent_id,
                      Long comment_image_id, String image_url) {
        this.count_comments_child = count_comments_child;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;
        this.comment_id = comment_id;
        this.comment_time = comment_time;
        this.comment_content = comment_content;
        this.count_like = count_like;
        this.is_deprecated = is_deprecated;
        this.parent_id = parent_id;
        this.comment_image_id = comment_image_id;
        this.image_url = image_url;
    }

    public CommentDTO(
            Long user_id, String user_name, String user_avatar,
            Long manga_id, Long post_id,
            Long comment_id, Calendar comment_time, String comment_content, int count_like, boolean is_deprecated,
            Long parent_id,
            Long comment_image_id, String image_url) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;
        this.manga_id = manga_id;
        this.post_id = post_id;
        this.comment_id = comment_id;
        this.comment_time = comment_time;
        this.comment_content = comment_content;
        this.count_like = count_like;
        this.is_deprecated = is_deprecated;
        this.parent_id = parent_id;
        this.comment_image_id = comment_image_id;
        this.image_url = image_url;
    }

    // For comments child
    public CommentDTO(
            Long user_id, String user_name, String user_avatar,
            Long comment_id, Calendar comment_time, String comment_content, int count_like, boolean is_deprecated,
            Long parent_id,
            Long comment_image_id, String image_url) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;
        this.comment_id = comment_id;
        this.comment_time = comment_time;
        this.comment_content = comment_content;
        this.count_like = count_like;
        this.is_deprecated = is_deprecated;
        this.parent_id = parent_id;
        this.comment_image_id = comment_image_id;
        this.image_url = image_url;
    }
}
