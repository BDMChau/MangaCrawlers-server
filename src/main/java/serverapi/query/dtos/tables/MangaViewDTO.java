package serverapi.query.dtos.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MangaViewDTO {

    private Long views;
    private Long manga_id;
    private String manga_name;

    public MangaViewDTO(Long views, Long manga_id, String manga_name) {
        this.views = views;
        this.manga_id = manga_id;
        this.manga_name = manga_name;
    }
}
