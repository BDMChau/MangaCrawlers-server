package serverapi.Tables.ReadingHistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.User.User;

import javax.persistence.*;

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
    @JoinColumn(name="manga_id", insertable = false, updatable = false)
    private Manga manga;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", insertable = false, updatable = false)
    private User user;

    @JsonManagedReference
    @ManyToOne()
    @JoinColumn(name="chapter_id", insertable = false, updatable = false)
    private Chapter chapter;
}
