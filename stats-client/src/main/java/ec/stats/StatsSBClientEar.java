package ec.stats;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import ec.stats.sb.StatsSingletonRemote;
import ec.stats.sb.StatsStatelessRemote;
import ec.stats.sb.StatsStatefulRemote;

public class StatsSBClientEar {

    public static void main(String[] args) throws NamingException {

        // Lookup and use StatsStatelessRemote
        StatsStatelessRemote statsStateless = (StatsStatelessRemote) InitialContext
                .doLookup("ejb:stats-ear/ec.asgmt-stats-ejb-0.2.0/StatsStateless!ec.stats.sb.StatsStatelessRemote");
        System.out.println("Stateless Bean - Count: " + statsStateless.getCount());
        System.out.println("Stateless Bean - Mean: " + statsStateless.getMean());

        // Lookup and use StatsStatefulRemote
        StatsStatefulRemote statsStateful = (StatsStatefulRemote) InitialContext
                .doLookup("ejb:stats-ear/ec.asgmt-stats-ejb-0.2.0/StatsStateful!ec.stats.sb.StatsStatefulRemote?stateful");
        for (int i = 11; i <= 100; i++) {
            statsStateful.insertData((double) i);
        }
        statsStateful.createModel();
        System.out.println("Stateful Bean - Stats Summary: " + statsStateful.getStats());

        // Lookup and use StatsSingletonRemote
        StatsSingletonRemote statsSingleton = (StatsSingletonRemote) InitialContext
                .doLookup("ejb:stats-ear/ec.asgmt-stats-ejb-0.2.0/StatsSingleton!ec.stats.sb.StatsSingletonRemote");
        for (int i = 1; i <= 10; i++) {
            statsSingleton.addData((double) i);
        }
        statsSingleton.saveModel();
        System.out.println("Singleton Bean - Count: " + statsSingleton.getCount());

        System.out.println("RMI calls to EJBs completed successfully!");
    }
}
