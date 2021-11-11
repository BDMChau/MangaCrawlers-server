package serverapi.tables.user_tables.report.report_image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.report.reports.Report;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "report_image")
public class ReportImage {

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
    private Report report;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String image_url;


}
