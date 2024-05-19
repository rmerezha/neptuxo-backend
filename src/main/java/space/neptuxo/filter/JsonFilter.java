package space.neptuxo.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class JsonFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String jsonContentType = "application/json";
        if(!servletRequest.getContentType().equals(jsonContentType)) {
            ((HttpServletResponse)servletResponse).setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }
        servletResponse.setContentType(jsonContentType);
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
