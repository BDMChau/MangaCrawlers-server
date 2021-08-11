package serverapi.query.dtos.features;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingMangaDTO {

    private Long ratingmanga_id;

    private Long user_id;
    private String user_name;
    private String user_email;

    private Long manga_id;

    public RatingMangaDTO(Long ratingmanga_id, Long user_id, String user_name, String user_email, Long manga_id, String manga_name, String thumbnail, float stars, Long views, int date_publications, String status) {
        this.ratingmanga_id = ratingmanga_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.manga_id = manga_id;
        this.manga_name = manga_name;
        this.thumbnail = thumbnail;
        this.stars = stars;
        this.views = views;
        this.date_publications = date_publications;
        this.status = status;
    }

    private String manga_name;
    private String thumbnail;
    private float stars;
    private Long views;
    private int date_publications;
    private String status;
}
