package serverapi.tables.user_tables.notificate.notification_replies;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.notificate.notification_images.NotificationImages;
import serverapi.tables.user_tables.notificate.notifications.Notifications;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "notification_replies")
public class NotificationReplies {

    @Id
    @SequenceGenerator(
            name = "notification_reply_sequence",
            sequenceName = "notification_reply_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_reply_sequence" // same as NAME in SequenceGenerator
    )
    private Long notification_reply_id;

    @JsonManagedReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="notification_id")
    private Notifications notification;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    @JsonBackReference
    @OneToMany(mappedBy = "notification_reply", cascade = CascadeType.ALL)
    private Collection<NotificationImages> notification_image;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;



}
