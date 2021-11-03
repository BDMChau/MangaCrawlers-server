package serverapi.tables.forum.category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.forum.post_category.PostCategory;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "category")
public class Category {
    @Id
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_sequence" // same as NAME in SequenceGenerator
    )
    private Long category_id;

    @JsonBackReference
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Collection<PostCategory> postCategories;

//    @ManyToMany(mappedBy = "genre") // variable genre in manga class
//    private Collection<MangaGenre> mangaGenre;


    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(columnDefinition = "varchar(30)", nullable = true)
    private String color;

}