package serverapi.query.dtos.tables;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FieldsCreateMangaDTO {
    private String mangaName;
    private String author;
    private List<String> genres;
    private Integer publicationYear;
    private String description;
    private String status;
    private Integer rating;
    private Integer views;

    public Boolean isFieldsEmpty() {
        return mangaName == null
                || author == null
                || description == null
                || status == null
                || mangaName.equals("")
                || author.equals("")
                || genres.isEmpty()
                || publicationYear == 0
                || description.equals("")
                || status.equals("");
    }

}
