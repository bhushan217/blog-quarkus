package in.b2k.camel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.camel.BindToRegistry;
import org.apache.camel.Produce;
import org.apache.camel.builder.LambdaRouteBuilder;
import org.apache.camel.component.log.LogComponent;
import org.apache.camel.support.processor.DefaultExchangeFormatter;

@ApplicationScoped
public class Configurations {
    /**
     * Produces a {@link LogComponent} instance with a custom exchange formatter set-up.
     */
    @Named
    LogComponent log() {
        DefaultExchangeFormatter formatter = new DefaultExchangeFormatter();
        formatter.setShowExchangePattern(false);
        formatter.setShowBodyType(false);

        LogComponent component = new LogComponent();
        component.setExchangeFormatter(formatter);

        return component;
    }

    @Produce
    public LambdaRouteBuilder myRoute() {
        return rb -> rb.from("kafka:cheese").to("jms:queue:foo");
    }
}
