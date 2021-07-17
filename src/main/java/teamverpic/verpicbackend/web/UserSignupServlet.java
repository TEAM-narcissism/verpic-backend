package teamverpic.verpicbackend.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="userSignupServlet", urlPatterns = "/signup")
public class UserSignupServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String viewPath="/WEB-INF/views/error404.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath); // controller에서 view로 이동
        dispatcher.forward(request, response); // servlet에서 jsp를 호출
    }
}
