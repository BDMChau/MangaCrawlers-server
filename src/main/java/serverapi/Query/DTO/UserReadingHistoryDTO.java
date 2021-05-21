package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class UserReadingHistoryDTO {

    private String user_id;
    private Long reading_history_id;
    private Calendar reading_historytime;

    public UserReadingHistoryDTO(String user_id, Calendar reading_historytime) {
        this.user_id = user_id;
        this.reading_historytime = reading_historytime;
    }
}
