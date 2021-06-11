package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTO.UpdateViewDTO;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.UpdateView.UpdateView;

import java.util.List;

@Repository
public interface UpdateViewRepos extends JpaRepository<UpdateView, Long>, JpaSpecificationExecutor<UpdateView> {


    @Query("SELECT new serverapi.Query.DTO.UpdateViewDTO(m.manga_id,m.manga_name, m.thumbnail, m.description, m.status, m.stars," +
            "m.views, m.date_publications, m.createdAt, u.updatedview_id, u.totalviews, u.createdAt) " +
            "FROM UpdateView u JOIN u.manga m " +
            "WHERE u.createdAt >= current_date - :from_time and u.createdAt < current_date - :to_time ")
    List<UpdateViewDTO> getWeekly(@Param("from_time")int from_time, @Param("to_time") int to_time);

}
