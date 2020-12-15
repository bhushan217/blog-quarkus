package in.b2k.camel;

import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TimerRoute extends RouteBuilder {
    /**
     * {@code timer.period} is defined in {@code src/main/resources/application.properties}
     */
    @ConfigProperty( name = "timer.period", defaultValue = "1000")
    String period;

    /**
     * An injected bean
     */
    @Inject
    Counter counter;

    @Override
    public void configure() throws Exception {
        //Integer period = propertyInject("{{timer.period}}", Integer.class);
        fromF("timer:fooTP?period=%s", period)
            .setBody(exchange -> "Incremented the counter: " + counter.increment())
                // the configuration of the log component is done programmatically using CDI
                // by the org.acme.timer.Beans::log method.
                .to("log:example");
    }
}
