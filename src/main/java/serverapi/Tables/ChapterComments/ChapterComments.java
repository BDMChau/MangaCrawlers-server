package serverapi.Tables.ChapterComments;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.User.User;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Getter
@Setter
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
