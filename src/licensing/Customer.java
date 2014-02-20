package assignment1;

// All members public for simplicity - no setters/getters, null by default
public class Customer {
    public EmiratesId emiratesId = null;
    public DriversLicense driversLicense = null;
    public Passport passport = null;
    public EyeTest eyeTest = null;
    public DriversLicenseTranslation driversLicenseTranslation = null;
    
    public Customer(EmiratesId emiratesId, DriversLicense driversLicense,
                    Passport passport, EyeTest eyeTest,
                    DriversLicenseTranslation driversLicenseTranslation) {
        this.emiratesId = emiratesId;
        this.driversLicense = driversLicense;
        this.passport = passport;
        this.eyeTest = eyeTest;
        this.driversLicenseTranslation = driversLicenseTranslation;
	}
}
