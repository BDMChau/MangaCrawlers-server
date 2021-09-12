package serverapi.authentication.service;

import com.cloudinary.utils.StringUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import serverapi.api.Response;
import serverapi.authentication.AuthRepository;
import serverapi.authentication.pojo.ChangePassPojo;
import serverapi.authentication.pojo.SignInPojo;
import serverapi.authentication.pojo.SignUpPojo;
import serverapi.authentication.service.Interface.IAuthService;
import serverapi.helpers.UserAvatarCollection;
import serverapi.security.HashSHA512;
import serverapi.security.RandomBytes;
import serverapi.security.TokenService;
import serverapi.sharing_services.Mailer;
import serverapi.tables.user_tables.trans_group.TransGroup;
import serverapi.tables.user_tables.user.User;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Service
@NoArgsConstructor
public class AuthService implements IAuthService {
    private AuthRepository authRepository;

    // token and userData for OAuth Google
    private String token = "";
    private Map userData = new HashMap();

    @Autowired
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Autowired
    Mailer mailer;


    private Map<String, Serializable> getCustomFieldsUser(User user) {
        Long id = user.getUser_id();
        String name = user.getUser_name();
        String email = user.getUser_email();
        String avatar = user.getUser_avatar();
        Boolean isAdmin = user.getUser_isAdmin();
        Boolean isVerified = user.getUser_isVerified();
        Optional<TransGroup> transGroup = Optional.ofNullable(user.getTransgroup());

        Map<String, Serializable> userFields = new HashMap<>();
        if (transGroup.isEmpty()) {
            userFields.put("user_id", id);
            userFields.put("user_name", name);
            userFields.put("user_email", email);
            userFields.put("user_avatar", avatar);
            userFields.put("user_isAdmin", isAdmin);
            userFields.put("user_isVerified", isVerified);

        } else {
            userFields.put("user_id", id);
            userFields.put("user_name", name);
        
        signUpPojo.setUser_password(hashedPassword);


        User newUser = new User();
        newUser.setUser_name(signUpPojo.getUser_name());
        newUser.setUser_email(signUpPojo.getUser_email());
        newUser.setUser_password(signUpPojo.getUser_password());
        newUser.setUser_isAdmin(false);
        newUser.setUser_isVerified(false);
        newUser.setCreated_at(Calendar.getInstance(TimeZone.getTimeZone("UTC")));

        UserAvatarCollection userAvatarCollection = new UserAvatarCollection();
        newUser.setUser_avatar(userAvatarCollection.getAvatar_member());

        // send mail to verify account
        Integer ThreeDays = (1000 * 60 * 60 * 24) * 3; // 3 days in miliseconds
        String timeExpired =
                String.valueOf(Calendar.getInstance().getTimeInMillis() + Long.parseLong(String.valueOf(ThreeDays)));
        String token = new RandomBytes().randomBytes(32);
        String hashedToken = new HashSHA512().hash(token);
        newUser.setToken_verify(hashedToken);
        newUser.setToken_verify_created_at(timeExpired);


        String userName = signUpPojo.getUser_name();
        String userEmail = signUpPojo.getUser_email();
        mailer.sendMailToVerifyAccount(userName, userEmail, hashedToken);


        authRepository.save(newUser);

        Map<String, String> msg = Map.of(
                "msg", "Sign up success!",
                "msg2", "Check email to verify the account!"
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity signIn(String userEmail, String userPassword) {
        Optional<User> optionalUser = authRepository.findByEmail(userEmail);
        if (!optionalUser.isPresent()) {
            Map<String, String> error = Map.of("err", "Email is not existed!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        }
        User user = optionalUser.get();


        // this user just created account with google oauth, so the password will be null
        if (user.getUser_password().equals("")) {
            Map<String, String> error = Map.of("err", "This user does not have password!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        }


        HashSHA512 hashingSHA512 = new HashSHA512();
        Boolean comparePass = hashingSHA512.compare(userPassword, user.getUser_password());
        if (!comparePass) {
            Map<String, String> error = Map.of("err", "Password does not match!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        }

        Boolean isVerified = user.getUser_isVerified();
        if (Boolean.FALSE.equals(isVerified)) {
            Map<String, String> error = Map.of("err", "Check email to verify the account!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        }

        Map userData = getCustomFieldsUser(user);

        TokenService tokenService = new TokenService();
        long expirationTime = 86400000L * 30; // 30 days
        String token = tokenService.genHS256(userData, expirationTime);


        Map<String, Object> msg = Map.of(
                "msg", "Sign in successfully!",
                "token", token,
                "user", userData
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity requestChangePassword(String email) throws MessagingException, NoSuchAlgorithmException {
        Optional<User> userOptional = authRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            Map<String, String> error = Map.of("err", "Email is not existed!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        }

        User user = userOptional.get();

        String timeExpired = String.valueOf(Calendar.getInstance().getTimeInMillis() + 600000); // 10 minutes

        String token = new RandomBytes().randomBytes(32);
        String hashedToken = new HashSHA512().hash(token);
        user.setToken_reset_pass(hashedToken);
        user.setToken_reset_pass_created_at(timeExpired);

        String userEmail = user.getUser_email();
        String userName = user.getUser_name();
        mailer.sendMailToChangePass(userName, userEmail, hashedToken);

        authRepository.save(user);

        Map<String, String> msg = Map.of("msg", "A mail is sent, please check your email!");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity changePassword(ChangePassPojo changePassPojo) throws NoSuchAlgorithmException {
        Long currentTime = Calendar.getInstance().getTimeInMillis(); // by miliseconds
        String token = changePassPojo.getUser_change_pass_token();

        Optional<User> userOptional = authRepository.findByTokenResetPass(token);
        if (userOptional.isEmpty()) {
            Map<String, String> err = Map.of("err", "Token verification is failed!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();

        Long tokenExpiredAt = Long.parseLong(user.getToken_reset_pass_created_at());
        if (currentTime >= tokenExpiredAt || tokenExpiredAt == null) {
            Map<String, String> err = Map.of("err", "Token has expired!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        String newPassword = changePassPojo.getUser_password();
        String hashedNewPassword = new HashSHA512().hash(newPassword);
        user.setUser_password(hashedNewPassword);

        user.setToken_reset_pass_created_at(null);
        user.setToken_reset_pass(null);
        authRepository.save(user);

        Map<String, String> msg = Map.of("msg", "Change password successfully");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity confirmVerification(String token) {
        long currentTime = Calendar.getInstance().getTimeInMillis(); // by miliseconds

        Optional<User> userOptional = authRepository.findByTokenVerify(token);
        if (userOptional.isEmpty()) {
            Map<String, String> err = Map.of("err", "Token verification is failed!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();


        Long tokenExpiredAt = Long.parseLong(user.getToken_verify_created_at());
        if (currentTime >= tokenExpiredAt || tokenExpiredAt == null) {
            user.setToken_verify(null);
            user.setToken_verify_created_at(null);

            Map<String, String> err = Map.of(
                    "err", "Token has expired!",
                    "err2", "Contact the admin for more information!"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }


        user.setUser_isVerified(true);
        user.setToken_verify(null);
        user.setToken_verify_created_at(null);
        authRepository.save(user);

        Map<String, String> msg = Map.of("msg", "Verify account successfully!");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    ////////////////////////////// OAuth /////////////////////////////////////
    public ResponseEntity oauthGoogleSignInSusscess(String userInfoEndpointUri, OAuth2AuthorizedClient client, HttpServletResponse responseHttpServlel) throws IOException {
        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity entity = new HttpEntity("", headers);
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
            ResponseEntity response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);

            Map userAttributes = (Map) response.getBody();
            System.err.println("UserData OAuth GG: " + userAttributes);


            Optional<User> optionalUser = authRepository.findByEmail((String) userAttributes.get("email"));
            if (optionalUser.isEmpty()) {
                // if email does not exist >> create a new user
                System.err.println("create new user");

                User newUser = new User();
                newUser.setUser_name((String) userAttributes.get("name"));
                newUser.setUser_email((String) userAttributes.get("email"));
                newUser.setCreated_at(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
                newUser.setUser_password(null);
                newUser.setUser_isAdmin(false);
                newUser.setUser_isVerified(true);
                newUser.setUser_password("");

                UserAvatarCollection userAvatarCollection = new UserAvatarCollection();
                if (userAttributes.get("picture") == null || userAttributes.get("picture").equals("")) {
                    newUser.setUser_avatar(userAvatarCollection.getAvatar_member());
                } else {
                    newUser.setUser_avatar((String) userAttributes.get("picture"));
                }

                authRepository.saveAndFlush(newUser);

                this.userData = getCustomFieldsUser(newUser);
                TokenService tokenService = new TokenService();
                this.token = tokenService.genHS256(userData);
            } else {
                // if email exist >> return user data
                System.err.println("user exited");
                User user = optionalUser.get();

                Boolean isVerified = user.getUser_isVerified();
                if (Boolean.FALSE.equals(isVerified)) {
                    user.setUser_isVerified(true);
                    authRepository.saveAndFlush(user);
                }

                this.userData = getCustomFieldsUser(user);
                TokenService tokenService = new TokenService();
                this.token = tokenService.genHS256(userData);
                System.err.println("dfghdfnfghndfnfthgynmfghjmghm,yfkmhjm,hmhjm");
            }

        }


        // redirect to client
        responseHttpServlel.sendRedirect(System.getenv("ORIGIN_CLIENT"));
//        responseHttpServlel.sendRedirect("https://mangacrawlers-58f1e.web.app");

        Map<String, Object> msg = Map.of("msg", "Signin with oauth google susscessfully");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }


    public ResponseEntity getDataOAuthGoogle() {
        if (this.userData.isEmpty() || this.token.equals("")) {
            Map<String, Object> err = Map.of("err", "Notthing from OAuth Google!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        Map userDataToResponse = this.userData;
        String tokenToResponse = this.token;
        this.userData = new HashMap();
        this.token = "new HashMap()";

        Map<String, Object> msg = Map.of(
                "msg", "Signin with oauth google susscessfully",
                "token", tokenToResponse,
                "user", userDataToResponse
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }

}
