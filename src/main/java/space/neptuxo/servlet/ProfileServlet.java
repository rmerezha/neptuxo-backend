package space.neptuxo.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import space.neptuxo.service.UserService;
import space.neptuxo.util.DependencyInjector;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    /*
    /profile?name=<username>
     */

    private final static UserService SERVICE = DependencyInjector.getBean(UserService.class);

    private final static String KEY_PARAMETER_NAME = "name";

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        String username = req.getParameter(KEY_PARAMETER_NAME);

        String json = SERVICE.getProfile(username);

        try (ServletOutputStream body = resp.getOutputStream()) {
            body.write(json.getBytes());
        }
    }
}
