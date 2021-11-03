package serverapi.query.dtos.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.forum.category.Category;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class PostUserDTO {
    Long post_id;
    String title;
    String content;
    List<Category> categoryList;
    Map user;

    public PostUserDTO(Long post_id, String title, String content, List<Category> categoryList, Map user) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.categoryList = categoryList;
        this.user = user;
    }
}
