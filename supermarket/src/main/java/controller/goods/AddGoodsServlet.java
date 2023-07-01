package controller.goods;

import com.alibaba.fastjson.JSONObject;
import entity.goods.GoodsAdd;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "AddGoodsServlet", value = "/AddGoodsServlet")
public class AddGoodsServlet extends HttpServlet {
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

        // 将请求体的JSON数据转换为GoodsAdd对象
        GoodsAdd goodsAdd = JSONObject.parseObject(requestBody.toString(), GoodsAdd.class);

        // 空值检查
        if (goodsAdd.getAddGoodsID() == null || goodsAdd.getAddGoodsName() == null || goodsAdd.getAddGoodsType() == null || goodsAdd.getAddGoodsPrice() == 0 || goodsAdd.getAddGoodsNumber() == 0) {
            sendJsonResponse(response, false, "商品ID、商品名、商品所属种类、商品价格和商品数量不能为空");
            return;
        }

        // 连接数据库并进行添加商品逻辑
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载 MySQL JDBC 驱动程序
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // 检查商品是否已存在
                String checkExistQuery = "SELECT g_id, g_number FROM goods WHERE g_id = ?";
                try (PreparedStatement checkExistStatement = connection.prepareStatement(checkExistQuery)) {
                    checkExistStatement.setString(1, goodsAdd.getAddGoodsID());
                    ResultSet resultSet = checkExistStatement.executeQuery();
                    if (resultSet.next()) {
                        // 商品已存在，更新数量
                        int currentQuantity = resultSet.getInt("g_number");
                        int newQuantity = currentQuantity + goodsAdd.getAddGoodsNumber();

                        String updateQuantityQuery = "UPDATE goods SET g_number = ? WHERE g_id = ?";
                        try (PreparedStatement updateQuantityStatement = connection.prepareStatement(updateQuantityQuery)) {
                            updateQuantityStatement.setInt(1, newQuantity);
                            updateQuantityStatement.setString(2, goodsAdd.getAddGoodsID());

                            int rows = updateQuantityStatement.executeUpdate();
                            if (rows > 0) {
                                sendJsonResponse(response, true, "成功更新商品数量");
                                System.out.println("成功更新商品数量！");
                                // 更新种类表中的数量字段
                                String updateTypeQuery = "UPDATE types SET t_number = t_number + ? WHERE g_type = ?";
                                try (PreparedStatement updateTypeStatement = connection.prepareStatement(updateTypeQuery)) {
                                    updateTypeStatement.setInt(1, goodsAdd.getAddGoodsNumber());
                                    updateTypeStatement.setString(2, goodsAdd.getAddGoodsType());

                                    int typeRows = updateTypeStatement.executeUpdate();
                                    if (typeRows > 0) {
                                        // 种类数量更新成功
                                        System.out.println("成功更新种类数量");
                                    } else {
                                        // 种类名称不存在或更新失败
                                        sendJsonResponse(response, false, "种类数量更新失败");
                                    }
                                }
                            } else {
                                sendJsonResponse(response, false, "商品数量更新失败");
                            }
                        }
                    } else {
                        // 商品不存在，插入新条目
                        String insertQuery = "INSERT INTO goods (g_id, g_name, g_type, g_price, g_number) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                            insertStatement.setString(1, goodsAdd.getAddGoodsID());
                            insertStatement.setString(2, goodsAdd.getAddGoodsName());
                            insertStatement.setString(3, goodsAdd.getAddGoodsType());
                            insertStatement.setDouble(4, goodsAdd.getAddGoodsPrice());
                            insertStatement.setInt(5, goodsAdd.getAddGoodsNumber());

                            int rows = insertStatement.executeUpdate();
                            if (rows > 0) {
                                sendJsonResponse(response, true, "成功添加商品");
                                System.out.println("成功添加商品！");
                                // 更新种类表中的数量字段
                                String updateTypeQuery = "UPDATE types SET t_number = t_number + ? WHERE g_type = ?";
                                try (PreparedStatement updateTypeStatement = connection.prepareStatement(updateTypeQuery)) {
                                    updateTypeStatement.setInt(1, goodsAdd.getAddGoodsNumber());
                                    updateTypeStatement.setString(2, goodsAdd.getAddGoodsType());

                                    int typeRows = updateTypeStatement.executeUpdate();
                                    if (typeRows > 0) {
                                        // 种类数量更新成功
                                        System.out.println("成功更新种类数量");
                                    } else {
                                        // 种类名称不存在或更新失败
                                        sendJsonResponse(response, false, "种类数量更新失败");
                                    }
                                }
                            } else {
                                sendJsonResponse(response, false, "商品添加失败");
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "商品操作失败，请稍后重试");
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
