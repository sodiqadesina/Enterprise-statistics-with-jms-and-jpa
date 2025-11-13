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

@WebServlet("/addUser")
public class AddUserServlet extends HttpServlet {
    @EJB
    private UserDao userDao;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String encryptedPassword = null;

        // Try-catch block for password encryption
        try {
            encryptedPassword = PasswordUtils.md5Hex(password);
        } catch (NoSuchAlgorithmException e) {
            response.getWriter().println("Encryption error. Please try again.");
            e.printStackTrace();
            return;
        }

        try {
            User user = new User();
            user.setName(username);
            user.setPassword(encryptedPassword);
            user.setRole(Integer.parseInt(role));
            userDao.addUser(user);
            response.getWriter().println("User added successfully!");
        } catch (Exception e) {
            response.getWriter().println("Error adding user. Please try again.");
            e.printStackTrace();
        }
    }
}
