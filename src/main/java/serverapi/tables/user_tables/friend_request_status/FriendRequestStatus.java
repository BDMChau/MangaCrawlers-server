package serverapi.tables.user_tables.friend_request_status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.user.User;
import serverapi.tables.user_tables.user_relations.UserRelations;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "friend_request_status")
public class FriendRequestStatus {
    @Id
    @SequenceGenerator(
            name = "friend_request_status_sequence",
            sequenceName = "friend_request_status_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "friend_request_status_sequence" // same as NAME in SequenceGenerator
    )
    private Long friend_request_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = true, updatable = true)
    private User user;

    @Column(columnDefinition = "boolean default false", nullable = true)
    private Boolean status;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", insertable = true, updatable = true)
    private User to_user;

    @Column(columnDefinition = "timestamp with time zone", nullable = true)
    private Calendar time_accepted;

    @OneToOne(mappedBy = "friendRequest")
    private UserRelations userRelations;
}