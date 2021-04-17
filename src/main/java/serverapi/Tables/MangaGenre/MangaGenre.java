package serverapi.Tables.MangaGenre;

import lombok.Data;
import lombok.NoArgsConstructor;
import serverapi.Tables.Genre.Genre;
import serverapi.Tables.Manga.Manga;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "manga_genre")
public class MangaGenre {
    @Id
    @SequenceGenerator(
            name = "mangagenre_sequence",
            sequenceName = "mangagenre_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mangagenre_sequence" // same as NAME in SequenceGenerator
    )
    private Long mangagenre_id;

    @ManyToOne()
    @JoinColumn(name = "manga_id", insertable = false, updatable = false)
    private Manga manga;

    @ManyToOne()
    @JoinColumn(name = "genre_id", insertable = false, updatable = false)
    private Genre genre;
}
