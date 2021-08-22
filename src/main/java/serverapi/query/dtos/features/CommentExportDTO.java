package serverapi.query.dtos.features;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentExportDTO {

    public CommentExportDTO(Long user_id, String user_name, String user_email, String user_avatar,
                            Long chapter_id, String chapter_name, Calendar created_at,
                            Long chaptercmt_id, Calendar chaptercmt_time, String chaptercmt_content) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.created_at = created_at;
        this.chaptercmt_id = chaptercmt_id;
        this.chaptercmt_time = chaptercmt_time;
        this.chaptercmt_content = chaptercmt_content;
    }

    public CommentExportDTO(Long user_id,
                            String user_name,
                            String user_email,
                            String user_avatar,
                            Long chaptercmt_id,
                            Calendar chaptercmt_time,
                            String chaptercmt_content,
                            List comments
    ) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;
        this.chaptercmt_id = chaptercmt_id;
        this.chaptercmt_time = chaptercmt_time;
        this.chaptercmt_content = chaptercmt_content;
        this.comments = comments;

    }


    private Long user_id;
    private String user_name;
    private String user_email;
    private String user_avatar;

    private Long chapter_id;
    private String chapter_name;
    private Calendar created_at;

    private Long chaptercmt_id;
    private Calendar chaptercmt_time;
    private String chaptercmt_content;

    private List comments = new ArrayList<>();
}
