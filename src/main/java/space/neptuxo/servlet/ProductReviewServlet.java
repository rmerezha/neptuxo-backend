package space.neptuxo.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.neptuxo.service.ReviewService;
import space.neptuxo.util.DependencyInjector;

import java.io.IOException;

/*
 * /review/product?productId=?
 */

@WebServlet("/review/product")
public class ProductReviewServlet extends HttpServlet {

    private final static ReviewService SERVICE = DependencyInjector.getBean(ReviewService.class);

    private final static String KEY_PARAMETER_PRODUCT_ID = "productId";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String productId = req.getParameter(KEY_PARAMETER_PRODUCT_ID);

        String json = SERVICE.findByProductId(Long.parseLong(productId));

        try (ServletOutputStream body = resp.getOutputStream()) {
            body.write(json.getBytes());
        }
    }
}
