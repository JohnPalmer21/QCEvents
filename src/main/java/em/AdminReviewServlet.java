package em;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/secure/admin/review")
public class AdminReviewServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = (String) request.getAttribute("role");

        if (role == null || !role.equals("admin")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Only admins can access this route.");
            return;
        }

        try (Connection conn = DB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, title, description, date, time, location, flag_reason FROM events WHERE flagged = true AND approved = false");
            ResultSet rs = stmt.executeQuery();

            StringBuilder json = new StringBuilder("[");
            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                json.append("{\"id\":").append(rs.getInt("id"))
                    .append(",\"title\":\"").append(rs.getString("title"))
                    .append("\",\"description\":\"").append(rs.getString("description"))
                    .append("\",\"date\":\"").append(rs.getString("date"))
                    .append("\",\"time\":\"").append(rs.getString("time"))
                    .append("\",\"location\":\"").append(rs.getString("location"))
                    .append("\",\"flag_reason\":\"").append(rs.getString("flag_reason")).append("\"}");
                first = false;
            }
            json.append("]");

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("Error fetching flagged events");
        }
    }
}
