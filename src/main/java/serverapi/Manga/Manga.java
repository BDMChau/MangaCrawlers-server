package serverapi.Manga;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "manga")
public class Manga {
    @Id
    @SequenceGenerator(
            name = "manga_sequence",
            sequenceName = "manga_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "manga_sequence" // same as NAME in SequenceGenerator
    )
    private Long manga_id;

    @Column(
            nullable = false,
            columnDefinition = "varchar(50)"
    )
    private String manga_id2;


    @Column(
            columnDefinition = "varchar(150)"
    )
    private String manga_name;

    @Column(
            columnDefinition = "varchar(20)"
    )
    private String status;

    @Column(
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            nullable = true,
            updatable = true,
            columnDefinition = "float default 0"
    )
    private float stars;

    @Column(
            columnDefinition = "Integer default 0"
    )
    private Integer views;

    @Column(
            nullable = true,
            updatable = true,
            columnDefinition = "TEXT"
    )
    private String thumbnail;

    @Column(
            nullable = false,
            updatable = false,
            columnDefinition = "timestamp with time zone"
    )
    private Calendar date_publication;

    @Column(
            nullable = false,
            updatable = false,
            columnDefinition = "timestamp with time zone"
    )
    private Calendar CreatedAt;


    public Manga() {
    }

    public Manga(String manga_id2, String manga_name, String status, String description, float stars, Integer views, String thumbnail, Calendar date_publication, Calendar createdAt) {
        this.manga_id2 = manga_id2;
        this.manga_name = manga_name;
        this.status = status;
        this.description = description;
        this.stars = stars;
        this.views = views;
        this.thumbnail = thumbnail;
        this.date_publication = date_publication;
        CreatedAt = createdAt;
    }


    public void setManga_id(Long manga_id) {
        this.manga_id = manga_id;
    }

    public void setManga_id2(String manga_id2) {
        this.manga_id2 = manga_id2;
    }

    public void setManga_name(String manga_name) {
        this.manga_name = manga_name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setDate_publication(Calendar date_publication) {
        this.date_publication = date_publication;
    }

    public void setCreatedAt(Calendar createdAt) {
        CreatedAt = createdAt;
    }

    public Long getManga_id() {
        return manga_id;
    }

    public String getManga_id2() {
        return manga_id2;
    }

    public String getManga_name() {
        return manga_name;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public float getStars() {
        return stars;
    }

    public Integer getViews() {
        return views;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Calendar getDate_publication() {
        return date_publication;
    }

    public Calendar getCreatedAt() {
        return CreatedAt;
    }
}
