package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Tables.User.User;

@Repository
public interface UserRepos extends JpaRepository<User, Long> {

    @Query("SELECT new serverapi.Query.DTO. ")
}
