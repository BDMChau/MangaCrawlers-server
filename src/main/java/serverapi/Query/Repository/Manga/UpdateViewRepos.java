package serverapi.Query.Repository.Manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTOs.FeaturesDTOs.UpdateViewDTO;
import serverapi.Tables.MangaTables.UpdateView.UpdateView;

import java.util.List;

@Repository
public interface UpdateViewRepos extends JpaRepository<UpdateView, Long>, JpaSpecificationExecutor<UpdateView> {


    @Query(value = "SELECT new serverapi.Query.DTOs.FeaturesDTOs.UpdateViewDTO(m.manga_id,m.manga_name, m.thumbnail, m.description, m.status, m.stars," +
            "m.views, m.date_publications, m.created_at, u.updatedview_id, u.totalviews, u.created_at) " +
            "FROM UpdateView u JOIN u.manga m " +
            "WHERE u.created_at >= current_date - :from_time and u.created_at < current_date - :to_time", nativeQuery = true)
    List<UpdateViewDTO> getWeekly(@Param("from_time")int from_time, @Param("to_time") int to_time);

}
