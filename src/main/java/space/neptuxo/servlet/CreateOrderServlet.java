package space.neptuxo.servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.neptuxo.service.OrderService;
import space.neptuxo.util.DependencyInjector;

import java.io.IOException;

@WebServlet("/create/order")
public class CreateOrderServlet extends HttpServlet {

    private final static OrderService SERVICE = DependencyInjector.getBean(OrderService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream reqJson = req.getInputStream();

        String json = SERVICE.create(reqJson);

        try (ServletOutputStream body = resp.getOutputStream()) {
            body.write(json.getBytes());
        }
    }
}
