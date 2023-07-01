package controller.users;

import com.alibaba.fastjson.JSONObject;
import entity.users.ReqWrapperBodyAdd;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "UserRegisterServlet", value = "/UserRegisterServlet")
public class UserRegisterServlet extends HttpServlet {
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

        // 将请求体的JSON数据转换为ReqWrapperBody对象
        ReqWrapperBodyAdd reqWrapperBody = JSONObject.parseObject(requestBody.toString(), ReqWrapperBodyAdd.class);

        // 空值检查
        if (reqWrapperBody.getAddUserID() == null || reqWrapperBody.getAddUserName() == null || reqWrapperBody.getAddUserPassword() == null || reqWrapperBody.getAddUserSex() == null || reqWrapperBody.getAddUserAge() == null) {
            sendJsonResponse(response, false, "用户ID、用户名、密码、性别和年龄不能为空");
            return;
        }

        // 连接数据库并进行添加收银员逻辑
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载 MySQL JDBC 驱动程序
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO users (user_id,username,password,user_sex,user_age) VALUES (?,?,?,?,?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, reqWrapperBody.getAddUserID());
                    statement.setString(2, reqWrapperBody.getAddUserName());
                    statement.setString(3, reqWrapperBody.getAddUserPassword());
                    statement.setString(4,reqWrapperBody.getAddUserSex());
                    statement.setString(5,reqWrapperBody.getAddUserAge());

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        sendJsonResponse(response, true, "成功添加收银员");
                        System.out.println("成功添加收银员！");
                    } else {
                        sendJsonResponse(response, false, "收银员添加失败");  // 收银员添加失败
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "收银员添加失败，请稍后重试");  // 数据库连接或查询出错，返回错误响应
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
