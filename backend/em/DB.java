package em;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String URL = "jdbc:mysql://3.86.107.16:3306/event_manager";
    private static final String USER = "teamuser"; // or root if you're using that
    private static final String PASS = "teammate123"; // or whatever password you set

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
 
