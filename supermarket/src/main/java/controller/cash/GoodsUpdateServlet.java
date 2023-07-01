package controller.cash;

import com.alibaba.fastjson.JSONObject;
import entity.consume.AddMessage;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "GoodsUpdateServlet", value = "/GoodsUpdateServlet")
public class GoodsUpdateServlet extends HttpServlet {
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
        // 将请求体的JSON数据转换为AddMessage对象
        AddMessage addMessage = JSONObject.parseObject(requestBody.toString(), AddMessage.class);

        // 连接数据库并进行商品数量更新逻辑
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载 MySQL JDBC 驱动程序
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // 查询商品表，获取对应的 g_type
                String selectTypeSql = "SELECT g_type FROM goods WHERE g_id = ?";
                try (PreparedStatement selectTypeStatement = connection.prepareStatement(selectTypeSql)) {
                    selectTypeStatement.setString(1, addMessage.getProductId());
                    ResultSet resultSet = selectTypeStatement.executeQuery();
                    if (resultSet.next()) {
                        String gType = resultSet.getString("g_type");

                        // 更新商品表
                        String updateGoodsSql = "UPDATE goods SET g_number = g_number - ? WHERE g_id = ?";
                        try (PreparedStatement goodsStatement = connection.prepareStatement(updateGoodsSql)) {
                            goodsStatement.setInt(1, addMessage.getQuantity());
                            goodsStatement.setString(2, addMessage.getProductId());
                            int goodsRowsUpdated = goodsStatement.executeUpdate();

                            // 更新种类表
                            String updateTypeSql = "UPDATE types SET t_number = t_number - ? WHERE g_type = ?";
                            try (PreparedStatement typeStatement = connection.prepareStatement(updateTypeSql)) {
                                typeStatement.setInt(1, addMessage.getQuantity());
                                typeStatement.setString(2, gType);
                                int categoryRowsUpdated = typeStatement.executeUpdate();

                                if (goodsRowsUpdated > 0 && categoryRowsUpdated > 0) {
                                    sendJsonResponse(response, true, "成功更新数量");
                                    System.out.println("成功更新数量！");
                                } else {
                                    sendJsonResponse(response, false, "数量更新失败");  // 数量更新失败
                                }
                            }
                        }
                    } else {
                        sendJsonResponse(response, false, "商品不存在");  // 商品不存在
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "数量更新失败，请稍后重试");  // 数据库连接或查询出错，返回错误响应
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
