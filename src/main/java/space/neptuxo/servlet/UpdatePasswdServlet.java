package space.neptuxo.servlet;

import jakarta.servlet.ServletInputStream;
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

@WebServlet("/update/passwd")
public class UpdatePasswdServlet extends HttpServlet {

    private final static UserService SERVICE = DependencyInjector.getBean(UserService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ReadUserDto user = (ReadUserDto) req.getSession().getAttribute(SessionKey.USER.get());
        try (ServletInputStream reqJsonStream = req.getInputStream();
             ServletOutputStream body = resp.getOutputStream()) {
            String json = SERVICE.updatePassword(user.id(), reqJsonStream);
            body.write(json.getBytes());
        }
    }
}
