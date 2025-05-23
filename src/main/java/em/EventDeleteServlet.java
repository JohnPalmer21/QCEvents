package em;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/secure/event/delete")
public class EventDeleteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = (String) request.getAttribute("role");
        String phone = (String) request.getAttribute("phone");
        /*
        if (role == null || !role.equals("officer")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Only club officers can delete events.");
            return;
        }
        */
        int eventId = Integer.parseInt(request.getParameter("event_id"));

        try (Connection conn = DB.getConnection()) {
            PreparedStatement check = conn.prepareStatement(
                "SELECT e.id FROM events e JOIN users u ON e.created_by = u.id WHERE e.id = ? AND u.phone = ?");
            check.setInt(1, eventId);
            check.setString(2, phone);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("You do not own this event.");
                return;
            }

            PreparedStatement stmt = conn.prepareStatement("DELETE FROM events WHERE id = ?");
            stmt.setInt(1, eventId);
            stmt.executeUpdate();

            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"success\": false, \"error\": \"Database error\"}");
        }
    }
}
