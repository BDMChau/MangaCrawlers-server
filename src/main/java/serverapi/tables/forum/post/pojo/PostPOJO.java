package serverapi.tables.forum.post.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostPOJO {
    String title;
    List<String> categoriesId;
    String content;
}
