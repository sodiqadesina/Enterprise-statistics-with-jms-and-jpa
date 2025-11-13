package ec.stats.sb;

import ec.stats.StatsSummary;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

@Singleton
@LocalBean

public class StatsSingleton implements StatsSingletonLocal, StatsSingletonRemote {
    
    private List<Double> dataSet = new ArrayList<>();
    private StatsSummary summary = new StatsSummary();

    @Override
    public void addData(double value) {
        dataSet.add(value);
        // Update stats incrementally
        updateStats(value);
    }

    private void updateStats(double value) {
        int count = summary.getCount() + 1;
        summary.setCount(count);

        if (count == 1) {
            summary.setMin(value);
            summary.setMax(value);
            summary.setMean(value);
            summary.setSTD(0);
        } else {
            if (value < summary.getMin()) summary.setMin(value);
            if (value > summary.getMax()) summary.setMax(value);

            double mean = summary.getMean() + (value - summary.getMean()) / count;
            summary.setMean(mean);

            // For standard deviation, more complex incremental calculation is needed.
            // For simplicity, we'll recompute it in stats()
        }
    }

    @Override
    public int getCount() {
        return summary.getCount();
    }

    @Override
    public void stats() {
        int count = dataSet.size();
        if (count == 0) {
            summary = new StatsSummary();
            return;
        }

        double sum = 0;
        double sumSq = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (double value : dataSet) {
            sum += value;
            sumSq += value * value;
            if (value < min) min = value;
            if (value > max) max = value;
        }

        double mean = sum / count;
        double variance = (sumSq - (sum * sum) / count) / count;
        double std = Math.sqrt(variance);

        summary.setCount(count);
        summary.setMin(min);
        summary.setMax(max);
        summary.setMean(mean);
        summary.setSTD(std);
    }

    @Override
    public void saveModel() {
        try (FileOutputStream fos = new FileOutputStream("C:/enterprise/tmp/model/stats.bin");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(summary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
