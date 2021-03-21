package scheduler;

import com.mysql.cj.jdbc.MysqlDataSource;
import scheduler.model.User;
import scheduler.util.DBUtil;

import java.sql.Connection;
import java.util.Optional;

public class State {
    private static final String configFile = "config.properties";
    private static Connection dbConnection = null;
    private static boolean dbConnected, loggedIn;
    private static User loggedInUser;

    /**
     * Provides an access handle to the SQL database connection.
     *
     * @return the database connection object to run SQL operations on.
     */
    public static Connection getDBConnection() {
        return dbConnection;
    }

    /**
     * Attempts to login to an SQL database and saves the connection state.
     *
     * @return whether or not the connection attempt was successful.
     */
    public static boolean startDBConnection() {
        MysqlDataSource ds = DBUtil.loadConnectionConfig(configFile);
        Optional<Connection> result = DBUtil.createConnection(ds);
        if (result.isPresent()) {
            dbConnection = result.get();
            dbConnected = true;
            return true;
        }
        return false;
    }

    /**
     * Closes the database connection if one is active.
     */
    public static void closeDBConnection() {
        if (dbConnection != null) {
            DBUtil.closeConnection(dbConnection);
            dbConnected = false;
        }
    }

    /**
     * @return whether or not the database connection is active.
     */
    public static boolean isDBConnected() {
        return dbConnected;
    }

    /**
     * @return whether or not a valid user is logged in.
     */
    public static boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * @return the User object of the currently logged in user.
     */
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * @param user the User object of the user logging in.
     */
    public static void login(User user) {
        State.loggedInUser = user;
        State.loggedIn = true;
    }
}
