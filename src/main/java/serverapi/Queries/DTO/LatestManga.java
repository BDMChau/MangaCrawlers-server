package serverapi.Queries.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;


@Getter
@Setter
@NoArgsConstructor
public class LatestManga {


    private Long chapter_id;
    private String chapter_name;
    private Calendar createdAt;

    private Long manga_id;
    private String manga_name;
    private String thumbnail;
    private Calendar createdAtChapter;



    public LatestManga(Long chapter_id, String chapter_name, Calendar createdAt, Long manga_id, String manga_name, String thumbnail, Calendar createdAtChapter) {
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.createdAt = createdAt;
        this.manga_id = manga_id;
        this.manga_name = manga_name;
        this.thumbnail=thumbnail;
        this.createdAtChapter= createdAtChapter;

    }
}
