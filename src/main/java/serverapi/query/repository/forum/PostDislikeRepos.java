package serverapi.query.repository.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serverapi.query.dtos.tables.PostLikeDTO;
import serverapi.tables.forum.post_dislike.PostDislike;
import serverapi.tables.forum.post_like.PostLike;

import java.util.List;
import java.util.Optional;

public interface PostDislikeRepos extends JpaRepository<PostDislike, Long> {

    @Query("SELECT new serverapi.query.dtos.tables.PostDislikeDTO(" +
            "po.post_id," +
            "pdl.post_dislike_id, " +
            "us.user_id, us.user_name, us.user_avatar)" +
            "FROM PostDislike pdl " +
            "JOIN Post po ON pdl.post.post_id = po.post_id " +
            "JOIN pdl.user us " +
            "WHERE po.post_id =?1 ")
    List<PostLikeDTO> getListDislikes(Long post_id);



    @Query(value = """
            SELECT pdl
            FROM PostDislike pdl
            WHERE pdl.post.post_id =?1
                AND pdl.user.user_id =?2
            order by pdl.post_dislike_id
            """)
    Optional<PostDislike> getPostDislike(Long post_id, Long user_id);
}