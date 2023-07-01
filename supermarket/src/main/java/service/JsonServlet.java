package service;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "JsonServlet", value = "/JsonServlet")
public class JsonServlet extends HttpServlet {
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
