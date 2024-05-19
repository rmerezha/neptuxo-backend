package space.neptuxo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.service.UserService;

import java.io.IOException;

@WebServlet("/account")
public class AccountServlet extends HttpServlet {
    /*
    /account?name=<username>
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("name");

        UserService service = new UserService();

        ReadUserDto user = service.searchProfile(username);

    }
}
