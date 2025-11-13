package ec.stats.web;

import ec.stats.sb.StatsStatelessLocal;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/get")
public class StatsStatelessServlet extends HttpServlet {

    @EJB
    private StatsStatelessLocal statsStateless;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        statsStateless.loadModel();

        out.println("<html><body>");
        if (query != null) {
            switch (query) {
                case "count":
                    out.println("<h2>Count: " + statsStateless.getCount() + "</h2>");
                    break;
                case "min":
                    out.println("<h2>Min: " + statsStateless.getMin() + "</h2>");
                    break;
                case "max":
                    out.println("<h2>Max: " + statsStateless.getMax() + "</h2>");
                    break;
                case "mean":
                    out.println("<h2>Mean: " + statsStateless.getMean() + "</h2>");
                    break;
                case "std":
                    out.println("<h2>Standard Deviation: " + statsStateless.getSTD() + "</h2>");
                    break;
                case "summary":
                    out.println("<h2>Stats Summary:</h2>");
                    out.println("<pre>" + statsStateless.toString() + "</pre>");
                    break;
                default:
                    out.println("<h2>Unknown query parameter.</h2>");
            }
        } else {
            out.println("<h2>No query parameter provided.</h2>");
        }
        out.println("</body></html>");
    }
}
