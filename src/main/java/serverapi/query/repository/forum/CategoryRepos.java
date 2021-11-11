package serverapi.query.repository.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.MangaGenreDTO;
import serverapi.query.dtos.tables.PostUserDTO;
import serverapi.tables.forum.category.Category;
import serverapi.tables.forum.post_category.PostCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepos extends JpaRepository<Category, Long> {


    @Query("""
            SELECT cate
            FROM Category cate
            JOIN cate.postCategories post_cate ON post_cate.category.category_id = cate.category_id
            WHERE post_cate.post.post_id = ?1
            """)
    List<Category> getAllByPostId(Long postId);
}

