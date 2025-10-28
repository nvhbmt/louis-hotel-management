package com.example.louishotelmanagement.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class XemHoaDonTxtController {

    @FXML
    private TextArea txtHoaDon;
    @FXML
    private Button btnInHoaDon;
    @FXML
    private Button btnDong;

    /**
     * Hàm này được gọi từ QuanLyHoaDonController để nhận nội dung TXT
     */
    public void initData(String noiDungHoaDon) {
        txtHoaDon.setText(noiDungHoaDon);
    }

    @FXML
    private void handleDong(ActionEvent event) {
        // Lấy cửa sổ (Stage) và đóng nó
        Stage stage = (Stage) btnDong.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleIn(ActionEvent event) {
        // Đây là nơi bạn sẽ thêm logic in ấn thực tế (dùng PrinterJob)

        System.out.println("--- Đang in nội dung ---");
        System.out.println(txtHoaDon.getText());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText("Chức năng đang phát triển");
        alert.setContentText("Logic in hóa đơn (dùng PrinterJob) sẽ được triển khai ở đây.");
        alert.showAndWait();
    }
}