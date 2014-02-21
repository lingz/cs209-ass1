package licensing;
import java.util.Date;

public class EyeTest extends AbstractPrimaryDocument {
    public String emiratesIdNumber;

    public EyeTest(String firstName, String lastName, String nationality,
                   char gender, Date dateOfBirth, Date expiryDate,
                   String emiratesIdNumber) {
        // Java date libraries suck; this is imprecise
        super(firstName,lastName,nationality,gender,dateOfBirth,
              expiryDate != null ? expiryDate : new Date(
				  System.currentTimeMillis()+2592000000L));
        this.emiratesIdNumber = emiratesIdNumber;
    }
}
