package scheduler.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.DBConnection;
import scheduler.model.User;
import scheduler.util.TimeUtil;

import java.sql.*;

public class UserDao implements DAO<User> {
    public ObservableList<User> listAll() {
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from users;";

        try {
            Connection c = DBConnection.get();
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

    public User find(User user) {
        User userResult = new User();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from users where User_Name = ?;";

        try {
            Connection c = DBConnection.get();
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

    public void update(User user) {
        PreparedStatement ps;
        String rawSQL = "update users set User_Name = ?, Password = ?, Last_Update = ? where User_ID = ?;";

        try {
            Connection c = DBConnection.get();
            ps = c.prepareStatement(rawSQL);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setTimestamp(3, Timestamp.valueOf(TimeUtil.utcNow()));
            ps.setInt(4, user.getUserID());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(User user) {
        PreparedStatement ps;
        String rawSQL = "delete users where User_ID = ?;";

        try {
            Connection c = DBConnection.get();
            ps = c.prepareStatement(rawSQL);
            ps.setInt(1, user.getUserID());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(User user) {
        PreparedStatement ps;
        String rawSQL = "insert into users (User_Name, Password, Created_By, Last_Updated_By) values (?, ?, ?, ?)";

        try {
            Connection c = DBConnection.get();
            ps = c.prepareStatement(rawSQL);
            ps.setInt(1, user.getUserID());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
