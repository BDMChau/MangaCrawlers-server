package serverapi.Authentication;

import com.cloudinary.utils.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ResolvableType;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import serverapi.Api.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;
import serverapi.Authentication.POJO.SignPOJO;
import serverapi.Enums.isValidEnum;

import javax.mail.MessagingException;
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


    @Lazy
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Lazy
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

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

    @GetMapping("/oauthgooglesignin")
    public ResponseEntity oauthGoogleSignIn() {
        Iterable<ClientRegistration> clientRegistrations = null;

        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }


        Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));


        System.err.println("User logged in with google oauth");

        Map<String, Object> msg = Map.of(
                "msg", "Get urls oauth google susscessfully",
                "urls", oauth2AuthenticationUrls
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }


    @GetMapping("/oauthgooglesigninsusscess")
    public ResponseEntity oauthGoogleSignInSusscess(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());


        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();

        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                    .getTokenValue());
            HttpEntity entity = new HttpEntity("", headers);
            ResponseEntity response = restTemplate
                    .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
            Map userAttributes = (Map) response.getBody();

            System.err.println(userAttributes);


            Map<String, Object> msg = Map.of(
                    "msg", "Login with oauth google susscessfully",
                    "user", userAttributes.isEmpty() ? new HashMap<>() {} : userAttributes
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                    HttpStatus.OK);
        }
        return null;
    }


    /////////////////// auto call http every 25 minutes to wake up app on heroku
    @GetMapping("/autocallhttp")
    public ResponseEntity autoCallHttp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("ICT"));

        System.err.println("Auto call http to wake up app on heroku every 25 minutes");
        System.err.println(calendar.getTime());

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}