package serverapi.tables.forum.post_category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.forum.post_topic.PostTopic;
import serverapi.tables.user_tables.report.report_example_titles.ReportExampleTitles;
import serverapi.tables.user_tables.report.reports.Reports;

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
            name = "post_category_sequence",
            sequenceName = "post_category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_category_sequence" // same as NAME in SequenceGenerator
    )
    private Long post_category_id;

    @JsonBackReference
    @OneToMany(mappedBy = "post_category", cascade = CascadeType.ALL)
    private Collection<PostTopic> postTopics;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String category_name;

}
