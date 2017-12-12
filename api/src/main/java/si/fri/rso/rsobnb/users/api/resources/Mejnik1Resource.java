package si.fri.rso.rsobnb.users.api.resources;


import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso.rsobnb.users.User;
import si.fri.rso.rsobnb.users.services.UsersBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RequestScoped
@Path("/info")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log
public class Mejnik1Resource {
    @Inject
    private UsersBean userBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getInfo() {
        return Response.ok().build();
    }
}
