package serverapi.query.repository.manga.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.CommentDTOs.CommentDTO;
import serverapi.tables.comment.comment.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepos extends JpaRepository<Comment, Long> {


    @Query("""
        SELECT cmt from Comment cmt where cmt.post.post_id = ?1
    """)
    List<Comment> getCmtsByPostId(Long post_id);


    @Query("""
            SELECT new serverapi.query.dtos.features.CommentDTOs.CommentDTO(
                            COUNT(case when cr.child_id.is_deprecated = false then cr.child_id.comment_id else null end),
                            us.user_id, us.user_name, us.user_avatar,
                            cm.comment_id, cm.comment_time, cm.comment_content, cm.count_like, cm.is_deprecated,
                            cr.parent_id.comment_id,
                            ci.comment_image_id, ci.image_url)
                
            FROM Comment cm
            JOIN CommentRelation cr ON cm.comment_id = cr.parent_id.comment_id
            LEFT JOIN cm.user us
            LEFT JOIN cm.manga ma
            LEFT JOIN cm.chapter ch
            LEFT JOIN cm.post po
            LEFT JOIN cm.comment_image ci
            WHERE (cm.is_deprecated = false
                   AND po.post_id = case when (:title) = 'post' then (select post_id from Post where post_id = (:target_id))
                                 else null
                                 end)
                    OR (ma.manga_id = case when (:title) = 'manga' then (select manga_id from Manga where manga_id = (:target_id))
                                 else null
                                 end
                    AND  cm.is_deprecated = false)
            GROUP BY us.user_id, us.user_name, us.user_avatar,
                    po.post_id,
                    cm.comment_id, cm.comment_time, cm.comment_content,cm.is_deprecated,
                    cr.parent_id.comment_id,
                    ci.comment_image_id, ci.image_url
            ORDER BY cm.comment_time desc
            """)
    List<CommentDTO> getComments(@Param("title") String target_title, @Param("target_id") Long target_id, Pageable pageable);

    @Query("""
            SELECT new serverapi.query.dtos.features.CommentDTOs.CommentDTO(
                            us.user_id, us.user_name, us.user_avatar,
                            cm.comment_id, cm.comment_time, cm.comment_content, cm.count_like, cm.is_deprecated,
                            cr.parent_id.comment_id,
                            ci.comment_image_id, ci.image_url)  
            FROM Comment cm
            JOIN CommentRelation cr ON cm.comment_id = cr.child_id.comment_id
            LEFT JOIN cm.user us
            LEFT JOIN cm.comment_image ci
            WHERE cm.is_deprecated = false
                   AND cr.parent_id.comment_id = (:comment_id)
                   AND cr.level = '1'
            ORDER BY cm.comment_time ASC
            """)
    List<CommentDTO> getCommentsChild(@Param("comment_id") Long comment_id, Pageable pageable);


    @Query("""
            SELECT new serverapi.query.dtos.features.CommentDTOs.CommentDTO(
                            us.user_id, us.user_name, us.user_avatar,
                            cm.comment_id, cm.comment_time, cm.comment_content, cm.count_like, cm.is_deprecated,
                            cr.parent_id.comment_id,
                            ci.comment_image_id, ci.image_url)  
            FROM Comment cm
            JOIN CommentRelation cr ON cm.comment_id = cr.child_id.comment_id
            LEFT JOIN cm.user us
            LEFT JOIN cm.comment_image ci
            WHERE cr.child_id.comment_id =(:comment_id) 
            """)
    Optional<CommentDTO> getCommentByID(@Param("comment_id") Long comment_id);


    @Query("""
            SELECT new serverapi.query.dtos.features.CommentDTOs.CommentDTO(
                            us.user_id, manga.manga_id, post.post_id, cm.comment_id
                            )  
            FROM Comment cm
            JOIN CommentRelation cr ON cm.comment_id = cr.child_id.comment_id
            LEFT JOIN cm.user us
            LEFT JOIN cm.manga manga
            LEFT JOIN cm.post post
            LEFT JOIN cm.comment_image ci
            WHERE cr.child_id.comment_id =(:comment_id) 
            """)
    Optional<CommentDTO> getCommentForNotification(@Param("comment_id") Long comment_id);
}
