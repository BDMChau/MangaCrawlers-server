package serverapi.Tables.MangaTransGroup;

import lombok.Data;
import lombok.NoArgsConstructor;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.TransGroup.TransGroup;
import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@Table(name = "manga_transgroup")
public class MangaTransGroup {
    @Id
    @SequenceGenerator(
            name = "mangatransgroup_sequence",
            sequenceName = "mangatransgroup_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mangatransgroup_sequence" // same as NAME in SequenceGenerator
    )
    private Long mangatransgroup_id;

    @ManyToOne()
    @JoinColumn(name = "manga_id", insertable = false, updatable = false)
    private Manga manga;

    @ManyToOne()
    @JoinColumn(name = "transgroup_id", insertable = false, updatable = false)
    private TransGroup transGroup;
}
