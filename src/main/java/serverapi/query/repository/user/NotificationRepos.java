package serverapi.query.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.FollowingDTO;
import serverapi.tables.user_tables.following_manga.FollowingManga;
import serverapi.tables.user_tables.notification.notifications.Notifications;
import serverapi.tables.user_tables.user.User;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface NotificationRepos extends JpaRepository<Notifications, Long> {


}
