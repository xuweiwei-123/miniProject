package controller.members;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.*;

@WebServlet(name = "CheckMembershipServlet", value = "/CheckMembershipServlet")
public class CheckMembershipServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String memberId = request.getParameter("memberId");

        // 在这里编写检查会员是否存在的逻辑
        boolean isMember = false;
        try {
            isMember = checkMembership(memberId);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 将检查结果作为JSON响应发送回客户端
        String jsonResponse = "{\"isMember\":" + isMember + "}";
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }

    private static final String DB_URL = "jdbc:mysql://localhost:3306/supermarket";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    private static boolean checkMembership(String memberId) throws ClassNotFoundException {
        boolean isMember = false;
        Class.forName("com.mysql.cj.jdbc.Driver"); // 加载 MySQL JDBC 驱动程序
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT COUNT(*) FROM members WHERE m_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, memberId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                isMember = count > 0;

                if (isMember) {
                    System.out.println("验证成功！");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理数据库连接或查询错误
            System.out.println("查询错误！");
        }

        return isMember;
    }

}
