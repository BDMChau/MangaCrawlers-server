package serverapi.tables.User.POJO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPOJO {
    private String user_id;
    private String manga_id;
    private String chapter_id;
    private String readinghistory_id;
    private Boolean user_isAdmin;


}
