package devops.codereview;

public class Country {
    //Variables for countries table in world database
    private String countryCode;
    private String countryName;
    private String countryCont;
    private String countryReg;
    private String countryPopulation;
    private String countryCap;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCont() {
        return countryCont;
    }

    public void setCountryCont(String countryCont) {
        this.countryCont = countryCont;
    }

    public String getCountryReg() {
        return countryReg;
    }

    public void setCountryReg(String countryReg) {
        this.countryReg = countryReg;
    }

    public String getCountryPopulation() {
        return countryPopulation;
    }

    public void setCountryPopulation(String countryPopulation) {
        this.countryPopulation = countryPopulation;
    }

    public String getCountryCap() {
        return countryCap;
    }

    public void setCountryCap(String countryCap) {
        this.countryCap = countryCap;
    }
}