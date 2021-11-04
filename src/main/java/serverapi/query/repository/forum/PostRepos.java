package serverapi.query.repository.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.AuthorMangaDTO;
import serverapi.query.dtos.tables.MangaGenreDTO;
import serverapi.query.dtos.tables.PostUserDTO;
import serverapi.tables.forum.post.Post;
import serverapi.tables.forum.post_category.PostCategory;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepos extends JpaRepository<Post, Long> {


    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(
            post.post_id, post.title, post.content, post.created_at,
            cate.category_id, cate.name, cate.color, cate.description,
            user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
            )
            FROM Post post
            LEFT JOIN PostCategory p_cate ON post.post_id = p_cate.post.post_id
            LEFT JOIN Category cate ON cate.category_id = p_cate.category.category_id
            LEFT JOIN User user ON user.user_id = post.user.user_id
            WHERE post.post_id = ?1
            """)
    Optional<PostUserDTO> getByPostId(Long postId);

}
