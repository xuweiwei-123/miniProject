package controller.imAndput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImportData {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        String csvFilePath = "F:\\file\\data.csv"; // 替换为您的 CSV 文件路径

        try {
            // 创建数据库连接
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket", "root", "");

            // 准备插入数据的 SQL 语句
            String sql = "INSERT INTO goods (g_id, g_name, g_type, g_price, g_number) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            // 读取 CSV 文件并逐行插入数据
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(","); // 假设 CSV 文件中数据以逗号分隔
                // 设置 SQL 语句中的参数
                statement.setString(1, data[0]);
                statement.setString(2, data[1]);
                statement.setString(3, data[2]);
                statement.setDouble(4, Double.parseDouble(data[3])); // 假设价格为 double 类型
                statement.setInt(5, Integer.parseInt(data[4])); // 假设数量为整数类型

                // 执行插入操作
                statement.executeUpdate();
            }

            // 关闭资源
            reader.close();
            statement.close();
            conn.close();

            System.out.println("数据导入成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
