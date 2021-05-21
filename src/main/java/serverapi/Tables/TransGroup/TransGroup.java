package serverapi.Tables.TransGroup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.Tables.Manga.Manga;


import javax.persistence.*;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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


        @JsonBackReference
        @OneToMany(mappedBy = "transgroup", cascade = CascadeType.ALL)
        private List<Manga> mangas;


        @Column(columnDefinition = "varchar(100)", nullable = false)
        private String transgroup_name;

        @Column(columnDefinition = "varchar(50)", nullable = false)
        private String transgroup_email;


        public TransGroup(String transgroup_name, String transgroup_email) {
            this.transgroup_name = transgroup_name;
            this.transgroup_email = transgroup_email;
        }


}
