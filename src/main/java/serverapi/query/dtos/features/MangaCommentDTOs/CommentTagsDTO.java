package serverapi.query.dtos.features.MangaCommentDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentTagsDTO {

    public String user_id;
    public String user_name;
    public String user_avatar;

    public int off_set;

    // for query only
    public CommentTagsDTO(String user_id, String user_name, String user_avatar,
                          int off_set) {

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;
        this.off_set = off_set;
    }

    // for dto response

    public CommentTagsDTO(String user_id, String user_name, String user_avatar) {

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;
    }
}
