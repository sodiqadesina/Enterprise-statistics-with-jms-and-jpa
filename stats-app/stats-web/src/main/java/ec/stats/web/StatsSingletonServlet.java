package ec.stats.web;

import ec.stats.sb.StatsSingletonLocal;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/add-data")
public class StatsSingletonServlet extends HttpServlet {

    @EJB
    private StatsSingletonLocal statsSingleton;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String valueStr = request.getParameter("value");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        if (valueStr != null) {
            try {
                double value = Double.parseDouble(valueStr);
                statsSingleton.addData(value);
                statsSingleton.saveModel();
                out.println("<html><body>");
                out.println("<h2>" + value + " added, model saved.</h2>");
                out.println("</body></html>");
            } catch (NumberFormatException e) {
                out.println("<html><body>");
                out.println("<h2>Invalid input. Please enter a valid number.</h2>");
                out.println("</body></html>");
            }
        } else {
            out.println("<html><body>");
            out.println("<h2>No value provided.</h2>");
            out.println("</body></html>");
        }
    }
}
