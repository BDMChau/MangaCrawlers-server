package serverapi.tables.user_tables.user.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransGroupPOJO {
    private String group_name;
    private String group_desc;
    private String transgroup_id;
    private Integer manga_id;
    private String user_id;
    private String member_id;


    public Boolean isValid() {
        return group_name != null
                && group_desc != null
                && !group_name.equals("")
                && !group_desc.equals("");
    }


}
