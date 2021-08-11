package serverapi.tables.manga_tables.chapter_comments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.chapter.Chapter;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "chaptercomments")
public class ChapterComments {
    @Id
    @SequenceGenerator(
            name = "chaptercmt_sequence",
            sequenceName = "chaptercmt_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chaptercmt_sequence" // same as NAME in SequenceGenerator
    )
    private Long chaptercmt_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @Column(columnDefinition = "timestamp with time zone", nullable = false)
    private Calendar chaptercmt_time;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String chaptercmt_content;
}
