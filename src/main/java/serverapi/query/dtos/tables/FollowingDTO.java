package serverapi.query.dtos.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class FollowingDTO {

    public FollowingDTO(Long followId, Long manga_id, String manga_name, String status,
                        String description, float stars, Long views, String thumbnail,
                        int date_publications, Calendar manga_created_at  ) {
        FollowId = followId;
        this.manga_id = manga_id;
        this.manga_name = manga_name;
        this.status = status;
        this.description = description;
        this.stars = stars;
        this.views = views;
        this.thumbnail = thumbnail;
        this.date_publications = date_publications;
        this.manga_created_at = manga_created_at;

    }

    private Long FollowId;

    private Long manga_id;
    private String manga_name;
    private String status;
    private String description;
    private float stars;
    private Long views;
    private String thumbnail;
    private int date_publications;
    private Calendar manga_created_at;


}
