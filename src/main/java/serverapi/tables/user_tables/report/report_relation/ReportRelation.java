package serverapi.tables.user_tables.report.report_relation;

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
@Table(name = "report_relation")
public class ReportRelation {

    @Id
    @SequenceGenerator(
            name = "report_relation_sequence",
            sequenceName = "report_relation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "report_relation_sequence" // same as NAME in SequenceGenerator
    )
    private Long report_relation_id;

    @JsonManagedReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="report_id")
    private Report report;

    @JsonManagedReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="parent_report_id")
    private Report parent_report;





}
