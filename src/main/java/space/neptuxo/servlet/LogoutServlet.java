package space.neptuxo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.neptuxo.util.JsonBuilder;
import space.neptuxo.util.Status;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getSession().invalidate();

        try (ServletOutputStream body = resp.getOutputStream()) {
            body.write(new JsonBuilder().setStatus(Status.SUCCESS).build());
        }

    }
}
