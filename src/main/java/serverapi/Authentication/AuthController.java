package serverapi.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;
import serverapi.Api.Response;
import serverapi.Authentication.POJO.SignPOJO;
import serverapi.Enums.isValidEnum;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    private static String authorizationRequestBaseUri = "oauth2/authorization";


    // Note*: @Autowired clientRegistrationRepository before @Autowired authorizedClientService
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }



    //////////////////////////////////////////////////////////

    @CacheEvict(allEntries = true, value = {"allusers"})
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity signUp(@RequestBody SignPOJO signPOJO) throws NoSuchAlgorithmException, MessagingException {
        if (signPOJO.isValidSignUp() == isValidEnum.missing_credentials) {
            Map<String, String> error = Map.of("err", "Missing credentials!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(),
                    HttpStatus.BAD_REQUEST);

        } else if (signPOJO.isValidSignUp() == isValidEnum.password_strong_fail) {
            Map<String, String> error = Map.of("err", "Eight characters, at least one letter and 1 number for " +
                    "password required!");
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
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(),
                    HttpStatus.BAD_REQUEST);

        } else if (signPOJO.isValidSignIn() == isValidEnum.email_invalid) {
            Map<String, String> error = Map.of("err", "Invalid email!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        }


        return authService.signIn(signPOJO);
    }


    @PostMapping("/requestchangepass")
    @ResponseBody
    public ResponseEntity requestChangePassword(@RequestBody SignPOJO signPOJO) throws MailException,
            MessagingException, NoSuchAlgorithmException {
        if (signPOJO.isNullEmail()) {
            Map<String, String> error = Map.of("err", "Missing credentials!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        return authService.requestChangePassword(signPOJO);
    }


    @PutMapping("/changepass")
    @ResponseBody
    public ResponseEntity changePassword(@RequestBody SignPOJO signPOJO) throws MailException, MessagingException,
            NoSuchAlgorithmException {
        if (signPOJO.isValidChangePassword()) {
            Map<String, String> error = Map.of("err", "Body request wrong!" +
                    " please try " +
                    "again!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        return authService.changePassword(signPOJO);
    }


    @PostMapping("/confirmverification")
    @ResponseBody
    public ResponseEntity confirmVerification(@RequestBody SignPOJO signPOJO) throws MailException, MessagingException,
            NoSuchAlgorithmException {
        if (signPOJO.isValidTokenVerifyAccount()) {
            Map<String, String> error = Map.of("err", "Body request wrong!" +
                    " please try " +
                    "again!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        return authService.confirmVerification(signPOJO);
    }


    //////////////////////// OAuth ///////////////////////////////
    @GetMapping("/geturloauthgoogle")
    public ResponseEntity getUrlOauthGoogle() {
        Iterable<ClientRegistration> clientRegistrations = null;

        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        if (clientRegistrations == null) {
            Map<String, Object> err = Map.of("err", "Request to login with Google is failed!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));


        System.err.println("User requested to log in with google oauth");

        Map<String, Object> msg = Map.of(
                "msg", "Get urls oauth google susscessfully",
                "urls", oauth2AuthenticationUrls
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }


    @GetMapping("/oauthgooglesigninsusscess")
    public ResponseEntity oauthGoogleSignInSusscess(OAuth2AuthenticationToken authentication, HttpServletResponse response) throws IOException {
        System.err.println(authentication);
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());


        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();


        return authService.oauthGoogleSignInSusscess(userInfoEndpointUri, client, response);
    }


    @GetMapping("getdataoauthgoogle")
    public ResponseEntity getDataOAuthGoogle() {

        return authService.getDataOAuthGoogle();
    }


    @GetMapping("oauthgooglesigninfailed")
    public String oauthGoogleSignInFailed() {

        return "Request to login with Google is failed!";
    }


    /////////////////// auto call http every 25 minutes to wake up app on heroku
    @GetMapping("autocallhttp")
    public ResponseEntity autoCallHttp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("ICT"));

        System.err.println("Auto call http to wake up app on heroku every 25 minutes");
        System.err.println(calendar.getTime());

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}