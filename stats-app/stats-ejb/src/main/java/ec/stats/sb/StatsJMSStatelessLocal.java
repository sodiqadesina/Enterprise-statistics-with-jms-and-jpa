package ec.stats.sb;

import javax.ejb.Local;

@Local
public interface StatsJMSStatelessLocal {
    boolean produce(String message);
    boolean publish(String data);
}
