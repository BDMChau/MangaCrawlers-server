package serverapi.query.repository.manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.tables.manga_tables.manga_comment.manga_comment_images.CommentImages;

import java.util.Optional;

@Repository
public interface CommentImageRepos extends JpaRepository<CommentImages, Long> {

    @Query(value = "select * " +
            "from manga_comment_images " +
            "where manga_comment_images.manga_comment_id =?1", nativeQuery = true)
    Optional<CommentImages> getCommentImagesByManga_comment(Long manga_comment_id);
}
