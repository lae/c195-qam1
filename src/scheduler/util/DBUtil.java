package scheduler.util;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static Connection conn = null;

    /**
     * Initializes a network connection to an SQL database.
     */
    public static void open() {
        DataSource ds = getMySQLDataSource();

        try {
            conn = ds.getConnection();
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the network connection to the SQL database.
     */
    public static void close() {
        try {
            if (conn != null)
                conn.close();
            System.out.println("Database connection safely closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Provides an access handle to the SQL database connection.
     *
     * @return the database connection object to run SQL operations on.
     */
    public static Connection get() {
        return conn;
    }

    /**
     * Reads database connection information from a configuration file.
     *
     * @return a MySQL data source to connect to.
     */
    private static MysqlDataSource getMySQLDataSource() {
        Properties props = new Properties();
        FileInputStream fis;
        MysqlDataSource mysqlDS = null;
        try {
            fis = new FileInputStream("config.properties");
            props.load(fis);
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
            mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mysqlDS;
    }
}