package serverapi.tables.user_tables.notificate.notification_images;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.notificate.notification_replies.NotificationReplies;
import serverapi.tables.user_tables.notificate.notifications.Notifications;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "notification_images")
public class NotificationImages {

    @Id
    @SequenceGenerator(
            name = "notification_image_sequence",
            sequenceName = "notification_image_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_image_sequence" // same as NAME in SequenceGenerator
    )
    private Long notification_image_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="notification_id")
    private Notifications notifications;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="notification_reply_id")
    private NotificationReplies notification_reply;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String image_url;


}
