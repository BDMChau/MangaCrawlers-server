package serverapi.Query.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTO.ChapterCommentsDTO;
import serverapi.Query.DTO.CommentExportDTO;
import serverapi.Tables.ChapterComments.ChapterComments;
import serverapi.Tables.User.User;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ChapterCommentsRepos extends JpaRepository<ChapterComments, Long> {


    @Query("SELECT new serverapi.Query.DTO.CommentExportDTO(u.user_id, u.user_name, u.user_email, u.user_avatar, " +
            "c.chapter_id, c.chapter_name, c.createdAt, " +
            "cm.chaptercmt_id, cm.chaptercmt_time, cm.chaptercmt_content) " +
            " FROM ChapterComments cm JOIN cm.chapter c JOIN cm.user u JOIN Manga m ON m.manga_id = c.manga WHERE c" +
            ".chapter_id =?1 ORDER BY cm.chaptercmt_time DESC")
    List<CommentExportDTO> getCommentsChapter(Long chapter_id, Pageable pageable);



    @Transactional
    @Modifying
    @Query("DELETE FROM ChapterComments c WHERE c.user =:user")
    void deleteAllCommentsByUserId(@Param("user") User user);


}
