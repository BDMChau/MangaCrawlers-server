package serverapi.Tables.Manga;

import lombok.Data;
import lombok.NoArgsConstructor;
import serverapi.Tables.Author.Author;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.FollowingManga.FollowingManga;
import serverapi.Tables.MangaGenre.MangaGenre;
import serverapi.Tables.MangaTransGroup.MangaTransGroup;
import serverapi.Tables.ReadingHistory.ReadingHistory;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;


@Entity
@Data
@NoArgsConstructor
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

    @ManyToOne()
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Collection<Chapter> chapters;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Collection<MangaGenre> mangaGenres;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Collection<FollowingManga> followingMangas;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Collection<ReadingHistory> readingHistories;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Collection<MangaTransGroup> mangaTransGroups;


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
    private float stars;

    @Column(
            nullable = false,
            columnDefinition = "Integer default 0"
    )
    private int views;

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
    private Calendar createdAt;


    public Manga( String manga_name, String status, String description, float stars, Integer views,
                 String thumbnail, Calendar date_publication, Calendar createdAt) {

        this.manga_name = manga_name;
        this.status = status;
        this.description = description;
        this.stars = stars;
        this.views = views;
        this.thumbnail = thumbnail;
        this.date_publication = date_publication;
        this.createdAt = createdAt;

    }

}
