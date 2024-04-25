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



    /*
    username
    email
    passwd
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService service = new UserService();
        ServletInputStream json = req.getInputStream();
        boolean isSaved = service.registration(json);
        JsonBuilder jsonBuilder = new JsonBuilder();
        byte[] respJson;
        if (isSaved) {
            respJson = jsonBuilder.setStatus(Status.SUCCESS).build();
        } else {
            respJson = jsonBuilder.setStatus(Status.FAIL).setErrors(service.getErrorHandler().getErrors()).build();
        }
        ServletOutputStream body = resp.getOutputStream();
        body.write(respJson);
    }
}
