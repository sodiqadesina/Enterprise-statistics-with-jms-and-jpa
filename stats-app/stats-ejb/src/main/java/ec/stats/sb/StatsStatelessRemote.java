package ec.stats.sb;

import javax.ejb.Remote;

@Remote
public interface StatsStatelessRemote {
    int getCount();
    double getMin();
    double getMax();
    double getMean();
    double getSTD();
    String toString();
    void loadModel();
}
