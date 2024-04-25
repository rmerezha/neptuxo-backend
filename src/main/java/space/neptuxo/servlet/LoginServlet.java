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


    /*
    email
    passwd
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream json = req.getInputStream();
        UserService service = new UserService();
        Optional<ReadUserDto> user = service.login(json);

        JsonBuilder jsonBuilder = new JsonBuilder();

        user.ifPresentOrElse(u -> {
            req.getSession().setAttribute("user", u);
            jsonBuilder.setStatus(Status.SUCCESS);
        }, () -> {
            jsonBuilder.setStatus(Status.FAIL).setErrors(service.getErrorHandler().getErrors());
        });
        ServletOutputStream body = resp.getOutputStream();
        body.write(jsonBuilder.build());

    }
}
