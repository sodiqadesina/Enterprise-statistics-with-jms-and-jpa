package ec.stats.web;

import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ec.stats.dao.UserDao;
import ec.stats.model.User;

@WebServlet("/listUsers")
public class ListUsersServlet extends HttpServlet {  

    @EJB
    private UserDao userDao;  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<User> users = userDao.getAllUsers();
            if (users != null && !users.isEmpty()) {
                for (User user : users) {
                    response.getWriter().println("User: " + user.getName() + ", Role: " + user.getRole());
                }
            } else {
                response.getWriter().println("No users found.");
            }
        } catch (Exception e) {
            response.getWriter().println("Error listing users. Please try again.");
            e.printStackTrace();
        }
    }
}
