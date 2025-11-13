package ec.stats.jms;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ec.stats.sb.StatsJMSStatelessLocal;

@WebServlet("/sbpublisher")
public class StatsJMSStatelessPublisherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private StatsJMSStatelessLocal statsJMSStateless;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String message = request.getParameter("message");
            if (message != null && !message.isEmpty()) {
                statsJMSStateless.publish(message);
                out.println("Message published to topic: " + message);
            } else {
                out.println("No message received.");
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            out.close();
        }
    }
}
