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

    @Query("SELECT no FROM Notifications no JOIN User u ON no.to_user.user_id = u.user_id WHERE u.user_id = ?1 AND no.is_delete = false")
    List<Notifications> getAllByUserId(Long user_id);

    @Query("SELECT no FROM Notifications no WHERE no.target_id = ?1 AND no.is_interacted = false AND no.is_delete = false")
    List<Notifications> findByTargetTitleUserAndNotInteract(Long targetId);

    @Query("SELECT no FROM Notifications no WHERE no.to_user.user_id = ?2 AND no.target_id = ?1 AND no.is_interacted = false AND no.is_delete = false")
    List<Notifications> findByTargetTitleTransGroupAndNotInteract(Long targetId, Long toUserId);


    @Query("""
            SELECT new serverapi.query.dtos.tables.NotificationDTO(
                no.notification_id, no.content, no.image_url, no.created_at, no.target_id, no.target_title, no.is_viewed,no.is_interacted,
                type.notification_type_id, type.type, sender.user_id, sender.user_name, receiver.user_id, receiver.user_name
                )
             FROM Notifications no JOIN NotificationTypes type ON no.notification_type = type.notification_type_id 
             JOIN User receiver ON no.to_user.user_id = receiver.user_id 
             JOIN User sender ON no.from_user.user_id = sender.user_id 
             WHERE receiver.user_id = ?1 AND no.is_delete = false ORDER BY no.created_at DESC
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
             WHERE receiver.user_id = ?1 AND type.type = ?2 AND no.is_delete = false AND no.is_interacted = false
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
             WHERE receiver.user_id = ?1 AND type.type = ?2 AND no.is_delete = false
             ORDER BY no.created_at DESC
            """)
    List<NotificationDTO> getAllByUserIdAndType(Long user_id, int notification_type);


    @Query("""
            SELECT no
             FROM Notifications no JOIN NotificationTypes type ON no.notification_type = type.notification_type_id 
             JOIN User receiver ON no.to_user.user_id = receiver.user_id 
             JOIN User sender ON no.from_user.user_id = sender.user_id 
             WHERE sender.user_id = ?1 AND receiver.user_id = ?2 AND no.target_title = ?3 AND type.type = ?4 AND no.is_delete = false
             ORDER BY no.created_at DESC
            """)
    Optional<Notifications> getFriendReqByTargetTitleUser(Long senderId, Long recieverId, String targetTitle, int notification_type);
}
