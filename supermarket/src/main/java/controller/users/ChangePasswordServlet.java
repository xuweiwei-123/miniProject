package controller.users;

import com.alibaba.fastjson.JSONObject;
import entity.users.ReqWrapperBodyChange;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "ChangePasswordServlet", value = "/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {
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
        ReqWrapperBodyChange reqWrapperBody = JSONObject.parseObject(requestBody.toString(), ReqWrapperBodyChange.class);

        // 空值检查
        if (reqWrapperBody.getCurrentUsername() == null || reqWrapperBody.getCurrentPassword() == null || reqWrapperBody.getNewPassword() == null) {
            sendJsonResponse(response, false, "用户名、当前密码或新密码不能为空");
            return;
        }

        // 连接数据库并进行密码修改逻辑
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载 MySQL JDBC 驱动程序
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "UPDATE users SET password = ? WHERE username = ? AND password = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, reqWrapperBody.getNewPassword());
                    statement.setString(2, reqWrapperBody.getCurrentUsername());
                    statement.setString(3, reqWrapperBody.getCurrentPassword());

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        sendJsonResponse(response, true, "密码修改成功");
                        System.out.println("修改密码成功！");
                    } else {
                        sendJsonResponse(response, false, "当前用户名或当前密码不正确");  // 密码修改失败
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "密码修改失败，请稍后重试");  // 数据库连接或查询出错，返回错误响应
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
