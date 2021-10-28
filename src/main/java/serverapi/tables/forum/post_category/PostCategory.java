package serverapi.tables.forum.post_category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.notification.notifications.Notifications;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "post_category")
public class PostCategory {

    @Id
    @SequenceGenerator(
            name = "notification_type_sequence",
            sequenceName = "notification_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_type_sequence" // same as NAME in SequenceGenerator
    )
    private Long notification_type_id;


    @JsonBackReference
    @OneToMany(mappedBy = "notification_type", cascade = CascadeType.ALL)
    private Collection<Notifications> notifications;

    @Column(nullable = false)
    private Integer type;

}