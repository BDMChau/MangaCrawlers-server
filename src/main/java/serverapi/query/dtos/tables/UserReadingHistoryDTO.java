package serverapi.query.dtos.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class UserReadingHistoryDTO {


    private Long readingHistory_id;
    private Calendar reading_History_time;

    private Long user_id;

    private Long manga_id;
    private String manga_name;
    private String thumbnail;
    private float stars;
    private Long views;
    private int date_publications;
    private String status;


    private Long chapter_id;
    private String chapter_name;
    private Calendar created_at;

    public UserReadingHistoryDTO(Long readingHistory_id, Calendar reading_History_time, Long user_id, Long manga_id, String manga_name, String thumbnail, float stars, Long views, int date_publications, String status, Long chapter_id, String chapter_name, Calendar created_at) {
        this.readingHistory_id = readingHistory_id;
        this.reading_History_time = reading_History_time;
        this.user_id = user_id;
        this.manga_id = manga_id;
        this.manga_name = manga_name;
        this.thumbnail = thumbnail;
        this.stars = stars;
        this.views = views;
        this.date_publications = date_publications;
        this.status = status;
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.created_at = created_at;
    }
}
