package serverapi.query.repository.manga.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.CommentDTOs.CommentLikesDTO;
import serverapi.tables.manga_tables.comment.comment_like.CommentLike;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentLikesRepos extends JpaRepository<CommentLike, Long> {

    @Query("SELECT new serverapi.query.dtos.features.CommentDTOs.CommentLikesDTO(" +
            "cm.comment_id," +
            "cl.comment_like_id, " +
            "us.user_id, us.user_name, us.user_avatar)" +
            "FROM CommentLike cl " +
            "JOIN Comment cm ON cl.comment.comment_id = cm.comment_id " +
            "JOIN cl.user us " +
            "WHERE cm.comment_id =?1 ")
    List<CommentLikesDTO> getListLikes(Long manga_comment_id);

//    @Query("SELECT new serverapi.query.dtos.features.CommentDTOs.CommentLikesDTO(" +
//            "cm.manga_comment_id," +
//            "cl.manga_comment_like_id, " +
//            "us.user_id, us.user_name, us.user_avatar)" +
//            "FROM CommentLike cl " +
//            "JOIN Comment cm ON cl.comment.manga_comment_id = cm.manga_comment_id " +
//            "JOIN cl.user us " +
//            "WHERE cm.manga_comment_id =?1 ")
//    List<CommentLike>getListLikes(Long manga_comment_id);

    @Query(value = """
            SELECT cl
            FROM CommentLike cl
            WHERE cl.comment.comment_id =?1
                AND cl.user.user_id =?2
            order by cl.comment_like_id
            """)
    Optional<CommentLike> getCommentLike(Long comment_id, Long user_id);
}
