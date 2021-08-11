package serverapi.query.dtos.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class ChapterDTO {

    public ChapterDTO(Long chapter_id, String chapter_name, Calendar created_at) {
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.created_at = created_at;
    }

    public ChapterDTO(Long chapter_id, String chapter_name, Calendar created_at, Long manga_id) {
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.created_at = created_at;
        this.manga_id = manga_id;
    }

    private Long chapter_id;
    private String chapter_name;
    private Calendar created_at;

    private Long manga_id;
}
