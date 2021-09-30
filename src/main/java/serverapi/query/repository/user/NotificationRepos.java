package serverapi.query.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.AverageStarDTO;
import serverapi.query.dtos.tables.FollowingDTO;
import serverapi.tables.user_tables.following_manga.FollowingManga;
import serverapi.tables.user_tables.notification.notifications.Notifications;
import serverapi.tables.user_tables.user.User;

import java.util.List;


@Repository
public interface NotificationRepos extends JpaRepository<Notifications, Long> {

    @Query("SELECT no FROM Notifications no JOIN User u ON no.to_user.user_id = u.user_id WHERE u.user_id = ?1")
    List<Notifications> getAllByUserId(Long user_id);


    @Query("SELECT no FROM Notifications no JOIN User u ON no.to_user.user_id = u.user_id WHERE u.user_id = ?1")
    List<Notifications> getListByUserId(Long user_id, Pageable pageable);
}
