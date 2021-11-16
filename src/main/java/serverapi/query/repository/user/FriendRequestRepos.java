package serverapi.query.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.FriendDTO;
import serverapi.query.dtos.tables.NotificationDTO;
import serverapi.tables.user_tables.friend_request_status.FriendRequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepos extends JpaRepository<FriendRequestStatus, Long> {

//    @Query("""
//             SELECT new serverapi.query.dtos.features.FriendDTO(u.user_id ,
//              us.user_relation_id, us.child_id.user_id, us.parent_id.user_id
//              )
//            FROM UserRelations us join FriendRequestStatus frs on frs.user = us.parent_id
//            left join User u on u.user_id = us.child_id.user_id
//            where frs.status = true
//            and us.parent_id.user_id =?1
//            order by frs.friend_request_id
//             """)
//    List<NotificationDTO> getListByUserId(Long user_id, Pageable pageable);

    @Query("""
             SELECT DISTINCT new serverapi.query.dtos.features.FriendDTO(
              us.child_id.user_id, us.parent_id.user_id,
              frs.status
              )
            FROM UserRelations us
            JOIN FriendRequestStatus frs on frs.friend_request_id = us.friendRequest.friend_request_id
            left join User u on u.user_id = us.child_id.user_id
            or frs.to_user = us.child_id
            where us.parent_id.user_id =?1 or us.child_id.user_id =?1
             """)
    Page<FriendDTO> getListByUserId(Long user_id, Pageable pageable);


    @Query("""
             SELECT DISTINCT new serverapi.query.dtos.features.FriendDTO(
              us.child_id.user_id, us.parent_id.user_id,
              frs.status
              )
            FROM UserRelations us
            JOIN FriendRequestStatus frs on frs.friend_request_id = us.friendRequest.friend_request_id
            left join User u on u.user_id = us.child_id.user_id
            or frs.to_user = us.child_id
            where us.parent_id.user_id =?1 or us.child_id.user_id =?1
             """)
    List<FriendDTO> getListByUserId(Long user_id);


    @Query("""
             SELECT DISTINCT new serverapi.query.dtos.features.FriendDTO(
             COUNT(us.user_relation_id)
               )
            FROM UserRelations us
            JOIN FriendRequestStatus frs on frs.friend_request_id = us.friendRequest.friend_request_id
            where us.parent_id.user_id =?1 or us.child_id.user_id =?1
             """)
    Optional<FriendDTO> getTotalFriend(Long user_id);


    @Query("""
             SELECT new serverapi.query.dtos.features.FriendDTO(us.user_relation_id, us.parent_id.user_id, us.child_id.user_id,
                                                                frs.friend_request_id)
            FROM UserRelations us
            JOIN FriendRequestStatus frs ON us.friendRequest.friend_request_id = frs.friend_request_id
            where (us.parent_id.user_id = ?1 and us.child_id.user_id = ?2)
            or (us.parent_id.user_id =?2 and us.child_id.user_id = ?1)
             """)
    Optional<FriendDTO> findFriendByUserId(Long user_id, Long to_user_id);

    @Query(value = """
            SELECT frs FROM FriendRequestStatus frs
            WHERE (frs.user.user_id =?1 AND frs.to_user.user_id =?2 AND frs.status = true) or (frs.user.user_id =?2 AND frs.to_user.user_id =?1 AND frs.status = true)
                      
            """)
    Optional<FriendRequestStatus> getFriendStatus(Long user_id, Long to_user_id);

    @Query(value = """
            SELECT frs FROM FriendRequestStatus frs
            WHERE (frs.user.user_id =?1 AND frs.to_user.user_id =?2) or (frs.user.user_id =?2 AND frs.to_user.user_id =?1)
            ORDER BY frs.friend_request_id desc 
            """)
    List<FriendRequestStatus> getAllFriendStatus(Long user_id, Long to_user_id);

//    @Query("""
//             SELECT DISTINCT new serverapi.query.dtos.features.FriendDTO(
//              us.child_id.user_id, us.parent_id.user_id,
//              frs.status
//              )
//            FROM UserRelations us
//            JOIN FriendRequestStatus frs on frs.user.user_id = us.parent_id.user_id  and frs.to_user = us.child_id
//            left join User u on u.user_id = us.child_id.user_id
//            or frs.to_user = us.child_id
//            where us.parent_id.user_id =?1
//            and frs.status = false
//            or us.child_id.user_id =? 1
//             """)
//    List<NotificationDTO> agetListByUserId(Long user_id, Pageable pageable);
}