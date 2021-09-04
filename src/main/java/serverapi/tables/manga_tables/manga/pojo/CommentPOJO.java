package serverapi.tables.manga_tables.manga.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentPOJO {

    private String manga_id;
    private String chapter_id;
    private String chaptercmt_content;

    private int from;
    private int amount;

}
