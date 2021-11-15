package serverapi.tables.manga_tables.comment.comment_image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.comment.comment.Comment;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "comment_image")
public class CommentImage {

    @Id
    @SequenceGenerator(
            name = "comment_image_sequence",
            sequenceName = "comment_image_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_image_sequence" // same as NAME in SequenceGenerator
    )
    private Long comment_image_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="comment_id")
    private Comment comment;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String image_url;

}
