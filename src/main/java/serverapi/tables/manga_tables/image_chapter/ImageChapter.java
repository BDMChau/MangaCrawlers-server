package serverapi.tables.manga_tables.image_chapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.manga_tables.chapter.Chapter;

import javax.persistence.*;
import java.util.Calendar;

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

    @Column(
            nullable = true,
            updatable = true,
            columnDefinition = "timestamp with time zone"
    )
    private Calendar created_at;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
}
