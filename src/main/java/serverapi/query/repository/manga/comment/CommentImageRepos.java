package serverapi.query.repository.manga.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.tables.manga_tables.comment.comment_image.CommentImage;

import java.util.Optional;

@Repository
public interface CommentImageRepos extends JpaRepository<CommentImage, Long> {

    @Query(value = "select * " +
            "from manga_comment_images " +
            "where manga_comment_images.manga_comment_id =?1", nativeQuery = true)
    Optional<CommentImage> getCommentImagesByManga_comment(Long manga_comment_id);
}
