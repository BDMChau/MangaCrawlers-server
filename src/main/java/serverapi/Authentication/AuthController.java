package serverapi.Authentication;

import serverapi.Api.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;
import serverapi.Authentication.POJO.SignPOJO;
import serverapi.Enums.isValidEnum;

import java.security.NoSuchAlgorithmException;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity signUp(@RequestBody SignPOJO signPOJO) throws NoSuchAlgorithmException {
        if (signPOJO.isValidSignUp() == isValidEnum.missing_credentials) {
            Map<String, String> error = Map.of("err", "Missing credentials!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(), HttpStatus.BAD_REQUEST);

        } else if (signPOJO.isValidSignUp() == isValidEnum.password_strong_fail) {
            Map<String, String> error = Map.of("err", "Eight characters, at least one letter and 1 number for password required!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);

        } else if (signPOJO.isValidSignUp() == isValidEnum.email_invalid) {
            Map<String, String> error = Map.of("err", "Invalid email!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        }


        return authService.signUp(signPOJO);
    }

    @PostMapping("/signin")
    @ResponseBody
    public ResponseEntity signIn(@RequestBody SignPOJO signPOJO) throws NoSuchAlgorithmException {
        if (signPOJO.isValidSignIn() == isValidEnum.missing_credentials) {
            Map<String, String> error = Map.of("err", "Missing credentials!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(), HttpStatus.BAD_REQUEST);

        }  else if (signPOJO.isValidSignIn() == isValidEnum.email_invalid) {
            Map<String, String> error = Map.of("err", "Invalid email!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        }


        return authService.signIn(signPOJO);
    }

    @PostMapping("/resetpassword")
    @ResponseBody
    public ResponseEntity resetPassword(@RequestBody SignPOJO signPOJO) throws NoSuchAlgorithmException, MailException {
        if (signPOJO.isNullEmail()) {
            Map<String, String> error = Map.of("err", "Missing email, please fill out your email!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(), HttpStatus.BAD_REQUEST);
        }

        return authService.resetPassword(signPOJO);
    }
}