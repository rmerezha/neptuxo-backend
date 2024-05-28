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
 * /review?id=<userId>
 */
@WebServlet("/review")
public class UserReviewServlet extends HttpServlet {

    private final static ReviewService SERVICE = DependencyInjector.getBean(ReviewService.class);

    private final static String KEY_PARAMETER_ID = "id";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = req.getParameter(KEY_PARAMETER_ID);

        String json = SERVICE.findByUserId(Long.parseLong(userId));

        try (ServletOutputStream body = resp.getOutputStream()) {
            body.write(json.getBytes());
        }
    }
}
