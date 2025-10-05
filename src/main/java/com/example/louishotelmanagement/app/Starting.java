package com.example.louishotelmanagement.app;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class Starting extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Kiểm tra kết nối database trước khi khởi tạo UI
        if (!initializeDatabase()) {
            showDatabaseErrorDialog();
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Starting.class.getResource("/com/example/louishotelmanagement/fxml/layout-chinh.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Chương trình quản lí khách sạn Louis");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(600);
        stage.show();
    }
    
    /**
     * Khởi tạo kết nối database
     * @return true nếu kết nối thành công, false nếu thất bại
     */
    private boolean initializeDatabase() {
        System.out.println("Đang khởi tạo ứng dụng...");
        System.out.println("Kiểm tra kết nối database...");
        
        boolean isConnected = CauHinhDatabase.testConnection();
        
        if (isConnected) {
            System.out.println("Ứng dụng sẵn sàng hoạt động!");
        } else {
            System.err.println("Không thể khởi tạo ứng dụng do lỗi database");
        }
        
        return isConnected;
    }
    
    /**
     * Hiển thị dialog thông báo lỗi kết nối database
     */
    private void showDatabaseErrorDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi Kết Nối Database");
            alert.setHeaderText("Không thể kết nối đến cơ sở dữ liệu");
            
            // Lấy thông tin chi tiết lỗi
            String errorDetails = CauHinhDatabase.getConnectionErrorDetails();
            
            alert.setContentText(
                "Ứng dụng không thể kết nối đến SQL Server.\n\n" +
                "Chi tiết lỗi:\n" + errorDetails + "\n\n" +
                "Vui lòng kiểm tra:\n" +
                "• SQL Server JDBC Driver đã được cài đặt (mssql-jdbc)\n" +
                "• SQL Server đã được khởi động\n" +
                "• Database 'QuanLyKhachSan' đã được tạo\n" +
                "• Thông tin kết nối trong CauHinhDatabase.java\n" +
                "• Port 1433 có thể truy cập\n\n" +
                "Hướng dẫn cài đặt JDBC Driver:\n" +
                "1. Mở pom.xml\n" +
                "2. Thêm dependency mssql-jdbc\n" +
                "3. Refresh Maven project\n\n" +
                "Ứng dụng sẽ thoát sau khi bạn nhấn OK."
            );
            
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}