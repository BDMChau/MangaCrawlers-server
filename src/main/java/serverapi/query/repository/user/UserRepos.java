package serverapi.query.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.ReportDTOs.ReportTopMangaDTO;
import serverapi.query.dtos.features.ReportDTOs.ReportUserFollowMangaDTO;
import serverapi.query.dtos.features.ReportDTOs.UserRDTO;
import serverapi.query.dtos.tables.ChapterDTO;
import serverapi.query.dtos.tables.MangaDTO;
import serverapi.query.dtos.tables.UserTransGroupDTO;
import serverapi.query.specification.Specificationn;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.user_tables.trans_group.TransGroup;
import serverapi.tables.user_tables.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepos extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("SELECT new serverapi.query.dtos.tables.ChapterDTO(c.chapter_id, c.chapter_name, c.created_at) FROM Chapter c JOIN c.readingHistories rd WHERE c.chapter_id =?1 ")
    List<ChapterDTO> findChapterByReadingHistory(Long chapter_id);

    @Query("SELECT new serverapi.query.dtos.tables.MangaDTO(m.manga_id, m.manga_name, m.thumbnail) FROM Manga m JOIN m.readingHistories rd WHERE m.manga_id =?1 ")
    List<MangaDTO> findMangaByReadingHistory(Long manga_id);

    @Query("SELECT new serverapi.query.dtos.features.ReportDTOs.ReportUserFollowMangaDTO(COUNT(u.user_id), a.author_id, a.author_name, m.manga_id, m.manga_name, m.thumbnail, m.stars, m.views, m.date_publications)" +
            "FROM FollowingManga fm JOIN fm.manga m JOIN fm.user u JOIN m.author a GROUP BY a.author_id, a.author_name, m.manga_id, m.manga_name, m.thumbnail, m.stars, m.views, m.date_publications" +
            " ORDER BY COUNT(u.user_id) DESC")
    List<ReportUserFollowMangaDTO> findAllFollwingManga(Pageable pageable);


//    @Query("SELECT new serverapi.Query.DTO.UserTransgroupDTO(u.user_id, u.user_name, u.user_email, user_avatar," +
//            " tg.transgroup_id)" +
//            " FROM TransGroup tg JOIN tg.users u" +
//            " WHERE tg.transgroup_id =?1")
//    List<UserTransgroupDTO> getUsersTransgroup(Long transgroup_id);

    @Query("SELECT new serverapi.query.dtos.tables.UserTransGroupDTO(u.user_id, u.user_name, u.user_email, u.user_avatar, tg.transgroup_id)" +
            "FROM TransGroup tg INNER JOIN tg.users u WHERE tg.transgroup_id =?1 ORDER BY u.user_id DESC")
    List<UserTransGroupDTO> getUsersTransGroup(Long transgroup_id);

//    @Query("SELECT new serverapi.Query.DTO.UserDTO(u.user_id, u.user_name, u.user_email, u.user_avatar")
//    Optional<UserDTO> getUser(Long UserDTO);

    @Query("SELECT new serverapi.query.dtos.features.ReportDTOs.ReportTopMangaDTO(a.author_id,a.author_name, m.manga_id, m.manga_name, m.thumbnail, m.stars, m.views, m.date_publications, m.created_at)" +
            "FROM Manga m JOIN m.author a ORDER BY m.views DESC ")
    List<ReportTopMangaDTO> findTopManga(Pageable pageable);


    @Query("SELECT new serverapi.query.dtos.features.ReportDTOs.UserRDTO(u.user_id, u.created_at) FROM User u")
    List<UserRDTO> getAllUser();

    @Query("SELECT u FROM User u JOIN u.transgroup WHERE u.user_id = ?1")
    Optional<TransGroup> getTransGroupById(Long userId);


    @Query("SELECT u FROM User u WHERE u.user_email = ?1")
    Optional<User> findByEmail(String email);
}

