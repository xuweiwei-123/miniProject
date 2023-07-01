package controller.goods;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.goods.GoodsAdd;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GoodsShowServlet", value = "/GoodsShowServlet")
public class GoodsShowServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int page = Integer.parseInt(request.getParameter("page")); // 从请求参数中获取页码
        int pageSize = 5; // 每页显示的记录数

        int startIndex = (page - 1) * pageSize;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // 连接数据库
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 查询总记录数
            String countSql = "SELECT COUNT(*) AS total FROM goods";
            PreparedStatement countStmt = conn.prepareStatement(countSql);
            ResultSet countRs = countStmt.executeQuery();
            countRs.next();
            int totalRecords = countRs.getInt("total");
            countRs.close();
            countStmt.close();

            // 计算总页数
            int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

            // 构建 SQL 查询语句
            String sql = "SELECT * FROM goods LIMIT ?, ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, startIndex);
            stmt.setInt(2, pageSize);

            // 执行查询
            rs = stmt.executeQuery();

            // 处理查询结果
            List<GoodsAdd> pageGoods = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("g_id");
                String name = rs.getString("g_name");
                String type = rs.getString("g_type");
                double price = rs.getDouble("g_price");
                int number = rs.getInt("g_number");

                GoodsAdd goodsAdd = new GoodsAdd(id,name,type,price,number);
                pageGoods.add(goodsAdd);
            }

            // 构建包含数据列表和总页数的 JSON 对象
            JsonObject jsonData = new JsonObject();
            Gson gson = new Gson();
            jsonData.add("data", gson.toJsonTree(pageGoods));
            jsonData.addProperty("totalPages", totalPages);

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
