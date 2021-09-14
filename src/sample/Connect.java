package sample;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connect {

    private static Properties info = new Properties();
    private static String driver;
    private static String url;
    private static String userName;
    private static String password;

    // 驱动程序加载
    static {
        // 获得属性文件输入流
        InputStream input = Connect.class.getResourceAsStream("config.properties");
        try {
            // 加载属性文件内容到Properties对象
            info.load(input);
            // 从属性文件中取出url
            url = info.getProperty("url");
            // 从属性文件中取出driver
            driver = info.getProperty("driver");
            userName = info.getProperty("userName");
            password = info.getProperty("password");
            Class.forName(driver);
            System.out.println("驱动程序加载成功...");
        } catch (ClassNotFoundException e) {
            System.out.println("驱动程序加载失败...");
        } catch (IOException e) {
            System.out.println("加载属性文件失败...");
        }
    }

    // 获得数据库连接
    public static Connection getConnection() {
        // 创建数据库连接
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, userName, password);
             System.out.println("数据库连接成功！");
        } catch (SQLException e) {
            System.out.println("数据库连接失败！");
            System.out.println(url);
            System.out.println(info);
        }
        return conn;
    }
}
