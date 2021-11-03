package serverapi.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import serverapi.api.Response;
import serverapi.authentication.pojo.SignInPojo;
import serverapi.authentication.service.AuthService;
import serverapi.authentication.service.Interface.IAuthService;
import serverapi.helpers.HelpersTest;
import serverapi.security.HashSHA512;
import serverapi.tables.user_tables.user.User;

import javax.annotation.Resource;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    private HelpersTest helpersTest = new HelpersTest();

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private AuthRepository authRepository;

    @Test // OK
    public void testSignIn01() throws Exception {
        String uri = "/api/auth/signin";

        Map<String, String> dataObj = Map.of(
                "user_email", "bdmchau105@gmail.com",
                "user_password", "MinhTriet11223"
        );
        String inputJson = new HelpersTest().mapToJson(dataObj);


        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();
        String resultStr = response.getContentAsString();
        System.err.println(resultStr);

        Object[] expectedList = {true, 200};
        Object[] resultList = {resultStr.contains("Sign in successfully!"), response.getStatus()};

        assertArrayEquals(expectedList, resultList);
    }

    @Test // invalid format email
    public void testSignIn02() throws Exception {
        String uri = "/api/auth/signin";

        Map<String, String> dataObj = Map.of(
                "user_email", "bdmchau105",
                "user_password", "MinhTriet11223"
        );
        String inputJson = new HelpersTest().mapToJson(dataObj);


        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();
        String resultStr = response.getContentAsString();
        System.err.println(resultStr);

        Object[] expectedList = {true, 200};
        Object[] resultList = {resultStr.contains("Sign in successfully!"), response.getStatus()};

        assertArrayEquals(expectedList, resultList);
    }

    @Test // missing credential
    public void testSignIn03() throws Exception {
        String uri = "/api/auth/signin";

        Map<String, String> dataObj = Map.of(
                "user_email", "bdmchau105@gmail.com",
                "user_password", ""
        );
        String inputJson = new HelpersTest().mapToJson(dataObj);


        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();
        String resultStr = response.getContentAsString();
        System.err.println(resultStr);

        Object[] expectedList = {true, 200};
        Object[] resultList = {resultStr.contains("Sign in successfully!"), response.getStatus()};

        assertArrayEquals(expectedList, resultList);
    }

    @Test // email null
    public void testSignIn04() throws Exception {
        String uri = "/api/auth/signin";

        Map<String, String> dataObj = new HashMap<>();
        dataObj.put("user_email", null);
        dataObj.put("user_password", "MinhTriet11223");

        String inputJson = new HelpersTest().mapToJson(dataObj);


        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();
        String resultStr = response.getContentAsString();
        System.err.println(resultStr);

        Object[] expectedList = {true, 200};
        Object[] resultList = {resultStr.contains("Sign in successfully!"), response.getStatus()};

        assertArrayEquals(expectedList, resultList);
    }

    @Test // password null
    public void testSignIn05() throws Exception {
        String uri = "/api/auth/signin";

        Map<String, String> dataObj = new HashMap<>();
        dataObj.put("user_email", "bdmchau105@gmail.com");
        dataObj.put("user_password", null);

        String inputJson = new HelpersTest().mapToJson(dataObj);


        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();
        String resultStr = response.getContentAsString();
        System.err.println(resultStr);

        Object[] expectedList = {true, 200};
        Object[] resultList = {resultStr.contains("Sign in successfully!"), response.getStatus()};

        assertArrayEquals(expectedList, resultList);
    }


    /////////////////////////////////////////////
    @Test // password is not strong enough (1 number, 8 length)
    public void testSignUp01() throws Exception {
        String uri = "/api/auth/signup";

        Map<String, String> dataObj = Map.of(
                "user_name", "MinhChau",
                "user_email", "bdmchau105@gmail.com",
                "user_password", "MinhMinh"
        );
        String inputJson = new HelpersTest().mapToJson(dataObj);


        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();
        String resultStr = response.getContentAsString();
        System.err.println(resultStr);

        Object[] expectedList = {true, 200};
        Object[] resultList = {resultStr.contains("Sign up successfully!"), response.getStatus()};

        assertArrayEquals(expectedList, resultList);
    }

    @Test // missing creadential name
    public void testSignUp02() throws Exception {
        String uri = "/api/auth/signup";

        Map<String, String> dataObj = Map.of(
                "user_name", "",
                "user_email", "bdmchau105@gmail.com",
                "user_password", "MinhMinh11234"
        );
        String inputJson = new HelpersTest().mapToJson(dataObj);


        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();
        String resultStr = response.getContentAsString();
        System.err.println(resultStr);

        Object[] expectedList = {true, 200};
        Object[] resultList = {resultStr.contains("Sign up successfully!"), response.getStatus()};

        assertArrayEquals(expectedList, resultList);
    }

    @Test // password null
    public void testSignUp03() throws Exception {
        String uri = "/api/auth/signup";

        Map dataObj = new HashMap();
        dataObj.put("user_name", "MinhChau");
        dataObj.put("user_email", "bdmchau105@gmail.com");
        dataObj.put("user_password", null);

        String inputJson = new HelpersTest().mapToJson(dataObj);


        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();
        String resultStr = response.getContentAsString();
        System.err.println(resultStr);

        Object[] expectedList = {true, 200};
        Object[] resultList = {resultStr.contains("Sign up successfully!"), response.getStatus()};

        assertArrayEquals(expectedList, resultList);
    }

    @Test // invalid email
    public void testSignUp04() throws Exception {
        String uri = "/api/auth/signup";

        Map<String, String> dataObj = Map.of(
                "user_name", "MinhChau",
                "user_email", "bdmchau10",
                "user_password", "MinhMinh554"
        );
        String inputJson = new HelpersTest().mapToJson(dataObj);


        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();
        String resultStr = response.getContentAsString();
        System.err.println(resultStr);

        Object[] expectedList = {true, 200};
        Object[] resultList = {resultStr.contains("Sign up successfully!"), response.getStatus()};

        assertArrayEquals(expectedList, resultList);
    }

    @Test // OK
    public void testSignUp05() throws Exception {
        String uri = "/api/auth/signup";

        Map<String, String> dataObj = Map.of(
                "user_name", "MinhChau",
                "user_email", "bdmchau105@gmail.com",
                "user_password", "MinhMinh567"
        );
        String inputJson = new HelpersTest().mapToJson(dataObj);


        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();
        String resultStr = response.getContentAsString();
        System.err.println(resultStr);

        Object[] expectedList = {true, 200};
        Object[] resultList = {resultStr.contains("Sign up successfully!"), response.getStatus()};

        assertArrayEquals(expectedList, resultList);
    }
    
}