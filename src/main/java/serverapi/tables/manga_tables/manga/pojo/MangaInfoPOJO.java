package serverapi.tables.manga_tables.manga.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.author.Author;
import serverapi.tables.user_tables.trans_group.TransGroup;

import java.util.Calendar;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class MangaInfoPOJO {
    private Map author;
    private Calendar created_at;
    private String description;
    private String manga_authorName;
    private String manga_id;
    private String manga_name;
    private float star;
    private String status;
    private String thumbnail;
    private int views;
    private Map transgroup;
}
