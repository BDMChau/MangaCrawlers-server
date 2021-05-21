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

    private Long chapter_id;
    private String chapter_name;
    private Calendar createdAt;
}
