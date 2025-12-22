package com.example.louishotelmanagement.view;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
public class LoaiPhongFormDialogView {
    private Label lblTieuDe;
    private VBox formContainer;
    private Button btnHuy;
    private Button btnLuu;
    private Parent root;

    public LoaiPhongFormDialogView() {
        BorderPane borderpane1 = new BorderPane();
        borderpane1.setPrefHeight(202.0);
        borderpane1.setPrefWidth(602.0);
        borderpane1.getStylesheets().addAll(getClass().getResource("/com/example/louishotelmanagement/css/formsfx.css").toExternalForm());

        VBox vbox1 = new VBox(20.0);
        vbox1.setPrefHeight(138.0);
        vbox1.setPrefWidth(369.0);
        vbox1.getStyleClass().addAll("dialog-container");
        vbox1.setPadding(new Insets(32.0, 32.0, 32.0, 32.0));

        VBox vbox2 = new VBox();
        vbox2.getStyleClass().addAll("dialog-header");
        lblTieuDe = new Label("Thêm Loại Phòng Mới");
        lblTieuDe.getStyleClass().addAll("dialog-title");
        Label label1 = new Label("Nhập thông tin loại phòng");
        label1.getStyleClass().addAll("dialog-subtitle");
        vbox2.getChildren().addAll(lblTieuDe, label1);

        formContainer = new VBox();

        HBox hbox1 = new HBox(10.0); // Thêm spacing giữa 2 nút
        hbox1.setPrefHeight(36.0);
        hbox1.setPrefWidth(2538.0); // Giữ nguyên thông số của bạn
        hbox1.getStyleClass().addAll("dialog-buttons");

        btnHuy = new Button("Hủy");
        btnHuy.getStyleClass().addAll("btn-cancel");
        btnHuy.setOnAction(e -> this.handleHuy(e));

        btnLuu = new Button("Lưu");
        btnLuu.getStyleClass().addAll("btn-save");
        btnLuu.setOnAction(e -> this.handleLuu(e));

        hbox1.getChildren().addAll(btnHuy, btnLuu);

        // SỬA LỖI TẠI ĐÂY:
        // Không add borderpane.margin1 vào getChildren()
        vbox1.getChildren().addAll(vbox2, formContainer, hbox1);

        borderpane1.setCenter(vbox1);

        // Nếu bạn muốn đặt Margin cho vbox1 bên trong BorderPane:
        BorderPane.setMargin(vbox1, new Insets(0, 0, 0, 0));

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
    public Button getBtnHuy() {
        return btnHuy;
    }
    public Button getBtnLuu() {
        return btnLuu;
    }
    private void handleHuy(ActionEvent e) {
        // TODO: implement handler
    }
    private void handleLuu(ActionEvent e) {
        // TODO: implement handler
    }
}
