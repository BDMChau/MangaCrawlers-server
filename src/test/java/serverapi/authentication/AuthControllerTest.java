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
    private final HelpersTest helpersTest = new HelpersTest();

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private AuthService authService;

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


        Map<String, Object> msg = Map.of("msg", "Sign in successfully!");
        ResponseEntity expectedResponseEntity = new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

        Optional<User> optionalUser = authRepository.findByEmail(dataObj.get("user_email"));
        if (!optionalUser.isPresent()) {
            Map<String, String> error = Map.of("err", "Email is not existed!");
            expectedResponseEntity = new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        } else {
            User user = optionalUser.get();

            // this user just created account with google oauth, so the password will be null
            if (user.getUser_password().equals("")) {
                Map<String, String> error = Map.of("err", "This user does not have password!");
                expectedResponseEntity = new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
            }

            HashSHA512 hashingSHA512 = new HashSHA512();
            Boolean comparePass = hashingSHA512.compare(dataObj.get("user_password"), user.getUser_password());
            if (!comparePass) {
                Map<String, String> error = Map.of("err", "Password does not match!");
                expectedResponseEntity = new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
            }
        }



        // start to test
        Mockito.when(authService.signIn(dataObj.get("user_email"), dataObj.get("user_password"))).thenReturn(expectedResponseEntity);

        MockHttpServletResponse response = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn()
                .getResponse();


        assertEquals(200, response.getStatus());
        assertEquals(true, response.getContentAsString().contains("Sign in successfully!"));
    }
}
