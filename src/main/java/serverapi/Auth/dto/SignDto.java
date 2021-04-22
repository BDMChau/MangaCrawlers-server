package serverapi.Auth.dto;

import serverapi.Enums.isValidEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Data
@NoArgsConstructor
public class SignDto {
    String user_name;
    String user_email;
    String user_password;
    String user_avatar;
    Boolean isAdmin = false;

    public Boolean isNullAvatar() {
        if (user_avatar == null || user_avatar.equals("")) {
            return true;
        } else {
            return false;
        }
    }


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

    public isValidEnum isValidSignUp() {
        if (user_name == null
                || user_email == null
                || user_password == null
                || user_name.equals("")
                || user_email.equals("")
                || user_password.equals("")
        ) {

            return isValidEnum.missing_credentials;
        } else if (!Pattern.matches(
                "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$",
                user_email)) {

            return isValidEnum.email_invalid;

        } else if (!Pattern.matches(
                "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                user_password
        )) {

            // length 8 , at least 1 letter and 1 number
            return isValidEnum.password_strong_fail;
        }


        return isValidEnum.everything_success;
    }


    @Override
    public String toString() {
        return "SignUpDto{" +
                "userName='" + user_name + '\'' +
                ", userEmail='" + user_email + '\'' +
                ", password='" + user_password + '\'' +
                '}';
    }


}