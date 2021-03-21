package scheduler.model;

/**
 * Contacts
 *
 * @author Musee Ullah
 */
public class Contact {
    private int contactID;
    private String name, email;

    /**
     * Constructor for fully defined Contact. Mainly used in database queries.
     *
     * @param contactID unique Contact ID
     * @param name      name of the Customer.
     * @param email     the Customer's email address.
     */
    public Contact(int contactID, String name, String email) {
        this.contactID = contactID;
        this.name = name;
        this.email = email;
    }

    /**
     * a null customer.
     */
    public Contact() {
    }

    /**
     * ID-based Constructor for a Contact to use for database lookups from foreign keys.
     *
     * @param contactID the ID of this contact.
     */
    public Contact(int contactID) {
        this.contactID = contactID;
    }

    /**
     * @return the contact ID of the Contact.
     */
    public int getID() {
        return contactID;
    }

    /**
     * @return the full name of the Contact.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the full name of the Contact.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the email address of the Contact.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email address to associate with the Contact.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}