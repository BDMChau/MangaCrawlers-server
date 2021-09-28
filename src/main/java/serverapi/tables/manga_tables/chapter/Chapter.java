package serverapi.tables.manga_tables.chapter;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.manga_comment.manga_comments.MangaComments;
import serverapi.tables.manga_tables.image_chapter.ImageChapter;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.user_tables.reading_history.ReadingHistory;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "chapter")
public class Chapter {
    @Id
    @SequenceGenerator(
            name = "chapter_sequence",
            sequenceName = "chapter_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chapter_sequence" // same as NAME in SequenceGenerator
    )
    private Long chapter_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manga_id")
    private Manga manga;



    @JsonBackReference
    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private Collection<ReadingHistory> readingHistories;

    @JsonBackReference
    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private Collection<ImageChapter> imageChapters;

    @JsonBackReference
    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private Collection<MangaComments> mangaComments;



    @Column(columnDefinition = "float default 0", nullable = false)
    private float chapter_number;

    @Column(columnDefinition = "varchar(150)", nullable = false)
    private String chapter_name;

    @Column( columnDefinition = "Integer default 0")
    private int views;

    @Column(
            nullable = false,
            updatable = false,
            columnDefinition = "timestamp with time zone"
    )
    private Calendar created_at;


    public Chapter(float chapter_number, String chapter_name, int views, Calendar created_at) {
        this.chapter_number = chapter_number;
        this.chapter_name = chapter_name;
        this.views = views;
        this.created_at= created_at;
    }
}
