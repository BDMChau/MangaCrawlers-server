package serverapi.Query.DTOs.TablesDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class MangaChapterGenreDTO {

    private Long chapter_id;
    private String chapter_name;
    private Calendar created_at;

    private Long manga_id;
    private String manga_name;
    private String thumbnail;

    private Long genre_id;
    private String genre_name;
    private String description;
    private String genre_color;

    public MangaChapterGenreDTO(Long chapter_id, String chapter_name, Calendar created_at, Long manga_id,
                                String manga_name, String thumbnail, Long genre_id, String genre_name,
                                String description, String genre_color) {
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.created_at = created_at;

        this.manga_id = manga_id;
        this.manga_name = manga_name;
        this.thumbnail = thumbnail;

        this.genre_id = genre_id;
        this.genre_name = genre_name;
        this.description = description;
        this.genre_color = genre_color;
    }




}
