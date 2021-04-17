package serverapi.Tables.ImageChapter;

import lombok.Data;
import lombok.NoArgsConstructor;
import serverapi.Tables.Chapter.Chapter;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;


}
