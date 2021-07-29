package serverapi.Auth;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import serverapi.Authentication.AuthRepository;
import serverapi.Authentication.Service.AuthService;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:test-application.properties")
public class AuthServiceTest {

    @MockBean
    AuthRepository authRepository;

    @Autowired
    private AuthService authService;


//    @Before
//    private void setup(){
//
//
//
//        Map<String, Object> msg = Map.of(
//                "msg", "Sign in successfully!",
//                "token", "token",
//                "user", "userData"
//        );
//
//        Mockito.when(authService.signIn(signPOJO)).thenReturn(new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK));
//    }
//
//    @Test
//    public void signIn() throws Exception {
//
//
//
//        Map<String, Object> msg = Map.of(
//                "msg", "Sign in successfully!",
//                "token", "token",
//                "user", "userData"
//        );
//
//        Assert.assertEquals(new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK), authService.signIn(signPOJO));
//    }

}
