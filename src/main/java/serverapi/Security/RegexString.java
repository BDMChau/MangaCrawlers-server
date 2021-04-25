package serverapi.Security;

import lombok.Getter;

@Getter
public class RegexString {
    private String email = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";

    // length 8 , at least 1 letter and 1 number
    private String password01 = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

}
