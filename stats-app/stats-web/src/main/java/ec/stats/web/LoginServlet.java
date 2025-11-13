package ec.stats.web;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ec.stats.dao.UserDao;
import ec.stats.model.User;
import java.security.NoSuchAlgorithmException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @EJB
    private UserDao userDao;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String encryptedPassword = null;

        // Try-catch block for password encryption
        try {
            encryptedPassword = PasswordUtils.md5Hex(password);  // created this utility method separately
        } catch (NoSuchAlgorithmException e) {
            response.getWriter().println("Encryption error. Please try again.");
            e.printStackTrace();
            return;
        }

        try {
            User user = userDao.getUser(username, encryptedPassword);
            if (user != null) {
                int role = user.getRole();
                switch (role) {
                    case 1:
                        response.sendRedirect("admin.html");
                        break;
                    case 2:
                        response.sendRedirect("developer.html");
                        break;
                    case 3:
                        response.sendRedirect("user.html");
                        break;
                    default:
                        response.getWriter().println("Unknown role.");
                }
            } else {
                response.getWriter().println("Invalid username or password.");
            }
        } catch (Exception e) {
            response.getWriter().println("Login failed. Please try again.");
            e.printStackTrace();
        }
    }
}
