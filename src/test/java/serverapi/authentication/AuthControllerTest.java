package serverapi.authentication;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import serverapi.helpers.HelpersTest;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    private HelpersTest helpersTest = new HelpersTest();

    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void testSignIn() throws Exception {
        String uri = "/api/auth/signin";

        Map<String, String> dataObj = Map.of(
                "user_email", "bdmchau105@gmail.com",
                "user_password", "MinhTriet1234"
        );
        String inputJson = new HelpersTest().mapToJson(dataObj);


        
        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();

        System.err.println(response.getContentAsString());
        assertEquals(200, response.getStatus());
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

        System.err.println(response.getContentAsString());
        assertEquals(200, response.getStatus());
    }
}
