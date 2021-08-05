package serverapi.Authentication.Filters;

import com.google.gson.Gson;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import serverapi.Api.Response;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenVerificationAdmin implements Filter {

    private Gson gson = new Gson();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Map userAttribute = (HashMap) req.getAttribute("user");
        Boolean isAdmin = (Boolean) userAttribute.get("user_isAdmin");

        try {
            if (Boolean.FALSE.equals(isAdmin)) {
                res.setStatus(401);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");

                Map<String, String> error = Map.of("err", "No permission, not an admin!");
                new Response().ResponseJsonMiddleware(res, gson, res.getContentType(), res.getCharacterEncoding(), res.getStatus(),
                        HttpStatus.UNAUTHORIZED, error);
                return;
            }


            chain.doFilter(request, response);
        } catch (IOException | ServletException | JwtException e) {
            res.setStatus(401);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");

            Map<String, String> error = Map.of("err", "Admin token validation is failed!");
            new Response().ResponseJsonMiddleware(res, gson, res.getContentType(), res.getCharacterEncoding(), res.getStatus(), HttpStatus.UNAUTHORIZED, error);


            System.out.println(e);
            //e.printStackTrace();
        }
    }


    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }


}