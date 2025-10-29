package com.example.louishotelmanagement.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class QRController {

    private boolean transactionConfirmed = false; // Mặc định là HỦY

    @FXML
    private Button btnXacNhan;
    @FXML
    private Button btnHuy;

    // Phương thức getter để ThanhToanDialogController lấy kết quả
    public boolean isTransactionConfirmed() {
        return transactionConfirmed;
    }

    @FXML
    private void handleXacNhan() {
        transactionConfirmed = true; // Đánh dấu đã xác nhận

        // Hiển thị thông báo thành công (tùy chọn)
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Giao dịch thanh toán đã được xác nhận thành công!");
        alert.showAndWait();

        // Đóng cửa sổ
        Stage stage = (Stage) btnXacNhan.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleHuy() {
        transactionConfirmed = false; // Giữ nguyên trạng thái hủy

        // Hiển thị cảnh báo hủy giao dịch
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo");
        alert.setHeaderText("Hủy giao dịch");
        alert.setContentText("Giao dịch thanh toán đã bị hủy bởi người dùng.");
        alert.showAndWait();

        // Đóng cửa sổ
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }
}