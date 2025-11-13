package ec.stats.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@MessageDriven(name = "StatsMDBStoreData", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "topic/test"),
})
public class StatsMDBStoreData implements MessageListener {

    private static final String FILE_PATH = "C:/enterprise/tmp/data/stats.dat";
    //handling the in comming message
    @Override
    public void onMessage(Message message) {
        try {
            String messageText = ((TextMessage) message).getText();
            appendToFile(messageText);
            System.out.println("=========== SMDB-Store-Data ============");
            System.out.println("Data appended to file: " + messageText);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    //appending to file
    private void appendToFile(String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
