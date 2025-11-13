package ec.stats.sb;

import javax.ejb.Remote;

@Remote
public interface StatsSingletonRemote {
    void addData(double value);
    int getCount();
    void stats();
    void saveModel();
}
