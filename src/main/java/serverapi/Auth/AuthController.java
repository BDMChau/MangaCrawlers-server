package serverapi.Auth;

import Enums.isValidEnum;
import Helpers.Response;
import serverapi.Auth.dto.SignDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity signUp(@RequestBody SignDto signDto) throws NoSuchAlgorithmException {
        if (signDto.isValidSignUp() == isValidEnum.missing_credentials) {
            Map<String, String> error = Map.of("err", "Missing credentials!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).jsonObject(), HttpStatus.BAD_REQUEST);

        } else if (signDto.isValidSignUp() == isValidEnum.password_strong_fail) {
            Map<String, String> error = Map.of("err", "Eight characters, at least one letter and 1 number for password required!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).jsonObject(), HttpStatus.ACCEPTED);

        } else if (signDto.isValidSignUp() == isValidEnum.email_invalid) {
            Map<String, String> error = Map.of("err", "Invalid email!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).jsonObject(), HttpStatus.ACCEPTED);
        }


        return authService.signUp(signDto);
    }

    @PostMapping("/signin")
    @ResponseBody
    public ResponseEntity signIn(@RequestBody SignDto signDto) throws NoSuchAlgorithmException {
        if (signDto.isValidSignIn() == isValidEnum.missing_credentials) {
            Map<String, String> error = Map.of("err", "Missing credentials!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).jsonObject(), HttpStatus.BAD_REQUEST);

        }  else if (signDto.isValidSignIn() == isValidEnum.email_invalid) {
            Map<String, String> error = Map.of("err", "Invalid email!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).jsonObject(), HttpStatus.ACCEPTED);
        }


        return authService.signIn(signDto);
    }

}