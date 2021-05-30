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
import serverapi.Query.DTO.MangaChapterDTO;
import serverapi.Query.DTO.MangaChapterGenreDTO;
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

//    @Query("SELECT new serverapi.Query.DTO.CommentExportDTO(u.user_id, u.user_name, u.user_email, u.user_avatar, " +
//            "c.chapter_id, c.chapter_name, c.createdAt, " +
//            "cm.chaptercmt_id, cm.chaptercmt_time, cm.chaptercmt_content) " +
//            "FROM ChapterComments cm JOIN cm.chapter c JOIN cm.user u JOIN Manga m ON m.manga_id = c.manga "+
//            "WHERE cm.chaptercmt_id =(SELECT MAX(cmt.chaptercmt_id) " +
//                                       "FROM ChapterComments cmt INNER JOIN cmt.chapter ct) " +
//            "AND m.manga_id =?1 ORDER BY cm.chaptercmt_id DESC")
//    List<CommentExportDTO> getCommentsManga(Long manga_id, Pageable pageable);

    @Query("SELECT new serverapi.Query.DTO.CommentExportDTO(u.user_id, u.user_name, u.user_email, u.user_avatar, " +
            "c.chapter_id, c.chapter_name, c.createdAt, " +
            "cm.chaptercmt_id, cm.chaptercmt_time, cm.chaptercmt_content) " +
            "FROM ChapterComments cm JOIN cm.chapter c JOIN cm.user u JOIN Manga m ON m.manga_id = c.manga " +
            "WHERE m.manga_id =?1 " +
            "AND cm.chaptercmt_id =(SELECT MAX(cmt.chaptercmt_id) " +
                                   "FROM ChapterComments cmt JOIN cmt.chapter ct " +
                                   "WHERE cmt.chapter = c.chapter_id ORDER BY cm.chaptercmt_time DESC) "
            )
    List<CommentExportDTO> getCommentsManga(Long manga_id, Pageable pageable);


    @Query("SELECT new serverapi.Query.DTO.MangaChapterDTO(c.chapter_id, c.chapter_name, c.createdAt, m.manga_id," +
            " m.manga_name, m.thumbnail) FROM Manga m JOIN m.chapters c WHERE c.chapter_id = (SELECT MAX(ct.chapter_id) FROM Manga mg INNER JOIN mg.chapters ct " +
            "WHERE mg.manga_id = m.manga_id ) Order by c.chapter_id Desc")
    List<MangaChapterDTO> getLatestChapterFromManga();


    @Transactional
    @Modifying
    @Query("DELETE FROM ChapterComments c WHERE c.user =:user")
    void deleteAllCommentsByUserId(@Param("user") User user);


}
