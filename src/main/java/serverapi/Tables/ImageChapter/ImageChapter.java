package serverapi.Tables.ImageChapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.Tables.Chapter.Chapter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name="imageschapter")
public class ImageChapter {
    @Id
    @SequenceGenerator(
            name = "imgchapter_sequence",
            sequenceName = "imgchapter_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "imgchapter_sequence" // same as NAME in SequenceGenerator
    )
    private Long imgchapter_id;


    @Column(columnDefinition = "TEXT", nullable = false)
    private String imgchapter_url;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String imgchapter_public_id_cloudinary;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;


}
