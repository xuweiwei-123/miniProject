package controller.users;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "DeleteCashierServlet", value = "/DeleteCashierServlet")
public class DeleteCashierServlet extends HttpServlet {
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取要删除的收银员ID
        String cashierId = request.getParameter("cashierId");

        // 在这里执行删除收银员的操作
        boolean success = deleteCashier(cashierId, response);

        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("收银员删除成功");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("收银员删除失败");
        }
    }

    private boolean deleteCashier(String cashierId, HttpServletResponse response) {
        // 在这里编写删除会员的代码逻辑
        try {
            // 连接数据库，执行删除操作
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket", "root", "");
            String sql = "DELETE FROM users WHERE user_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, cashierId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected>0){
                System.out.println("删除收银员成功！");
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
