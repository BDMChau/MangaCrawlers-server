package serverapi.authentication.filters;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import serverapi.api.Response;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenVerification implements Filter {

    private Gson gson = new Gson();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        try {
            if (req.getHeader("Authorization") == null || req.getHeader("Authorization").equals("")) {
                res.setStatus(401);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");

                Map<String, String> error = Map.of("err", "Missing Token!");
                new Response().ResponseJsonMiddleware(res, gson, res.getContentType(), res.getCharacterEncoding(), res.getStatus(),
                        HttpStatus.UNAUTHORIZED, error);
                return;
            }

            String token = req.getHeader("Authorization");
            Jws<Claims> tokenParsed = Jwts.parser()
                    .setSigningKey(System.getenv("JWT_KEY").getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);
            Claims tokenBody = tokenParsed.getBody();
            Map<String, Object> tokenPayload = (HashMap<String, Object>) tokenBody.get("payload");


            req.setAttribute("user", tokenPayload);

            chain.doFilter(request, response);
        } catch (IOException | ServletException | JwtException e) {
            String err = e.toString();

            if (err.contains("jsonwebtoken")) {
                res.setStatus(401);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");

                if (err.contains("MalformedJwtException")) {
                    Map<String, String> error = Map.of("err", "Invalid Token Format!");
                    new Response().ResponseJsonMiddleware(res, gson, res.getContentType(), res.getCharacterEncoding(), res.getStatus(), HttpStatus.UNAUTHORIZED, error);

                } else if (err.contains("SignatureException")) {
                    Map<String, String> error = Map.of("err", "Invalid Key Token!");
                    new Response().ResponseJsonMiddleware(res, gson, res.getContentType(), res.getCharacterEncoding(), res.getStatus(), HttpStatus.UNAUTHORIZED, error);

                } else if (err.contains("ExpiredJwtException")) {
                    Map<String, String> error = Map.of("err", "Token Expired!");
                    new Response().ResponseJsonMiddleware(res, gson, res.getContentType(), res.getCharacterEncoding(), res.getStatus(), HttpStatus.UNAUTHORIZED, error);

                } else if (err.contains("IllegalArgumentException")) {
                    Map<String, String> error = Map.of("err", "Illegal Argument Token!");
                    new Response().ResponseJsonMiddleware(res, gson, res.getContentType(), res.getCharacterEncoding(), res.getStatus(), HttpStatus.UNAUTHORIZED, error);
                }
            }

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