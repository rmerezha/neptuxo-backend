package space.neptuxo.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.neptuxo.util.Error;
import space.neptuxo.util.JsonBuilder;
import space.neptuxo.util.Status;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/search",
            "/account"
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
            sendForbiddenResponse((HttpServletResponse)servletResponse);
        }
    }

    private boolean isAllowedAccess(String uri, HttpServletRequest request) {
        return isPublicPath(uri) || (!isLoggedIn(request) && isAuthenticationPath(uri)) || isLoggedIn(request);
    }

    private void sendForbiddenResponse(HttpServletResponse response) throws IOException {
        try (OutputStream outputStream = response.getOutputStream()) {
            response.getOutputStream().write(new JsonBuilder().setStatus(Status.FAIL).setErrors(List.of(Error.FORBIDDEN)).build());
        }
    }
    private boolean isPublicPath(String uri) {
        return PUBLIC_PATHS.stream().anyMatch(uri::startsWith);
    }

    private boolean isAuthenticationPath(String uri) {
        return AUTHENTICATION_PATHS.stream().anyMatch(uri::startsWith);
    }

    private boolean isLoggedIn(ServletRequest servletRequest) {
        return ((HttpServletRequest) servletRequest).getSession().getAttribute("user") != null;
    }
}
