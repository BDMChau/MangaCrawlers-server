package serverapi.query.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTypesRepos extends JpaRepository<serverapi.tables.user_tables.notification.notification_types.NotificationTypes, Long> {
}
