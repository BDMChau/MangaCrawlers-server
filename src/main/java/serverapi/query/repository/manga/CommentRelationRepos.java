package serverapi.query.repository.manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverapi.tables.manga_tables.manga_comment_relations.CommentRelations;

@Repository
public interface CommentRelationRepos extends JpaRepository<CommentRelations, Long> {
}
