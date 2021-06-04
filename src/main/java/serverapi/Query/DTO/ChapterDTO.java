package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class ChapterDTO {

    public ChapterDTO(Long chapter_id, String chapter_name, Calendar createdAt) {
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.createdAt = createdAt;
    }

    public ChapterDTO(Long chapter_id, String chapter_name, Calendar createdAt, Long manga_id) {
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.createdAt = createdAt;
        this.manga_id = manga_id;
    }

    private Long chapter_id;
    private String chapter_name;
    private Calendar createdAt;

    private Long manga_id;
}
