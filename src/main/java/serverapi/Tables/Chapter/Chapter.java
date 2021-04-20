package serverapi.Tables.Chapter;


import lombok.Data;
import lombok.NoArgsConstructor;
import serverapi.Tables.ChapterComments.ChapterComments;
import serverapi.Tables.ImageChapter.ImageChapter;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.ReadingHistory.ReadingHistory;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "manga_id")
    private Manga manga;



    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private Collection<ReadingHistory> readingHistories;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private Collection<ImageChapter> imageChapters;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private Collection<ChapterComments> chapterComments;



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
    private Calendar createdAt;


    public Chapter(float chapter_number, String chapter_name, int views, Calendar createdAt) {
        this.chapter_number = chapter_number;
        this.chapter_name = chapter_name;
        this.views = views;
        this.createdAt = createdAt;
    }
}