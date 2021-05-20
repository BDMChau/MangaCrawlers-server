package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChapterImgDTO {

    Long Img_id;
    String Img_url;

    public ChapterImgDTO(Long img_id, String img_url) {
        Img_id = img_id;
        Img_url = img_url;
    }
}
