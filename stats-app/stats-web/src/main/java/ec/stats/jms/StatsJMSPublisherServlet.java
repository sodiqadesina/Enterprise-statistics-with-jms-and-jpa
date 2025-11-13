package ec.stats.jms;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/publisher")
public class StatsJMSPublisherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    JMSContext context;

    @Resource(lookup = "java:jboss/exported/jms/topic/test")
    private Topic topic;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Retrieving the "message" parameter from the HTTP request
            String message = request.getParameter("message");
            if (message != null) {
                try {
                    // Attempting to parse the message as a double value
                    double value = Double.parseDouble(message);
                    // Send the message to the JMS topic
                    context.createProducer().send(topic, String.valueOf(value));
                    out.println("Message sent to topic: " + value);
                } catch (NumberFormatException e) {
                    out.println("Invalid message for publisher. Please provide a valid double value.");
                }
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
