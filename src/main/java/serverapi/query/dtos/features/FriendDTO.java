package serverapi.query.dtos.features;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.h2.api.TimestampWithTimeZone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private Long friend_request_id;
    private boolean status;
    private Calendar time_accepted;

    public FriendDTO(Long friend_request_id, Long user_id, Long to_user_id, boolean status, Calendar time_accepted) {
        this.friend_request_id = friend_request_id;
        this.user_id = user_id;
        this.to_user_id = to_user_id;
        this.status = status;
        this.time_accepted = time_accepted;
    }

    public FriendDTO( Long child_user_id, Long parent_user_id, boolean status) {
        this.child_user_id = child_user_id;
        this.parent_user_id = parent_user_id;
        this.status = status;
    }


    public FriendDTO(Long user_relations_id, Long user_id, Long to_user_id, Long friend_request_id) {
        this.user_relations_id = user_relations_id;
        this.user_id = user_id;
        this.to_user_id = to_user_id;
        this.friend_request_id = friend_request_id;
    }
}