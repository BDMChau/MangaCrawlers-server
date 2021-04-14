package serverapi.TransGroup;


import lombok.Data;
import lombok.NoArgsConstructor;
import serverapi.Manga.Manga;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@Table(name = "transgroup")
public class TransGroup {
    @Id
    @SequenceGenerator(
            name = "transgroup_sequence",
            sequenceName = "transgroup_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transgroup_sequence" // same as NAME in SequenceGenerator
    )
    private Long transgroup_id;


    @ManyToMany(mappedBy = "transGroup") // variable transGroup in manga class
    private Collection<Manga> manga;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String transgroup_name;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String transgroup_email;


    public TransGroup(String transgroup_name, String transgroup_email) {
        this.transgroup_name = transgroup_name;
        this.transgroup_email = transgroup_email;
    }
}
