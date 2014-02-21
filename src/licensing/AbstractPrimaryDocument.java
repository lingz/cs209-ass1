
import java.util.Date;

public abstract class AbstractPrimaryDocument {
    public String firstName;
    public String lastName;
    public String nationality;
    public char gender;
    public Date dateOfBirth;
    public Date expiryDate;
    
    public AbstractPrimaryDocument(String firstName, String lastName,
                                   String nationality, char gender,
                                   Date dateOfBirth, Date expiryDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.expiryDate = expiryDate;
    }                                   
}
