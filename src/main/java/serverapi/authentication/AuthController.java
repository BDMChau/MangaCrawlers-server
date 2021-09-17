package serverapi.authentication;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import serverapi.api.Response;
import serverapi.authentication.pojo.ChangePassPojo;
import serverapi.authentication.pojo.SignInPojo;
import serverapi.authentication.pojo.SignUpPojo;
import serverapi.authentication.validation.Validation;
import serverapi.authentication.service.Interface.IAuthService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IAuthService authService;

    private static String authorizationRequestBaseUri = "oauth2/authorization";


    // Note*: @Autowired clientRegistrationRepository before @Autowired authorizedClientService
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }


    @InitBinder({"signInPojo", "signUpPojo", "changePassPojo"})
    public void InitBinder(WebDataBinder binder) {
        binder.addValidators(new Validation());
    }

    //////////////////////////////////////////////////////////

    @CacheEvict(allEntries = true, value = {"allusers"})
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity signUp(@Valid @RequestBody SignUpPojo signUpPojo, BindingResult bindingResult) throws NoSuchAlgorithmException, MessagingException {
        if (bindingResult.hasErrors()) {
            String errMsg = bindingResult.getAllErrors().get(0).getCode();

            Map<String, String> error = Map.of("err", errMsg);
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        return authService.signUp(signUpPojo);
    }


    @PostMapping("/signin")
    @ResponseBody
    public ResponseEntity signIn(@Valid @RequestBody SignInPojo signInPojo, BindingResult bindingResult) throws NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            String errMsg = bindingResult.getAllErrors().get(0).getCode();

            Map<String, String> error = Map.of("err", errMsg);
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        Map result = authService.signIn(signInPojo.getUser_email(), signInPojo.getUser_password());

        if(result.get("err") != null){
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, result).toJSON(), HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(new Response(400, HttpStatus.OK, result).toJSON(), HttpStatus.OK);
    }


    @PostMapping("/requestchangepass")
    @ResponseBody
    public ResponseEntity requestChangePassword(@RequestBody Map<String, String> data) throws MailException,
            MessagingException, NoSuchAlgorithmException {

        if (data.get("user_email") == null || data.get("user_email").equals("")) {
            Map<String, String> error = Map.of("err", "Missing email!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        String userEmail = data.get("user_email");

        return authService.requestChangePassword(userEmail);
    }


    @PutMapping("/changepass")
    @ResponseBody
    public ResponseEntity changePassword(@Valid @RequestBody ChangePassPojo changePassPojo, BindingResult bindingResult) throws MailException, MessagingException,
            NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            String errMsg = bindingResult.getAllErrors().get(0).getCode();

            Map<String, String> error = Map.of("err", errMsg);
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        return authService.changePassword(changePassPojo);
    }

    @PostMapping("/confirmverification")
    @ResponseBody
    public ResponseEntity confirmVerification(@RequestBody Map<String, String> data) throws MailException {
        if (data.get("user_verify_token") == null || data.get("user_verify_token").equals("")) {
            Map<String, String> error = Map.of("err", "Missing token for verification");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, error).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        String token = data.get("user_verify_token");

        return authService.confirmVerification(token);
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


    ///////////////// youtube bot music
    @GetMapping("youtubeapikey")
    public ResponseEntity getYoutubeApiKey() {
        String apiKey = System.getenv("YOUTUBE_API_KEY");
        if (apiKey == null || apiKey.equals("")) {
            Map<String, Object> msg = Map.of("err", "api_key for youtube bot was failed");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get api_key for youtube bot susscessfully",
                "api_key", apiKey
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
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