package space.neptuxo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.neptuxo.service.ProductService;
import space.neptuxo.util.DependencyInjector;

import java.io.IOException;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    private final static ProductService SERVICE = DependencyInjector.getBean(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream reqJson = req.getInputStream();

        String json = SERVICE.search(reqJson);

        try (ServletOutputStream body = resp.getOutputStream()) {
            body.write(json.getBytes());
        }

    }
}
