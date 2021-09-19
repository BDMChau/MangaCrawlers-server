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
public class MangaCommentDTOs {


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

    private List<CommentTreesDTO> comments_level_01 = new ArrayList<>();

    /**
     * Constructor for query comments get by manga_id
     * @param user_id
     * @param user_name
     * @param user_avatar
     * @param manga_id
     * @param chapter_id
     * @param chapter_name
     * @param created_at
     * @param manga_comment_id
     * @param manga_comment_time
     * @param manga_comment_content
     * @param to_user_id
     * @param to_user_name
     * @param to_user_avatar
     * @param manga_comment_relation_id
     * @param parent_id
     * @param child_id
     * @param level
     * @param manga_comment_image_id
     * @param image_url
     */
    public MangaCommentDTOs(Long user_id, String user_name, String user_avatar,
                            Long manga_id, Long chapter_id, String chapter_name, Calendar created_at,
                            Long manga_comment_id, Calendar manga_comment_time, String manga_comment_content,
                            Long to_user_id, String to_user_name, String to_user_avatar,
                            Long manga_comment_relation_id, Long parent_id, Long child_id, String level,
                            Long manga_comment_image_id, String image_url) {

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;

        this.manga_id = manga_id;

        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.created_at = created_at;

        this.manga_comment_id = manga_comment_id;
        this.manga_comment_time = manga_comment_time;
        this.manga_comment_content = manga_comment_content;
        this.to_user_id = to_user_id;
        this.to_user_name = to_user_name;
        this.to_user_avatar = to_user_avatar;

        this.manga_comment_relation_id = manga_comment_relation_id;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.level = level;

        this.manga_comment_image_id = manga_comment_image_id;
        this.image_url = image_url;
    }

    /**
     * Constructor for query comments get by chapter_id
     * @param user_id
     * @param user_name
     * @param user_avatar
     * @param manga_id
     * @param chapter_id
     * @param manga_comment_id
     * @param manga_comment_time
     * @param manga_comment_content
     * @param to_user_id
     * @param to_user_name
     * @param to_user_avatar
     * @param manga_comment_relation_id
     * @param parent_id
     * @param child_id
     * @param level
     * @param manga_comment_image_id
     * @param image_url
     */
    public MangaCommentDTOs(Long user_id, String user_name, String user_avatar,
                            Long manga_id,
                            Long chapter_id,
                            Long manga_comment_id, Calendar manga_comment_time, String manga_comment_content,
                            Long to_user_id, String to_user_name, String to_user_avatar,
                            Long manga_comment_relation_id, Long parent_id, Long child_id, String level,
                            Long manga_comment_image_id, String image_url) {

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;

        this.manga_id = manga_id;

        this.chapter_id = chapter_id;

        this.manga_comment_id = manga_comment_id;
        this.manga_comment_time = manga_comment_time;
        this.manga_comment_content = manga_comment_content;

        this.to_user_id = to_user_id;
        this.to_user_name = to_user_name;
        this.to_user_avatar = to_user_avatar;

        this.manga_comment_relation_id = manga_comment_relation_id;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.level = level;

        this.manga_comment_image_id = manga_comment_image_id;
        this.image_url = image_url;
    }


    /**
     * Constructor for manga comments
     * @param user_id
     * @param user_name
     * @param user_avatar
     * @param manga_id
     * @param chapter_id
     * @param chapter_name
     * @param created_at
     * @param manga_comment_id
     * @param manga_comment_time
     * @param manga_comment_content
     * @param to_user_id
     * @param to_user_name
     * @param to_user_avatar
     * @param manga_comment_relation_id
     * @param parent_id
     * @param child_id
     * @param level
     * @param manga_comment_image_id
     * @param image_url
     * @param comments_level_01
     */
    public MangaCommentDTOs(Long user_id, String user_name, String user_avatar, Long manga_id,
                            Long chapter_id, String chapter_name, Calendar created_at,
                            Long manga_comment_id, Calendar manga_comment_time, String manga_comment_content,
                            Long to_user_id, String to_user_name, String to_user_avatar,
                            Long manga_comment_relation_id, Long parent_id, Long child_id, String level,
                            Long manga_comment_image_id, String image_url,
                            List<CommentTreesDTO> comments_level_01) {

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;

        this.manga_id = manga_id;

        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.created_at = created_at;

        this.manga_comment_id = manga_comment_id;
        this.manga_comment_time = manga_comment_time;
        this.manga_comment_content = manga_comment_content;

        this.to_user_id = to_user_id;
        this.to_user_name = to_user_name;
        this.to_user_avatar = to_user_avatar;

        this.manga_comment_relation_id = manga_comment_relation_id;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.level = level;

        this.manga_comment_image_id = manga_comment_image_id;
        this.image_url = image_url;

        this.comments_level_01 = comments_level_01;
    }

    /**
     * Constructor for chapter comments
     * @param user_id
     * @param user_name
     * @param user_avatar
     * @param manga_id
     * @param chapter_id
     * @param manga_comment_id
     * @param manga_comment_time
     * @param manga_comment_content
     * @param to_user_id
     * @param to_user_name
     * @param to_user_avatar
     * @param manga_comment_relation_id
     * @param parent_id
     * @param child_id
     * @param level
     * @param manga_comment_image_id
     * @param image_url
     * @param comments_level_01
     */
    public MangaCommentDTOs(Long user_id, String user_name, String user_avatar,
                            Long manga_id,
                            Long chapter_id,
                            Long manga_comment_id, Calendar manga_comment_time, String manga_comment_content,
                            Long to_user_id, String to_user_name, String to_user_avatar,
                            Long manga_comment_relation_id, Long parent_id, Long child_id, String level,
                            Long manga_comment_image_id, String image_url,
                            List<CommentTreesDTO> comments_level_01) {

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;

        this.manga_id = manga_id;

        this.chapter_id = chapter_id;

        this.manga_comment_id = manga_comment_id;
        this.manga_comment_time = manga_comment_time;
        this.manga_comment_content = manga_comment_content;

        this.to_user_id = to_user_id;
        this.to_user_name = to_user_name;
        this.to_user_avatar = to_user_avatar;

        this.manga_comment_relation_id = manga_comment_relation_id;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.level = level;

        this.manga_comment_image_id = manga_comment_image_id;
        this.image_url = image_url;

        this.comments_level_01 = comments_level_01;
    }
}
