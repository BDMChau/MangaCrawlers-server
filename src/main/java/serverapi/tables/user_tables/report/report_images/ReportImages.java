package serverapi.tables.user_tables.report.report_images;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.report.report_replies.ReportReplies;
import serverapi.tables.user_tables.report.reports.Reports;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "report_images")
public class ReportImages {

    @Id
    @SequenceGenerator(
            name = "report_image_sequence",
            sequenceName = "report_image_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "report_image_sequence" // same as NAME in SequenceGenerator
    )
    private Long report_image_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="report_id")
    private Reports reports;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="report_reply_id")
    private ReportReplies report_reply;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String image_url;


}
