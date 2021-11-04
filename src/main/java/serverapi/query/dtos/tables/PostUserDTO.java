package serverapi.query.dtos.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.forum.category.Category;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class PostUserDTO {
    Long post_id;
    String title;
    String content;
    Calendar created_at;

    Long cate_id;
    String cate_name;
    String cate_color;
    String cate_desc;
    List<Category> categoryList;

    Long user_id;
    String user_name;
    String user_email;
    String user_avatar;
    Boolean user_isAdmin;
    Map user;

    public PostUserDTO(Long post_id, String title, String content, Calendar created_at, List<Category> categoryList, Map user) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.categoryList = categoryList;
        this.created_at = created_at;
        this.user = user;
    }

    public PostUserDTO(Long post_id, String title, String content, Calendar created_at, Long cate_id, String cate_name, String cate_color, String cate_desc, Long user_id, String user_name, String user_email, String user_avatar, Boolean user_isAdmin) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.created_at = created_at;

        this.cate_id = cate_id;
        this.cate_name = cate_name;
        this.cate_color = cate_color;
        this.cate_desc = cate_desc;

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;
        this.user_isAdmin = user_isAdmin;
    }

}
