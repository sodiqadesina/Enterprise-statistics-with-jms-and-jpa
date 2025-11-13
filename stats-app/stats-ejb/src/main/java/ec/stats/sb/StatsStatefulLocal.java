package ec.stats.sb;

import javax.ejb.Local;

@Local
public interface StatsStatefulLocal {
    void insertData(double value);
    void createModel();
    String getStats();
    int getCount();
}
