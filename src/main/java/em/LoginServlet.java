package em;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String phoneStr = request.getParameter("phone");
        String password = request.getParameter("password");
        String registerFlag = request.getParameter("register");
        String major = request.getParameter("major");
        String interests = request.getParameter("interests");
        String username = request.getParameter("username");

        if (phoneStr == null || password == null || phoneStr.isEmpty() || password.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Phone number and password are required");
            return;
        }

        int phone_num;
        try {
            phone_num = Integer.parseInt(phoneStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid phone number format");
            return;
        }

        try (Connection conn = DB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE phone_num = ?");
            stmt.setInt(1, phone_num);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // User exists, check password for login
                String dbPassword = rs.getString("password");
                if (!password.equals(dbPassword)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Incorrect password");
                    return;
                }
                String userRole = rs.getString("role");
                String userMajor = rs.getString("major");
                String userInterests = rs.getString("interests");
                String userName = rs.getString("username");

                String token = JWTUtil.generateToken(phoneStr, userRole, userMajor, userInterests);

                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print("{\"token\": \"" + token + "\", \"role\": \"" + userRole + "\", \"username\": \"" + userName + "\"}");
                out.flush();
            } else if ("true".equalsIgnoreCase(registerFlag)) {
                // Registration flow
                if (username == null || username.isEmpty() || major == null || interests == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Missing registration fields");
                    return;
                }

                // Check if username is unique
                PreparedStatement userCheck = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
                userCheck.setString(1, username);
                ResultSet userRs = userCheck.executeQuery();
                if (userRs.next()) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("Username already exists");
                    return;
                }
                PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO users (username, phone_num, password, role, major, interests) VALUES (?, ?, ?, ?, ?, ?)"
                );
                insertStmt.setString(1, username);
                insertStmt.setInt(2, phone_num);
                insertStmt.setString(3, password); // For production, hash the password
                insertStmt.setString(4, "user"); // Set role to 'user' by default
                insertStmt.setString(5, major);
                insertStmt.setString(6, interests);
                insertStmt.executeUpdate();

                String token = JWTUtil.generateToken(phoneStr, "user", major, interests);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print("{\"token\": \"" + token + "\", \"role\": \"user\", \"username\": \"" + username + "\"}");
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
