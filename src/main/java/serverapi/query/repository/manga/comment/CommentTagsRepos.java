package serverapi.query.repository.manga.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.features.MangaCommentDTOs.CommentTagsDTO;
import serverapi.query.dtos.features.MangaCommentDTOs.MangaCommentDTOs;
import serverapi.tables.manga_tables.manga_comment.manga_comment_tags.CommentTags;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface CommentTagsRepos extends JpaRepository<CommentTags, Long> {

    @Query("SELECT new serverapi.query.dtos.features.MangaCommentDTOs.CommentTagsDTO(" +
            "ct.manga_comment_tag_id," +
            "cm.manga_comment_id, " +
            "us.user_id, us.user_name, us.user_avatar, " +
            "ct.off_set)" +
            "FROM CommentTags ct " +
            "JOIN MangaComments cm ON ct.manga_comment.manga_comment_id = cm.manga_comment_id " +
            "JOIN ct.user us " +
            "WHERE cm.manga_comment_id =?1 ")
            List<CommentTagsDTO> getListTags(Long manga_comment_id);

}
