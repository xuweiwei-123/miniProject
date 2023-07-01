package controller.users;

import com.alibaba.fastjson.JSONObject;
import entity.users.ReqWrapperBodyLogin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "UserLoginServlet", value = "/UserLoginServlet")
public class UserLoginServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/supermarket";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求体的JSON数据
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        // 将请求体的JSON数据转换为ReqWrapperBody对象
        ReqWrapperBodyLogin reqWrapperBody = JSONObject.parseObject(requestBody.toString(), ReqWrapperBodyLogin.class);

        // 空值检查
        if (reqWrapperBody.getUsername() == null || reqWrapperBody.getPassword() == null) {
            sendJsonResponse(resp, false, "用户名或密码不能为空");
            return;
        }

        // 连接数据库并进行身份验证逻辑
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载 MySQL JDBC 驱动程序
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, reqWrapperBody.getUsername());
                    statement.setString(2, reqWrapperBody.getPassword());
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            sendJsonResponse(resp, true, null);  // 登录成功
                            System.out.println("登录成功");
                        } else {
                            sendJsonResponse(resp, false, "用户名或密码错误");  // 登录失败
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            sendJsonResponse(resp, false, "登录失败，请稍后重试");  // 数据库连接或查询出错，返回错误响应
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
