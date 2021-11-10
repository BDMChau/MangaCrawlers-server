package serverapi.query.dtos.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long user_id;
    private String user_name;
    private String user_email;
    private String user_avatar;
    private Boolean is_online;
    private UUID socket_session_id;
    private String user_desc;

    private int status;

    private Long transgroup_id;
    private String transgroup_name;

    public UserDTO(Long user_id, String user_name, String user_email, String user_avatar, String user_desc) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;
        this.user_desc = user_desc;
    }

    public UserDTO(Long user_id, String user_name, String user_email, String user_avatar, UUID socket_session_id, String user_desc, Long transgroup_id, String transgroup_name) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;
        this.socket_session_id = socket_session_id;
        this.user_desc = user_desc;
        this.transgroup_id = transgroup_id;
        this.transgroup_name = transgroup_name;
    }

    public UserDTO(Long user_id, String user_name, String user_email, String user_avatar) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;
    }
}
