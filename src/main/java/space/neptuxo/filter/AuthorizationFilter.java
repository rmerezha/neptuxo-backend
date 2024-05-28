package space.neptuxo.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.neptuxo.util.SessionKey;

import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/search",
            "/profile",
            "/review",
            "/review/product"
    );

    private static final Set<String> AUTHENTICATION_PATHS = Set.of(
            "/registration",
            "/login"
    );


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var uri = ((HttpServletRequest) servletRequest).getRequestURI();
        if (isAllowedAccess(uri, (HttpServletRequest) servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean isAllowedAccess(String uri, HttpServletRequest request) {
        return isPublicPath(uri) || (isAuthenticationPath(uri) ^ isLoggedIn(request));
    }

    private boolean isPublicPath(String uri) {
        return PUBLIC_PATHS.stream().anyMatch(uri::startsWith);
    }

    private boolean isAuthenticationPath(String uri) {
        return AUTHENTICATION_PATHS.stream().anyMatch(uri::startsWith);
    }

    private boolean isLoggedIn(ServletRequest servletRequest) {
        return ((HttpServletRequest) servletRequest).getSession().getAttribute(SessionKey.USER.get()) != null;
    }
}
