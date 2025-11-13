package ec.stats.sb;

import javax.ejb.Local;

@Local
public interface StatsStatelessLocal {
    int getCount();
    double getMin();
    double getMax();
    double getMean();
    double getSTD();
    String toString();
    void loadModel();
}
