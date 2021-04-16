package serverapi.ReadingHistory;

import lombok.Data;
import lombok.NoArgsConstructor;
import serverapi.Chapter.Chapter;
import serverapi.Manga.Manga;
import serverapi.Users.Users;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name="readinghistory")
public class ReadingHistory {
    @EmbeddedId
    private ReadingHistoryId readingHistoryId;

    @ManyToOne()
    @JoinColumn(name="manga_id", insertable = false, updatable = false)
    private Manga manga;

    @ManyToOne()
    @JoinColumn(name="user_id", insertable = false, updatable = false)
    private Users users;

    @ManyToOne()
    @JoinColumn(name="chapter_id", insertable = false, updatable = false)
    private Chapter chapter;
}
