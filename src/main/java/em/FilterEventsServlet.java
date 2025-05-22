package em;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/events/filter")
public class FilterEventsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String major = request.getParameter("major"); // User's major (optional)
        String interest = request.getParameter("interest"); // User's interest (optional)

        ArrayList<Map<String, Object>> events = new ArrayList<>(); // List to hold filtered events
        StringBuilder sql = new StringBuilder("SELECT * FROM events WHERE approved = true"); // Base query
        if (major != null && !major.isEmpty()) {
            sql.append(" AND (major = ? OR major IS NULL)"); // Filter by major if provided
        }
        if (interest != null && !interest.isEmpty()) {
            sql.append(" AND (interest = ? OR interest IS NULL)"); // Filter by interest if provided
        }
        try (Connection conn = DB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            int idx = 1;
            if (major != null && !major.isEmpty()) {
                stmt.setString(idx++, major); // Set major parameter
            }
            if (interest != null && !interest.isEmpty()) {
                stmt.setString(idx++, interest); // Set interest parameter
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> event = new HashMap<>();
                // Populate event map with database fields
                event.put("id", rs.getInt("id"));
                event.put("title", rs.getString("title"));
                event.put("date", rs.getString("date"));
                event.put("time", rs.getString("time"));
                event.put("location", rs.getString("location"));
                event.put("description", rs.getString("description"));
                event.put("image", rs.getString("image"));
                event.put("major", rs.getString("major"));
                event.put("interest", rs.getString("interest"));
                event.put("approved", rs.getBoolean("approved"));
                event.put("flagged", rs.getBoolean("flagged"));
                event.put("flag_reason", rs.getString("flag_reason"));
                event.put("flaggedByGemini", rs.getBoolean("flaggedByGemini"));
                events.add(event);
            }
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("Error filtering events");
            return;
        }
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(events));
    }
}
