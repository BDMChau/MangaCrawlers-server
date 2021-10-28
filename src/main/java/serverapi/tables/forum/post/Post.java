package serverapi.tables.forum.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.forum.post_image.PostImage;
import serverapi.tables.forum.post_topic.PostTopic;
import serverapi.tables.user_tables.notification.notification_types.NotificationTypes;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "post")
public class Post {

    @Id
    @SequenceGenerator(
            name = "post_sequence",
            sequenceName = "post_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_sequence" // same as NAME in SequenceGenerator
    )
    private Long post_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_topic_id")
    private PostTopic post_topic;

    @JsonBackReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Collection<PostImage> postImages;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean is_deprecated;

    @Column(nullable = false, updatable = true, columnDefinition = "timestamp with time zone")
    private Calendar created_at;

    @Column(columnDefinition = "integer default 0" , nullable = false)
    private int count_like;
}
