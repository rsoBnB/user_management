package si.fri.rso.rsobnb.users.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import si.fri.rso.rsobnb.users.RealEstate;
import si.fri.rso.rsobnb.users.User;
import si.fri.rso.rsobnb.users.services.config.RestProperties;


@RequestScoped
public class UsersBean {

    private Logger log = LogManager.getLogger(UsersBean.class.getName());

    private Client httpClient;

    @Context
    protected UriInfo uriInfo;

    @Inject
    private UsersBean userBean;

    @Inject
    private RestProperties restProperties;

    @Inject
    private EntityManager em;

    @Inject
    @DiscoverService("real_estates")
    private Optional<String> baseUrl;


    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    public List<User> getUsers(UriInfo uriInfo) {

        System.out.println("RealEstate enabled: "+restProperties.isRealEstateServiceEnabled());

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, User.class, queryParameters);

    }

    public List<User> getUsersFilter(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0).build();
        return JPAUtils.queryEntities(em, User.class, queryParameters);
    }

    public User getUser(String userId) {

        User user = em.find(User.class, userId);

        if (user == null) {
            throw new NotFoundException();
        }

        if(true){
            List<RealEstate> realEstates = userBean.getRealEstates(userId);
            user.setRealEstates(realEstates);
        }

        return user;
    }


    public User createdUser(User user) {

        try {
            beginTx();
            em.persist(user);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return user;
    }


    public User putUser(String userId, User user) {

        User u = em.find(User.class, userId);

        if (u == null) {
            return null;
        }

        try {
            beginTx();
            user.setId(u.getId());
            user = em.merge(user);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return user;
    }

    public boolean deleteUser(String userId) {

        User user = em.find(User.class, userId);

        if (user != null) {
            try {
                beginTx();
                em.remove(user);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getRealEstatesFallback")
    @Timeout
    public List<RealEstate> getRealEstates(String userId) {

        System.out.println("Base url: "+baseUrl);

        if (baseUrl.isPresent()) {
            System.out.println("Base url: "+baseUrl);

            try {
                return httpClient
                        .target(baseUrl.get() + "/v1/real_estates?where=userId:EQ:" + userId)
                        .request().get(new GenericType<List<RealEstate>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.error(e);
                System.out.println("Error: "+e);
                throw new InternalServerErrorException(e);
            }
        }

        return new ArrayList<>();
    }

    public List<RealEstate> getRealEstatesFallback(String userId) {
        System.out.println("Fallback called");

        List<RealEstate> realEstates = new ArrayList<>();

        RealEstate realEstate= new RealEstate();

        realEstate.setName("N/A");
        realEstate.setId("N/A");
        realEstate.setLocation("N/A");

        realEstates.add(realEstate);

        return realEstates;
    }

    public String getInfo() {
        JsonObject object = Json.createObjectBuilder()
                .add("clani", Json.createArrayBuilder()
                    .add("gs5076")
                    .add("jk0108"))
                .add("opis_projekta", "Nas projekt implementira aplikacijo za oddajo nepremicnin.")
                .add("mikrostoritve", Json.createArrayBuilder()
                    .add("http://168.1.149.41:30626/v1/users")
                    .add("http://168.1.149.41:32622/v1/real_estates"))
                .add("github", Json.createArrayBuilder()
                    .add("https://github.com/rsoBnB/users")
                    .add("https://github.com/rsoBnB/real_estates"))
                .add("travis", Json.createArrayBuilder()
                    .add("https://travis-ci.org/rsoBnB/users")
                    .add("https://travis-ci.org/rsoBnB/real_estates"))
                .add("dockerhub", Json.createArrayBuilder()
                    .add("https://hub.docker.com/r/ggrex/rsobnb-users/")
                    .add("https://hub.docker.com/r/ggrex/rsobnb-real_estates/"))
                .build();


        return object.toString();
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}