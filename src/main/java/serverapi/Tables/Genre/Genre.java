package serverapi.Tables.Genre;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.Tables.MangaGenre.MangaGenre;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "genre")
public class Genre {
    @Id
    @SequenceGenerator(
            name = "genre_sequence",
            sequenceName = "genre_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "genre_sequence" // same as NAME in SequenceGenerator
    )
    private Long genre_id;

    @JsonBackReference
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    private Collection<MangaGenre> mangaGenres;

//    @ManyToMany(mappedBy = "genre") // variable genre in manga class
//    private Collection<MangaGenre> mangaGenre;


    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String genre_name;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String genre_description;

}
