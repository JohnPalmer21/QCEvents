package em;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EventsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try (Connection conn = DB.getConnection()) {
            System.out.println("✅ Connected to DB. Running query...");

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
                    .append("\"description\":\"").append(rs.getString("description")).append("\"");

                // Optional image column support
                try {
                    String image = rs.getString("image");
                    if (image != null && !image.isEmpty()) {
                        json.append(",\"image\":\"").append(image).append("\"");
                    }
                } catch (Exception ignored) {}

                json.append("},");
            }

            if (json.length() > 1 && json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1); // remove trailing comma
            }

            json.append("]");
            out.println(json);
        } catch (Exception e) {
            System.out.println("❌ Database connection or query failed.");
            e.printStackTrace();
            out.println("[]");
        }
    }
}
