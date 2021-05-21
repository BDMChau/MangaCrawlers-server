package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class UserReadingHistoryDTO {

    private Long user_id;
    private Long readingHistory_id;
    private Calendar reading_history_time;

    public UserReadingHistoryDTO(Long user_id, Long readingHistory_id, Calendar reading_history_time) {
        this.user_id = user_id;
        this.readingHistory_id = readingHistory_id;
        this.reading_history_time = reading_history_time;
    }
}
