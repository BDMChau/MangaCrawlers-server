package serverapi.Tables.TransGroup;


import lombok.Data;
import lombok.NoArgsConstructor;
import serverapi.Tables.MangaTransGroup.MangaTransGroup;

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


    @OneToMany(mappedBy = "transGroup", cascade = CascadeType.ALL)
    private Collection<MangaTransGroup> mangaTransGroups;


    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String transgroup_name;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String transgroup_email;


    public TransGroup(String transgroup_name, String transgroup_email) {
        this.transgroup_name = transgroup_name;
        this.transgroup_email = transgroup_email;
    }
}
