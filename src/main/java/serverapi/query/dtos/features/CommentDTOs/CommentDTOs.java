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
public class CommentDTOs {


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

    private List<CommentTagsDTO> to_users = new ArrayList<>();

    private Long comment_relation_id;
    private Long parent_id;
    private Long child_id;
    private String level;

    private Long comment_image_id;
    private String image_url;

    private List<CommentTreesDTO> comments_level_01 = new ArrayList<>();

    /**
     * Use for get manga comments
     * @param user_id
     * @param user_name
     * @param user_avatar
     * @param manga_id
     * @param comment_id
     * @param comment_time
     * @param comment_content
     * @param comment_relation_id
     * @param parent_id
     * @param child_id
     * @param level
     * @param comment_image_id
     * @param image_url
     */



    public CommentDTOs(Long user_id, String user_name, String user_avatar,
                       Long manga_id,
                       Long comment_id, Calendar comment_time, String comment_content,
                       Long comment_relation_id, Long parent_id, Long child_id, String level,
                       Long comment_image_id, String image_url) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;

        this.manga_id = manga_id;

        this.comment_id = comment_id;
        this.comment_time = comment_time;
        this.comment_content = comment_content;

        this.comment_relation_id = comment_relation_id;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.level = level;

        this.comment_image_id = comment_image_id;
        this.image_url = image_url;
    }

    /**
     * Use for get chapter comment
     * @param user_id
     * @param user_name
     * @param user_avatar
     * @param manga_id
     * @param chapter_id
     * @param chapter_name
     * @param created_at
     * @param comment_id
     * @param comment_time
     * @param comment_content
     * @param comment_relation_id
     * @param parent_id
     * @param child_id
     * @param level
     * @param comment_image_id
     * @param image_url
     */
    public CommentDTOs(Long user_id, String user_name, String user_avatar,
                       Long manga_id,
                       Long chapter_id, String chapter_name, Calendar created_at,
                       Long comment_id, Calendar comment_time, String comment_content,
                       Long comment_relation_id, Long parent_id, Long child_id, String level,
                       Long comment_image_id, String image_url) {

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;

        this.manga_id = manga_id;
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.created_at = created_at;

        this.comment_id = comment_id;
        this.comment_time = comment_time;
        this.comment_content = comment_content;

        this.comment_relation_id = comment_relation_id;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.level = level;

        this.comment_image_id = comment_image_id;
        this.image_url = image_url;
    }

    public CommentDTOs(Long user_id, String user_name, String user_avatar,
                       Long manga_id,
                       Long chapter_id, String chapter_name, Calendar created_at,
                       Long comment_id, Calendar comment_time, String comment_content,
                       List<CommentTagsDTO> to_users,
                       Long comment_relation_id, Long parent_id, Long child_id, String level,
                       Long comment_image_id, String image_url,
                       List<CommentTreesDTO> comments_level_01) {

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;

        this.manga_id = manga_id;

        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.created_at = created_at;

        this.comment_id = comment_id;
        this.comment_time = comment_time;
        this.comment_content = comment_content;

        this.to_users = to_users;

        this.comment_relation_id = comment_relation_id;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.level = level;

        this.comment_image_id = comment_image_id;
        this.image_url = image_url;

        this.comments_level_01 = comments_level_01;
    }

    /**
     * For chapter query
     * @param user_id
     * @param user_name
     * @param user_avatar
     * @param chapter_id
     * @param comment_id
     * @param comment_time
     * @param comment_content
     * @param to_users
     * @param comment_relation_id
     * @param parent_id
     * @param child_id
     * @param level
     * @param comment_image_id
     * @param image_url
     * @param comments_level_01
     */
    public CommentDTOs(Long user_id, String user_name, String user_avatar,
                       Long chapter_id,
                       Long comment_id, Calendar comment_time, String comment_content,
                       List<CommentTagsDTO> to_users,
                       Long comment_relation_id, Long parent_id, Long child_id, String level,
                       Long comment_image_id, String image_url,
                       List<CommentTreesDTO> comments_level_01) {

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;
        this.chapter_id = chapter_id;
        this.comment_id = comment_id;
        this.comment_time = comment_time;
        this.comment_content = comment_content;
        this.to_users = to_users;
        this.comment_relation_id = comment_relation_id;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.level = level;
        this.comment_image_id = comment_image_id;
        this.image_url = image_url;
        this.comments_level_01 = comments_level_01;
    }


    public CommentDTOs(Long user_id, String user_name, String user_avatar,
                       Long manga_id,
                       Long chapter_id,
                       Long post_id,
                       Long comment_id, Calendar comment_time, String comment_content,
                       Long comment_relation_id, Long parent_id, Long child_id, String level,
                       Long comment_image_id, String image_url) {

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;

        this.manga_id = manga_id;

        this.chapter_id = chapter_id;

        this.post_id = post_id;

        this.comment_id = comment_id;
        this.comment_time = comment_time;
        this.comment_content = comment_content;

        this.comment_relation_id = comment_relation_id;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.level = level;

        this.comment_image_id = comment_image_id;
        this.image_url = image_url;
    }

    public CommentDTOs(Long user_id, String user_name, String user_avatar,
                       Long post_id, String post_content,
                       Long comment_id, Calendar comment_time, String comment_content,
                       Long comment_relation_id, Long parent_id, Long child_id, String level,
                       Long comment_image_id, String image_url) {

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;

        this.post_id = post_id;
        this.post_content = post_content;

        this.comment_id = comment_id;
        this.comment_time = comment_time;
        this.comment_content = comment_content;

        this.comment_relation_id = comment_relation_id;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.level = level;

        this.comment_image_id = comment_image_id;
        this.image_url = image_url;
    }

    public CommentDTOs(Long user_id, String user_name, String user_avatar,
                       Long post_id, String post_content,
                       Long comment_id, Calendar comment_time, String comment_content,
                       List<CommentTagsDTO> to_users,
                       Long comment_relation_id, Long parent_id, Long child_id, String level,
                       Long comment_image_id, String image_url,
                       List<CommentTreesDTO> comments_level_01) {

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;

        this.post_id = post_id;
        this.post_content = post_content;

        this.comment_id = comment_id;
        this.comment_time = comment_time;
        this.comment_content = comment_content;

        this.to_users = to_users;

        this.comment_relation_id = comment_relation_id;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.level = level;

        this.comment_image_id = comment_image_id;
        this.image_url = image_url;

        this.comments_level_01 = comments_level_01;
    }




}
