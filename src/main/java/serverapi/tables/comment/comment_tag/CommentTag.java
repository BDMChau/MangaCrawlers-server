package serverapi.tables.comment.comment_tag;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.comment.comment.Comment;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "comment_tag")
public class CommentTag {

    @Id
    @SequenceGenerator(
            name = "comment_tag_sequence",
            sequenceName = "comment_tag_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_tag_sequence" // same as NAME in SequenceGenerator
    )
    private Long comment_tag_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="comment_id")
    private Comment comment;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(
            nullable = false,
            columnDefinition = "int"
    )
    private int off_set;

}
