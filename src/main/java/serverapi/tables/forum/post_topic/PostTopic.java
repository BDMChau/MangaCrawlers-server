package serverapi.tables.forum.post_topic;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.forum.post.Post;
import serverapi.tables.forum.post_category.PostCategory;
import serverapi.tables.user_tables.report.report_example_titles.ReportExampleTitles;
import serverapi.tables.user_tables.report.reports.Reports;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "post_topic")
public class PostTopic {

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
    private Long post_topic_id;

    @JsonBackReference
    @OneToMany(mappedBy = "post_topic", cascade = CascadeType.ALL)
    private Collection<Post> posts;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_category_id")
    private PostCategory post_category;


    @Column(columnDefinition = "TEXT", nullable = false)
    private String topic_name;

}
