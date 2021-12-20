package serverapi.tables.user_tables.trans_group.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.chapter.Chapter;
import serverapi.tables.manga_tables.manga.Manga;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class UpdateChapterPOJO {
    Map chapter;
    String manga_id;
    List list_img;
}
