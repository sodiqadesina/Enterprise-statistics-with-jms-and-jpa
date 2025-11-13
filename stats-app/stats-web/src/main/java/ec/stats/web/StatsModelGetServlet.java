package ec.stats.web;

import ec.stats.dao.ModelDao;
import ec.stats.model.Model;
import ec.stats.StatsSummary;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@WebServlet("/modelget")
public class StatsModelGetServlet extends HttpServlet {

    @EJB
    private ModelDao modelDao;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String modelname = request.getParameter("modelname");
        String query = request.getParameter("query");

        try {
            // Retrieve the model from the database by name
            Model model = modelDao.getModel(modelname);
            if (model == null) {
                response.getWriter().println("Model not found.");
                return;
            }

            // Deserialize the StatsSummary object from the byte array
            byte[] objectData = model.getObject();
            StatsSummary statsSummary;
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(objectData))) {
                statsSummary = (StatsSummary) ois.readObject();
            } catch (ClassNotFoundException e) {
                response.getWriter().println("Error deserializing model object.");
                return;
            }

            // Return the appropriate value based on the query parameter
            switch (query) {
                case "count":
                    response.getWriter().println("Count: " + statsSummary.getCount());
                    break;
                case "mean":
                    response.getWriter().println("Mean: " + statsSummary.getMean());
                    break;
                case "min":
                    response.getWriter().println("Min: " + statsSummary.getMin());
                    break;
                case "max":
                    response.getWriter().println("Max: " + statsSummary.getMax());
                    break;
                case "std":
                    response.getWriter().println("Std: " + statsSummary.getSTD());
                    break;
                default:
                    response.getWriter().println("Unknown query parameter.");
                    break;
            }
        } catch (Exception e) {
            response.getWriter().println("Error retrieving model: " + e.getMessage());
        }
    }
}
