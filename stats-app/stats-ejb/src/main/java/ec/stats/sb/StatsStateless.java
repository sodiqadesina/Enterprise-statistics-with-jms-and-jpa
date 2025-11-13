package ec.stats.sb;

import ec.stats.StatsSummary;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

@Stateless
@LocalBean
public class StatsStateless implements StatsStatelessLocal, StatsStatelessRemote {
    private StatsSummary summary;

    @Override
    public void loadModel() {
        try (FileInputStream fis = new FileInputStream("C:/enterprise/tmp/model/stats.bin");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            summary = (StatsSummary) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public StatsSummary getSummary() {
        return summary;
    }

    @Override
    public int getCount() {
        if (summary == null) loadModel();
        return summary != null ? summary.getCount() : 0;
    }

    @Override
    public double getMin() {
        if (summary == null) loadModel();
        return summary != null ? summary.getMin() : 0;
    }

    @Override
    public double getMax() {
        if (summary == null) loadModel();
        return summary != null ? summary.getMax() : 0;
    }

    @Override
    public double getMean() {
        if (summary == null) loadModel();
        return summary != null ? summary.getMean() : 0;
    }

    @Override
    public double getSTD() {
        if (summary == null) loadModel();
        return summary != null ? summary.getSTD() : 0;
    }

    @Override
    public String toString() {
        if (summary == null) loadModel();
        return summary != null ? summary.toString() : "No data available.";
    }
}
