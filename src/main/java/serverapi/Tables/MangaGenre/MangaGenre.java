package serverapi.Tables.MangaGenre;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.Tables.Genre.Genre;
import serverapi.Tables.Manga.Manga;

import javax.persistence.*;

@Entity
@Getter
@Setter
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

    @JsonManagedReference
    @ManyToOne()
    @JoinColumn(name = "manga_id", insertable = false, updatable = false)
    private Manga manga;

    @JsonManagedReference
    @ManyToOne()
    @JoinColumn(name = "genre_id", insertable = false, updatable = false)
    private Genre genre;
}
