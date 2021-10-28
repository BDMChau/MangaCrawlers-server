package serverapi.tables.manga_tables.manga.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import serverapi.query.dtos.features.MangaCommentDTOs.MangaCommentDTOs;
import serverapi.tables.manga_tables.manga_comment.manga_comments.MangaComments;

import javax.mail.Multipart;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CommentPOJO {

    private List<String> to_users_id = new ArrayList<>();
    private String manga_id;
    private String chapter_id;
    private String manga_comment_id;
    private String manga_comment_content;
    private MultipartFile image;
    private String sticker_url;
    private String level;
    private String parent_id;
    private int key;

    private List<MangaCommentDTOs> comments;
    private int from;
    private int amount;

}
