package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
public class UpdateViewDTO {
    private Long manga_id;
    private String manga_name;
    private String thumbnail;
    private String description;
    private String status;
    private float stars;
    private Long views;
    private int date_publications;
    private Calendar createdAt;

    private Long updatedview_id;
    private Long totalviews;
    private Calendar created_At;

    public UpdateViewDTO(Long manga_id, String manga_name, String thumbnail, String description, String status, float stars, Long views, int date_publications, Calendar createdAt, Long updatedview_id, Long totalviews, Calendar created_At) {
        this.manga_id = manga_id;
        this.manga_name = manga_name;
        this.thumbnail = thumbnail;
        this.description = description;
        this.status = status;
        this.stars = stars;
        this.views = views;
        this.date_publications = date_publications;
        this.createdAt = createdAt;
        this.updatedview_id = updatedview_id;
        this.totalviews = totalviews;
        this.created_At = created_At;
    }
}
