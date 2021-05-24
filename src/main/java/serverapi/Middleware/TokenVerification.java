package serverapi.Middleware;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import serverapi.Api.Response;
import serverapi.Security.AccountVerification;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
        System.out.println(req.getHeader("Authorization"));
        try {
            if (req.getHeader("Authorization") == null || req.getHeader("Authorization").equals("")) {
                res.setStatus(401);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");

                Map<String, String> error = Map.of("err", "Missing Token!");
                ResJsonMiddleware(res, res.getContentType(), res.getCharacterEncoding(), res.getStatus(),
                        HttpStatus.UNAUTHORIZED, error);
                return;
            }

            String token = req.getHeader("Authorization");
            Jws<Claims> tokenParsed = Jwts.parser()
                    .setSigningKey(System.getenv("JWT_KEY").getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);
            Claims tokenBody = tokenParsed.getBody();
            Map<String, Object> tokenPayload = (HashMap<String, Object>) tokenBody.get("payload");


            // Account verification checking
            Boolean accountStatus = (Boolean) tokenPayload.get("user_isVerified");
            Boolean isVerified = new AccountVerification(accountStatus).cheking();
            System.out.println(isVerified);



            req.setAttribute("user", tokenPayload);

            chain.doFilter(request, response);
        } catch (IOException | ServletException | JwtException e) {
            String err = e.toString();

            if (err.contains("jsonwebtoken")) {
                res.setStatus(401);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");

                if (err.contains("MalformedJwtException")) {
                    Map<String, String> error = Map.of("err", "Invalid Format Token!");
                    ResJsonMiddleware(res, res.getContentType(), res.getCharacterEncoding(), res.getStatus(), HttpStatus.UNAUTHORIZED, error);

                } else if (err.contains("SignatureException")) {
                    Map<String, String> error = Map.of("err", "Invalid Key Token!");
                    ResJsonMiddleware(res, res.getContentType(), res.getCharacterEncoding(), res.getStatus(), HttpStatus.UNAUTHORIZED, error);

                } else if (err.contains("ExpiredJwtException")) {
                    Map<String, String> error = Map.of("err", "Token Expired!");
                    ResJsonMiddleware(res, res.getContentType(), res.getCharacterEncoding(), res.getStatus(), HttpStatus.UNAUTHORIZED, error);

                } else if (err.contains("IllegalArgumentException")) {
                    Map<String, String> error = Map.of("err", "Illegal Argument Token!");
                    ResJsonMiddleware(res, res.getContentType(), res.getCharacterEncoding(), res.getStatus(), HttpStatus.UNAUTHORIZED, error);
                }
            }

            System.out.println(e);
            //e.printStackTrace();
        }
    }

    private void ResJsonMiddleware(HttpServletResponse res, String contentType, String charSet, int httpCode,
                                   HttpStatus httpStatus, Object content) throws IOException {
        PrintWriter printWriter = res.getWriter();

        res.setContentType(contentType);
        res.setCharacterEncoding(charSet);

        Response responseHelper = new Response(httpCode, httpStatus, content);
        String responseHelperString = this.gson.toJson(responseHelper);

        printWriter.print(responseHelperString);
        printWriter.flush();
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }


}