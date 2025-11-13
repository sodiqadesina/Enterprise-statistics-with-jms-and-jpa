package ec.stats.sb;

import javax.ejb.Local;

@Local
public interface StatsSingletonLocal {
    void addData(double value);
    int getCount();
    void stats();
    void saveModel();
}
