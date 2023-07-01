package controller.goods;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "DeleteGoodsServlet", value = "/DeleteGoodsServlet")
public class DeleteGoodsServlet extends HttpServlet {
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取要删除的商品ID
        String goodsId = request.getParameter("goodsId");

        // 在这里执行删除商品的操作
        boolean success = deleteGoods(goodsId, response);

        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("商品删除成功");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("商品删除失败");
        }
    }

    private boolean deleteGoods(String goodsId, HttpServletResponse response) {
        // 在这里编写删除商品的代码逻辑
        try {
            // 连接数据库，执行删除操作
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket", "root", "");

            // 获取商品的类型和数量
            String getTypeAndNumberQuery = "SELECT g_type, g_number FROM goods WHERE g_id = ?";
            PreparedStatement getTypeAndNumberStatement = conn.prepareStatement(getTypeAndNumberQuery);
            getTypeAndNumberStatement.setString(1, goodsId);
            ResultSet typeAndNumberResultSet = getTypeAndNumberStatement.executeQuery();
            int rowsAffected = 0;
            if (typeAndNumberResultSet.next()) {
                String goodsType = typeAndNumberResultSet.getString("g_type");
                int goodsNumber = typeAndNumberResultSet.getInt("g_number");

                // 删除商品
                String deleteGoodsQuery = "DELETE FROM goods WHERE g_id = ?";
                PreparedStatement deleteGoodsStatement = conn.prepareStatement(deleteGoodsQuery);
                deleteGoodsStatement.setString(1, goodsId);
                rowsAffected = deleteGoodsStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("删除商品成功！");

                    // 更新种类表中的数量
                    String updateTypeQuery = "UPDATE types SET t_number = t_number - ? WHERE g_type = ?";
                    PreparedStatement updateTypeStatement = conn.prepareStatement(updateTypeQuery);
                    updateTypeStatement.setInt(1, goodsNumber);
                    updateTypeStatement.setString(2, goodsType);
                    int updateRowsAffected = updateTypeStatement.executeUpdate();
                    if (updateRowsAffected > 0) {
                        System.out.println("成功更新种类数量");
                    } else {
                        sendJsonResponse(response, false, "种类数量更新失败");
                    }
                }
            } else {
                sendJsonResponse(response, false, "找不到商品的类型和数量");
            }

            conn.close();
            return rowsAffected > 0; // 返回是否删除成功
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8081");
        response.setHeader("Access-Control-Allow-Methods", "DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "3600");
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
