package serverapi.tables.manga_tables.manga_comment_relations;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.manga_comments.MangaComments;
import serverapi.tables.user_tables.user.User;

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
            name = "mangacommentrelations_sequence",
            sequenceName = "mangacommentrelations_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mangacommentrelations_sequence" // same as NAME in SequenceGenerator
    )
    private Long mangacommentrelation_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_mangacomment_id")
    private MangaComments parent;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_mangacomment_id")
    private MangaComments child;

}
