package licensing;
import java.util.Date;

public class EmiratesId extends AbstractPrimaryDocument {
    public String idNumber;
    
    public EmiratesId(String firstName, String lastName, String nationality,
                      char gender, Date dateOfBirth, Date expiryDate,
                      String idNumber) {
        super(firstName,lastName,nationality,gender,dateOfBirth,expiryDate);
        this.idNumber = idNumber;
    }
}
