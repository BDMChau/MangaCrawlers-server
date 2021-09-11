package serverapi.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import serverapi.authentication.pojo.SignInPojo;
import serverapi.authentication.service.AuthService;
import serverapi.authentication.service.Interface.IAuthService;
import serverapi.helpers.HelpersTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private IAuthService authService;

    @MockBean
    private AuthRepository authRepository;

    @Test
    public void testSignIn() throws Exception {
        HelpersTest helpersTest = new HelpersTest();

        String uri = "/api/auth/signin";
        Map<String, String> dataObj = Map.of(
                "user_email", "bdmchau105@gmail.com",
                "user_password", "MinhChau105"
        );

        String inputJson = new HelpersTest().mapToJson(dataObj);

        MockHttpServletResponse controllerRes = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson))
                .andReturn()
                .getResponse();

        assertEquals(200, controllerRes.getStatus());



    }

//    @Test
//    public void testSignInService() throws Exception {
//        HelpersTest helpersTest = new HelpersTest();
//
//        String uri = "/api/auth/signin";
//        Map<String, String> dataObj = Map.of(
//                "user_email", "bdmchau105@gmail.com",
//                "user_password", "MinhChau105"
//        );
//
//        String inputJson = new HelpersTest().mapToJson(dataObj);
//
//        Map<String, Object> msg = Map.of(
//                "msg", "Sign in successfully!"
//        );
//
//        OngoingStubbing ongoingStubbing = Mockito.when(restTemplate.getForEntity(url + uri, AuthService.class)).thenReturn(new ResponseEntity(msg, HttpStatus.OK));
//
//        ResponseEntity serviceRes = authService.signIn(dataObj.get("user_email"), dataObj.get("user_password"));
//
//        System.err.println(ongoingStubbing);
//    }

}
