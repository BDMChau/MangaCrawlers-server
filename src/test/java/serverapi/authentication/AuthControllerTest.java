package serverapi.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import serverapi.authentication.service.AuthService;
import serverapi.authentication.service.Interface.IAuthService;
import serverapi.helpers.HelpersTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private IAuthService authService;

    @MockBean
    private AuthRepository authRepository;

    @Test
    public void testSignIn() throws Exception {
        String uri = "/api/auth/signin";
        Map<String, String> dataObj = Map.of(
                "user_email", "bdmchau105@gmail.com",
                "user_password", "MinhChau105"
        );

        String inputJson = new HelpersTest().mapToJson(dataObj);
        MvcResult mvcResult = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        System.err.println(mvcResult.getResponse().getContentAsString());
        System.err.println(mvcResult.getResponse().getStatus());
        assertEquals(200, statusRes);
        assertEquals(contentRes, "Sign in successfully!");

    }

//    @Test
//    public void testSignIn() throws Exception {
//        String uri = "/api/auth/signin";
//        Map<String, String> dataObj = Map.of(
//                "user_email", "bdmchau105@gmail.com",
//                "user_password", "MinhChau105"
//        );
//
//        String inputJson = new HelpersTest().mapToJson(dataObj);
//        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
//                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
//
//        int statusRes = mvcResult.getResponse().getStatus();
//        String contentRes = mvcResult.getResponse().getContentAsString();
//
////        System.err.println(statusRes);
////        System.err.println(mvcResult.getResponse());
//
//        assertEquals(200, statusRes);
//        assertEquals(contentRes, "Sign in successfully!");
//    }

}
