package in.b2k.camel;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class CamelHttpEndpointRouteTest {

    @Test
    void getHellowShouldSucceed() {
        given().get("/camel/hello").then().statusCode(200);
    }
}
