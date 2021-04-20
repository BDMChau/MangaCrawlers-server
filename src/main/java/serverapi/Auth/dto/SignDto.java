package serverapi.Auth.dto;

import Enums.isValidEnum;

import java.util.regex.Pattern;

public class SignDto {
    String name;
    String email;
    String password;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public isValidEnum isValid() {
        if (name == null
                || email == null
                || password == null
                || name.equals("")
                || email.equals("")
                || password.equals("")
        ) {

            return isValidEnum.missing_credentials;
        } else if (!Pattern.matches(
                "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$",
                email)) {

            return isValidEnum.email_invalid;

        } else if (!Pattern.matches(
                "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                password
        )) {

            // length 8 , at least 1 letter and 1 number
            return isValidEnum.password_strong_fail;
        }


        return isValidEnum.everything_success;
    }


    @Override
    public String toString() {
        return "SignUpDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}