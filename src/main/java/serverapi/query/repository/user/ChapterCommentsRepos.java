package serverapi.query.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.CommentExportDTO;
import serverapi.query.dtos.tables.MangaChapterDTO;
import serverapi.tables.manga_tables.manga_comments.MangaComments;
import serverapi.tables.user_tables.user.User;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ChapterCommentsRepos extends JpaRepository<MangaComments, Long> {


    @Query("SELECT new serverapi.query.dtos.features.CommentExportDTO(u.user_id, u.user_name, u.user_email, u.user_avatar, " +
            "c.chapter_id, c.chapter_name, c.created_at, " +
            "cm.mangacomment_id, cm.mangacomment_time, cm.mangacomment_content) " +
            " FROM MangaComments cm JOIN cm.chapter c JOIN cm.user u JOIN Manga m ON m.manga_id = c.manga WHERE c" +
            ".chapter_id =?1 ORDER BY cm.mangacomment_time DESC")
    List<CommentExportDTO> getCommentsChapter(Long chapter_id, Pageable pageable);

//    @Query("SELECT new serverapi.Query.DTO.CommentExportDTO(u.user_id, u.user_name, u.user_email, u.user_avatar, " +
//            "c.chapter_id, c.chapter_name, c.created_at, " +
//            "cm.chaptercmt_id, cm.chaptercmt_time, cm.chaptercmt_content) " +
//            "FROM MangaComments cm JOIN cm.chapter c JOIN cm.user u JOIN Manga m ON m.manga_id = c.manga "+
//            "WHERE cm.chaptercmt_id =(SELECT MAX(cmt.chaptercmt_id) " +
//                                       "FROM MangaComments cmt INNER JOIN cmt.chapter ct) " +
//            "AND m.manga_id =?1 ORDER BY cm.chaptercmt_id DESC")
//    List<CommentExportDTO> getCommentsManga(Long manga_id, Pageable pageable);

    @Query("SELECT new serverapi.query.dtos.features.CommentExportDTO(u.user_id, u.user_name, u.user_email, u.user_avatar, " +
            "c.chapter_id, c.chapter_name, c.created_at, " +
            "cm.mangacomment_id, cm.mangacomment_time, cm.mangacomment_content) " +
            "FROM MangaComments cm JOIN cm.chapter c JOIN cm.user u JOIN Manga m ON m.manga_id = c.manga " +
            "WHERE m.manga_id =?1 " +
            "AND cm.mangacomment_id =(SELECT MAX(cmt.mangacomment_id) " +
            "FROM MangaComments cmt JOIN cmt.chapter ct " +
            "WHERE cmt.chapter = c.chapter_id ) ORDER BY cm.mangacomment_time DESC "
    )
    List<CommentExportDTO> getCommentsManga(Long manga_id, Pageable pageable);


    @Query("SELECT new serverapi.query.dtos.tables.MangaChapterDTO(c.chapter_id, c.chapter_name, c.created_at, m.manga_id," +
            " m.manga_name, m.thumbnail) FROM Manga m JOIN m.chapters c WHERE c.chapter_id = (SELECT MAX(ct" +
            ".chapter_id) FROM Manga mg INNER JOIN mg.chapters ct " +
            "WHERE mg.manga_id = m.manga_id ) ORDER BY c.chapter_id DESC")
    List<MangaChapterDTO> getLatestChapterFromManga();


    @Transactional
    @Modifying
    @Query("DELETE FROM MangaComments c WHERE c.user =:user")
    void deleteAllCommentsByUserId(@Param("user") User user);


}
