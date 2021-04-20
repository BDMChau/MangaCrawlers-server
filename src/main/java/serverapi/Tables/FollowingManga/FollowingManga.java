package serverapi.Tables.FollowingManga;


import lombok.Data;
import lombok.NoArgsConstructor;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.User.User;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "following_manga")
public class FollowingManga {
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
    private Long readinghistory_id;

    @ManyToOne()
    @JoinColumn(name = "manga_id", insertable = false, updatable = false)
    private Manga manga;

    @ManyToOne()
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

}
