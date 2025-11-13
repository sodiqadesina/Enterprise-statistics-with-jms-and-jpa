package ec.stats.sb;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;

@Stateful
@LocalBean
public class StatsStateful implements StatsStatefulLocal, StatsStatefulRemote {

    @EJB
    private StatsSingletonLocal statsSingleton;

    @EJB
    private StatsStatelessLocal statsStateless;

    @Override
    public void insertData(double value) {
        statsSingleton.addData(value);
    }

    @Override
    public void createModel() {
        statsSingleton.saveModel();
    }

    @Override
    public String getStats() {
        statsStateless.loadModel();
        return statsStateless.toString();
    }

    @Override
    public int getCount() {
        return statsSingleton.getCount();
    }
}
