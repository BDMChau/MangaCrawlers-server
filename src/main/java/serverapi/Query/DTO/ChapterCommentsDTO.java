package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class ChapterCommentsDTO {

    public ChapterCommentsDTO(Long user_id, String user_name, String user_email, String user_avatar,
                              Long manga_id, String manga_name, String status, String description,
                              float stars, Long views, String thumbnail, int date_publications, Calendar manga_createdAt,
                              Long chapter_id, String chapter_name, Calendar createdAt, Long chaptercmt_id, Calendar chaptercmt_time, String chaptercmt_content) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;
        this.manga_id = manga_id;
        this.manga_name = manga_name;
        this.status = status;
        this.description = description;
        this.stars = stars;
        this.views = views;
        this.thumbnail = thumbnail;
        this.date_publications = date_publications;
        this.manga_createdAt = manga_createdAt;
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.createdAt = createdAt;
        this.chaptercmt_id = chaptercmt_id;
        this.chaptercmt_time = chaptercmt_time;
        this.chaptercmt_content = chaptercmt_content;
    }



    public ChapterCommentsDTO(Long user_id, String user_name, String user_email, String user_avatar,
                              Long chapter_id, String chapter_name, Calendar createdAt,
                              Long chaptercmt_id, Calendar chaptercmt_time, String chaptercmt_content) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.createdAt = createdAt;
        this.chaptercmt_id = chaptercmt_id;
        this.chaptercmt_time = chaptercmt_time;
        this.chaptercmt_content = chaptercmt_content;
    }

    private Long user_id;
    private String user_name;
    private String user_email;
    private String user_avatar;

    private Long manga_id;
    private String manga_name;
    private String status;
    private String description;
    private float stars;
    private Long views;
    private String thumbnail;
    private int date_publications;
    private Calendar manga_createdAt;

    private Long chapter_id;
    private String chapter_name;
    private Calendar createdAt;

    private Long chaptercmt_id;
    private Calendar chaptercmt_time;
    private String chaptercmt_content;


}
