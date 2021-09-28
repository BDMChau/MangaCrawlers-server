package serverapi.tables.user_tables.report.report_replies;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.report.report_images.ReportImages;
import serverapi.tables.user_tables.report.reports.Reports;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "report_replies")
public class ReportReplies {

    @Id
    @SequenceGenerator(
            name = "report_reply_sequence",
            sequenceName = "report_reply_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "report_reply_sequence" // same as NAME in SequenceGenerator
    )
    private Long report_reply_id;

    @JsonManagedReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="report_id")
    private Reports report;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    @JsonBackReference
    @OneToMany(mappedBy = "report_reply", cascade = CascadeType.ALL)
    private Collection<ReportImages> report_image;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;



}
