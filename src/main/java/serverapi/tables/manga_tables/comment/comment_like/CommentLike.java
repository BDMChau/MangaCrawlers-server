package serverapi.tables.manga_tables.comment.comment_like;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.comment.comment.Comment;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "comment_like")
public class CommentLike {

    @Id
    @SequenceGenerator(
            name = "comment_like_sequence",
            sequenceName = "comment_like_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_like_sequence" // same as NAME in SequenceGenerator
    )
    private Long comment_like_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="comment_id")
    private Comment comment;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
