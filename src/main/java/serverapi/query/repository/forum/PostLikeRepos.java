package serverapi.query.repository.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serverapi.query.dtos.features.MangaCommentDTOs.CommentLikesDTO;
import serverapi.query.dtos.tables.PostLikeDTO;
import serverapi.tables.forum.post_like.PostLike;
import serverapi.tables.manga_tables.manga_comment.manga_comment_likes.CommentLikes;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepos extends JpaRepository<PostLike, Long> {

    @Query("SELECT new serverapi.query.dtos.tables.PostLikeDTO(" +
            "po.post_id," +
            "pl.post_like_id, " +
            "us.user_id, us.user_name, us.user_avatar)" +
            "FROM PostLike pl " +
            "JOIN Post po ON pl.post.post_id = po.post_id " +
            "JOIN pl.user us " +
            "WHERE po.post_id =?1 ")
    List<PostLikeDTO> getListLikes(Long post_id);


    @Query(value = """
            SELECT pl
            FROM PostLike pl
            WHERE pl.post.post_id =?1
                AND pl.user.user_id =?2
            order by pl.post_like_id
            """)
    Optional<PostLike> getPostLike(Long post_id, Long user_id);
}
