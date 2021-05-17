package serverapi.Queries.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LatestManga {


    private Long chapter_id;
    private String chapter_name;
    private Calendar createdAt;

    private Long manga_id;
    private String manga_name;

}
