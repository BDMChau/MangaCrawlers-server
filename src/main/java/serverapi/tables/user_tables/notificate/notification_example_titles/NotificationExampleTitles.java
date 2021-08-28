package serverapi.tables.user_tables.notificate.notification_example_titles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.notificate.notification_types.NotificationTypes;
import serverapi.tables.user_tables.notificate.notifications.Notifications;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "notification_example_titles")
public class NotificationExampleTitles {

    @Id
    @SequenceGenerator(
            name = "notification_example_title_sequence",
            sequenceName = "notification_example_title_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_example_title_sequence" // same as NAME in SequenceGenerator
    )
    private Long notification_exam_title_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="notification_type_id")
    private NotificationTypes notification_type;

    @JsonBackReference
    @OneToMany(mappedBy = "notification_example_title", cascade = CascadeType.ALL)
    private Collection<Notifications> notifications;


    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}
