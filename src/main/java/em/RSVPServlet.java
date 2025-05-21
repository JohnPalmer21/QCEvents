// ---- Fix for RSVPServlet.java ----
package em;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/rsvp")
public class RSVPServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("user_id"));
        int eventId = Integer.parseInt(request.getParameter("event_id"));

        try (Connection conn = DB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO rsvps (user_id, event_id) VALUES (?, ?)");
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            ps.executeUpdate();

            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"message\": \"RSVP saved.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
