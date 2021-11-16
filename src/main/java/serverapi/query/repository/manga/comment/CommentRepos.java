package serverapi.query.repository.manga.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.CommentDTOs.CommentDTO;
import serverapi.query.dtos.features.CommentDTOs.CommentTreesDTO;
import serverapi.query.dtos.features.CommentDTOs.CommentDTOs;
import serverapi.tables.comment.comment.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepos extends JpaRepository<Comment, Long> {


    @Query("""
        SELECT cmt from Comment cmt where cmt.post.post_id = ?1
    """)
    List<Comment> getCmtsByPostId(Long post_id);

    /**
     * Use for get manga comments level 0 by using manga_id, level
     * Use pageable to get a number of comments
     *
     * @param manga_id
     * @param pageable
     * @return list manga comments
     */
//    @Query("""
//            SELECT new serverapi.query.dtos.features.CommentDTOs.CommentDTOs(
//                us.user_id, us.user_name, us.user_avatar,
//                ma.manga_id,
//                cm.comment_id, cm.comment_time, cm.comment_content,
//                cr.comment_relation_id, cr.parent_id.comment_id, cr.child_id.comment_id, cr.level,
//                ci.comment_image_id, ci.image_url )
//
//            FROM CommentRelation cr INNER JOIN Comment cm ON cm.comment_id = cr.child_id.comment_id
//            LEFT JOIN cm.user us
//            LEFT JOIN cm.manga ma
//            LEFT JOIN cm.comment_image ci
//
//            WHERE cm.is_deprecated = false
//                AND cm.manga.manga_id = ?1
//                AND cm.chapter.chapter_id is null
//                AND cr.level = '0'
//            ORDER BY cm.comment_id desc
//            """)
//    List<CommentDTOs> getMangaCommentsLevel0(Long manga_id, Pageable pageable);



    @Query("""
            SELECT new serverapi.query.dtos.features.CommentDTOs.CommentDTO(
                            COUNT(cr.child_id.comment_id),
                            us.user_id, us.user_name, us.user_avatar,
                            cm.comment_id, cm.comment_time, cm.comment_content,
                            ci.comment_image_id, ci.image_url)
                
            FROM Comment cm
            JOIN CommentRelation cr ON cm.comment_id = cr.parent_id.comment_id
            LEFT JOIN cm.user us
            LEFT JOIN cm.manga ma
            LEFT JOIN cm.chapter ch
            LEFT JOIN cm.post po
            LEFT JOIN cm.comment_image ci    
            WHERE cm.is_deprecated = false
                    AND po.post_id = case 
                                         when (:title) = 'post' then (select post_id from Post where post_id = (:target_id))
                                         else null 
                                     end
                    OR ma.manga_id = case when (:title) = 'manga' then (select manga_id from Manga where manga_id = (:target_id))       
                                          else null 
                                     end                
            GROUP BY us.user_id, us.user_name, us.user_avatar,
                    po.post_id,
                    cm.comment_id, cm.comment_time, cm.comment_content,
                    ci.comment_image_id, ci.image_url       
            ORDER BY cm.comment_id desc
            """)
    List<CommentDTO> getComments(@Param("title") String title, @Param("target_id") Long target_id, Pageable pageable);

    @Query("""
            SELECT new serverapi.query.dtos.features.CommentDTOs.CommentDTO(
                            us.user_id, us.user_name, us.user_avatar,
                            cm.comment_id, cm.comment_time, cm.comment_content,
                            ci.comment_image_id, ci.image_url)  
            FROM Comment cm
            JOIN CommentRelation cr ON cm.comment_id = cr.child_id.comment_id
            LEFT JOIN cm.user us
            LEFT JOIN cm.comment_image ci
            WHERE cm.is_deprecated = false
                   AND cr.parent_id.comment_id = (:comment_id)
                   AND cr.level = '1'
            ORDER BY cm.comment_id desc
            """)
    List<CommentDTO> getCommentsChild(@Param("comment_id") Long comment_id, Pageable pageable);


    @Query("""
            SELECT new serverapi.query.dtos.features.CommentDTOs.CommentDTOs(
                us.user_id, us.user_name, us.user_avatar, 
                po.post_id, po.content,
                cm.comment_id, cm.comment_time, cm.comment_content, 
                cr.comment_relation_id, cr.parent_id.comment_id, cr.child_id.comment_id, cr.level, 
                ci.comment_image_id, ci.image_url ) 
                
            FROM CommentRelation cr           
            JOIN Comment cm ON cm.comment_id = cr.child_id.comment_id 
            LEFT JOIN cm.user us 
            LEFT JOIN cm.post po
            LEFT JOIN cm.comment_image ci 
                        
            WHERE cm.is_deprecated = false 
                AND cm.post.post_id =?1
                AND cr.level = '0'
            ORDER BY cm.comment_id desc 
            """)
    List<CommentDTO> getPostCommentsLevel0(Long post_id, Pageable pageable);
    /**
     * Use for get manga child comment (level deeper)
     *
     * @param manga_comment_id
     * @param level
     * @param pageable
     * @return
     */
    @Query(""" 
            SELECT new serverapi.query.dtos.features.CommentDTOs.CommentTreesDTO( 
                us.user_id, us.user_name, us.user_avatar, 
                ma.manga_id, ch.chapter_id, po.post_id,
                cm.comment_id, cm.comment_time, cm.comment_content, 
                cr.comment_relation_id, cr.parent_id.comment_id, cr.child_id.comment_id, cr.level, 
                ci.comment_image_id, ci.image_url ) 
                
            FROM CommentRelation cr 
            JOIN Comment cm ON cm.comment_id = cr.child_id.comment_id 
            LEFT JOIN cm.user us 
            LEFT JOIN cm.manga ma 
            LEFT JOIN cm.chapter ch
            LEFT JOIN cm.post po
            LEFT JOIN cm.comment_image ci 
                        
            WHERE cm.is_deprecated = false 
                AND cr.parent_id.comment_id =?1 
                AND cr.level =?2 
            ORDER BY cm.comment_id desc 
            """)
    List<CommentTreesDTO> getCommentsChild(Long manga_comment_id, String level, Pageable pageable);

    @Query(""" 
            SELECT new serverapi.query.dtos.features.CommentDTOs.CommentTreesDTO( 
                us.user_id, us.user_name, us.user_avatar, 
                ma.manga_id, ch.chapter_id, po.post_id,
                cm.comment_id, cm.comment_time, cm.comment_content, 
                cr.comment_relation_id, cr.parent_id.comment_id, cr.child_id.comment_id, cr.level, 
                ci.comment_image_id, ci.image_url ) 
                
            FROM CommentRelation cr 
            JOIN Comment cm ON cm.comment_id = cr.child_id.comment_id 
            LEFT JOIN cm.user us 
            LEFT JOIN cm.manga ma 
            LEFT JOIN cm.chapter ch
            LEFT JOIN cm.post po
            LEFT JOIN cm.comment_image ci 
                        
            WHERE cm.is_deprecated = false 
                AND cr.parent_id.comment_id =?1 
                AND cr.level =?2 
            ORDER BY cm.comment_id desc 
            """)
    List<CommentTreesDTO> getCommentsChild(Long manga_comment_id, String level);


    @Query(""" 
            SELECT new serverapi.query.dtos.features.CommentDTOs.CommentDTOs( 
                us.user_id, us.user_name, us.user_avatar, 
                ma.manga_id, 
                ch.chapter_id,
                po.post_id,
                cm.comment_id, cm.comment_time, cm.comment_content, 
                cr.comment_relation_id, cr.parent_id.comment_id, cr.child_id.comment_id, cr.level, 
                ci.comment_image_id, ci.image_url ) 
                
            FROM CommentRelation cr 
            JOIN Comment cm ON cm.comment_id = cr.child_id.comment_id 
            LEFT JOIN cm.user us 
            LEFT JOIN cm.manga ma 
            LEFT JOIN cm.chapter ch
            LEFT JOIN cm.post po
            LEFT JOIN cm.comment_image ci 
                        
            WHERE cr.child_id.comment_id =?1 
            ORDER BY cm.comment_id desc 
            """)
    Optional<CommentDTOs> findByCommentID(Long manga_comment_id);




}
