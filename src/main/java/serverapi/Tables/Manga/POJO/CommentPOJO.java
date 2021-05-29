package serverapi.Tables.Manga.POJO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentPOJO {

    private String manga_id;
    private String chapter_id;
    private String from;
    private String amount;
}
