package serverapi.tables.user_tables.following_manga;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "following_manga")
public class FollowingManga {
    @Id
    @SequenceGenerator(
            name = "following_sequence",
            sequenceName = "following_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "following_sequence" // same as NAME in SequenceGenerator
    )
    private Long followingmanga_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manga_id", insertable = true, updatable = true)
    private Manga manga;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = true, updatable = true)
    private User user;

}
