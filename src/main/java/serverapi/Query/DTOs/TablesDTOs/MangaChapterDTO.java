package serverapi.Query.DTOs.TablesDTOs;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;


@Getter
@Setter
@NoArgsConstructor
public class MangaChapterDTO {


    private Long chapter_id;
    private String chapter_name;
    private Calendar created_at;

    private Long manga_id;
    private String manga_name;
    private String thumbnail;
    private String status;
    private float stars;

    public MangaChapterDTO(Long manga_id, String manga_name, String thumbnail, float stars) {
        this.manga_id = manga_id;
        this.manga_name = manga_name;
        this.thumbnail = thumbnail;
        this.stars = stars;
    }

    public MangaChapterDTO(Long chapter_id, String chapter_name, Calendar created_at, Long manga_id, String manga_name
            , String thumbnail) {
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.created_at = created_at;
        this.manga_id = manga_id;
        this.manga_name = manga_name;
        this.thumbnail = thumbnail;
    }
}
