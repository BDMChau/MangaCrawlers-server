package serverapi.query.dtos.tables;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    Long notification_id;
    String notification_content;
    String image_url;
    Calendar created_at;
    Long target_id;
    String target_title;
    Boolean is_viewed;
    Boolean is_interacted;

    Long notification_type_id;
    Integer notification_type;

    Long sender_id;
    String sender_name;

    Long receiver_id;
    String receiver_name;
    UUID receiver_socket_id;


    public NotificationDTO(Long notification_id, String notification_content, String image_url, Calendar created_at, Long target_id, String target_title, Boolean is_viewed, Boolean is_interacted, Long notification_type_id, Integer notification_type, Long sender_id, String sender_name, Long receiver_id, String receiver_name) {
        this.notification_id = notification_id;
        this.notification_content = notification_content;
        this.image_url = image_url;
        this.created_at = created_at;
        this.target_id = target_id;
        this.target_title = target_title;
        this.is_viewed = is_viewed;
        this.is_interacted = is_interacted;
        this.notification_type_id = notification_type_id;
        this.notification_type = notification_type;
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.receiver_id = receiver_id;
        this.receiver_name = receiver_name;
    }
}
