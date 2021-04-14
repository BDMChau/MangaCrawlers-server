package serverapi.ChapterComments;

import lombok.Data;
import serverapi.Chapter.Chapter;
import serverapi.Users.Users;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Data
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @Column(columnDefinition = "timestamp with time zone")
    private Calendar chaptercmt_time;

    @Column(columnDefinition = "TEXT")
    private String chaptercmt_content;
}
