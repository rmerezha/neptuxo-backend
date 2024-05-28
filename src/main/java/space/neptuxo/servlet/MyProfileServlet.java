package space.neptuxo.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.service.UserService;
import space.neptuxo.util.DependencyInjector;
import space.neptuxo.util.SessionKey;

import java.io.IOException;


@WebServlet("/my-profile")
public class MyProfileServlet extends HttpServlet {

    private final static UserService SERVICE = DependencyInjector.getBean(UserService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ReadUserDto user = (ReadUserDto) req.getSession().getAttribute(SessionKey.USER.get());

        String json = SERVICE.getProfile(user.username());

        try (ServletOutputStream body = resp.getOutputStream()) {
            body.write(json.getBytes());
        }
    }


}
