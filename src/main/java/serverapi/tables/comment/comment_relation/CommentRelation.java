package serverapi.tables.comment.comment_relation;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.comment.comment.Comment;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "comment_relation")
public class CommentRelation {
    @Id
    @SequenceGenerator(
            name = "comment_relations_sequence",
            sequenceName = "comment_relations_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_relations_sequence" // same as NAME in SequenceGenerator
    )
    private Long comment_relation_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parent_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_comment_id")
    private Comment child_id;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String level;
}
