package controller.dashboard;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;

import entity.types.ShowConsume;

@WebServlet(name = "GetConsumeServlet", value = "/GetConsumeServlet")
public class GetConsumeServlet extends HttpServlet {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取当前日期
        Date currentDate = new Date();

        // 连接数据库并查询当日消费记录
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket", "root", "");
            String deleteSql = "DELETE FROM typeconsume WHERE time = ?";
            PreparedStatement deleteStatement = conn.prepareStatement(deleteSql);
            deleteStatement.setDate(1, new java.sql.Date(currentDate.getTime()));
            deleteStatement.executeUpdate();

            String sql = "SELECT g.g_type, COUNT(c.cart) AS quantity, SUM(c.payMount) AS totalAmount FROM consume c JOIN goods g ON c.cart = g.g_name WHERE c.time = ? GROUP BY g.g_type";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setDate(1, new java.sql.Date(currentDate.getTime()));
            ResultSet resultSet = statement.executeQuery();

            // 构建 JSON 数组用于存储每个商品种类的消费信息
            JSONArray recordsArray = new JSONArray();

            // 计算总消费金额
            BigDecimal totalExpense = BigDecimal.ZERO;

            // 遍历查询结果
            while (resultSet.next()) {
                String type = resultSet.getString("g_type");
                int quantity = resultSet.getInt("quantity");
                BigDecimal totalAmount = resultSet.getBigDecimal("totalAmount");
                // 构建 ShowConsume 对象存储当前商品种类的消费信息
                ShowConsume recordObj = new ShowConsume(type, quantity, totalAmount);

                // 将当前商品种类的消费信息添加到数组中
                recordsArray.add(recordObj);

                // 累加总消费金额
                totalExpense = totalExpense.add(totalAmount);

                // 将消费记录存储到新表中
                saveExpenseRecordToDatabase(type, quantity, totalAmount, totalExpense, currentDate, conn);
            }

            // 构建 JSON 对象，包含商品消费信息和总消费金额
            JSONObject responseObj = new JSONObject();
            responseObj.put("records", recordsArray);
            responseObj.put("total", totalExpense);

            // 将 JSON 响应发送给前端
            response.setContentType("application/json");
            response.getWriter().write(responseObj.toString());

            // 关闭数据库连接
            resultSet.close();
            statement.close();
            deleteStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理异常
        }
    }

    private void saveExpenseRecordToDatabase(String type, int quantity, BigDecimal amount, BigDecimal totalExpense, Date recordDate, Connection conn) throws SQLException {
        String insertSql = "INSERT INTO typeconsume (type, quantity, amount, time) VALUES (?, ?, ?, ?)";
        PreparedStatement insertStatement = conn.prepareStatement(insertSql);
        insertStatement.setString(1, type);
        insertStatement.setInt(2, quantity);
        insertStatement.setBigDecimal(3, amount);
        insertStatement.setDate(4, new java.sql.Date(recordDate.getTime()));
        insertStatement.executeUpdate();
        insertStatement.close();
    }
}
