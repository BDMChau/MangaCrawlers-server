package serverapi.tables.forum.post_relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.forum.post.Post;
import serverapi.tables.user_tables.friend_request_status.FriendRequestStatus;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "post_relation")
public class PostRelation {
    @Id
    @SequenceGenerator(
            name = "post_relation_sequence",
            sequenceName = "post_relation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_relation_sequence" // same as NAME in SequenceGenerator
    )
    private Long user_relation_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_post_id")
    private Post reply_post_id;


}