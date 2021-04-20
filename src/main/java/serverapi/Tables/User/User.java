package serverapi.Tables.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import serverapi.Tables.ChapterComments.ChapterComments;
import serverapi.Tables.FollowingManga.FollowingManga;
import serverapi.Tables.ReadingHistory.ReadingHistory;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @SequenceGenerator(
            name = "users_sequence",
            sequenceName = "users_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "users_sequence" // same as NAME in SequenceGenerator
    )
    private Long user_id;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Collection<ReadingHistory> readingHistory;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Collection<ChapterComments> chapterComments;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Collection<FollowingManga> followingManga;

//    @ManyToMany(mappedBy = "user")
//    private Collection<Manga> manga;


    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String user_name;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String user_email;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String user_password;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String user_avatar;

    @Column(nullable = false)
    private Boolean user_isAdmin;


    public User(String user_name, String user_email, String user_password, String user_avatar, Boolean user_isAdmin) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_avatar = user_avatar;
        this.user_isAdmin = user_isAdmin;
    }
}
