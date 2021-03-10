package scheduler.model;

import scheduler.exceptions.MissingFieldsException;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Users with access to the scheduling application.
 *
 * @author Musee Ullah
 */
public class User {
    private int userID;
    private String username, password, createdBy, lastUpdatedBy;
    private Date createDate;
    private Timestamp lastUpdate;

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
}