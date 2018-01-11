package si.fri.rso.rsobnb.users.api.health;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import si.fri.rso.rsobnb.users.services.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@Health
@ApplicationScoped
public class UserServiceHealthCheck implements HealthCheck{

    @Inject
    private RestProperties restProperties;

    private Logger log = Logger.getLogger(UserServiceHealthCheck.class.getName());

    @Override
    public HealthCheckResponse call() {

        if (restProperties.isHealthy()) {
            return HealthCheckResponse.named(UserServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(UserServiceHealthCheck.class.getSimpleName()).down().build();
        }

    }

}
