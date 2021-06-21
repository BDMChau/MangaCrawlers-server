package serverapi.Auth;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import serverapi.Api.Response;
import serverapi.Authentication.AuthRepository;
import serverapi.Authentication.AuthService;
import serverapi.Authentication.POJO.SignPOJO;

import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:test-application.properties")
public class AuthServiceTest {

    @MockBean
    AuthRepository authRepository;

    @Autowired
    private AuthService authService;


    @Before
    private void setup(){
        SignPOJO signPOJO = new SignPOJO();
        signPOJO.setUser_email("bdmchau105@gmail.com");
        signPOJO.setUser_password("MinhChau105");


        Map<String, Object> msg = Map.of(
                "msg", "Sign in successfully!",
                "token", "token",
                "user", "userData"
        );

        Mockito.when(authService.signIn(signPOJO)).thenReturn(new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK));
    }

    @Test
    public void signIn() throws Exception {
        SignPOJO signPOJO = new SignPOJO();
        signPOJO.setUser_email("bdmchau105@gmail.com");
        signPOJO.setUser_password("MinhChau105");


        Map<String, Object> msg = Map.of(
                "msg", "Sign in successfully!",
                "token", "token",
                "user", "userData"
        );

        Assert.assertEquals(new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK), authService.signIn(signPOJO));
    }

}
