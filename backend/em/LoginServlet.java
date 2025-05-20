package em;

//LoginServlet.java
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
 protected void doPost(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException {

     String phone = request.getParameter("phone");

     try (Connection conn = DB.getConnection()) {
         PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE phone = ?");
         ps.setString(1, phone);
         ResultSet rs = ps.executeQuery();

         if (rs.next()) {
             response.setStatus(200); // Successful login
         } else {
             // optional: auto-register user
             PreparedStatement insert = conn.prepareStatement("INSERT INTO users (phone) VALUES (?)");
             insert.setString(1, phone);
             insert.executeUpdate();
             response.setStatus(201); // Sucessful registration created
         }

     } catch (Exception e) {
         e.printStackTrace();
         response.setStatus(500); // Server Error
     }
 }
}
