package scheduler.util;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class DBUtil {
    /**
     * Initializes a network connection to an SQL database.
     *
     * @param ds a data source object containing database connection information.
     */
    public static Optional<Connection> createConnection(DataSource ds) {
        Connection c = null;
        try {
            c = ds.getConnection();
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database.");
        }
        return Optional.ofNullable(c);
    }

    /**
     * Logs out and closes out of a connection to an SQL database.
     */
    public static void closeConnection(Connection c) {
        try {
            c.close();
            System.out.println("Database connection safely closed.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to cleanly close database connection.");
        }
    }

    /**
     * Reads database connection information from a configuration file.
     *
     * @param rbLocation path to the ResourceBundle with database connection configuration.
     * @return a MySQL data source to connect to.
     */
    public static MysqlDataSource loadConnectionConfig(String rbLocation) {
        Properties props = new Properties();
        FileInputStream fis;
        MysqlDataSource mysqlDS = null;
        try {
            fis = new FileInputStream(rbLocation);
            props.load(fis);
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
            mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load database connection information from file.");
        }
        return mysqlDS;
    }
}