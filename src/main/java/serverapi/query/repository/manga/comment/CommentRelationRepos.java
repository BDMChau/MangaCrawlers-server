package serverapi.query.repository.manga.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.CommentDTOs.CommentDTO;
import serverapi.tables.comment.comment_relation.CommentRelation;

import java.util.List;

@Repository
public interface CommentRelationRepos extends JpaRepository<CommentRelation, Long> {

}
