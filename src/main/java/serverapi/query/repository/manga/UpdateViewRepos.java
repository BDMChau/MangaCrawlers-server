package serverapi.query.repository.manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.UpdateViewDTO;
import serverapi.tables.manga_tables.update_view.UpdateView;

import java.util.List;

@Repository
public interface UpdateViewRepos extends JpaRepository<UpdateView, Long>, JpaSpecificationExecutor<UpdateView> {


    @Query(value = "SELECT new serverapi.query.dtos.features.UpdateViewDTO(m.manga_id,m.manga_name, m.thumbnail, m.description, m.status, m.stars," +
            "m.views, m.date_publications, m.created_at, u.updatedview_id, u.totalviews, u.created_at) " +
            "FROM UpdateView u JOIN u.manga m " +
            "WHERE u.created_at >= current_date - :from_time and u.created_at < current_date - :to_time")
    List<UpdateViewDTO> getWeekly(@Param("from_time")int from_time, @Param("to_time") int to_time);

}
