package serverapi.Queries.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;

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
