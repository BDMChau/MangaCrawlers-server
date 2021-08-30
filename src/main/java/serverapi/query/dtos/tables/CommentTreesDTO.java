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
    private String user_email;
    private String user_avatar;

    private Long manga_id;

    private Long chapter_id;
    private String chapter_name;
    private Calendar created_at;

    private Long manga_comment_id;
    private Calendar manga_comment_time;
    private String manga_comment_content;

    private Long manga_comment_image_id;
    private String image_url;

    List<CommentTreesDTO> terraces_comments_02nd = new ArrayList<>();

    //for chapter's comment child level 02
    public CommentTreesDTO(Long user_id, String user_name, String user_email, String user_avatar,
                           Long manga_id,
                           Long chapter_id,
                           Long manga_comment_id, Calendar manga_comment_time, String manga_comment_content,
                           Long manga_comment_image_id, String image_url,
                           List<CommentTreesDTO> terraces_comments_02nd) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;

        this.manga_id = manga_id;

        this.chapter_id = chapter_id;

        this.manga_comment_id = manga_comment_id;
        this.manga_comment_time = manga_comment_time;
        this.manga_comment_content = manga_comment_content;

        this.manga_comment_image_id = manga_comment_image_id;
        this.image_url = image_url;

        this.terraces_comments_02nd = terraces_comments_02nd;
    }

    //for manga's comment child level 02
    public CommentTreesDTO(Long user_id, String user_name, String user_email, String user_avatar,
                           Long manga_id,
                           Long chapter_id, String chapter_name, Calendar created_at,
                           Long manga_comment_id, Calendar manga_comment_time, String manga_comment_content,
                           Long manga_comment_image_id, String image_url,
                           List<CommentTreesDTO> terraces_comments_02nd) {

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;

        this.manga_id = manga_id;

        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.created_at = created_at;

        this.manga_comment_id = manga_comment_id;
        this.manga_comment_time = manga_comment_time;
        this.manga_comment_content = manga_comment_content;


        this.manga_comment_image_id = manga_comment_image_id;
        this.image_url = image_url;

        this.terraces_comments_02nd = terraces_comments_02nd;
    }
}
