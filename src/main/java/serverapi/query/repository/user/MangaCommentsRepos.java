package serverapi.query.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.MangaCommentDTOs;
import serverapi.tables.manga_tables.manga_comment_images.CommentImages;
import serverapi.tables.manga_tables.manga_comments.MangaComments;
import serverapi.tables.user_tables.user.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface MangaCommentsRepos extends JpaRepository<MangaComments, Long> {


//    @Query("SELECT new serverapi.query.dtos.features.CommentExportDTO(u.user_id, u.user_name, u.user_email, u.user_avatar, " +
//            "c.chapter_id, c.chapter_name, c.created_at, " +
//            "cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content) " +
//            " FROM MangaComments cm JOIN cm.chapter c JOIN cm.user u JOIN Manga m ON m.manga_id = c.manga WHERE c" +
//            ".chapter_id =?1 ORDER BY cm.manga_comment_time DESC")
//    List<CommentExportDTO> getCommentsChapter(Long chapter_id, Pageable pageable);

//    @Query("SELECT new serverapi.Query.DTO.CommentExportDTO(u.user_id, u.user_name, u.user_email, u.user_avatar, " +
//            "c.chapter_id, c.chapter_name, c.created_at, " +
//            "cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content) " +
//            "FROM MangaComments cm JOIN cm.chapter c JOIN cm.user u JOIN Manga m ON m.manga_id = c.manga "+
//            "WHERE cm.manga_comment_id =(SELECT MAX(cmt.manga_comment_id) " +
//                                       "FROM MangaComments cmt INNER JOIN cmt.chapter ct) " +
//            "AND m.manga_id =?1 ORDER BY cm.manga_comment_id DESC")
//    List<CommentExportDTO> getCommentsManga(Long manga_id, Pageable pageable);

//    @Query("SELECT new serverapi.query.dtos.features.CommentExportDTO(u.user_id, u.user_name, u.user_email, u.user_avatar, " +
//            "c.chapter_id, c.chapter_name, c.created_at, " +
//            "cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content) " +
//            "FROM MangaComments cm JOIN cm.chapter c JOIN cm.user u JOIN Manga m ON m.manga_id = c.manga " +
//            "WHERE m.manga_id =?1 " +
//            "AND cm.manga_comment_id =(SELECT MAX(cmt.manga_comment_id) " +
//            "FROM MangaComments cmt JOIN cmt.chapter ct " +
//            "WHERE cmt.chapter = c.chapter_id ) ORDER BY cm.manga_comment_time DESC "
//    )
//    List<CommentExportDTO> getCommentsManga(Long manga_id, Pageable pageable);

    /**
     * Use for get all comments
     * @return list comments
     */
    @Query("SELECT new serverapi.query.dtos.tables.MangaCommentDTOs(" +
            "us.user_id, us.user_name, us.user_avatar, " +
            "ma.manga_id, "+
            "ch.chapter_id, " +
            "cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content, " +
            "cm.to_user.user_id, cm.to_user.user_name, cm.to_user.user_avatar, " +
            "cr.manga_comment_relation_id, cr.parent_id.manga_comment_id, cr.child_id.manga_comment_id, cr.level, " +
            "ci.manga_comment_image_id, ci.image_url ) " +
            "FROM CommentRelations cr INNER JOIN MangaComments cm ON cm.manga_comment_id = cr.child_id.manga_comment_id " +
            "left join cm.user us " +
            "left join cm.manga ma " +
            "left join cm.chapter ch " +
            "left join cm.comment_image ci " +
            "ORDER BY cm.manga_comment_time DESC ")
    List<MangaCommentDTOs> getAllComments();

    /**
     * Use for get manga comments by using manga_id, level
     * Use pageable to get a number of comments
     * @param manga_id
     * @param level
     * @param pageable
     * @return list manga comments
     */
    @Query("SELECT new serverapi.query.dtos.tables.MangaCommentDTOs(" +
            "us.user_id, us.user_name, us.user_avatar, " +
            "ma.manga_id, "+
            "ch.chapter_id, ch.chapter_name, ch.created_at, " +
            "cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content, " +
            "cm.to_user.user_id, cm.to_user.user_name, cm.to_user.user_avatar, " +
            "cr.manga_comment_relation_id, cr.parent_id.manga_comment_id, cr.child_id.manga_comment_id, cr.level, " +
            "ci.manga_comment_image_id, ci.image_url ) " +
            "FROM CommentRelations cr INNER JOIN MangaComments cm ON cm.manga_comment_id = cr.child_id.manga_comment_id " +
            "left join cm.user us " +
            "left join cm.manga ma " +
            "left join cm.chapter ch " +
            "left join cm.comment_image ci " +
            "WHERE cm.manga.manga_id = ?1 " +
            "AND cr.level =?2 " +
            "ORDER BY cm.manga_comment_time DESC ")
    List<MangaCommentDTOs> getCommentsManga(Long manga_id, String level, Pageable pageable);

    /**
     * Use for get manga comments to_user by using manga_id, level, to_user_id
     * @param manga_id
     * @param level
     * @param to_user_id
     * @param pageable
     * @return
     */
    @Query("SELECT new serverapi.query.dtos.tables.MangaCommentDTOs(" +
            "us.user_id, us.user_name, us.user_avatar, " +
            "ma.manga_id, "+
            "ch.chapter_id, ch.chapter_name, ch.created_at, " +
            "cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content, " +
            "cm.to_user.user_id, cm.to_user.user_name, cm.to_user.user_avatar, " +
            "cr.manga_comment_relation_id, cr.parent_id.manga_comment_id, cr.child_id.manga_comment_id, cr.level, " +
            "ci.manga_comment_image_id, ci.image_url ) " +
            "FROM CommentRelations cr INNER JOIN MangaComments cm ON cm.manga_comment_id = cr.child_id.manga_comment_id " +
            "left join cm.user us " +
            "left join cm.manga ma " +
            "left join cm.chapter ch " +
            "left join cm.comment_image ci " +
            "WHERE cm.manga.manga_id = ?1 " +
            "AND cr.level =?2 " +
            "AND cm.to_user.user_id =?3 " +
            "ORDER BY cm.manga_comment_time DESC ")
    List<MangaCommentDTOs> getCommentsMangaByToUser(Long manga_id,String level, Long to_user_id, Pageable pageable);

    /**
     * Use for get manga comments in each level by using manga_id, level
     * @param manga_id
     * @param level
     * @return list manga comments
     */
    @Query("SELECT new serverapi.query.dtos.tables.MangaCommentDTOs(" +
            "us.user_id, us.user_name, us.user_avatar, " +
            "ma.manga_id, "+
            "ch.chapter_id, ch.chapter_name, ch.created_at, " +
            "cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content, " +
            "cm.to_user.user_id, cm.to_user.user_name, cm.to_user.user_avatar, " +
            "cr.manga_comment_relation_id, cr.parent_id.manga_comment_id, cr.child_id.manga_comment_id, cr.level, " +
            "ci.manga_comment_image_id, ci.image_url ) " +
            "FROM CommentRelations cr INNER JOIN MangaComments cm ON cm.manga_comment_id = cr.child_id.manga_comment_id " +
            "left join cm.user us " +
            "left join cm.manga ma " +
            "left join cm.chapter ch " +
            "left join cm.comment_image ci " +
            "WHERE cm.manga.manga_id = ?1 " +
            "AND cr.level =?2 " +
            "ORDER BY cm.manga_comment_time DESC ")
    List<MangaCommentDTOs> getCommentsMangaLevels(Long manga_id, String level);

    /**
     * Use for get chapter comments in each level by using chapter_id, level
     * Use pageable to get a number of comments
     * @param chapter_id
     * @param level
     * @param pageable
     * @return
     */
    @Query("SELECT new serverapi.query.dtos.tables.MangaCommentDTOs(" +
            "us.user_id, us.user_name, us.user_avatar, " +
            "ma.manga_id, "+
            "ch.chapter_id, " +
            "cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content, " +
            "cm.to_user.user_id, cm.to_user.user_name, cm.to_user.user_avatar, " +
            "cr.manga_comment_relation_id, cr.parent_id.manga_comment_id, cr.child_id.manga_comment_id, cr.level, " +
            "ci.manga_comment_image_id, ci.image_url) " +
            "FROM CommentRelations cr INNER JOIN MangaComments cm ON cm.manga_comment_id = cr.child_id.manga_comment_id " +
            "left join cm.user us " +
            "left join cm.manga ma " +
            "left join cm.chapter ch " +
            "left join cm.comment_image ci " +
            "WHERE cm.chapter.chapter_id = ?1 " +
            "AND cr.level =?2 " +
            "ORDER BY cm.manga_comment_time DESC ")
    List<MangaCommentDTOs> getCommentsChapter(Long chapter_id, String level, Pageable pageable);




    @Transactional
    @Modifying
    @Query("DELETE FROM MangaComments c WHERE c.user =:user")
    void deleteAllCommentsByUserId(@Param("user") User user);


}
