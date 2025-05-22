package em;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phone = request.getParameter("phone");
        String registerFlag = request.getParameter("register");
        String role = request.getParameter("role");
        String major = request.getParameter("major");
        String interests = request.getParameter("interests");

        try (Connection conn = DB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE phone = ?");
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // User exists, proceed with login
                String userRole = rs.getString("role");
                String userMajor = rs.getString("major");
                String userInterests = rs.getString("interests");

                String token = JWTUtil.generateToken(phone, userRole, userMajor, userInterests);

                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print("{\"token\": \"" + token + "\", \"role\": \"" + userRole + "\"}");
                out.flush();
            } else if ("true".equalsIgnoreCase(registerFlag)) {
                // Registration flow
                if (role == null || major == null || interests == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Missing registration fields");
                    return;
                }
                PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO users (phone, role, major, interests) VALUES (?, ?, ?, ?)"
                );
                insertStmt.setString(1, phone);
                insertStmt.setString(2, role);
                insertStmt.setString(3, major);
                insertStmt.setString(4, interests);
                insertStmt.executeUpdate();

                // Successful registration : generate a token
                String token = JWTUtil.generateToken(phone, role, major, interests);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print("{\"token\": \"" + token + "\", \"role\": \"" + role + "\"}");
                out.flush();
            } else {
                // User not found and not registering
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(
                    "Phone number not registered. To register and keep track of or create events, please register with a valid number."
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Server error");
        }
    }
}
