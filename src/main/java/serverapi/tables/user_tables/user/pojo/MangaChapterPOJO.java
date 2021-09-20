package serverapi.tables.user_tables.user.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MangaChapterPOJO {
    Long manga_id;
    String manga_name;
    String author_name;

    Long chapter_id;
    String chapter_name;
}
