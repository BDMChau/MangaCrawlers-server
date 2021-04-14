package serverapi.Author;

import lombok.Data;
import lombok.NoArgsConstructor;
import serverapi.Manga.Manga;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@Table(name = "author")
public class Author {
    @Id
    @SequenceGenerator(
            name = "author_sequence",
            sequenceName = "author_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "author_sequence" // same as NAME in SequenceGenerator
    )
    private Long author_id;


    @Column(
            columnDefinition = "varchar(100)"
    )
    private String author_name;


    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Collection<Manga> manga;


    public Author(String author_name) {
        this.author_name = author_name;
    }

}
