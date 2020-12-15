package in.b2k.blog.resource;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

public class BaseResource {

    @OPTIONS
    @Produces("application/json; charset=UTF-8")
    public Response options() {
        return Response.ok().build();
    }

}
