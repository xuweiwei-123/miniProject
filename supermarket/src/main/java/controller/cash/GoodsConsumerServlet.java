package controller.cash;

import com.alibaba.fastjson.JSONObject;
import entity.consume.AddConsume;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "GoodsConsumerServlet", value = "/GoodsConsumerServlet")
public class GoodsConsumerServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/supermarket";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取请求体的JSON数据
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        System.out.println("Request Body: " + requestBody.toString());
        // 将请求体的JSON数据转换为GoodsAdd对象
        AddConsume addConsume = JSONObject.parseObject(requestBody.toString(), AddConsume.class);

        // 连接数据库并进行添加商品逻辑
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载 MySQL JDBC 驱动程序
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO consume (m_id,selectPayment,payMount,cart,time ) VALUES (?,?,?,?,?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, addConsume.getMemberId());
                    statement.setString(2, addConsume.getPaymentOption());
                    statement.setString(3, addConsume.getPaymentAmount());
                    statement.setString(4, addConsume.getCart());
                    statement.setTimestamp(5, new Timestamp(addConsume.getTime().getTime()));

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        sendJsonResponse(response, true, "成功添加消费信息");
                        System.out.println("成功添加消费信息！");

                    } else {
                        sendJsonResponse(response, false, "消费信息添加失败");  // 消费信息添加失败
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "消费信息添加失败，请稍后重试");  // 数据库连接或查询出错，返回错误响应
        }
    }

    private void sendJsonResponse(HttpServletResponse resp, boolean success, String message) throws IOException {
        // 构建响应JSON数据
        JSONObject responseJson = new JSONObject();
        responseJson.put("success", success);
        if (message != null) {
            responseJson.put("message", message);
        }

        // 设置响应头部和内容类型
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // 发送响应数据
        resp.getWriter().write(responseJson.toJSONString());
    }
}
