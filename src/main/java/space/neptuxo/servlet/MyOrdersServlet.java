package space.neptuxo.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.service.OrderService;
import space.neptuxo.util.DependencyInjector;
import space.neptuxo.util.SessionKey;

import java.io.IOException;

@WebServlet("/my-orders")
public class MyOrdersServlet extends HttpServlet {

    private final static OrderService SERVICE = DependencyInjector.getBean(OrderService.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var user = (ReadUserDto) req.getSession().getAttribute(SessionKey.USER.get());

        String json = SERVICE.findByUserId(user.id());

        try (ServletOutputStream body = resp.getOutputStream()) {
            body.write(json.getBytes());
        }
    }
}
