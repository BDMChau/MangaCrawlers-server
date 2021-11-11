package serverapi.query.repository.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.MangaGenreDTO;
import serverapi.query.dtos.tables.PostUserDTO;
import serverapi.tables.forum.category.Category;
import serverapi.tables.forum.post_category.PostCategory;

import java.util.List;


@Repository
public interface PostCategoryRepos extends JpaRepository<PostCategory, Long> {


    @Query("""
            SELECT cate
            FROM PostCategory post_cate
            JOIN Category cate ON cate.category_id = post_cate.category.category_id
            WHERE post_cate.post.post_id = ?1
            """)
    List<Category> getCategoriesByPostId(Long postId);
}
