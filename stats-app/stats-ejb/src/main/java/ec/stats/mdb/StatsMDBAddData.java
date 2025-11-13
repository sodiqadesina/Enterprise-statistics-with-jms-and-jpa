package ec.stats.mdb;

import ec.stats.sb.StatsSingletonLocal;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.JMSException;

@MessageDriven(name = "StatsMDBAddData", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "topic/test"),
})
public class StatsMDBAddData implements MessageListener {

    @EJB
    private StatsSingletonLocal statsSingleton;
// handling the incoming message 
    @Override
    public void onMessage(Message message) {
        try {
            String messageText = ((TextMessage) message).getText();
            double dataValue = Double.parseDouble(messageText);
            System.out.println("=========== SMDB-Add-Data ============");
            System.out.println("Adding data value: " + dataValue + "SMDB-Add-data");
            statsSingleton.addData(dataValue);
        } catch (JMSException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
