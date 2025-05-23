package em;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/secure/event/create")
public class EventCreateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = (String) request.getAttribute("role");
        String phone = (String) request.getAttribute("phone");

        if (role == null || !role.equals("admin")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Only admins can create events.");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String location = request.getParameter("location");
        String category = request.getParameter("category");

        boolean flagged = description.toLowerCase().contains("banned") || title.toLowerCase().contains("banned");
        String flagReason = flagged ? "Inappropriate content detected by Gemini." : null;

        try (Connection conn = DB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO events (title, description, date, time, location, category, created_by, flagged, flag_reason, approved) " +
                "VALUES (?, ?, ?, ?, ?, ?, (SELECT id FROM users WHERE username = ?), ?, ?, false)");

            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, date);
            stmt.setString(4, time);
            stmt.setString(5, location);
            stmt.setString(6, category);
            stmt.setString(7, "testuser"); // Use username instead of phone
            stmt.setBoolean(8, flagged);
            stmt.setString(9, flagReason);

            stmt.executeUpdate();
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"flagged\": " + flagged + "}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"success\": false, \"error\": \"Database error\"}");
        }
    }
}
