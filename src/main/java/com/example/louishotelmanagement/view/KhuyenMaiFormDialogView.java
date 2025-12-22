package com.example.louishotelmanagement.view;
// Generated Java code from FXML
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
public class KhuyenMaiFormDialogView {
    private Label lblTieuDe;
    private VBox formContainer;
    private Button btnLuu;
    private Button btnHuy;
    private Parent root;

    public KhuyenMaiFormDialogView() {
        BorderPane borderpane1 = new BorderPane();
        borderpane1.setPrefHeight(566.0);
        borderpane1.setPrefWidth(700.0);
        borderpane1.getStylesheets().addAll(getClass().getResource("/com/example/louishotelmanagement/css/formsfx.css").toExternalForm());

        VBox vbox1 = new VBox(20.0);
        vbox1.setPrefHeight(457.0);
        vbox1.setPrefWidth(636.0);
        vbox1.getStyleClass().addAll("dialog-container");

        VBox vbox2 = new VBox();
        vbox2.getStyleClass().addAll("dialog-header");
        lblTieuDe = new Label("Thêm Mã Giảm Giá Mới");
        lblTieuDe.getStyleClass().addAll("dialog-title");
        Label label1 = new Label("Nhập thông tin mã giảm giá");
        label1.getStyleClass().addAll("dialog-subtitle");
        vbox2.getChildren().addAll(lblTieuDe, label1);

        formContainer = new VBox();

        // --- KHU VỰC CÁC NÚT BẤM (ĐÃ SỬA) ---
        HBox hbox1 = new HBox(10.0); // Thêm khoảng cách 10px giữa các nút
        hbox1.getStyleClass().addAll("dialog-buttons");

        btnHuy = new Button("Hủy");
        btnHuy.getStyleClass().addAll("btn-cancel");
        btnHuy.setOnAction(e -> this.handleHuy(e));

        btnLuu = new Button("Lưu");
        btnLuu.getStyleClass().addAll("btn-save");
        btnLuu.setOnAction(e -> this.handleLuu(e));

        // CHỈ GỌI addAll MỘT LẦN DUY NHẤT
        hbox1.getChildren().addAll(btnHuy, btnLuu);
        // ------------------------------------

        vbox1.getChildren().addAll(vbox2, formContainer, hbox1);
        borderpane1.setCenter(vbox1);
        borderpane1.setPadding(new Insets(32.0, 32.0, 32.0, 32.0));
        this.root = borderpane1;
    }

    public Parent getRoot() {
        return root;
    }
    public Label getLblTieuDe() {
        return lblTieuDe;
    }
    public VBox getFormContainer() {
        return formContainer;
    }
    public Button getBtnLuu() {
        return btnLuu;
    }
    public Button getBtnHuy() {
        return btnHuy;
    }
    private void handleHuy(ActionEvent e) {
        // TODO: implement handler
    }
    private void handleLuu(ActionEvent e) {
        // TODO: implement handler
    }
}
