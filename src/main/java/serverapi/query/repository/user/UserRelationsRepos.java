package serverapi.query.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.FriendDTO;
import serverapi.tables.user_tables.user_relations.UserRelations;

import java.util.Optional;

@Repository
public interface UserRelationsRepos extends JpaRepository<UserRelations, Long> {

    @Query("""
             SELECT us
            FROM UserRelations us
            where (us.parent_id.user_id = ?1 and us.child_id.user_id = ?2)
            or (us.parent_id.user_id =?2 and us.child_id.user_id = ?1)
             """)
    Optional<UserRelations> findUserRelations(Long user_id, Long to_user_id);
}
