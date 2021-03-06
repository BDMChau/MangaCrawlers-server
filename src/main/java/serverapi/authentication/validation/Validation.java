package serverapi.authentication.validation;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import serverapi.helpers.ReadJSONFileAndGetValue;
import serverapi.authentication.pojo.ChangePassPojo;
import serverapi.authentication.pojo.SignInPojo;
import serverapi.authentication.pojo.SignUpPojo;

import java.util.regex.Pattern;

@NoArgsConstructor
@Component
public class Validation implements Validator {
    private final String regexEmail = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
    private final String regexPass = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"; //passwordLength8Number1

//    private String getRegexStr(String objKey) {
//        ReadJSONFileAndGetValue readJSONFileAndGetValue = new ReadJSONFileAndGetValue("src/main/java/serverapi/security/regexString.json", objKey);
//        readJSONFileAndGetValue.read();
//
//        return readJSONFileAndGetValue.getValue();
//    }


    @Override
    public boolean supports(Class<?> clazz) {

        return SignInPojo.class.equals(clazz)
                || SignUpPojo.class.equals(clazz)
                || ChangePassPojo.class.equals(clazz);
    }


    @Override
    public void validate(Object obj, Errors errors) {
        if (obj instanceof SignInPojo) {
            isValidSignIn(obj, errors);

        } else if (obj instanceof SignUpPojo) {
            isValidSignUp(obj, errors);
        } else if (obj instanceof ChangePassPojo) {
            isValidChangePass(obj, errors);
        }

    }

    //////////////////////////////////////////////////
    private void isValidSignIn(Object obj, Errors errors) {
        SignInPojo signInPojo = (SignInPojo) obj;

        if (signInPojo.getUser_email() == null
                || signInPojo.getUser_password() == null
                || signInPojo.getUser_email().equals("")
                || signInPojo.getUser_password().equals("")
        ) {

            errors.rejectValue(null, "Missing credential!");
        } else if (!Pattern.matches(regexEmail, signInPojo.getUser_email())) {

            errors.rejectValue(null, "Invalid format email!");
        }
    }


    private void isValidSignUp(Object obj, Errors errors) {
        SignUpPojo signUpPojo = (SignUpPojo) obj;

        if (signUpPojo.getUser_name() == null
                || signUpPojo.getUser_email() == null
                || signUpPojo.getUser_password() == null
                || signUpPojo.getUser_name().equals("")
                || signUpPojo.getUser_email().equals("")
                || signUpPojo.getUser_password().equals("")
        ) {

            errors.rejectValue(null, "Missing credential!");
        } else if (!Pattern.matches(
                regexEmail,
                signUpPojo.getUser_email())) {

            errors.rejectValue(null, "Invalid format email!");
        } else if (!Pattern.matches(
                regexPass,
                signUpPojo.getUser_password()
        )) {

            // length 8 , at least 1 letter and 1 number
            errors.rejectValue(null, "Not strong enough, password must be length 8 , at least 1 letter and 1 number!");
        }
    }


    private void isValidChangePass(Object obj, Errors errors) {
        ChangePassPojo changePassPojo = (ChangePassPojo) obj;

        if (changePassPojo.getUser_password() == null
                || changePassPojo.getUser_password().equals("")
                || changePassPojo.getUser_change_pass_token() == null
                || changePassPojo.getUser_change_pass_token().equals("")
                || !Pattern.matches(regexPass, changePassPojo.getUser_password())
        ) {
            errors.rejectValue(null, "Missing new password or token for changing!");
        }

    }


}
