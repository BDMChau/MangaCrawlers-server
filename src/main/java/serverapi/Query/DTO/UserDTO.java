package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    public UserDTO(Long user_id, Calendar created_at) {
        this.user_id = user_id;
        this.created_at = created_at;
    }

    private Long user_id;
    private Calendar created_at;
}
