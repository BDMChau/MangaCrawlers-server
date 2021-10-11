package serverapi.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import serverapi.helpers.HelpersTest;
import serverapi.security.HashSHA512;
import serverapi.tables.user_tables.user.User;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void testSignIn() throws Exception {
        String uri = "/api/auth/signin";

        Map<String, String> dataObj = Map.of(
                "user_email", "bdmchau105@gmail.com",
                "user_password", "MinhTriet"
        );
        String inputJson = new HelpersTest().mapToJson(dataObj);


        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();
        String resultStr = response.getContentAsString();

        System.err.println(resultStr);
        assertEquals(200, response.getStatus());
        assertEquals(resultStr.contains("Sign in successfully!"), resultStr.contains("Sign in successfully!"));
    }

    @Test
    public void testSignUp() throws Exception {
        String uri = "/api/auth/signup";


        Map<String, String> dataObj = Map.of(
                "user_name", "Minh Chou",
                "user_email", "bdmchau105@gmail.com",
                "user_password", "MinhTriet"
        );

        String inputJson = new HelpersTest().mapToJson(dataObj);


        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();
        String resultStr = response.getContentAsString();

        System.err.println(resultStr);
        assertEquals(200, response.getStatus());
        assertEquals(resultStr.contains("Sign up success!"), resultStr.contains("Sign up success!"));
    }
}
