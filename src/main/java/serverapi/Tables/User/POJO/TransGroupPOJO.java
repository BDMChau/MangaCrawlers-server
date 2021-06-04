package serverapi.Tables.User.POJO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransGroupPOJO {
    private String group_name;
    private String group_desc;
    private String transgroup_id;
    private Integer manga_id;


    public Boolean isValid() {
        if (group_name == null
                || group_desc == null
                || group_name.equals("")
                || group_desc.equals("")
        ) {
            return false;
        } else {
            return true;
        }
    }


}
