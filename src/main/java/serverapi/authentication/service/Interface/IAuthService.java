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

    public ResponseEntity signUp(SignUpPojo signUpPojo) throws NoSuchAlgorithmException, MessagingException;

    public ResponseEntity signIn(SignInPojo signInPojo);

    public ResponseEntity requestChangePassword(String email) throws MessagingException, NoSuchAlgorithmException;

    public ResponseEntity changePassword(ChangePassPojo changePassPojo) throws NoSuchAlgorithmException;

    public ResponseEntity confirmVerification(String token);

    public ResponseEntity oauthGoogleSignInSusscess(String userInfoEndpointUri, OAuth2AuthorizedClient client, HttpServletResponse responseHttpServlel) throws IOException;

    public ResponseEntity getDataOAuthGoogle();

}
