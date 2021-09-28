package serverapi.tables.user_tables.report.report_example_titles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.report.report_types.ReportTypes;
import serverapi.tables.user_tables.report.reports.Reports;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "report_example_titles")
public class ReportExampleTitles {

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
    @JoinColumn(name ="notification_type_id")
    private ReportTypes report_type;

    @JsonBackReference
    @OneToMany(mappedBy = "report_example_title", cascade = CascadeType.ALL)
    private Collection<Reports> reports;


    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}
