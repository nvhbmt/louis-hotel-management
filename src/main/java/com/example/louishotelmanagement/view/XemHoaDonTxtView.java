package com.example.louishotelmanagement.view;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.event.ActionEvent;
public class XemHoaDonTxtView {
    private TextArea txtHoaDon;
    private Button btnInHoaDon;
    private Button btnDong;
    private Parent root;

    public XemHoaDonTxtView() {
        VBox vbox1 = new VBox(10.0);
        vbox1.setAlignment(Pos.TOP_CENTER);
        vbox1.setPrefHeight(600.0);
        vbox1.setPrefWidth(650.0);

        // Thêm CSS
        vbox1.getStylesheets().addAll(getClass().getResource("/com/example/louishotelmanagement/css/xem-hoa-don-txt.css").toExternalForm());
        vbox1.setPadding(new Insets(15.0));

        // Cấu hình TextArea hiển thị nội dung hóa đơn
        txtHoaDon = new TextArea();
        txtHoaDon.setEditable(false); // Khách hàng chỉ xem, không được sửa
        txtHoaDon.setPrefHeight(530.0);
        txtHoaDon.setPrefWidth(620.0);

        // QUAN TRỌNG: Thiết lập font Monospaced để các cột trong hóa đơn thẳng hàng
        txtHoaDon.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 13px;");

        VBox.setVgrow(txtHoaDon, Priority.ALWAYS);

        // HBox chứa các nút bấm
        HBox hbox1 = new HBox(15.0);
        hbox1.setAlignment(Pos.CENTER_RIGHT);
        hbox1.setPadding(new Insets(5, 0, 0, 0));

        // SỬA TẠI ĐÂY: Gán trực tiếp vào biến của lớp (không khai báo lại kiểu Button)
        btnInHoaDon = new Button("🖨️ In Hóa Đơn");
        btnInHoaDon.setPrefHeight(35.0);
        btnInHoaDon.setPrefWidth(120.0);
        btnInHoaDon.getStyleClass().add("print-button");

        btnDong = new Button("Đóng");
        btnDong.setPrefHeight(35.0);
        btnDong.setPrefWidth(80.0);
        btnDong.getStyleClass().add("btn-secondary");

        // Gán sự kiện (Controller sẽ ghi đè lại nếu cần)
        btnInHoaDon.setOnAction(this::handleIn);
        btnDong.setOnAction(this::handleDong);

        hbox1.getChildren().addAll(btnInHoaDon, btnDong);
        vbox1.getChildren().addAll(txtHoaDon, hbox1);

        this.root = vbox1;
    }

    public Parent getRoot() {
        return root;
    }
    public TextArea getTxtHoaDon() {
        return txtHoaDon;
    }
    public Button getBtnInHoaDon() {
        return btnInHoaDon;
    }
    public Button getBtnDong() {
        return btnDong;
    }
    private void handleIn(ActionEvent e) {
        // TODO: implement handler
    }
    private void handleDong(ActionEvent e) {
        // TODO: implement handler
    }
}
