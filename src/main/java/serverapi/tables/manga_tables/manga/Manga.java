package serverapi.tables.manga_tables.manga;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.author.Author;
import serverapi.tables.manga_tables.chapter.Chapter;
import serverapi.tables.manga_tables.manga_comment.manga_comments.MangaComments;
import serverapi.tables.manga_tables.manga_genre.MangaGenre;
import serverapi.tables.manga_tables.rating_manga.RatingManga;
import serverapi.tables.manga_tables.update_view.UpdateView;
import serverapi.tables.user_tables.following_manga.FollowingManga;
import serverapi.tables.user_tables.report.reports.Reports;
import serverapi.tables.user_tables.reading_history.ReadingHistory;
import serverapi.tables.user_tables.trans_group.TransGroup;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="author_id")
    private Author author;


    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transgroup_id")
    private TransGroup transgroup;

    @JsonBackReference
    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private List<Chapter> chapters;

    @JsonBackReference
    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Collection<MangaGenre> mangaGenres;

    @JsonBackReference
    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Collection<FollowingManga> followingMangas;

    @JsonBackReference
    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Collection<ReadingHistory> readingHistories;

    @JsonBackReference
    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Collection<RatingManga> ratingMangas;

    @JsonBackReference
    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Collection<UpdateView> updateViews;

    @JsonBackReference
    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Collection<MangaComments> mangaComments;

    @JsonBackReference
    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Collection<Reports> report;


    @Column(
            nullable = false,
            columnDefinition = "varchar(150)"
    )
    private String manga_name;

    @Column(
            nullable = false,
            columnDefinition = "varchar(20)"
    )
    private String status;

    @Column(
            nullable = false,
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
            columnDefinition = "bigint(20) default 0"
    )
    private Long views;

    @Column(
            nullable = true,
            updatable = true,
            columnDefinition = "TEXT"
    )
    private String thumbnail;

    @Column(
            nullable = true,
            updatable = true,
            columnDefinition = "int "
    )
    private int date_publications;

    @Column(
            nullable = false,
            updatable = true,
            columnDefinition = "timestamp with time zone"
    )
    private Calendar created_at;


    public Manga(String manga_name, String status, String description, float stars, Long views,
                 String thumbnail, int date_publications, Calendar created_at) {

        this.manga_name = manga_name;
        this.status = status;
        this.description = description;
        this.stars = stars;
        this.views = views;
        this.thumbnail = thumbnail;
        this.date_publications = date_publications;
        this.created_at= created_at;

    }

}
