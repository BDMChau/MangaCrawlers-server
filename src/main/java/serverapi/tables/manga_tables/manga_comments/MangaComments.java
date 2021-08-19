package serverapi.tables.manga_tables.manga_comments;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.chapter.Chapter;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.manga_tables.manga_comment_relations.CommentRelations;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "manga_comments")
public class MangaComments {
    @Id
    @SequenceGenerator(
            name = "mangacomment_sequence",
            sequenceName = "mangacomment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mangacomment_sequence" // same as NAME in SequenceGenerator
    )
    private Long manga_comment_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manga_id")
    private Manga manga;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @JsonBackReference
    @OneToMany(mappedBy = "parent_comment_id", cascade = CascadeType.ALL)
    private Collection<CommentRelations> parentCommentRelations;

    @JsonBackReference
    @OneToMany(mappedBy = "child_comment_id", cascade = CascadeType.ALL)
    private Collection<CommentRelations> childCommentRelations;

    @Column(columnDefinition = "timestamp with time zone", nullable = false)
    private Calendar manga_comment_time;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String manga_comment_content;
}
