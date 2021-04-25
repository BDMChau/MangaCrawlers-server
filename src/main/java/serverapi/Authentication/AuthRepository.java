package serverapi.Authentication;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Tables.User.User;

import java.util.Optional;

@Qualifier("AuthRepository")
@Repository
public interface AuthRepository extends JpaRepository<User, Long> {


    @Query("SELECT u FROM User u WHERE u.user_email = ?1")
    Optional<User> findByEmail(String email);


}
