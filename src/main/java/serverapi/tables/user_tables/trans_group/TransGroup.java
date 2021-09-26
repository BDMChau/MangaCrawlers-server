package serverapi.tables.user_tables.trans_group;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;


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
        @OneToMany(mappedBy = "transgroup")
        private Collection<Manga> mangas;

        @JsonBackReference
        @OneToMany(mappedBy = "transgroup")
        private Collection<User> users;

        @Column(columnDefinition = "varchar(100)", nullable = false)
        private String transgroup_name;

        @Column(columnDefinition = "TEXT", nullable = true)
        private String transgroup_desc;

        @Column(columnDefinition = "varchar(50)", nullable = false)
        private String transgroup_email;

//        @Column(columnDefinition = "false", nullable = false)
//        private Boolean is_deprecated;

        @Column(
                nullable = false,
                updatable = true,
                columnDefinition = "timestamp with time zone"
        )
        private Calendar created_at;


        public TransGroup(String transgroup_name, String transgroup_email, String transgroup_desc,Calendar created_at) {
            this.transgroup_name = transgroup_name;
            this.transgroup_email = transgroup_email;
            this.transgroup_desc = transgroup_desc;
            this.created_at= created_at;
        }


}