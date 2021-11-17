package serverapi.query.repository.manga.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.tables.comment.comment_image.CommentImage;

import java.util.Optional;

@Repository
public interface CommentImageRepos extends JpaRepository<CommentImage, Long> {

    @Query(value = "select * " +
                "from comment_image " +
                "where comment_image.comment_id =?1", nativeQuery = true)
    Optional<CommentImage> getCommentImageByCommentID(Long comment_id);
}
