package scheduler;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static Connection conn = null;

    public static Connection open() {
        DataSource ds = getMySQLDataSource();

        try {
            conn = ds.getConnection();
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static void close() {
        try {
            if (conn != null)
                conn.close();
            System.out.println("Database connection safely closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection get() {
        return conn;
    }

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