package serverapi.tables.user_tables.report.report_example_title;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.report.report_types.ReportType;
import serverapi.tables.user_tables.report.reports.Report;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "report_example_title")
public class ReportExampleTitle {

    @Id
    @SequenceGenerator(
            name = "report_example_title_sequence",
            sequenceName = "report_example_title_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "report_example_title_sequence" // same as NAME in SequenceGenerator
    )
    private Long report_exam_title_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="report_type_id")
    private ReportType report_type;

    @JsonBackReference
    @OneToMany(mappedBy = "report_example_title", cascade = CascadeType.ALL)
    private Collection<Report> reports;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}
