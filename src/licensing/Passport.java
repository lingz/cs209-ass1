package licensing;
import java.util.Date;

public class Passport extends AbstractPrimaryDocument {
    public Passport(String firstName, String lastName, String nationality,
                    char gender, Date dateOfBirth, Date expiryDate) {
        super(firstName,lastName,nationality,gender,dateOfBirth,expiryDate);
    }
}
