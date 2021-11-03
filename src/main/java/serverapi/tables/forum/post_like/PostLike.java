package serverapi.tables.forum.post_like;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.forum.post.Post;
import serverapi.tables.manga_tables.manga_comment.manga_comments.MangaComments;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "post_like")
public class PostLike {

    @Id
    @SequenceGenerator(
            name = "post_like_sequence",
            sequenceName = "post_like_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_like_sequence" // same as NAME in SequenceGenerator
    )
    private Long post_like_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="post_id")
    private Post post;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
