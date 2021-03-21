package scheduler.model;

/**
 * Countries
 *
 * @author Musee Ullah
 */
public class Country {
    private int countryID;
    private String country;

    /**
     * Constructor for fully defined Country. Mainly used in database queries.
     * Extraneous fields like the timestamp fields not included as this application doesn't touch them.
     *
     * @param countryID the ID of this Country.
     * @param country   the name of this Country.
     */
    public Country(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }

    /**
     * a null Country
     */
    public Country() {
    }

    /**
     * ID-based Constructor for a Country to use for database lookups from foreign keys.
     *
     * @param countryID the ID of this country.
     */
    public Country(int countryID) {
        this.countryID = countryID;
    }

    /**
     * @return the ID of this Country.
     */
    public int getID() {
        return countryID;
    }

    /**
     * @return the name of this Country.
     */
    public String getName() {
        return country;
    }
}