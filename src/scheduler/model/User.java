package scheduler.model;

import scheduler.exceptions.MissingFieldsException;

import java.sql.Timestamp;

/**
 * Users with access to the scheduling application.
 *
 * @author Musee Ullah
 */
public class User {
    private int userID;
    private String username, password, createdBy, lastUpdatedBy;
    private Timestamp createDate, lastUpdate;

    /**
     * Constructor for use in a login form.
     *
     * @param username the provided username.
     * @param password the provided password.
     * @throws MissingFieldsException thrown when one of the required fields are empty.
     */
    public User(String username, String password) throws MissingFieldsException {
        int empty = 1;
        if(username.isEmpty()) { empty *= 2; }
        if(password.isEmpty()) { empty *= 4; }
        if(empty > 1) {
            if (empty == 2) {
                throw new MissingFieldsException("Missing username field.");
            } else if (empty == 4) {
                throw new MissingFieldsException("Missing password field.");
            } else {
                throw new MissingFieldsException("Missing username and password fields.");
            }
        }
        this.setUsername(username);
        this.setPassword(password);
    }

    /**
     * Constructor for fully defined User. Mainly used in database queries.
     *
     * @param userID User ID of the user.
     * @param username the username associated with the user.
     * @param password the plaintext password of the user.
     * @param createdBy who the user was created by.
     * @param lastUpdatedBy who the user was last updated by.
     * @param createDate when the user was created.
     * @param lastUpdate when the user was last updated.
     */
    public User(int userID, String username, String password, String createdBy, String lastUpdatedBy, Timestamp createDate, Timestamp lastUpdate) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    /**
     * a null user.
     */
    public User() {
    }

    /**
     * @return the user ID of the User
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @return the username of the User
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username an alphanumeric name associated with the User
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param password a password that the User uses to authenticate with
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the password of the User
     */
    public String getPassword() {
        return password;
    }
}