package serverapi.ReadingHistory;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ReadingHistoryId implements Serializable {
    private Long manga_id;
    private Long user_id;
    private Long chapter_id;
}
