package serverapi.query.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.CommentExportDTO;
import serverapi.query.dtos.tables.MangaCommentDTOs;
import serverapi.tables.manga_tables.manga_comments.MangaComments;
import serverapi.tables.user_tables.user.User;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MangaCommentsRepos extends JpaRepository<MangaComments, Long> {


    @Query("SELECT new serverapi.query.dtos.features.CommentExportDTO(u.user_id, u.user_name, u.user_email, u.user_avatar, " +
            "c.chapter_id, c.chapter_name, c.created_at, " +
            "cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content) " +
            " FROM MangaComments cm JOIN cm.chapter c JOIN cm.user u JOIN Manga m ON m.manga_id = c.manga WHERE c" +
            ".chapter_id =?1 ORDER BY cm.manga_comment_time DESC")
    List<CommentExportDTO> getCommentsChapter(Long chapter_id, Pageable pageable);

//    @Query("SELECT new serverapi.Query.DTO.CommentExportDTO(u.user_id, u.user_name, u.user_email, u.user_avatar, " +
//            "c.chapter_id, c.chapter_name, c.created_at, " +
//            "cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content) " +
//            "FROM MangaComments cm JOIN cm.chapter c JOIN cm.user u JOIN Manga m ON m.manga_id = c.manga "+
//            "WHERE cm.manga_comment_id =(SELECT MAX(cmt.manga_comment_id) " +
//                                       "FROM MangaComments cmt INNER JOIN cmt.chapter ct) " +
//            "AND m.manga_id =?1 ORDER BY cm.manga_comment_id DESC")
//    List<CommentExportDTO> getCommentsManga(Long manga_id, Pageable pageable);

    @Query("SELECT new serverapi.query.dtos.features.CommentExportDTO(u.user_id, u.user_name, u.user_email, u.user_avatar, " +
            "c.chapter_id, c.chapter_name, c.created_at, " +
            "cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content) " +
            "FROM MangaComments cm JOIN cm.chapter c JOIN cm.user u JOIN Manga m ON m.manga_id = c.manga " +
            "WHERE m.manga_id =?1 " +
            "AND cm.manga_comment_id =(SELECT MAX(cmt.manga_comment_id) " +
            "FROM MangaComments cmt JOIN cmt.chapter ct " +
            "WHERE cmt.chapter = c.chapter_id ) ORDER BY cm.manga_comment_time DESC "
    )
    List<CommentExportDTO> getCommentsManga(Long manga_id, Pageable pageable);

    @Query("SELECT new serverapi.query.dtos.tables.MangaCommentDTOs(" +
            "cm.user.user_id, cm.user.user_name, cm.user.user_email, cm.user.user_avatar, " +
            "cm.manga.manga_id, cm.manga.manga_name, cm.manga.status, cm.manga.description, " +
            "cm.manga.stars, cm.manga.views, cm.manga.thumbnail, cm.manga.date_publications, cm.manga.created_at, " +
            "cm.chapter.chapter_id, cm.chapter.chapter_name, cm.chapter.created_at, " +
            "cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content, " +
            "cr.manga_comment_relation_id, cr.parent_id.manga_comment_id, cr.child_id.manga_comment_id) " +
            "FROM CommentRelations cr INNER JOIN MangaComments cm ON cm.manga_comment_id = cr.child_id.manga_comment_id " +
            "left join cm.user " +
            "left join cm.manga " +
            "left join cm.chapter ")
    List<MangaCommentDTOs> getCommentsManga02();

    @Query("SELECT new serverapi.query.dtos.tables.MangaCommentDTOs(cm.user.user_id, cm.manga.manga_id, cm.chapter.chapter_id, " +
            "cm.manga_comment_id, cm.manga_comment_content, cr.manga_comment_relation_id, cr.parent_id.manga_comment_id, cr.child_id.manga_comment_id) "+
            "FROM CommentRelations cr INNER JOIN MangaComments cm ON cm.manga_comment_id = cr.child_id.manga_comment_id " +
            "left join cm.user " +
            "left join cm.manga " +
            "left join cm.chapter WHERE cm.manga.manga_id = 5")
    List<MangaCommentDTOs> getCommentsManga03();


    @Query("SELECT new serverapi.query.dtos.tables.MangaCommentDTOs(cm.user.user_id, cm.manga.manga_id, cm.chapter.chapter_id, " +
            "cm.manga_comment_id, cm.manga_comment_content, cr.manga_comment_relation_id, cr.parent_id.manga_comment_id, cr.child_id.manga_comment_id) "+
            "FROM CommentRelations cr INNER JOIN MangaComments cm ON cm.manga_comment_id = cr.child_id.manga_comment_id " +
            "left join cm.user " +
            "left join cm.manga " +
            "left join cm.chapter " +
            "WHERE cm.manga.manga_id = ?1 ORDER BY cm.manga_comment_time DESC")
    List<MangaCommentDTOs> getCommentsByMangaId(Long mangaId, Pageable pageable);

    @Query("SELECT new serverapi.query.dtos.tables.MangaCommentDTOs(cm.user.user_id, cm.manga.manga_id, cm.chapter.chapter_id, " +
            "cm.manga_comment_id, cm.manga_comment_content, cr.manga_comment_relation_id, cr.parent_id.manga_comment_id, cr.child_id.manga_comment_id) "+
            "FROM CommentRelations cr INNER JOIN MangaComments cm ON cm.manga_comment_id = cr.child_id.manga_comment_id " +
            "left join cm.user " +
            "left join cm.manga " +
            "left join cm.chapter " +
            "WHERE cm.chapter.chapter_id = ?1 ORDER BY cm.manga_comment_time DESC")
    List<CommentExportDTO> getCommentsByChapter(Long chapterId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE FROM MangaComments c WHERE c.user =:user")
    void deleteAllCommentsByUserId(@Param("user") User user);


}
