package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverapi.Tables.UpdateView.UpdateView;

@Repository
public interface UpdateViewRepos extends JpaRepository<UpdateView, Long> {


}
