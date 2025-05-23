package em;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/secure/*")
public class AuthFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        // For testing: allow any request to act as admin with testuser info
        req.setAttribute("phone", "1234567890");
        req.setAttribute("role", "admin");
        req.setAttribute("major", "CS");
        req.setAttribute("interests", "All");
        chain.doFilter(request, response);}
        // Uncomment below to restore JWT validation
        /*
        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        try {
            String token = authHeader.substring(7);
            Jws<Claims> claims = JWTUtil.parseToken(token);

            req.setAttribute("phone", claims.getBody().getSubject());
            req.setAttribute("role", claims.getBody().get("role"));
            req.setAttribute("major", claims.getBody().get("major"));
            req.setAttribute("interests", claims.getBody().get("interests"));

            chain.doFilter(request, response);
        } catch (JwtException e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Invalid token");
        }
    }

    public void init(FilterConfig filterConfig) {}
    public void destroy() {}
    */
}
