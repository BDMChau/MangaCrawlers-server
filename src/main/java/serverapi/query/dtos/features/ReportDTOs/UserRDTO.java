package serverapi.query.dtos.features.ReportDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class UserRDTO {
    public UserRDTO(Long user_id, Calendar created_at) {
        this.user_id = user_id;
        this.created_at = created_at;
    }

    private Long user_id;
    private Calendar created_at;

}
