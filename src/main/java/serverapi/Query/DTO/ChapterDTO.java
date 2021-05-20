package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class ChapterDTO {

    public ChapterDTO(Long chapter_id, String chapter_name, Calendar chapter_createdAT) {
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.chapter_createdAT = chapter_createdAT;
    }

    private Long chapter_id;
    private String chapter_name;
    private Calendar chapter_createdAT;
}
