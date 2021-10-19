package serverapi.query.dtos.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChapterImgDTO {

    Long Img_id;
    String Img_url;

    private Long chapter_id;
    private String chapter_name;

    private Long manga_id;
    private String manga_name;


    // For add data

    public ChapterImgDTO(Long img_id, String img_url) {
        Img_id = img_id;
        Img_url = img_url;
    }

    public ChapterImgDTO(Long chapter_id, String chapter_name, Long manga_id, String manga_name) {
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.manga_id = manga_id;
        this.manga_name = manga_name;
    }
}
