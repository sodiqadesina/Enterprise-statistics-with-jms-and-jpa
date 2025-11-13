package ec.stats.sb;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.Topic;

@Stateless
public class StatsJMSStateless implements StatsJMSStatelessLocal {

    @Inject
    private JMSContext context;

    @Resource(lookup = "java:jboss/exported/jms/queue/test")
    private Queue queue;

    @Resource(lookup = "java:jboss/exported/jms/topic/test")
    private Topic topic;

    @Override
    public boolean produce(String message) {
        try {
            context.createProducer().send(queue, message);
            System.out.println("Message sent to queue: " + message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean publish(String data) {
        try {
            context.createProducer().send(topic, data);
            System.out.println("Message published to topic: " + data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
