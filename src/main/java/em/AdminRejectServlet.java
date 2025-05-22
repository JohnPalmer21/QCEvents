package em;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/secure/admin/reject")
public class AdminRejectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = (String) request.getAttribute("role");

        if (role == null || !role.equals("admin")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Only admins can reject events.");
            return;
        }

        int eventId = Integer.parseInt(request.getParameter("event_id"));

        try (Connection conn = DB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM events WHERE id = ?");
            stmt.setInt(1, eventId);
            stmt.executeUpdate();

            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("Error rejecting event");
        }
    }
}
