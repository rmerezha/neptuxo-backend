package space.neptuxo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.service.UserService;
import space.neptuxo.util.JsonBuilder;
import space.neptuxo.util.Status;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserService service = new UserService();
        Optional<ReadUserDto> user;

        try (ServletInputStream jsonStream = req.getInputStream()) {
            user = service.login(jsonStream);
        }

        JsonBuilder jsonBuilder = new JsonBuilder();

        if (user.isPresent()) {
            req.getSession().setAttribute("user", user.get());
            jsonBuilder.setStatus(Status.SUCCESS);
        } else {
            jsonBuilder.setStatus(Status.FAIL).setErrors(service.getErrorHandler().getErrors());
        }

        try (ServletOutputStream body = resp.getOutputStream()) {
            body.write(jsonBuilder.build());
        }

    }

}
