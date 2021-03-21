package scheduler.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.util.DBUtil;
import scheduler.model.Customer;
import scheduler.util.TimeUtil;

import java.sql.*;

public class CustomerDao implements DAO<Customer> {
    /**
     * Fetches a list of all Customers from the SQL database.
     *
     * @return an ObservableList populated with all Customers.
     */
    @Override
    public ObservableList<Customer> listAll() {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from customers;";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                allCustomers.add(
                        new Customer(
                                rs.getInt("Customer_ID"),
                                rs.getString("Customer_Name"),
                                rs.getString("Address"),
                                rs.getString("Postal_Code"),
                                rs.getString("Phone"),
                                rs.getTimestamp("Create_Date"),
                                rs.getString("Created_By") != null ? rs.getString("Created_By") : "",
                                rs.getTimestamp("Last_Update"),
                                rs.getString("Last_Updated_By") != null ? rs.getString("Last_Updated_By") : "",
                                rs.getInt("Division_ID")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomers;
    }

    /**
     * Look up a Customer from the SQL database.
     *
     * @param customer a Customer object that includes an ID that we can lookup.
     * @return a populated Customer object if it exists in the database, otherwise an empty Customer object.
     */
    @Override
    public Customer find(Customer customer) {
        Customer customerResult = new Customer();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from customers where Customer_ID = ?;";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            ps.setInt(1, customer.getID());

            rs = ps.executeQuery();
            if (rs.next()) {
                customerResult = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By") != null ? rs.getString("Created_By") : "",
                        rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By") != null ? rs.getString("Last_Updated_By") : "",
                        rs.getInt("Division_ID")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerResult;
    }

    /**
     * Updates a Customer's information.
     *
     * @param customer the updated Customer object.
     */
    @Override
    public void update(Customer customer) {
        PreparedStatement ps;
        String rawSQL = "update customers " +
                "set Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ?," +
                "Last_Update = ?, Last_Updated_By = ?" +
                "where Customer_ID = ?;";
        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setInt(5, customer.getDivisionID());
            ps.setTimestamp(6, Timestamp.valueOf(TimeUtil.utcNow()));
            ps.setString(7, customer.getLastUpdatedBy());
            ps.setInt(8, customer.getID());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a Customer from the SQL database.
     *
     * @param customer the Customer object to delete.
     */
    @Override
    public void delete(Customer customer) {
        PreparedStatement ps;
        String rawSQL = "delete from customers where Customer_ID = ?;";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            ps.setInt(1, customer.getID());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new Customer into the SQL database.
     *
     * @param customer the Customer object to insert.
     */
    @Override
    public void add(Customer customer) {
        PreparedStatement ps;
        String rawSQL = "insert into customers" +
                "(Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID)" +
                "values (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getCreatedBy());
            ps.setString(6, customer.getLastUpdatedBy());
            ps.setInt(7, customer.getDivisionID());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
