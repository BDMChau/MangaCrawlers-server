package serverapi.query.repository.manga.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.MangaCommentDTOs.CommentTreesDTO;
import serverapi.query.dtos.features.MangaCommentDTOs.MangaCommentDTOs;
import serverapi.tables.manga_tables.manga_comment.manga_comments.MangaComments;

import java.util.List;
import java.util.Optional;

@Repository
public interface MangaCommentsRepos extends JpaRepository<MangaComments, Long> {


    /**
     * Use for get manga comments level 0 by using manga_id, level
     * Use pageable to get a number of comments
     *
     * @param manga_id
     * @param pageable
     * @return list manga comments
     */
    @Query("""
            SELECT new serverapi.query.dtos.features.MangaCommentDTOs.MangaCommentDTOs(
                us.user_id, us.user_name, us.user_avatar, 
                ma.manga_id, 
                cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content, 
                cr.manga_comment_relation_id, cr.parent_id.manga_comment_id, cr.child_id.manga_comment_id, cr.level, 
                ci.manga_comment_image_id, ci.image_url ) 
                        
            FROM CommentRelations cr INNER JOIN MangaComments cm ON cm.manga_comment_id = cr.child_id.manga_comment_id 
            LEFT JOIN cm.user us 
            LEFT JOIN cm.manga ma 
            LEFT JOIN cm.comment_image ci 
                        
            WHERE cm.is_deprecated = false 
                AND cm.manga.manga_id = ?1 
                AND cm.chapter.chapter_id is null 
                AND cr.level = '0'
            ORDER BY cm.manga_comment_id DESC  
            """)
    List<MangaCommentDTOs> getMangaCommentsLevel0(Long manga_id, Pageable pageable);

    /**
     * Use for get manga child comment (level deeper)
     *
     * @param manga_comment_id
     * @param level
     * @param pageable
     * @return
     */
    @Query(""" 
            SELECT new serverapi.query.dtos.features.MangaCommentDTOs.CommentTreesDTO( 
                us.user_id, us.user_name, us.user_avatar, 
                ma.manga_id, ch.chapter_id,
                cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content, 
                cr.manga_comment_relation_id, cr.parent_id.manga_comment_id, cr.child_id.manga_comment_id, cr.level, 
                ci.manga_comment_image_id, ci.image_url ) 
                
            FROM CommentRelations cr 
            JOIN MangaComments cm ON cm.manga_comment_id = cr.child_id.manga_comment_id 
            LEFT JOIN cm.user us 
            LEFT JOIN cm.manga ma 
            LEFT JOIN cm.chapter ch
            LEFT JOIN cm.comment_image ci 
                        
            WHERE cm.is_deprecated = false 
                AND cr.parent_id.manga_comment_id =?1 
                AND cr.level =?2 
            ORDER BY cm.manga_comment_id DESC 
            """)
    List<CommentTreesDTO> getCommentsChild(Long manga_comment_id, String level, Pageable pageable);


    @Query(""" 
            SELECT new serverapi.query.dtos.features.MangaCommentDTOs.MangaCommentDTOs( 
                us.user_id, us.user_name, us.user_avatar, 
                ma.manga_id, 
                ch.chapter_id,
                cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content, 
                cr.manga_comment_relation_id, cr.parent_id.manga_comment_id, cr.child_id.manga_comment_id, cr.level, 
                ci.manga_comment_image_id, ci.image_url ) 
                
            FROM CommentRelations cr 
            JOIN MangaComments cm ON cm.manga_comment_id = cr.child_id.manga_comment_id 
            LEFT JOIN cm.user us 
            LEFT JOIN cm.manga ma 
            LEFT JOIN cm.chapter ch
            LEFT JOIN cm.comment_image ci 
                        
            WHERE cr.child_id.manga_comment_id =?1 
            ORDER BY cm.manga_comment_id 
            """)
    Optional<MangaCommentDTOs> findByCommentID(Long manga_comment_id);


    /**
     * For chapter's comment level 0
     *
     * @param chapter_id
     * @param pageable
     * @return
     */
    @Query("""
            SELECT new serverapi.query.dtos.features.MangaCommentDTOs.MangaCommentDTOs(
                us.user_id, us.user_name, us.user_avatar, 
                ch.chapter_id, 
                cm.manga_comment_id, cm.manga_comment_time, cm.manga_comment_content, 
                cr.manga_comment_relation_id, cr.parent_id.manga_comment_id, cr.child_id.manga_comment_id, cr.level, 
                ci.manga_comment_image_id, ci.image_url ) 
                
            FROM CommentRelations cr           
            JOIN MangaComments cm ON cm.manga_comment_id = cr.child_id.manga_comment_id 
            LEFT JOIN cm.user us 
            LEFT JOIN cm.chapter ch
            LEFT JOIN cm.comment_image ci 
                        
            WHERE cm.is_deprecated = false 
                AND cm.chapter.chapter_id = ?1
                AND cr.level = '0'
            ORDER BY cm.manga_comment_id 
            """)
    List<MangaCommentDTOs> getChapterCommentsLevel0(Long chapter_id, Pageable pageable);


}
