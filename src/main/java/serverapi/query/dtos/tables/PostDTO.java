package serverapi.query.dtos.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {
    Long post_id;
    String title;
    String content;
    Calendar created_at;

    public PostDTO(Long post_id, String title, String content, Calendar created_at) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
    }
}
