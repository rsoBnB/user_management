package si.fri.rso.rsobnb.users;

import java.util.UUID;
import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import javax.persistence.criteria.Order;
import java.util.Date;
import java.util.List;

@Entity(name = "users")
@NamedQueries(value =
        {
                @NamedQuery(name = "Users.getAll", query = "SELECT u FROM users u")
        })
@UuidGenerator(name = "idGenerator")
public class User {

    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "password")
    private String password;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;



    @Transient
    private List<RealEstate> realEstates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<RealEstate> getRealEstates() {
        return realEstates;
    }

    public void setRealEstates(List<RealEstate> realEstates) {
        this.realEstates = realEstates;
    }
}