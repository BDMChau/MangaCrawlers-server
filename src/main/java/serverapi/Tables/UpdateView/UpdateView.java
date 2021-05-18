package serverapi.Tables.UpdateView;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.Tables.Manga.Manga;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "updateview")
public class UpdateView {

    @Id
    @SequenceGenerator(
            name = "updateview_sequence",
            sequenceName = "updateview_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "updateview_sequence" // same as NAME in SequenceGenerator
    )
    private Long updateview_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manga_id")
    private Manga manga;

    @Column(columnDefinition = "Integer default 0")
    private Long totalviews;


    @Column(
            nullable = false,
            updatable = false,
            columnDefinition = "timestamp with time zone"
    )
    private Calendar createdAt;

    public UpdateView(Long totalviews, Calendar createdAt) {
        this.totalviews = totalviews;
        this.createdAt = createdAt;


    }
}
