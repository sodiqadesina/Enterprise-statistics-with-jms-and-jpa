package ec.stats.jms;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/producer")
public class StatsJMSProducerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    JMSContext context;

    @Resource(lookup = "java:jboss/exported/jms/queue/test")
    private Queue queue;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String message = request.getParameter("message");
            if ("save".equalsIgnoreCase(message)) {
                context.createProducer().send(queue, "save");
                out.println("Message sent to queue: " + message);
            } else {
                out.println("Invalid message for producer");
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            out.close();
        }
    }
}
