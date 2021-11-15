package serverapi.tables.manga_tables.comment.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.forum.post.Post;
import serverapi.tables.manga_tables.comment.comment_like.CommentLike;
import serverapi.tables.manga_tables.comment.comment_tag.CommentTag;
import serverapi.tables.manga_tables.chapter.Chapter;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.manga_tables.comment.comment_image.CommentImage;
import serverapi.tables.manga_tables.comment.comment_relation.CommentRelation;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "comment")
public class Comment {
    @Id
    @SequenceGenerator(
            name = "comment_sequence",
            sequenceName = "comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_sequence" // same as NAME in SequenceGenerator
    )
    private Long comment_id;


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

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean is_deprecated;

    @JsonBackReference
    @OneToMany(mappedBy = "parent_id", cascade = CascadeType.ALL)
    private Collection<CommentRelation> parent_commentRelations;

    @JsonBackReference
    @OneToMany(mappedBy = "child_id", cascade = CascadeType.ALL)
    private Collection<CommentRelation> child_commentRelations;

    @JsonBackReference
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Collection<CommentImage> comment_image;

    @JsonBackReference
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Collection<CommentLike> comment_like;

    @JsonBackReference
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Collection<CommentTag> comment_tag;

    @Column(columnDefinition = "timestamp with time zone", nullable = false)
    private Calendar comment_time;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment_content;

    @Column(columnDefinition = "integer default 0" , nullable = false)
    private int count_like;

}
