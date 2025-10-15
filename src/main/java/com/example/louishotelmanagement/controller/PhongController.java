package com.example.louishotelmanagement.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent; // Cần thiết để lấy Stage an toàn

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PhongController implements Initializable {

    // Khai báo các thành phần UI (đảm bảo fx:id trong FXML khớp chính xác)
    @FXML
    private Label tieuDeLabel;

    @FXML
    private Button btnNhanPhong, btnDatTT, btnDoiPhong, btnHuyDat, btnDichVu, btnThanhToan;

    // Lưu ý: Nút "Hủy đặt" trong FXML của bạn có fx:id="btnHuy", nên bạn cần sửa tên biến ở đây
    // Ví dụ: private Button btnHuy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Phong page initialized");
        // Ở đây có thể thêm logic load dữ liệu ban đầu cho ComboBox hoặc TableView
    }

    // 🔹 HÀM DÙNG CHUNG: Tái sử dụng Stage để chuyển Scene
    /**
     * Tải FXML mới và đặt Scene mới lên Stage hiện tại.
     * @param tenFXML Tên file FXML cần tải (ví dụ: "nhan-phong-view.fxml")
     * @param event ActionEvent từ nút bấm
     */
    private void moTrang(String tenFXML, ActionEvent event) {
        try {
            // Lấy Node (Nút) đã kích hoạt sự kiện
            javafx.scene.Node sourceNode = (javafx.scene.Node) event.getSource();

            // 1. Lấy Stage hiện tại một cách an toàn
            Stage stage = (Stage) sourceNode.getScene().getWindow();

            // 2. Tải FXML mới
            // Đảm bảo đường dẫn này khớp với nơi bạn lưu trữ file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/" + tenFXML));
            Parent root = loader.load();

            // 3. Gán Scene mới
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("❌ Không thể mở file FXML: " + tenFXML);
            e.printStackTrace();
        }
    }

    // 🔹 CÁC HÀM XỬ LÝ SỰ KIỆN NÚT BẤM (onAction)

    @FXML
    private void moNhanPhong(ActionEvent event) {
        moTrang("nhan-phong-view.fxml", event);
    }

    @FXML
    private void moDatTT(ActionEvent event) {
        // Giả định tên FXML cho Đặt tại quầy
        moTrang("dat-tai-quay-view.fxml", event);
    }

    @FXML
    private void moDoiPhong(ActionEvent event) {
        moTrang("doi-phong-view.fxml", event);
    }

    @FXML
    private void moHuyDat(ActionEvent event) {
        // Dùng tên FXML tương ứng với hủy đặt
        moTrang("huy-dat-view.fxml", event);
    }

    @FXML
    private void moDichVu(ActionEvent event) {
        moTrang("cung-cap-dich-vu.fxml", event); // Dùng FXML cho Cung cấp dịch vụ
    }

    @FXML
    private void moThanhToan(ActionEvent event) {
        moTrang("thanh-toan-view.fxml", event);
    }
}