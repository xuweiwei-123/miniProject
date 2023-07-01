package controller.cash;

import com.google.gson.Gson;
import entity.goods.GoodsAdd;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GoodsCashServlet", value = "/GoodsCashServlet")
public class GoodsCashServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/supermarket";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<GoodsAdd> productList = new ArrayList<>();

        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); // 加载 MySQL JDBC 驱动程序
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            // 连接数据库
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 执行查询
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM goods");

            // 处理查询结果
            while (rs.next()) {
                String id = rs.getString("g_id");
                String name = rs.getString("g_name");
                String type = rs.getString("g_type");
                double price = rs.getDouble("g_price");
                int number = rs.getInt("g_number");

                GoodsAdd product = new GoodsAdd(id, name, type,price,number);
                productList.add(product);
            }

            // 关闭数据库连接和资源
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // 将商品列表转换为JSON字符串
        Gson gson = new Gson();
        String json = gson.toJson(productList);

        // 设置响应内容类型和编码
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 将JSON字符串作为响应发送给前端
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}
