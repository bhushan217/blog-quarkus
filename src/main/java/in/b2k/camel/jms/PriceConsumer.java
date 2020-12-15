package in.b2k.camel.jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * A bean consuming prices from the JMS queue.
 */
@ApplicationScoped
@JBossLog
public class PriceConsumer implements Runnable {

    @ConfigProperty(name = "artemis.queue.prices")
    String Q_PRICES;

    @Inject
    ConnectionFactory connectionFactory;

    private final ExecutorService scheduler = Executors.newSingleThreadExecutor();

    private volatile String lastPrice;

    public String getLastPrice() {
        return lastPrice;
    }

    void onStart(@Observes StartupEvent ev) {
        scheduler.submit(this);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
            JMSConsumer consumer = context.createConsumer(context.createQueue(Q_PRICES));
            while (true) {
                Message message = consumer.receive();
                if (message == null) {
                    // receive returns `null` if the JMSConsumer is closed
                    return;
                }
                lastPrice = message.getBody(String.class);
                log.info("Got last price: " + lastPrice);
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
