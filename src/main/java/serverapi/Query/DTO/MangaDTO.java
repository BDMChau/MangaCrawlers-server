package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MangaDTO {

    public MangaDTO(Long manga_id, String manga_name, String thumbnail) {
        this.manga_id = manga_id;
        this.manga_name = manga_name;
        this.thumbnail = thumbnail;
    }

    public MangaDTO(Long manga_id, String manga_name, String thumbnail, float stars, Long views, int date_publications) {
        this.manga_id = manga_id;
        this.manga_name = manga_name;
        this.thumbnail = thumbnail;
        this.stars = stars;
        this.views = views;
        this.date_publications = date_publications;
    }

    private Long manga_id;
    private String manga_name;
    private String thumbnail;
    private float stars;
    private Long views;
    private int date_publications;
}
