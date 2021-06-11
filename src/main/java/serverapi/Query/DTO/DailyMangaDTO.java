package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DailyMangaDTO {
    public DailyMangaDTO(Long manga_id, Long view_compares, Long views) {
        this.manga_id = manga_id;
        this.view_compares = view_compares;
        this.views = views;
    }

    private Long manga_id;
    private Long view_compares;
    private Long views;
}
