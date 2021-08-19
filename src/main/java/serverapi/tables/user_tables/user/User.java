package serverapi.tables.user_tables.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.manga_comments.MangaComments;
import serverapi.tables.user_tables.following_manga.FollowingManga;
import serverapi.tables.manga_tables.rating_manga.RatingManga;
import serverapi.tables.user_tables.reading_history.ReadingHistory;
import serverapi.tables.user_tables.trans_group.TransGroup;
import serverapi.tables.user_tables.user_relations.UserRelations;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<ReadingHistory> readingHistory;

    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<MangaComments> mangaComments;

    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<FollowingManga> followingManga;

    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<RatingManga> ratingMangas;

    @JsonBackReference
    @OneToMany(mappedBy = "parent_user_id", cascade = CascadeType.ALL)
    private Collection<UserRelations> parentUserRelations;

    @JsonBackReference
    @OneToMany(mappedBy = "child_user_id", cascade = CascadeType.ALL)
    private Collection<UserRelations> childUserRelations;


    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transgroup_id")
    private TransGroup transgroup;


    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String user_name;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String user_email;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String user_password;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String user_avatar;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String avatar_public_id_cloudinary;

    @Column(nullable = false)
    private Boolean user_isAdmin;

    @Column(nullable = false)
    private Boolean user_isVerified;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String token_reset_pass;

    @Column(nullable = true)
    private String token_reset_pass_created_at;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String token_verify;

    @Column(nullable = true)
    private String token_verify_created_at;

    @Column(
            nullable = false,
            updatable = true,
            columnDefinition = "timestamp with time zone"
    )
    private Calendar created_at;

    public User(String user_name, String user_email, String user_password, String user_avatar, Boolean user_isAdmin,
                Calendar created_at) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_avatar = user_avatar;
        this.user_isAdmin = user_isAdmin;
        this.created_at = created_at;
    }
}