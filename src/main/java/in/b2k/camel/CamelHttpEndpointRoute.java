package in.b2k.camel;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CamelHttpEndpointRoute extends EndpointRouteBuilder {

    @ConfigProperty(name = "temperature.threshold", defaultValue = "31")
    String temperatureThreshold;

    @Override
    public void configure() throws Exception {

        from("platform-http:/camel/hello")
            .setBody().simple("Camel runs on ${hostname}")
            .to(log("hi : /camel/hello")
        .showExchangePattern(false).showBodyType(false));

        from("platform-http:/camel/temperature")
            .choice()
                .when().jsonpath("$..room[?(@.temperature > {{temperature.threshold}})]")
                    .setBody(simple("{{msg}}"))
                .otherwise()
                    .setBody(constant("itsCold"))//;simple(jsonpath("$..room[?(@.temperature)]").getExpression().toString()))
            .end()
        //.to("pdf:create?fontSize=26"); //its just right simple text into pdf
        .to(log("hi : /camel/temperature")/*.showExchangePattern(false).showBodyType(false)*/);
    }
}
