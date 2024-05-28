package space.neptuxo.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.service.ProductService;
import space.neptuxo.util.DependencyInjector;
import space.neptuxo.util.SessionKey;

import java.io.IOException;

/*
/product/delete?id=<productId>
 */
@WebServlet("/delete/product")
public class DeleteProductServlet extends HttpServlet {

    private final static ProductService SERVICE = DependencyInjector.getBean(ProductService.class);

    private final static String KEY_PARAMETER_ID = "id";

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ReadUserDto dto = (ReadUserDto) req.getSession().getAttribute(SessionKey.USER.get());
        String productId = req.getParameter(KEY_PARAMETER_ID);

        String json = SERVICE.delete(dto.id(), Long.parseLong(productId));
        try (ServletOutputStream body = resp.getOutputStream()) {
            body.write(json.getBytes());
        }
    }
}
