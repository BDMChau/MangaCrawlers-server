package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Tables.TransGroup.TransGroup;

import java.util.Optional;

@Repository
public interface TransGroupRepos extends JpaRepository<TransGroup, Long> {

    @Query("SELECT transgr FROM TransGroup transgr WHERE transgr.transgroup_name = ?1")
    Optional<TransGroup> findByName(String groupName);

    @Query("SELECT transgr FROM TransGroup transgr WHERE transgr.transgroup_email = ?1")
    Optional<TransGroup> findByEmail(String groupEmail);

}
