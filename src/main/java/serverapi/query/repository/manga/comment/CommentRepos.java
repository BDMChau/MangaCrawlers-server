package serverapi.query.repository.manga.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.CommentDTOs.CommentDTO;
import serverapi.tables.comment.comment.Comment;

import java.util.List;

@Repository
public interface CommentRepos extends JpaRepository<Comment, Long> {


    @Query("""
        SELECT cmt from Comment cmt where cmt.post.post_id = ?1
    """)
    List<Comment> getCmtsByPostId(Long post_id);


    @Query("""
            SELECT new serverapi.query.dtos.features.CommentDTOs.CommentDTO(
                            COUNT(cr.child_id.comment_id),
                            us.user_id, us.user_name, us.user_avatar,
                            cm.comment_id, cm.comment_time, cm.comment_content, cm.count_like,
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
                            cm.comment_id, cm.comment_time, cm.comment_content, cm.count_like,
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

}
