package controller.members;

import com.alibaba.fastjson.JSONObject;
import entity.members.MemberAdd;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "MemberRegisterServlet", value = "/MemberRegisterServlet")
public class MemberRegisterServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/supermarket";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取请求体的JSON数据
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        // 将请求体的JSON数据转换为MemberAdd对象
        MemberAdd memberAdd = JSONObject.parseObject(requestBody.toString(), MemberAdd.class);

        // 空值检查
        if (memberAdd.getAddMemberID() == null || memberAdd.getAddMemberName() == null || memberAdd.getAddMemberPassword() == null || memberAdd.getAddMemberSex() == null || memberAdd.getAddMemberAge() == null) {
            sendJsonResponse(response, false, "会员ID、会员名、密码、性别和年龄不能为空");
            return;
        }

        // 连接数据库并进行添加会员逻辑
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载 MySQL JDBC 驱动程序
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO members (m_id,m_name,m_password,m_sex,m_age) VALUES (?,?,?,?,?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, memberAdd.getAddMemberID());
                    statement.setString(2, memberAdd.getAddMemberName());
                    statement.setString(3, memberAdd.getAddMemberPassword());
                    statement.setString(4,memberAdd.getAddMemberSex());
                    statement.setString(5,memberAdd.getAddMemberAge());

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        sendJsonResponse(response, true, "成功添加会员");
                        System.out.println("成功添加会员！");
                    } else {
                        sendJsonResponse(response, false, "会员添加失败");  // 会员添加失败
                    }
                }
                String sql1 = "INSERT INTO m_id (id) VALUES (?)";
                try (PreparedStatement st = connection.prepareStatement(sql1)){
                    st.setString(1,memberAdd.getAddMemberID());
                    int row = st.executeUpdate();
                    if (row > 0) {
                        System.out.println("成功添加id！");
                    } else {
                        System.out.println("添加id失败！");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "会员添加失败，请稍后重试");  // 数据库连接或查询出错，返回错误响应
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
