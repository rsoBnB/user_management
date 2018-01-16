package si.fri.rso.rsobnb.users.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-properties")
public class RestProperties {

    @ConfigValue(value = "external-services.realestate-service.enabled", watch = true)
    private boolean realEstateServiceEnabled;

    @ConfigValue(value = "external-services.supportticket-service.enabled", watch = true)
    private boolean supportTicketServiceEnabled;

    public boolean isRealEstateServiceEnabled() {
        return realEstateServiceEnabled;
    }

    public void setRealEstateServiceEnabled(boolean realEstateServiceEnabled) {
        this.realEstateServiceEnabled = realEstateServiceEnabled;
    }

    public boolean isSupportTicketServiceEnabled() {
        return supportTicketServiceEnabled;
    }

    public void setSupportTicketServiceEnabled(boolean supportTicketServiceEnabled) {
        this.supportTicketServiceEnabled = supportTicketServiceEnabled;
    }
}
