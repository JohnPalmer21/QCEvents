package em;

//LoginServlet.java
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/events")
public class EventsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try (Connection conn = DB.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM events");

            StringBuilder json = new StringBuilder("[");
            while (rs.next()) {
                json.append("{")
                    .append("\"id\":").append(rs.getInt("id")).append(",")
                    .append("\"title\":\"").append(rs.getString("title")).append("\",")
                    .append("\"date\":\"").append(rs.getString("date")).append("\",")
                    .append("\"time\":\"").append(rs.getString("time")).append("\",")
                    .append("\"location\":\"").append(rs.getString("location")).append("\",")
                    .append("\"description\":\"").append(rs.getString("description")).append("\",")
                    .append("\"image\":\"").append(rs.getString("image")).append("\"")
                    .append("},");
            }
            if (json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1); // remove trailing comma
            }
            json.append("]");
            out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("[]");
        }
    }
}
