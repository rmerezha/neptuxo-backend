package space.neptuxo.servlet;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.neptuxo.dto.LoginDto;
import space.neptuxo.service.UserService;
import space.neptuxo.util.DependencyInjector;
import space.neptuxo.util.SessionKey;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final static UserService SERVICE = DependencyInjector.getBean(UserService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        LoginDto loginDto;

        try (ServletInputStream jsonStream = req.getInputStream()) {
            loginDto = SERVICE.login(jsonStream);
        }

        loginDto.user().ifPresent(u -> req.getSession().setAttribute(SessionKey.USER.get(), u));

        try (ServletOutputStream body = resp.getOutputStream()) {
            body.write(loginDto.json().getBytes());
        }

    }

}
