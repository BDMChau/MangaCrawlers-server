package serverapi.Tables.MangaTables.UpdateView;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.Tables.MangaTables.Manga.Manga;

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
            name = "updated_view_sequence",
            sequenceName = "updated_view_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "updated_view_sequence" // same as NAME in SequenceGenerator
    )
    private Long updatedview_id;


    @JsonManagedReference
    @ManyToOne()
    @JoinColumn(name = "manga_id")
    private Manga manga;


    @Column(columnDefinition = "bigint(20) default 0")
    private Long totalviews;


    @Column(
            updatable = false,
            columnDefinition = "timestamp with time zone"
    )
    private Calendar created_at;

    public UpdateView(Long totalviews, Calendar created_at) {
        this.totalviews = totalviews;
        this.created_at = created_at;
    }
}
