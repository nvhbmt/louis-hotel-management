package com.example.louishotelmanagement.app;

import java.io.IOException;
import org.kordamp.bootstrapfx.BootstrapFX;
import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.view.DangNhapView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Starting extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Kiểm tra kết nối database trước khi khởi tạo UI
        if (!initializeDatabase()) {
            showDatabaseErrorDialog();
            return;
        }

        Scene scene = new Scene(new DangNhapView().getRoot(), 400, 800);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("Đăng nhập - Hệ thống quản lý khách sạn Louis");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        
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
            ThongBaoUtil.hienThiLoi("Lỗi Kết Nối Database", "Không thể kết nối đến cơ sở dữ liệu");
        });
    }

    public static void main(String[] args) {
        launch();
    }
}