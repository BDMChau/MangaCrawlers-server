package serverapi.tables.user_tables.Notification.Notification_types;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.Notification.Notifications.Notifications;
import serverapi.tables.user_tables.report.report_example_titles.ReportExampleTitles;
import serverapi.tables.user_tables.report.reports.Reports;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "notification_types")
public class NotificationTypes {

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


    @Column(columnDefinition = "TEXT", nullable = false)
    private String notification_name;

}
