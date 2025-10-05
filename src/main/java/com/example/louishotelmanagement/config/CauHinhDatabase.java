package com.example.louishotelmanagement.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CauHinhDatabase {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyKhachSan;encrypt=false;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "SApassword@123";
    
    // Timeout settings
    private static final int CONNECTION_TIMEOUT = 10; // seconds
    private static final int LOGIN_TIMEOUT = 10; // seconds

    static {
        try {
            // Load SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Đã tải thành công SQL Server JDBC Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy SQL Server JDBC Driver: " + e.getMessage());
            System.err.println("Hướng dẫn khắc phục:");
            System.err.println("1. Thêm dependency mssql-jdbc vào pom.xml:");
            System.err.println("    <dependency>");
            System.err.println("        <groupId>com.microsoft.sqlserver</groupId>");
            System.err.println("        <artifactId>mssql-jdbc</artifactId>");
            System.err.println("        <version>12.6.1.jre11</version>");
            System.err.println("    </dependency>");
            System.err.println("2. Chạy: mvn clean install");
            System.err.println("3. Hoặc refresh Maven project trong IDE");
            e.printStackTrace();
        }
    }

    /**
     * Tạo kết nối đến database
     * @return Connection object
     * @throws SQLException nếu không thể kết nối
     */
    public static Connection getConnection() throws SQLException {
        try {
            System.out.println("Đang kết nối đến: " + URL);
            System.out.println("User: " + USER);
            
            // Set connection properties
            java.util.Properties props = new java.util.Properties();
            props.setProperty("user", USER);
            props.setProperty("password", PASS);
            props.setProperty("loginTimeout", String.valueOf(LOGIN_TIMEOUT));
            props.setProperty("connectTimeout", String.valueOf(CONNECTION_TIMEOUT * 1000));
            
            Connection connection = DriverManager.getConnection(URL, props);
            
            // Kiểm tra kết nối
            if (connection.isValid(5)) {
                System.out.println("Kết nối database thành công!");
                return connection;
            } else {
                throw new SQLException("Kết nối không hợp lệ");
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối database:");
            System.err.println(" - Mã lỗi: " + e.getErrorCode());
            System.err.println(" - Trạng thái SQL: " + e.getSQLState());
            System.err.println(" - Thông báo: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Kiểm tra kết nối database có hoạt động không
     * @return true nếu kết nối thành công, false nếu thất bại
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("❌ Kiểm tra kết nối thất bại: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lấy thông tin chi tiết về lỗi kết nối
     * @return String mô tả lỗi
     */
    public static String getConnectionErrorDetails() {
        try {
            getConnection();
            return "Kết nối thành công";
        } catch (SQLException e) {
            StringBuilder error = new StringBuilder();
            error.append("Lỗi kết nối database:\n");
            error.append("• Mã lỗi: ").append(e.getErrorCode()).append("\n");
            error.append("• Trạng thái SQL: ").append(e.getSQLState()).append("\n");
            error.append("• Thông báo: ").append(e.getMessage()).append("\n");
            
            return error.toString();
        }
    }
}
