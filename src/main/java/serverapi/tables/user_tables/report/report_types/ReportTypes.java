package serverapi.tables.user_tables.report.report_types;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.report.report_example_titles.ReportExampleTitles;
import serverapi.tables.user_tables.report.reports.Reports;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "report_types")
public class ReportTypes {

    @Id
    @SequenceGenerator(
            name = "report_type_sequence",
            sequenceName = "report_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "report_type_sequence" // same as NAME in SequenceGenerator
    )
    private Long report_type_id;

    @JsonBackReference
    @OneToMany(mappedBy = "report_type", cascade = CascadeType.ALL)
    private Collection<ReportExampleTitles> report_example_title;

    @JsonBackReference
    @OneToMany(mappedBy = "report_type", cascade = CascadeType.ALL)
    private Collection<Reports> reports;


    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

}
