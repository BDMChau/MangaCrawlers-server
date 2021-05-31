package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class ReportUserDTO {

    private String user_name;
    private String user_email;
    private String user_avatar;
    private Boolean user_isVerified;
    private Calendar createdAt;


    public ReportUserDTO(String user_name, String user_email, String user_avatar, Boolean user_isVerified, Calendar createdAt) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;
        this.user_isVerified = user_isVerified;
        this.createdAt = createdAt;
    }

}
