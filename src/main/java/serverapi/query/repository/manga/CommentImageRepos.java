package serverapi.query.repository.manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverapi.tables.manga_tables.manga_comment_images.CommentImages;

@Repository
public interface CommentImageRepos extends JpaRepository<CommentImages, Long> {
}
