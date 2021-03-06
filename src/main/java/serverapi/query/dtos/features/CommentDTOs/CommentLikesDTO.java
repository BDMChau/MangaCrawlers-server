package serverapi.query.dtos.features.CommentDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentLikesDTO {

    private Long comment_id;
    private Long comment_like_id;

    private Long user_id;
    private String user_name;
    private String user_avatar;

}
