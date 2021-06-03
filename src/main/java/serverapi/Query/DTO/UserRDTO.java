package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class UserRDTO {
    public UserRDTO(Long user_id, Calendar createdAt) {
        this.user_id = user_id;
        this.createdAt = createdAt;
    }

    private Long user_id;
    private Calendar createdAt;

}
