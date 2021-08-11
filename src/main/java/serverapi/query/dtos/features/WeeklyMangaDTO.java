package serverapi.query.dtos.features;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WeeklyMangaDTO {

    private Long manga_id;
    private Long view_compares;
    private Long views;

    public WeeklyMangaDTO(Long manga_id, Long view_compares, Long views) {
        this.manga_id = manga_id;
        this.view_compares = view_compares;
        this.views = views;
    }
}
