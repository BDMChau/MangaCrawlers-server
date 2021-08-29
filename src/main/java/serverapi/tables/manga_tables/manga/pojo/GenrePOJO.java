package serverapi.tables.manga_tables.manga.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class GenrePOJO {
    private List<Integer> listgenre_id;
    private List genre_name;
}
