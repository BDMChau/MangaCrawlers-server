package serverapi.tables.manga_tables.manga_comment_relations;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.manga_comments.MangaComments;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "manga_comment_relations")
public class CommentRelations {
    @Id
    @SequenceGenerator(
            name = "manga_comment_relations_sequence",
            sequenceName = "manga_comment_relations_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "manga_comment_relations_sequence" // same as NAME in SequenceGenerator
    )
    private Long manga_comment_relation_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_mangacomment_id")
    private MangaComments parent_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_mangacomment_id")
    private MangaComments child_id;

}
