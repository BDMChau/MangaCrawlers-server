package serverapi.tables.manga_tables.rating_manga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "rating_manga")
public class RatingManga {
    @Id
    @SequenceGenerator(
            name = "ratingmanga_sequence",
            sequenceName = "ratingmanga_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ratingmanga_sequence" // same as NAME in SequenceGenerator
    )
    private Long ratingmanga_id;


    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="manga_id", insertable = true, updatable = true)
    private Manga manga;


    @Column(columnDefinition = "float", nullable = true)
    private Float value;

}
