package serverapi.tables.manga_tables.manga_comment.manga_comments;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.forum.post.Post;
import serverapi.tables.manga_tables.manga_comment.manga_comment_likes.CommentLikes;
import serverapi.tables.manga_tables.manga_comment.manga_comment_tags.CommentTags;
import serverapi.tables.manga_tables.chapter.Chapter;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.manga_tables.manga_comment.manga_comment_images.CommentImages;
import serverapi.tables.manga_tables.manga_comment.manga_comment_relations.CommentRelations;
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
            name = "manga_comment_sequence",
            sequenceName = "manga_comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "manga_comment_sequence" // same as NAME in SequenceGenerator
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

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean is_deprecated;

    @JsonBackReference
    @OneToMany(mappedBy = "parent_id", cascade = CascadeType.ALL)
    private Collection<CommentRelations> parent_commentRelations;

    @JsonBackReference
    @OneToMany(mappedBy = "child_id", cascade = CascadeType.ALL)
    private Collection<CommentRelations> child_commentRelations;

    @JsonBackReference
    @OneToMany(mappedBy = "manga_comment", cascade = CascadeType.ALL)
    private Collection<CommentImages> comment_image;

    @JsonBackReference
    @OneToMany(mappedBy = "manga_comment", cascade = CascadeType.ALL)
    private Collection<CommentLikes> comment_like;

    @JsonBackReference
    @OneToMany(mappedBy = "manga_comment", cascade = CascadeType.ALL)
    private Collection<CommentTags> comment_tag;

    @Column(columnDefinition = "timestamp with time zone", nullable = false)
    private Calendar manga_comment_time;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String manga_comment_content;

    @Column(columnDefinition = "integer default 0" , nullable = false)
    private int count_like;

}
