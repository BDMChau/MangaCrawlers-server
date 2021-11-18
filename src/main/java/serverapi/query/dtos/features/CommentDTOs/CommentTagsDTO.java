package serverapi.query.dtos.features.CommentDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentTagsDTO {

    private Long comment_id;

    private Long comment_tag_id;

    private Long user_id;
    private String user_name;
    private String user_avatar;

    private int off_set;

    public CommentTagsDTO(Long user_id, String user_name, String user_avatar, Long comment_tag_id) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;
        this.comment_tag_id = comment_tag_id;
    }

    public CommentTagsDTO(Long comment_tag_id, Long comment_id, Long user_id, String user_name, String user_avatar, int off_set) {
        this.comment_tag_id = comment_tag_id;
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;
        this.off_set = off_set;
    }
}
