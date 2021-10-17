package serverapi.query.dtos.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    public UserDTO(Long user_id, String user_name, String user_email, String user_avatar) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;
    }

    private Long user_id;
    private String user_name;
    private String user_email;
    private String user_avatar;
}
