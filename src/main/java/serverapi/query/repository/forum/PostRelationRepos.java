package serverapi.query.repository.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverapi.tables.forum.category.Category;
import serverapi.tables.forum.post_relation.PostRelation;

@Repository
public interface PostRelationRepos extends JpaRepository<PostRelation, Long> {
}

