package serverapi.query.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.NotificationDTO;
import serverapi.tables.user_tables.friend_request_status.FriendRequestStatus;

import java.util.List;
@Repository
public interface FriendRequestRepos extends JpaRepository<FriendRequestStatus, Long> {

    @Query("""
             SELECT new serverapi.query.dtos.features.FriendDTO(u.user_id or,
              us.user_relation_id, us.child_id.user_id, us.parent_id.user_id
              )
            FROM userRelations us join FriendRequestStatus frs on frs.user = us.parent_id
            left join User u on u.user_id = us.child_id
            where frs.status = true
            and us.parent_id.user_id =?1
            order by frs.friend_request_id
             """)
    List<NotificationDTO> getListByUserId(Long user_id, Pageable pageable);

//    @Query("""
//             SELECT new serverapi.query.dtos.features.FriendDTO(u.user_id,
//              us.user_relation_id, us.child_id.user_id, us.parent_id.user_id
//              )
//            FROM userRelations us
//            left join User u on u.user_id = us.child_id
//            where us.parent_id.user_id =?1 or us.child_id.user_id =?1
//             """)
//    List<NotificationDTO> getListByUserId(Long user_id, Pageable pageable);
}
