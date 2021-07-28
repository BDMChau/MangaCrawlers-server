package serverapi.Authentication.PojoAndValidation;

import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import serverapi.Authentication.PojoAndValidation.Pojo.SignInPojo;
import serverapi.Helpers.ReadJSONFileAndGetValue;

import java.util.regex.Pattern;

@NoArgsConstructor
public class Validation implements Validator {


    private String getRegexStr(String objKey) {
        ReadJSONFileAndGetValue readJSONFileAndGetValue = new ReadJSONFileAndGetValue("src/main/java/serverapi/Security/RegexString.json", objKey);
        readJSONFileAndGetValue.read();

        String value = readJSONFileAndGetValue.getValue();
        return value;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return SignInPojo.class.equals(clazz);
    }


    @Override
    public void validate(Object obj, Errors errors) {
        if (obj instanceof SignInPojo) {
            SignInPojo signInPojo = (SignInPojo) obj;

            if (signInPojo.getUser_email() == null
                    || signInPojo.getUser_password() == null
                    || signInPojo.getUser_email().equals("")
                    || signInPojo.getUser_password().equals("")
            ) {

                errors.rejectValue(null, "Missing credential!");
            } else if (!Pattern.matches(getRegexStr("email"), signInPojo.getUser_email())) {

                errors.rejectValue(null, "Invalid format email!");
            }
        }

    }
}
