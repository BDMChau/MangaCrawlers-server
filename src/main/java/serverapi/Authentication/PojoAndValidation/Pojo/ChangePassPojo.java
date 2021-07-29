package serverapi.Authentication.PojoAndValidation.Pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangePassPojo {
    private String user_password;
    private String user_change_pass_token;
    private String user_verify_token;


}