package scheduler.model;

/**
 * First Level Divisions
 *
 * @author Musee Ullah
 */
public class FirstLevelDivision {
    private int divisionID, countryID;
    private String division;

    /**
     * Constructor for fully defined Division. Mainly used in database queries.
     * Extraneous fields like the timestamp fields not included as this application doesn't touch them.
     *
     * @param divisionID the ID of this division.
     * @param countryID  the ID of the country this division is in.
     * @param division   the name of this division.
     */
    public FirstLevelDivision(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.countryID = countryID;
        this.division = division;
    }

    /**
     * a null Division
     */
    public FirstLevelDivision() {
    }

    /**
     * @return the ID of this division.
     */
    public int getID() {
        return divisionID;
    }

    /**
     * @return the ID of the country this division is in.
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * @return the name of this division.
     */
    public String getDivision() {
        return division;
    }
}
