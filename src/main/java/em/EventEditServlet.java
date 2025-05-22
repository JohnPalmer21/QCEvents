package em;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/secure/event/edit")
public class EventEditServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = (String) request.getAttribute("role");
        String phone = (String) request.getAttribute("phone");

        if (role == null || !role.equals("officer")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Only club officers can edit events.");
            return;
        }

        int eventId = Integer.parseInt(request.getParameter("event_id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String location = request.getParameter("location");
        String category = request.getParameter("category");

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

            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE events SET title=?, description=?, date=?, time=?, location=?, category=? WHERE id=?");
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, date);
            stmt.setString(4, time);
            stmt.setString(5, location);
            stmt.setString(6, category);
            stmt.setInt(7, eventId);

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
