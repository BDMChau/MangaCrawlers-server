package serverapi.Tables.dto;


import lombok.*;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;
import serverapi.Tables.Manga.Manga;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LatestManga {


    private  Long  chapter_id;

    private String manga_name;

}
