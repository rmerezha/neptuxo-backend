package space.neptuxo.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/*")
public class JsonFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String jsonContentType = "application/json";
        servletResponse.setContentType(jsonContentType);
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
