package scheduler.model;

import java.sql.Timestamp;

/**
 * Customers
 *
 * @author Musee Ullah
 */
public class Customer {
    private int customerID, divisionID;
    private String name, address, postalCode, phone, createdBy, lastUpdatedBy;
    private Timestamp createDate, lastUpdate;

    /**
     * Constructor for fully defined Customer. Mainly used in database queries.
     *
     * @param customerID    unique customer ID.
     * @param divisionID    ID of the division the customer is in.
     * @param name          full name of the customer.
     * @param address       street address of the customer.
     * @param postalCode    postal code of the customer.
     * @param phone         phone number of the customer.
     * @param createdBy     who the customer was created by.
     * @param lastUpdatedBy who the customer was last updated by.
     * @param createDate    when the customer was created.
     * @param lastUpdate    when the customer was last updated.
     */
    public Customer(int customerID, String name, String address, String postalCode, String phone, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int divisionID) {
        this.customerID = customerID;
        this.divisionID = divisionID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    /**
     * a null customer.
     */
    public Customer() {
    }

    /**
     * @return the customer ID of the Customer
     */
    public int getID() {
        return customerID;
    }

    /**
     * @return the full name of the Customer
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the full name of the Customer.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the ID of the division the Customer is in.
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @param divisionID the ID of the division to associate with the Customer.
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * @return the address of the Customer.
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to associate with the Customer.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the Customer's postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postal_code the postal code to associate with the Customer.
     */
    public void setPostalCode(String postal_code) {
        this.postalCode = postal_code;
    }

    /**
     * @return the Customer's phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone number to associate with the Customer.
     */
    public void setPhone(String phone) {
        this.phone = phone;
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
}