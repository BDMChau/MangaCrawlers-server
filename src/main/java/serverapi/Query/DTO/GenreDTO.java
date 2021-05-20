package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GenreDTO {

    public GenreDTO(Long genre_id, String genre_name, String color, String genre_description) {
        this.genre_id = genre_id;
        this.genre_name = genre_name;
        this.color = color;
        this.genre_description = genre_description;
    }

    private Long genre_id;
    private String genre_name;
    private String color;
    private String genre_description;

}
