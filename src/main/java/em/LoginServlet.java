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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phone = request.getParameter("phone");

        try (Connection conn = DB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE phone = ?");
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                String major = rs.getString("major");
                String interests = rs.getString("interests");

                String token = JWTUtil.generateToken(phone, role, major, interests);

                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print("{\"token\": \"" + token + "\", \"role\": \"" + role + "\"}");
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid phone number");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Server error");
        }
    }
}
