package xyz.actionding.servlet.user;

import xyz.actionding.pojo.User;
import xyz.actionding.service.user.UserService;
import xyz.actionding.service.user.UserServiceImpl;
import xyz.actionding.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Actionding
 * @create 2022-06-05 15:27
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet");
        String userCode = req.getParameter("userCode");
        String password = req.getParameter("userPassword");

        UserService userService = new UserServiceImpl();
        User user = userService.login(userCode, password);

        if (user != null && user.getUserPassword().equals(password)) {
            req.getSession().setAttribute(Constants.USER_SESSION, user);
            resp.sendRedirect("jsp/frame.jsp");
            System.out.println("success");
        } else {
            req.setAttribute("error", "用户名或者密码错误!");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            System.out.println("fail");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
