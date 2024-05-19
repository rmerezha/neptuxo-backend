package space.neptuxo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.neptuxo.service.UserService;
import space.neptuxo.util.JsonBuilder;
import space.neptuxo.util.Status;

import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        UserService service = new UserService();
        boolean isSaved;

        try (ServletInputStream jsonStream = req.getInputStream()) {
            isSaved = service.registration(jsonStream);
        }

        JsonBuilder jsonBuilder = new JsonBuilder();

        byte[] respJson = isSaved
                ? jsonBuilder.setStatus(Status.SUCCESS).build()
                : jsonBuilder.setStatus(Status.FAIL).setErrors(service.getErrorHandler().getErrors()).build();

        try (ServletOutputStream body = resp.getOutputStream()) {
            body.write(respJson);
        }
    }
}
