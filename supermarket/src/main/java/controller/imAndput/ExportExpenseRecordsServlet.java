package controller.imAndput;

import entity.types.ShowConsume;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ExportExpenseRecordsServlet", value = "/ExportExpenseRecordsServlet")
public class ExportExpenseRecordsServlet extends HttpServlet {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ShowConsume> expenseRecords = getExpenseRecordsFromDatabase(); // 从数据库获取消费记录

        // 创建Excel工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Expense Records");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Type");
        headerRow.createCell(1).setCellValue("Quantity");
        headerRow.createCell(2).setCellValue("Amount");

        // 填充数据
        int rowNum = 1;
        for (ShowConsume record : expenseRecords) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(record.getType());
            row.createCell(1).setCellValue(record.getQuantity());
            row.createCell(2).setCellValue(record.getAmount().doubleValue());
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=expense_records.xlsx"); // 修改文件名和扩展名为对应的 Excel 文件名和扩展名
        response.setCharacterEncoding("UTF-8");

        // 将Excel数据写入响应输出流
        try (OutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            outputStream.flush(); // 刷新输出流
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭工作簿
            workbook.close();
        }
    }



    private List<ShowConsume> getExpenseRecordsFromDatabase() {
        List<ShowConsume> expenseRecords = new ArrayList<>();

        // 连接数据库并查询消费记录
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket", "root", "");
            String sql = "SELECT type, quantity, amount, time FROM typeConsume";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // 遍历查询结果
            while (resultSet.next()) {
                String type = resultSet.getString("type");
                int quantity = resultSet.getInt("quantity");
                BigDecimal amount = resultSet.getBigDecimal("amount");
                Date time = resultSet.getDate("time");

                // 创建 ShowConsume 对象并添加到消费记录列表
                ShowConsume record = new ShowConsume(type, quantity, amount);
                expenseRecords.add(record);
            }

            // 关闭数据库连接
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理异常
        }

        return expenseRecords;
    }

}
