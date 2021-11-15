package serverapi.query.repository.manga.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverapi.tables.manga_tables.comment.comment_relation.CommentRelation;

@Repository
public interface CommentRelationRepos extends JpaRepository<CommentRelation, Long> {
}
