package serverapi.query.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.NotificationDTO;
import serverapi.tables.user_tables.notification.notifications.Notifications;

import java.util.List;
import java.util.Optional;


@Repository
public interface NotificationRepos extends JpaRepository<Notifications, Long> {

    @Query("SELECT no FROM Notifications no JOIN User u ON no.to_user.user_id = u.user_id WHERE u.user_id = ?1")
    List<Notifications> getAllByUserId(Long user_id);

    @Query("SELECT no FROM Notifications no WHERE no.target_id = ?1 AND no.is_interacted = false")
    List<Notifications> findByTargetTitleUserAndNotInteract(Long targetId);

    @Query("SELECT no FROM Notifications no WHERE no.to_user.user_id = ?1 AND no.is_interacted = false")
    List<Notifications> findByToUserIdAndNotInteract(Long targetId);


    @Query("""
            SELECT new serverapi.query.dtos.tables.NotificationDTO(
                no.notification_id, no.content, no.image_url, no.created_at, no.target_id, no.target_title, no.is_viewed,no.is_interacted,
                type.notification_type_id, type.type, sender.user_id, sender.user_name, receiver.user_id, receiver.user_name
                )
             FROM Notifications no JOIN NotificationTypes type ON no.notification_type = type.notification_type_id 
             JOIN User receiver ON no.to_user.user_id = receiver.user_id 
             JOIN User sender ON no.from_user.user_id = sender.user_id 
             WHERE receiver.user_id = ?1 ORDER BY no.created_at DESC
            """)
    List<NotificationDTO> getAllByUserId(Long user_id, Pageable pageable);



    @Query("""
            SELECT new serverapi.query.dtos.tables.NotificationDTO(
                no.notification_id, no.content, no.image_url, no.created_at, no.target_id, no.target_title, no.is_viewed,no.is_interacted,
                type.notification_type_id, type.type, sender.user_id, sender.user_name, sender.user_email, receiver.user_id, receiver.user_name, receiver.user_email
                )
             FROM Notifications no JOIN NotificationTypes type ON no.notification_type = type.notification_type_id 
             JOIN User receiver ON no.to_user.user_id = receiver.user_id 
             JOIN User sender ON no.from_user.user_id = sender.user_id 
             WHERE receiver.user_id = ?1 AND type.type = ?2 AND no.is_interacted = false OR no.is_interacted = null
             ORDER BY no.created_at DESC
            """)
    List<NotificationDTO> getListByUserIdAndTypeAndNotInteract(Long user_id, int notification_type);



    @Query("""
            SELECT new serverapi.query.dtos.tables.NotificationDTO(
                no.notification_id, no.content, no.image_url, no.created_at, no.target_id, no.target_title, no.is_viewed,no.is_interacted,
                type.notification_type_id, type.type, sender.user_id, sender.user_name, sender.user_email, receiver.user_id, receiver.user_name, receiver.user_email
                )
             FROM Notifications no JOIN NotificationTypes type ON no.notification_type = type.notification_type_id 
             JOIN User receiver ON no.to_user.user_id = receiver.user_id 
             JOIN User sender ON no.from_user.user_id = sender.user_id 
             WHERE receiver.user_id = ?1 AND type.type = ?2
             ORDER BY no.created_at DESC
            """)
    List<NotificationDTO> getAllByUserIdAndType(Long user_id, int notification_type);
}
