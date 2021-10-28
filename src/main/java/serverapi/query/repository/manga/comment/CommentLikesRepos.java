package serverapi.query.repository.manga.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.MangaCommentDTOs.CommentLikesDTO;
import serverapi.query.dtos.features.MangaCommentDTOs.CommentTagsDTO;
import serverapi.tables.manga_tables.manga_comment.manga_comment_likes.CommentLikes;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentLikesRepos extends JpaRepository<CommentLikes, Long> {

    @Query("SELECT new serverapi.query.dtos.features.MangaCommentDTOs.CommentLikesDTO(" +
            "cm.manga_comment_id," +
            "cl.manga_comment_like_id, " +
            "us.user_id, us.user_name, us.user_avatar)" +
            "FROM CommentLikes cl " +
            "JOIN MangaComments cm ON cl.manga_comment.manga_comment_id = cm.manga_comment_id " +
            "JOIN cl.user us " +
            "WHERE cm.manga_comment_id =?1 ")
    List<CommentLikesDTO> getListLikes(Long manga_comment_id);

//    @Query("SELECT new serverapi.query.dtos.features.MangaCommentDTOs.CommentLikesDTO(" +
//            "cm.manga_comment_id," +
//            "cl.manga_comment_like_id, " +
//            "us.user_id, us.user_name, us.user_avatar)" +
//            "FROM CommentLikes cl " +
//            "JOIN MangaComments cm ON cl.manga_comment.manga_comment_id = cm.manga_comment_id " +
//            "JOIN cl.user us " +
//            "WHERE cm.manga_comment_id =?1 ")
//    List<CommentLikes>getListLikes(Long manga_comment_id);

    @Query("""
            SELECT cl
            FROM CommentLikes cl
            WHERE cl.manga_comment.manga_comment_id =?1
                AND cl.user.user_id =?2
            order by cl.manga_comment_like_id
            """)
    Optional<CommentLikes> getCommentLike(Long comment_id, Long user_id);
}
