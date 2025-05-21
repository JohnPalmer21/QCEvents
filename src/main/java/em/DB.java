package em;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    public static Connection getConnection() throws Exception {
        // ✅ EC2 MySQL info
        String url = "jdbc:mysql://3.86.107.16:3306/event_manager";
        String user = "teamuser";
        String password = "teammate123";

        System.out.println("🔄 Attempting to connect to MySQL at " + url);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ MySQL connection successful.");
            return conn;
        } catch (Exception e) {
            System.out.println("❌ Failed to connect to MySQL:");
            e.printStackTrace();
            throw e;
        }
    }
}
