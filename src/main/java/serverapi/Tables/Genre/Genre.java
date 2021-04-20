package serverapi.Tables.Genre;

import lombok.Data;
import lombok.NoArgsConstructor;
import serverapi.Tables.Chapter.Chapter;

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
    private Collection<Chapter> chapters;

//    @ManyToMany(mappedBy = "genre") // variable genre in manga class
//    private Collection<MangaGenre> mangaGenre;


    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String genre_name;

}
