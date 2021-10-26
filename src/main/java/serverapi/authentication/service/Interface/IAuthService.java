package serverapi.authentication.service.Interface;


import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import serverapi.authentication.pojo.ChangePassPojo;
import serverapi.authentication.pojo.SignInPojo;
import serverapi.authentication.pojo.SignUpPojo;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface IAuthService {

    ResponseEntity signUp(SignUpPojo signUpPojo) throws NoSuchAlgorithmException, MessagingException;

    ResponseEntity signIn(String userEmail, String userPassword);

    ResponseEntity requestChangePassword(String email) throws MessagingException, NoSuchAlgorithmException;

    ResponseEntity changePassword(ChangePassPojo changePassPojo) throws NoSuchAlgorithmException;

    ResponseEntity confirmVerification(String token);

    ResponseEntity oauthGoogleSignInSusscess(String userInfoEndpointUri, OAuth2AuthorizedClient client, HttpServletResponse responseHttpServlel) throws IOException;

    ResponseEntity getDataOAuthGoogle();

}
