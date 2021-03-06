package serverapi.query.dtos.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class AuthorMangaDTO {


    private Long Chapters_length;
    private Long author_id;
    private String author_name;
    private Long transgroup_id;
    private String transgroup_name;

    private Long manga_id;
    private String manga_name;
    private String status;
    private String description;
    private float stars;
    private Long views;
    private String thumbnail;
    private int date_publications;
    private Calendar manga_created_at;


    public AuthorMangaDTO(String author_name, String manga_name) {
        this.author_name = author_name;
        this.manga_name = manga_name;
    }

    public AuthorMangaDTO(Long author_id, String author_name, Long transgroup_id, String transgroup_name, Long manga_id, String manga_name, String status, String description,
                          float stars, Long views, String thumbnail, int date_publications, Calendar manga_created_at) {
        this.author_id = author_id;
        this.author_name = author_name;
        this.transgroup_id = transgroup_id;
        this.transgroup_name = transgroup_name;
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

    public AuthorMangaDTO(Long author_id, String author_name, Long manga_id, String manga_name, String status, String description,
                          float stars, Long views, String thumbnail, int date_publications, Calendar manga_created_at) {
        this.author_id = author_id;
        this.author_name = author_name;

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

    public AuthorMangaDTO(Long chapters_length, Long author_id,
                          String author_name, Long manga_id, String manga_name, String status, String description,
                          float stars, Long views, String thumbnail, int date_publications, Calendar manga_created_at) {
        Chapters_length = chapters_length;
        this.author_id = author_id;
        this.author_name = author_name;
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


}
