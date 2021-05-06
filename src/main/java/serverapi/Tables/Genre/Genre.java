package serverapi.Tables.Genre;

import lombok.Data;
import lombok.NoArgsConstructor;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.MangaGenre.MangaGenre;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
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

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    private Collection<MangaGenre> mangaGenres;

//    @ManyToMany(mappedBy = "genre") // variable genre in manga class
//    private Collection<MangaGenre> mangaGenre;


    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String genre_name;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String genre_description;

}
