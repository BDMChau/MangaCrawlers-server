package serverapi.tables.forum.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.forum.post_category.PostCategory;
import serverapi.tables.forum.post_dislike.PostDislike;
import serverapi.tables.forum.post_like.PostLike;
import serverapi.tables.comment.comment.Comment;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "post")
public class Post {

    @Id
    @SequenceGenerator(
            name = "post_sequence",
            sequenceName = "post_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_sequence" // same as NAME in SequenceGenerator
    )
    private Long post_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostCategory> postCategories;

    @JsonBackReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Collection<PostLike> postLikes;

    @JsonBackReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Collection<PostDislike> postDislikes;

    @JsonBackReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean is_deprecated;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean is_approved;

    @Column(nullable = false, updatable = true, columnDefinition = "timestamp with time zone")
    private Calendar created_at;

    @Column(columnDefinition = "integer default 0" , nullable = false)
    private int count_like;

    @Column(columnDefinition = "integer default 0" , nullable = false)
    private int count_dislike;
}
