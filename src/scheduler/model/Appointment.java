package scheduler.model;

import java.sql.Timestamp;

/**
 * Customers
 *
 * @author Musee Ullah
 */
public class Appointment {
    private int appointmentID, customerID, userID, contactID;
    private String title, description, location, type, createdBy, lastUpdatedBy;
    private Timestamp start, end, createDate, lastUpdate;

    /**
     * Constructor for fully defined Appointment. Mainly used in database queries.
     *
     * @param appointmentID unique appointment ID.
     * @param title         title of the appointment.
     * @param description   further information for the appointment.
     * @param location      where the appointment will take place.
     * @param type          the category this appointment falls under.
     * @param start         the expected start time of the appointment.
     * @param end           the expected end time of the appointment.
     * @param createDate    when the appointment was created.
     * @param createdBy     who the appointment was created by.
     * @param lastUpdate    when the appointment was last updated.
     * @param lastUpdatedBy who the appointment was last updated by.
     * @param customerID    the ID of the Customer who is a party in the appointment.
     * @param userID        the ID of the User who is a party in the appointment.
     * @param contactID     the ID of the Contact associated with the appointment.
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, Timestamp start, Timestamp end, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int customerID, int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    /**
     * a null Appointment.
     */
    public Appointment() {
    }

    /**
     * @return the customer ID of the Customer
     */
    public int getID() {
        return appointmentID;
    }


    /**
     * @return the title of the appointment.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title of the appointment to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return further information for the appointment.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description additional information to associate with the appointment.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return where the appointment will take place.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location where the appointment will take place.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the category this appointment falls under.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the category this appointment falls under.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the expected start time of the appointment.
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * @param start the expected start time of the appointment.
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * @return the expected end time of the appointment.
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * @param end the expected end time of the appointment.
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * @return who the user was created by.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy who the user was created by.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return who the user was last updated by.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy who the user was last updated by.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return the ID of the Customer who is a party in the appointment.
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID the ID of the Customer who is a party in the appointment.
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return the ID of the User who is a party in the appointment.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID the ID of the User who is a party in the appointment.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return the ID of the Contact associated with the appointment.
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * @param contactID the ID of the Contact associated with the appointment.
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}