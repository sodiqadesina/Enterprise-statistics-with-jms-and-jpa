package ec.stats.mdb;

import ec.stats.sb.StatsSingletonLocal;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.JMSException;

@MessageDriven(name = "StatsMDBSaveModel", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/test"),
})
public class StatsMDBSaveModel implements MessageListener {

    @EJB
    private StatsSingletonLocal statsSingleton;

    @Override
    public void onMessage(Message message) {
        try {
            String messageText = ((TextMessage) message).getText();
            if ("save".equalsIgnoreCase(messageText)) {
                System.out.println("Save message received, saving StatsSummary...");
                statsSingleton.saveModel();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}