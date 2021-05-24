package serverapi.Middleware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class Cors implements Filter {

    private final Logger log = LoggerFactory.getLogger(Cors.class);

    public Cors() {
        log.info("Cors initialized!");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Set this config when on production
//        String origin = ((HttpServletRequest) req).getHeader("Origin");
//        if (origin.equals(System.getenv("ORIGIN_LOCAL"))
//                || origin.equals(System.getenv("ORIGIN_PRODUCTION01"))
//                || origin.equals(System.getenv("ORIGIN_PRODUCTION02")
//        )) response.setHeader("Access-Control-Allow-Origin", origin);

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("From", "MangaCrawlers-api");

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}