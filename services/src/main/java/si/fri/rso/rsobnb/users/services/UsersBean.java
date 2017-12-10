package si.fri.rso.rsobnb.users.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

import si.fri.rso.rsobnb.users.RealEstate;
import si.fri.rso.rsobnb.users.User;
import si.fri.rso.rsobnb.users.services.config.RestProperties;


@ApplicationScoped
public class UsersBean {

    private Client httpClient;

    @Context
    protected UriInfo uriInfo;

    @Inject
    private UsersBean userBean;

    @Inject
    private RestProperties restProperties;

    @Inject
    private EntityManager em;

    //@Inject
    //@DiscoverService("realestates")
    //private Optional<String> estatesUrl = "http://localhost:8081";

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    public List<User> getUsers(UriInfo uriInfo) {

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

        System.out.println("User:"+ user.getFirstName());
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

    public List<RealEstate> getRealEstates(String userId) {

        //if(estatesUrl.isPresent()) {
            try {
                System.out.println("try: "+userId);
                List<RealEstate> estates = httpClient
                        .target("http://172.17.0.1:8081/v1/real_estates?where=userId:EQ:" + userId)
                        .request().get(new GenericType<List<RealEstate>>() {
                        });
                System.out.println(Arrays.toString(estates.toArray()));

                return estates;
            } catch (WebApplicationException | ProcessingException e) {
                System.out.println(e);
                throw new InternalServerErrorException(e);
            }
        //}

        //return new ArrayList<>();
    }

    public List<RealEstate> getRealEstatesFallback(String userId) {
        return new ArrayList<RealEstate>();
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
