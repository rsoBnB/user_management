package si.fri.rso.rsobnb.users.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-properties")
public class RestProperties {

    @ConfigValue(value = "external-services.realestate-service.enabled", watch = true)
    private boolean realEstateServiceEnabled;
    private boolean isHealthy;

    public boolean isHealthy() {
        return isHealthy;
    }

    public void setHealthy(boolean isHealthy) {
        this.isHealthy = isHealthy;
    }


    public boolean isRealEstateServiceEnabled() {
        return realEstateServiceEnabled;
    }

    public void setRealEstateServiceEnabled(boolean realEstateServiceEnabled) {
        this.realEstateServiceEnabled = realEstateServiceEnabled;
    }
}
