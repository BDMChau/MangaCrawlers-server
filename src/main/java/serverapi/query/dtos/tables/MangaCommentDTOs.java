package serverapi.query.dtos.tables;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MangaCommentDTOs {


    private Long user_id;
    private String user_name;
    private String user_email;
    private String user_avatar;

    private Long manga_id;
    private String manga_name;
    private String status;
    private String description;
    private float stars;
    private Long views;
    private String thumbnail;
    private int date_publications;
    private Calendar manga_created_at;

    private Long chapter_id;
    private String chapter_name;
    private Calendar created_at;

    private Long manga_comment_id;
    private Calendar manga_comment_time;
    private String manga_comment_content;

    private Long manga_comment_relation_id;
    private Long parent_id;
    private Long child_id;


    public MangaCommentDTOs(Long user_id, String user_name, String user_email, String user_avatar,
                            Long manga_id, String manga_name, String status, String description,
                            float stars, Long views, String thumbnail, int date_publications, Calendar manga_created_at,
                            Long chapter_id, String chapter_name, Calendar created_at,
                            Long manga_comment_id, Calendar manga_comment_time, String manga_comment_content,
                            Long manga_comment_relation_id, Long parent_id, Long child_id) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;
        this.manga_id = manga_id;
        this.manga_name = manga_name;
        this.status = status;
        this.description = description;
        this.stars = stars;
        this.views = views;
        this.thumbnail = thumbnail;
        this.date_publications = date_publications;
        this.manga_created_at = manga_created_at;
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.created_at = created_at;
        this.manga_comment_id = manga_comment_id;
        this.manga_comment_time = manga_comment_time;
        this.manga_comment_content = manga_comment_content;
        this.manga_comment_relation_id = manga_comment_relation_id;
        this.parent_id = parent_id;
        this.child_id = child_id;
    }

    public MangaCommentDTOs(Long user_id, Long manga_id, Long chapter_id, Long manga_comment_id, String manga_comment_content, Long manga_comment_relation_id, Long parent_id, Long child_id) {
        this.user_id = user_id;
        this.manga_id = manga_id;
        this.chapter_id = chapter_id;
        this.manga_comment_id = manga_comment_id;
        this.manga_comment_content = manga_comment_content;
        this.manga_comment_relation_id = manga_comment_relation_id;
        this.parent_id = parent_id;
        this.child_id = child_id;
    }




}
