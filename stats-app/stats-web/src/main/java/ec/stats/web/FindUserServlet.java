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

@WebServlet("/findUser")
public class FindUserServlet extends HttpServlet {
    @EJB
    private UserDao userDao;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");

        try {
            User user = userDao.getUserByName(username);
            if (user != null) {
                response.getWriter().println("User found: " + user.getName() + ", Role: " + user.getRole());
            } else {
                response.getWriter().println("User not found.");
            }
        } catch (Exception e) {
            response.getWriter().println("Error finding user. Please try again.");
            e.printStackTrace();
        }
    }
}
