package em;

@WebServlet("/my-events")
public class MyEventsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int userId = Integer.parseInt(request.getParameter("user_id"));
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try (Connection conn = DB.getConnection()) {
            String sql = """
                SELECT e.* FROM events e
                JOIN rsvps r ON e.id = r.event_id
                WHERE r.user_id = ?
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            StringBuilder json = new StringBuilder("[");
            while (rs.next()) {
                json.append("{")
                    .append("\"id\":").append(rs.getInt("id")).append(",")
                    .append("\"title\":\"").append(rs.getString("title")).append("\",")
                    .append("\"date\":\"").append(rs.getString("date")).append("\",")
                    .append("\"time\":\"").append(rs.getString("time")).append("\",")
                    .append("\"location\":\"").append(rs.getString("location")).append("\",")
                    .append("\"description\":\"").append(rs.getString("description")).append("\"")
                    .append("},");
            }
            if (json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("]");
            out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("[]");
        }
    }
}