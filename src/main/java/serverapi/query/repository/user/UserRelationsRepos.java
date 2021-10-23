package serverapi.query.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverapi.tables.user_tables.user_relations.UserRelations;

@Repository
public interface UserRelationsRepos extends JpaRepository<UserRelations, Long> {
}
