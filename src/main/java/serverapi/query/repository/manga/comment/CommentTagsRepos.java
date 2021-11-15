package serverapi.query.repository.manga.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.CommentDTOs.CommentTagsDTO;
import serverapi.tables.manga_tables.comment.comment_tag.CommentTag;

import java.util.List;

@Repository
public interface CommentTagsRepos extends JpaRepository<CommentTag, Long> {

    // Using DTO
    @Query("SELECT new serverapi.query.dtos.features.CommentDTOs.CommentTagsDTO(" +
            "ct.comment_tag_id," +
            "cm.comment_id, " +
            "us.user_id, us.user_name, us.user_avatar, " +
            "ct.off_set)" +
            "FROM CommentTag ct " +
            "JOIN Comment cm ON ct.comment.comment_id = cm.comment_id " +
            "JOIN ct.user us " +
            "WHERE cm.comment_id =?1 ")
            List<CommentTagsDTO> getListTags(Long manga_comment_id);

    // Using table
    @Query("SELECT ct FROM CommentTag ct WHERE ct.comment.comment_id =?1")
            List<CommentTag> getListCommentTags(Long manga_comment_id);
}
