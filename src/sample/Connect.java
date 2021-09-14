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

    // �����������
    static {
        // ��������ļ�������
        InputStream input = Connect.class.getResourceAsStream("config.properties");
        try {
            // ���������ļ����ݵ�Properties����
            info.load(input);
            // �������ļ���ȡ��url
            url = info.getProperty("url");
            // �������ļ���ȡ��driver
            driver = info.getProperty("driver");
            userName = info.getProperty("userName");
            password = info.getProperty("password");
            Class.forName(driver);
            System.out.println("����������سɹ�...");
        } catch (ClassNotFoundException e) {
            System.out.println("�����������ʧ��...");
        } catch (IOException e) {
            System.out.println("���������ļ�ʧ��...");
        }
    }

    // ������ݿ�����
    public static Connection getConnection() {
        // �������ݿ�����
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, userName, password);
             System.out.println("���ݿ����ӳɹ���");
        } catch (SQLException e) {
            System.out.println("���ݿ�����ʧ�ܣ�");
            System.out.println(url);
            System.out.println(info);
        }
        return conn;
    }
}
