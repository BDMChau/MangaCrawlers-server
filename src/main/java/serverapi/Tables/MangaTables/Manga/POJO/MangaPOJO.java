package serverapi.Tables.Manga.POJO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MangaPOJO {
    String manga_id;
    String manga_name;
    String chapter_id;
    String chapter_name;
    String genre_id;

}
