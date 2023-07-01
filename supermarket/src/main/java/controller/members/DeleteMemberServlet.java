package controller.members;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "DeleteMemberServlet", value = "/DeleteMemberServlet")
public class DeleteMemberServlet extends HttpServlet {
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取要删除的会员ID
        String memberId = request.getParameter("memberId");

        // 在这里执行删除会员的操作
        boolean success = deleteMember(memberId, response);

        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("会员删除成功");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("会员删除失败");
        }
    }

    private boolean deleteMember(String memberId, HttpServletResponse response) {
        // 在这里编写删除会员的代码逻辑
        try {
            // 连接数据库，执行删除操作
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket", "root", "");
            String sql = "DELETE FROM members WHERE m_id = ?";
            String sql1 = "DELETE FROM m_id WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            PreparedStatement st = conn.prepareStatement(sql1);
            statement.setString(1, memberId);
            st.setString(1,memberId);
            int rowsAffected = statement.executeUpdate();
            int row = st.executeUpdate();
            if (rowsAffected>0){
                System.out.println("删除会员成功！");
            }
            if (row > 0){
                System.out.println("删除id号成功！");
            }
            conn.close();

            return rowsAffected > 0; // 返回是否删除成功
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8081");
        response.setHeader("Access-Control-Allow-Methods", "DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "3600");
    }

}
