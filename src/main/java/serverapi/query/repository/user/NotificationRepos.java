package serverapi.query.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.NotificationDTO;
import serverapi.tables.user_tables.notification.notifications.Notifications;

import java.util.List;


@Repository
public interface NotificationRepos extends JpaRepository<Notifications, Long> {

    @Query("SELECT no FROM Notifications no JOIN User u ON no.to_user.user_id = u.user_id WHERE u.user_id = ?1")
    List<Notifications> getAllByUserId(Long user_id);


    @Query("""
             SELECT new serverapi.query.dtos.tables.NotificationDTO(
                 no.notification_id, no.content, no.image_url, no.created_at, no.target_id, no.target_title, no.is_viewed,no.is_interacted,
                 noType.notification_type_id, noType.type, sender.user_id, sender.user_name, receiver.user_id, receiver.user_name
              ) 
            FROM Notifications no JOIN NotificationTypes noType ON no.notification_type = noType.notification_type_id 
              JOIN User receiver ON no.to_user.user_id = receiver.user_id 
              JOIN User sender ON no.from_user.user_id = sender.user_id 
              WHERE receiver.user_id = ?1 ORDER BY no.created_at DESC
             """)
    List<NotificationDTO> getListByUserId(Long user_id, Pageable pageable);
}
