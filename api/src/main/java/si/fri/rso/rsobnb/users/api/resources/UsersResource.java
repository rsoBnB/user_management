package si.fri.rso.rsobnb.users.api.resources;

import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso.rsobnb.users.User;
import si.fri.rso.rsobnb.users.services.UsersBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;


@RequestScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log
public class UsersResource {

    @Inject
    private UsersBean userBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getUsers() {

        List<User> users = userBean.getUsers(uriInfo);

        return Response.ok(users).build();
    }

    @GET
    @Path("/info")
    public Response getInfo() {
        String response = userBean.getInfo();

        return Response.ok(response).build();
    }

    @GET
    @Path("/filtered")
    public Response getUsersFiltered() {

        List<User> users;

        users = userBean.getUsersFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(users).build();
    }

    @GET
    @Path("/{userId}")
    public Response getUser(@PathParam("userId") String userId) {

        User user = userBean.getUser(userId);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(user).build();
    }

    @POST
    public Response createUser(User user) {

        if ((user.getFirstName() == null || user.getFirstName().isEmpty()) || (user.getLastName() == null
                || user.getLastName().isEmpty())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            user = userBean.createdUser(user);
        }

        if (user.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(user).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(user).build();
        }
    }

    @PUT
    @Path("{userId}")
    public Response putUser(@PathParam("userId") String userId, User user) {

        user = userBean.putUser(userId, user);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (user.getId() != null)
                return Response.status(Response.Status.OK).entity(user).build();
            else
                return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @DELETE
    @Path("{userId}")
    public Response deleteUser(@PathParam("userId") String userId) {

        boolean deleted = userBean.deleteUser(userId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
