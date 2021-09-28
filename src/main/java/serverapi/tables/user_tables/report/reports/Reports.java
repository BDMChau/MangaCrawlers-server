package serverapi.tables.user_tables.report.reports;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.user_tables.report.report_example_titles.ReportExampleTitles;
import serverapi.tables.user_tables.report.report_images.ReportImages;
import serverapi.tables.user_tables.report.report_types.ReportTypes;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "reports")
public class Reports {

    @Id
    @SequenceGenerator(
            name = "report_sequence",
            sequenceName = "report_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "report_sequence" // same as NAME in SequenceGenerator
    )
    private Long report_id;


    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="report_type_id")
    private ReportTypes report_type;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="report_exam_title_id")
    private ReportExampleTitles report_example_title;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="manga_id")
    private Manga manga;

    @JsonBackReference
    @OneToMany(mappedBy = "reports", cascade = CascadeType.ALL)
    private Collection<ReportImages> report_image;


    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean is_hidden;

    @Column(
            nullable = false,
            updatable = true,
            columnDefinition = "timestamp with time zone"
    )
    private Calendar created_at;



}
