package controller.dashboard;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.types.ShowTypes;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetTypesServlet", value = "/GetTypesServlet")
public class GetTypesServlet extends HttpServlet {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final String DB_URL = "jdbc:mysql://localhost:3306/supermarket";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // 连接数据库
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 构建 SQL 查询语句
            String sql = "SELECT * FROM types";
            stmt = conn.prepareStatement(sql);

            // 执行查询
            rs = stmt.executeQuery();

            // 处理查询结果
            List<ShowTypes> pageGoods = new ArrayList<>();
            while (rs.next()) {
                String type = rs.getString("g_type");
                int quantity = rs.getInt("t_number");

                ShowTypes showTypes = new ShowTypes(type, quantity);
                pageGoods.add(showTypes);
            }

            // 构建包含数据列表的 JSON 对象
            JsonObject jsonData = new JsonObject();
            Gson gson = new Gson();
            jsonData.add("data", gson.toJsonTree(pageGoods));

            // 将 JSON 对象作为响应发送给前端
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonData.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            // 构建异常情况下的响应
            ErrorResult errorResult = new ErrorResult("An error occurred while processing the request.");
            Gson gson = new Gson();
            String errorJson = gson.toJson(errorResult);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(errorJson);
        } finally {
            // 关闭数据库连接和资源
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 定义一个内部类用于表示错误结果
    private static class ErrorResult {
        private String message;

        public ErrorResult(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
