package ec.stats.dao;

import ec.stats.model.Model;

public interface ModelDao {
    void saveModel(Model model);
    Model getModel(String modelname);  // Getting model by name
}
