package scheduler.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.util.DBUtil;
import scheduler.model.User;
import scheduler.util.TimeUtil;

import java.sql.*;

public class UserDao implements DAO<User> {
    /**
     * Fetches a list of all Users from the SQL database.
     *
     * @return an ObservableList populated with all Users.
     */
    @Override
    public ObservableList<User> listAll() {
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from users;";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                allUsers.add(
                        new User(
                                rs.getInt("User_ID"),
                                rs.getString("User_Name"),
                                rs.getString("Password"),
                                rs.getString("Created_By") != null ? rs.getString("Created_By") : "",
                                rs.getString("Last_Updated_By") != null ? rs.getString("Last_Updated_By") : "",
                                rs.getTimestamp("Create_Date"),
                                rs.getTimestamp("Last_Update")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    /**
     * Look up a User from the SQL database.
     *
     * @param user a User object that includes a username that we can lookup
     * @return a populated User object if it exists in the database, otherwise an empty User object.
     */
    @Override
    public User find(User user) {
        User userResult = new User();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from users where User_Name = ?;";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            ps.setString(1, user.getUsername());

            rs = ps.executeQuery();
            if (rs.next()) {
                userResult = new User(
                        rs.getInt("User_ID"),
                        rs.getString("User_Name"),
                        rs.getString("Password"),
                        rs.getString("Created_By") != null ? rs.getString("Created_By") : "",
                        rs.getString("Last_Updated_By") != null ? rs.getString("Last_Updated_By") : "",
                        rs.getTimestamp("Create_Date"),
                        rs.getTimestamp("Last_Update")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userResult;
    }

    /**
     * Update a User's information.
     *
     * @param user the updated User object.
     */
    @Override
    public void update(User user) {
        PreparedStatement ps;
        String rawSQL = "update users set User_Name = ?, Password = ?, Last_Update = ? where User_ID = ?;";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setTimestamp(3, Timestamp.valueOf(TimeUtil.utcNow()));
            ps.setInt(4, user.getID());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a User from the SQL database.
     *
     * @param user the User object to delete.
     */
    @Override
    public void delete(User user) {
        PreparedStatement ps;
        String rawSQL = "delete from users where User_ID = ?;";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            ps.setInt(1, user.getID());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new User into the SQL database.
     *
     * @param user the User object to insert.
     */
    @Override
    public void add(User user) {
        PreparedStatement ps;
        String rawSQL = "insert into users (User_Name, Password, Created_By, Last_Updated_By) values (?, ?, ?, ?)";

        try {
            Connection c = DBUtil.get();
            ps = c.prepareStatement(rawSQL);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getCreatedBy());
            ps.setString(4, user.getLastUpdatedBy());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
