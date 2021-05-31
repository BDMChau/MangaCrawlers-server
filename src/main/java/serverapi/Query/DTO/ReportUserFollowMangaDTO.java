package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportUserFollowMangaDTO {

    public ReportUserFollowMangaDTO(Long total_user, Long author_id, String author_name, Long manga_id, String manga_name, String thumbnail, float stars, Long views, int date_publications) {
        this.total_user = total_user;
        this.author_id = author_id;
        this.author_name = author_name;
        this.manga_id = manga_id;
        this.manga_name = manga_name;
        this.thumbnail = thumbnail;
        this.stars = stars;
        this.views = views;
        this.date_publications = date_publications;
    }

    private Long total_user;

    private Long author_id;
    private String author_name;

    private Long manga_id;
    private String manga_name;
    private String thumbnail;
    private float stars;
    private Long views;
    private int date_publications;


}
