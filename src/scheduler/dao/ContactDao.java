package scheduler.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.util.DBUtil;
import scheduler.model.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDao implements DAO<Contact> {
    /**
     * Fetches a list of all Contacts from the SQL database.
     *
     * @return an ObservableList populated with all Contacts.
     */
    @Override
    public ObservableList<Contact> listAll() {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from contacts;";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                allContacts.add(
                        new Contact(
                                rs.getInt("Contact_ID"),
                                rs.getString("Contact_Name"),
                                rs.getString("Email")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allContacts;
    }

    /**
     * Look up a Contact from the SQL database.
     *
     * @param contact a Contact object that includes an ID that we can lookup.
     * @return a populated Contact object if it exists in the database, otherwise an empty Contact object.
     */
    @Override
    public Contact find(Contact contact) {
        Contact contactResult = new Contact();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from contacts where Contact_ID = ?;";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            ps.setInt(1, contact.getID());

            rs = ps.executeQuery();
            if (rs.next()) {
                contactResult = new Contact(
                        rs.getInt("Contact_ID"),
                        rs.getString("Contact_Name"),
                        rs.getString("Email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contactResult;
    }

    /**
     * Updates a Contact's information.
     *
     * @param contact the updated Contact object.
     */
    @Override
    public void update(Contact contact) {
        PreparedStatement ps;
        String rawSQL = "update contacts set Contact_Name = ?, Email = ? where Contact_ID = ?;";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            ps.setString(1, contact.getName());
            ps.setString(2, contact.getEmail());
            ps.setInt(3, contact.getID());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a Contact from the SQL database.
     *
     * @param contact the Contact object to delete.
     */
    @Override
    public void delete(Contact contact) {
        PreparedStatement ps;
        String rawSQL = "delete from contacts where Contact_ID = ?;";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            ps.setInt(1, contact.getID());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new Contact into the SQL database.
     *
     * @param contact the Contact object to insert.
     */
    @Override
    public void add(Contact contact) {
        PreparedStatement ps;
        String rawSQL = "insert into contacts (Contact_Name, Email) values (?, ?)";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            ps.setString(1, contact.getName());
            ps.setString(2, contact.getEmail());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
