package ec.stats.jms;

import javax.jms.DeliveryMode;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.NamingException;

public class StatsJMSPublisher {
    static TopicConnection connection = null;

    public static void main(String[] args) throws Exception {
        if (args.length < 2 || !args[0].equalsIgnoreCase("-message")) {
            System.out.println("Usage: java -cp target/stats-client.jar ec.stats.jms.StatsJMSPublisher -message <message_content>");
            return;
        }

        String messageContent = args[1];

        try {
            System.out.println("Creating JNDI Context...");
            Context context = ContextUtil.getInitialContext();
            System.out.println("Looking up Topic Connection Factory...");
            TopicConnectionFactory connectionFactory = (TopicConnectionFactory) context.lookup("jms/RemoteConnectionFactory");

            System.out.println("Creating Topic Connection...");
            connection = connectionFactory.createTopicConnection("quickstartUser", "quickstartPwd1!");

            System.out.println("Creating Topic Session...");
            TopicSession topicSession = connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);

            System.out.println("Looking up Topic...");
            Topic topic = (Topic) context.lookup("jms/topic/test");

            System.out.println("Starting Topic Connection...");
            connection.start();

            System.out.println("Creating Topic Publisher...");
            TopicPublisher topicPublisher = topicSession.createPublisher(topic);
            topicPublisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            System.out.println("Creating and Publishing Message...");
            TextMessage message = topicSession.createTextMessage();
            message.setText(messageContent);

            topicPublisher.publish(message);
            System.out.println("Message Published: " + message.getText());

        } finally {
            if (connection != null) {
                System.out.println("Closing the Connection...");
                connection.close();
            }
        }

        System.out.println("Publisher Done.");
    }
}
