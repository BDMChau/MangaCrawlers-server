package serverapi.tables.manga_tables.manga_comment.manga_comment_images;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.manga_comment.manga_comments.MangaComments;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "manga_comment_images")
public class CommentImages {

    @Id
    @SequenceGenerator(
            name = "manga_comment_image_sequence",
            sequenceName = "manga_comment_image_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "manga_comment_image_sequence" // same as NAME in SequenceGenerator
    )
    private Long manga_comment_image_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="manga_comment_id")
    private MangaComments manga_comment;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String image_url;

}
