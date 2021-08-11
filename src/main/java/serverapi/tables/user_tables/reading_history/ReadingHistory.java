package serverapi.tables.user_tables.reading_history;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.chapter.Chapter;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name="reading_history")
public class ReadingHistory {
    @Id
    @SequenceGenerator(
            name = "readinghistory_sequence",
            sequenceName = "readinghistory_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "readinghistory_sequence" // same as NAME in SequenceGenerator
    )
    private Long readingHistory_id;


    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="manga_id", insertable = true, updatable = true)
    private Manga manga;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", insertable = true, updatable = true)
    private User user;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chapter_id", insertable = true, updatable = true)
    private Chapter chapter;

    @Column(
            updatable = true,
            columnDefinition = "timestamp with time zone"
    )
    private Calendar reading_history_time;
}
