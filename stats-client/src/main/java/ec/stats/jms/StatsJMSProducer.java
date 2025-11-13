package ec.stats.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.NamingException;

public class StatsJMSProducer {
    public static void main(String[] args) {
        if (args.length < 2 || !args[0].equalsIgnoreCase("-message")) {
            System.out.println("Usage: java -cp target/stats-client.jar ec.stats.jms.StatsJMSProducer -message <message_content>");
            return;
        }

        String messageContent = args[1];
        Connection connection = null;

        try {
            System.out.println("Creating JNDI Context...");
            Context context = ContextUtil.getInitialContext();

            System.out.println("Looking up Connection Factory...");
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("jms/RemoteConnectionFactory");

            System.out.println("Creating Connection...");
            connection = connectionFactory.createConnection("quickstartUser", "quickstartPwd1!");

            System.out.println("Creating Session...");
            Session session = connection.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);

            System.out.println("Looking up Queue...");
            Destination queue = (Destination) context.lookup("jms/queue/test");

            System.out.println("Starting Connection...");
            connection.start();

            System.out.println("Creating Producer...");
            MessageProducer producer = session.createProducer(queue);

            System.out.println("Creating a Message...");
            Message msg = session.createTextMessage(messageContent);

            System.out.println("Sending Message...");
            producer.send(msg);

        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    System.out.println("Closing the Connection...");
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Producer Done.");
    }
}
