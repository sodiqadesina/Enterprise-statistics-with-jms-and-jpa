package ec.stats.dao;

import ec.stats.model.Model;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
public class ModelDaoImpl implements ModelDao {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    @Override
    public void saveModel(Model model) {
        em.persist(model);
    }

    @Override
    public Model getModel(String modelname) {
        try {
            return em.createQuery(
                    "SELECT m FROM Model m WHERE m.name = :name", Model.class)
                    .setParameter("name", modelname)
                    .getSingleResult();
        } catch (Exception e) {
            return null; // Handle exception 
        }
    }
}
