package serverapi.Manga;

import lombok.Data;
import serverapi.Author.Author;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Data
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

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author_id;



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

}
