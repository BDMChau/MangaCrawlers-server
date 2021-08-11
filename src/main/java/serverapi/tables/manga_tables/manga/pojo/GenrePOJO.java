package serverapi.tables.Manga.POJO;

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
