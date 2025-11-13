package ec.stats.sb;

import javax.ejb.Remote;

@Remote
public interface StatsStatefulRemote {
    void insertData(double value);
    void createModel();
    String getStats();
    int getCount();
}
