package serverapi.Authentication.PojoAndValidation.Pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.Enums.isValidEnum;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInPojo {
    private String user_email;
    private String user_password;



    public isValidEnum isValidSignIn() {
        if (user_email == null
                || user_password == null
                || user_email.equals("")
                || user_password.equals("")
        ) {

            return isValidEnum.missing_credentials;
        }

        return isValidEnum.everything_success;
    }




}