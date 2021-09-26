package serverapi.tables.manga_comment.manga_comment_tags;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_comment.manga_comments.MangaComments;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "manga_comment_tags")
public class CommentTags {

    @Id
    @SequenceGenerator(
            name = "manga_comment_tag_sequence",
            sequenceName = "manga_comment_tag_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "manga_comment_tag_sequence" // same as NAME in SequenceGenerator
    )
    private Long manga_comment_tag_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="manga_comment_id")
    private MangaComments manga_comment;

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
