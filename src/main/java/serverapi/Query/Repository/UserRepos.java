package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serverapi.Tables.User.User;

public interface UserRepos extends JpaRepository<User, Long> {
}
