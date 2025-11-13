package ec.stats.web;

import ec.stats.dao.ModelDao;
import ec.stats.model.Model;
import ec.stats.sb.StatsStateless;
import ec.stats.StatsSummary;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;

@WebServlet("/modelsave")
public class StatsModelSaveServlet extends HttpServlet {

    @EJB
    private ModelDao modelDao;

    @EJB
    private StatsStateless statsStateless;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String modelName = request.getParameter("modelname");

        if (modelName == null || modelName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Model name is required.");
            return;
        }

        statsStateless.loadModel(); 
        StatsSummary statsSummary = statsStateless.getSummary();

        if (statsSummary == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load stats summary.");
            return;
        }

        Model model = new Model();
        model.setName(modelName);

        byte[] serializedObject = serializeStatsSummary(statsSummary);

        if (serializedObject == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Serialization failed.");
            return;
        }

        model.setObject(serializedObject);
        model.setClassname(statsSummary.getClass().getName());
        model.setDate(new Timestamp(System.currentTimeMillis()));

        try {
            modelDao.saveModel(model);
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Model Saved Successfully!</h1>");
            response.getWriter().println("<p>Model Name: " + modelName + "</p>");
            response.getWriter().println("<a href='index_model_query.html'>Go Back</a>");
            response.getWriter().println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Error Saving Model</h1>");
            response.getWriter().println("<p>Model could not be saved. Please try again.</p>");
            response.getWriter().println("<a href='index_model_query.html'>Go Back</a>");
            response.getWriter().println("</body></html>");
        }
    }

    private byte[] serializeStatsSummary(StatsSummary statsSummary) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(statsSummary);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

