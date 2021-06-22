package serverapi.Authentication;


import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import serverapi.Authentication.POJO.SignPOJO;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface IAuthService {

    public ResponseEntity signUp(SignPOJO signPOJO) throws NoSuchAlgorithmException, MessagingException;

    public ResponseEntity signIn(SignPOJO signPOJO);

    public ResponseEntity requestChangePassword(SignPOJO signPOJO) throws MessagingException, NoSuchAlgorithmException;

    public ResponseEntity changePassword(SignPOJO signPOJO) throws NoSuchAlgorithmException;

    public ResponseEntity confirmVerification(SignPOJO signPOJO);

    public ResponseEntity oauthGoogleSignInSusscess(String userInfoEndpointUri, OAuth2AuthorizedClient client, HttpServletResponse responseHttpServlel) throws IOException;

    public ResponseEntity getDataOAuthGoogle();

}
