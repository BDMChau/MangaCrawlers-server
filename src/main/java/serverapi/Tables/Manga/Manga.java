package serverapi.Tables.Manga;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.Tables.Author.Author;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.FollowingManga.FollowingManga;
import serverapi.Tables.MangaGenre.MangaGenre;

import serverapi.Tables.RatingManga.RatingManga;
import serverapi.Tables.ReadingHistory.ReadingHistory;
import serverapi.Tables.TransGroup.TransGroup;
import serverapi.Tables.UpdateView.UpdateView;

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

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(name = "manga_genre", // create a table manga_genre
//            joinColumns = @JoinColumn(name = "manga_id"), // foreign key of class manga
//            inverseJoinColumns = @JoinColumn(name = "genre_id") // foreign key of class genre
//    )
//    private Collection<Genre> genre;
//
//
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(name = "manga_transgroup", // create a table manga_transgroup
//            joinColumns = @JoinColumn(name = "manga_id"), // foreign key of class manga
//            inverseJoinColumns = @JoinColumn(name = "transgroup_id") // foreign key of class transgroup
//    )
//    private Collection<TransGroup> transGroup;
//
//
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(name = "following_manga", // create a table following_manga
//            joinColumns = @JoinColumn(name = "manga_id"), // foreign key of class manga
//            inverseJoinColumns = @JoinColumn(name = "user_id") // foreign key of class user
//    )
//    private Collection<Users> user;


    /////////////////////////////



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
    private Float stars;

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
    private Calendar createdAt;


    public Manga(String manga_name, String status, String description, Float stars, Long views,
                 String thumbnail, int date_publications, Calendar createdAt) {

        this.manga_name = manga_name;
        this.status = status;
        this.description = description;
        this.stars = stars;
        this.views = views;
        this.thumbnail = thumbnail;
        this.date_publications = date_publications;
        this.createdAt = createdAt;

    }

}
