package serverapi.Authentication.POJO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import serverapi.Enums.isValidEnum;
import serverapi.Helpers.ReadJSONFileAndGetValue;

import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignPOJO {
    private String user_name;
    private String user_email;
    private String user_password;
    private String user_change_pass_token;
    private String user_verify_token;
    private String user_avatar;



    private String getRegexStr(String objKey) {
        ReadJSONFileAndGetValue readJSONFileAndGetValue = new ReadJSONFileAndGetValue("src/main/java/serverapi/Security/RegexString.json", objKey);
        readJSONFileAndGetValue.read();

        String value = readJSONFileAndGetValue.getValue();
        return value;
    }


    public Boolean isNullAvatar() {
        if (user_avatar == null || user_avatar.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isValidTokenVerifyAccount() {
        if (user_verify_token == null || user_verify_token.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isNullEmail() {
        if (user_email == null || user_email.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isValidChangePassword() {
        if (user_password == null
                || user_password.equals("")
                || user_change_pass_token == null
                || user_change_pass_token.equals("")
                || !Pattern.matches(
                getRegexStr("passwordLength8Number1"),
                user_password
        )
        ) {
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
                getRegexStr("email"),
                user_email)) {

            return isValidEnum.email_invalid;

        } else if (!Pattern.matches(
                getRegexStr("passwordLength8Number1"),
                user_password
        )) {

            // length 8 , at least 1 letter and 1 number
            return isValidEnum.password_strong_fail;
        }


        return isValidEnum.everything_success;
    }


}