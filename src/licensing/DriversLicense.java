package licensing;
import java.util.Date;

public class DriversLicense extends AbstractPrimaryDocument {
    public DriversLicense(String firstName, String lastName, String nationality,
                          char gender, Date dateOfBirth, Date expiryDate) {
        super(firstName,lastName,nationality,gender,dateOfBirth,expiryDate);
    }
}
