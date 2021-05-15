package serverapi.Tables.MangaTransGroup;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.TransGroup.TransGroup;

import javax.persistence.*;


@Entity
@Getter
@Setter
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

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manga_id", insertable = false, updatable = false)
    private Manga manga;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transgroup_id", insertable = false, updatable = false)
    private TransGroup transGroup;
}
