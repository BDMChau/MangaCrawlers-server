package serverapi.tables.forum.post_image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.forum.post.Post;
import serverapi.tables.manga_tables.manga_comment.manga_comments.MangaComments;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "post_image")
public class PostImage {

    @Id
    @SequenceGenerator(
            name = "post_image_sequence",
            sequenceName = "post_image_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_image_sequence" // same as NAME in SequenceGenerator
    )
    private Long post_image_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="post_id")
    private Post post;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String image_url;

}
