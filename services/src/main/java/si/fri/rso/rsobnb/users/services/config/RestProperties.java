package si.fri.rso.rsobnb.users.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-properties")
public class RestProperties {

    @ConfigValue(value = "external-services.realestate-service.enabled", watch = true)
    private boolean realEstatesServiceEnabled;

    public boolean isRealEstatesServiceEnabled() {
        return realEstatesServiceEnabled;
    }

    public void setRealEstatesServiceEnabled(boolean realEstatesServiceEnabled) {
        this.realEstatesServiceEnabled = realEstatesServiceEnabled;
    }

    private boolean healthy;

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }
}
