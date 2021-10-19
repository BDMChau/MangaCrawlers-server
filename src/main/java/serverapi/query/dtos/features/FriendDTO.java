package serverapi.query.dtos.features;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.user.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FriendDTO {
    private Long user_id;
    private String user_name;
    private String user_avatar;
    private String user_email;

    private Long user_relations_id;
    private Long child_user_id;
    private Long parent_user_id;

    private Long to_user_id;
    private String to_user_name;
    private String to_user_avatar;
    private String to_user_email;

    private List<User> to_users = new ArrayList<>();

    public FriendDTO(Long user_id,
                     Long user_relations_id, Long child_user_id, Long parent_user_id,
                     Long to_user_id, String to_user_name, String to_user_avatar, String to_user_email) {

        this.user_id = user_id;
        this.user_relations_id = user_relations_id;
        this.child_user_id = child_user_id;
        this.parent_user_id = parent_user_id;
        this.to_user_id = to_user_id;
        this.to_user_name = to_user_name;
        this.to_user_avatar = to_user_avatar;
        this.to_user_email = to_user_email;
    }

    public FriendDTO(Long user_id, Long user_relations_id, Long child_user_id, Long parent_user_id) {
        this.user_id = user_id;
        this.user_relations_id = user_relations_id;
        this.child_user_id = child_user_id;
        this.parent_user_id = parent_user_id;
    }
}
