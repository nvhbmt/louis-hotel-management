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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
public class TienCocDialogView {
    private Label lblTongTienPhong;
    private Label lblTienCocDeXuat;
    private TextField txtTienCocThucTe;
    private ComboBox cboPhuongThucTT;
    private VBox vbQrCodeContainer;
    private ImageView imgQrCode;
    private Button btnXacNhan;
    private Button btnHuy;
    private Parent root;

    public TienCocDialogView() {
        VBox vbox1 = new VBox(20.0);
        vbox1.setAlignment(Pos.CENTER);
        vbox1.getStyleClass().addAll("dialog-background");
        vbox1.getStylesheets().addAll(getClass().getResource("/com/example/louishotelmanagement/css/style.css").toExternalForm());
        vbox1.setPadding(new Insets(30.0, 30.0, 30.0, 30.0));
        Label label1 = new Label("Xác Nhận Tiền Cọc");
        label1.getStyleClass().addAll("dialog-title");

        VBox vbox2 = new VBox(10.0);
        vbox2.getStyleClass().addAll("info-panel");
        Label label2 = new Label("Thông tin tổng quan đặt phòng");
        label2.getStyleClass().addAll("section-title");

        HBox hbox1 = new HBox(15.0);
        hbox1.setAlignment(Pos.CENTER_LEFT);
        Label label3 = new Label("Tổng chi phí phòng (Ước tính):");
        label3.getStyleClass().addAll("info-label");
        HBox.setHgrow(label3, Priority.ALWAYS);
        lblTongTienPhong = new Label("0 VND");
        lblTongTienPhong.getStyleClass().addAll("total-amount-label");
        hbox1.getChildren().addAll(label3, lblTongTienPhong);

        HBox hbox2 = new HBox(15.0);
        hbox2.setAlignment(Pos.CENTER_LEFT);
        Label label4 = new Label("Tiền cọc đề xuất (20%):");
        label4.getStyleClass().addAll("info-label");
        HBox.setHgrow(label4, Priority.ALWAYS);
        lblTienCocDeXuat = new Label("0 VND");
        lblTienCocDeXuat.getStyleClass().addAll("deposit-suggest-label");
        hbox2.getChildren().addAll(label4, lblTienCocDeXuat);
        vbox2.getChildren().addAll(label2, hbox1, hbox2);

        VBox vbox3 = new VBox(10.0);
        vbox3.getStyleClass().addAll("input-panel");
        Label label5 = new Label("Số tiền cọc thực tế nhận:");
        label5.getStyleClass().addAll("section-title");
        txtTienCocThucTe = new TextField();
        txtTienCocThucTe.setPromptText("Nhập số tiền cọc thực tế...");
        txtTienCocThucTe.getStyleClass().addAll("deposit-input");
        Label label6 = new Label("Phương thức thanh toán cọc:");
        label6.getStyleClass().addAll("section-title");
        VBox.setVgrow(label6, Priority.ALWAYS);
        cboPhuongThucTT = new ComboBox();
        cboPhuongThucTT.setPrefWidth(200.0);
        vbox3.getChildren().addAll(label5, txtTienCocThucTe, label6, cboPhuongThucTT);

        vbQrCodeContainer = new VBox(10.0);
        vbQrCodeContainer.setAlignment(Pos.CENTER);
        vbQrCodeContainer.getStyleClass().addAll("qr-container");
        Label label7 = new Label("Quét mã QR để chuyển khoản");
        label7.getStyleClass().addAll("qr-label");
        imgQrCode = new ImageView();
        imgQrCode.setFitHeight(150.0);
        imgQrCode.setFitWidth(150.0);
        imgQrCode.setPickOnBounds(true);
        imgQrCode.setPreserveRatio(true);
        imgQrCode.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/QR.jpg").toExternalForm()));
        vbQrCodeContainer.getChildren().addAll(label7, imgQrCode);

        HBox hbox3 = new HBox(25.0);
        hbox3.setAlignment(Pos.CENTER);
        btnXacNhan = new Button("Xác Nhận");
        btnXacNhan.setMnemonicParsing(false);
        btnXacNhan.setPrefWidth(120.0);
        btnXacNhan.getStyleClass().addAll("btn-success", "action-button");
        btnXacNhan.setOnAction(e -> this.handleXacNhan(e));
        btnHuy = new Button("Hủy Bỏ");
        btnHuy.setMnemonicParsing(false);
        btnHuy.setPrefWidth(120.0);
        btnHuy.getStyleClass().addAll("btn-danger", "action-button");
        btnHuy.setOnAction(e -> this.handleHuy(e));
        hbox3.getChildren().addAll(btnXacNhan, btnHuy);
        vbox1.getChildren().addAll(label1, vbox2, vbox3, vbQrCodeContainer, hbox3);
        this.root = vbox1;
    }

    public Parent getRoot() {
        return root;
    }
    public Label getLblTongTienPhong() {
        return lblTongTienPhong;
    }
    public Label getLblTienCocDeXuat() {
        return lblTienCocDeXuat;
    }
    public TextField getTxtTienCocThucTe() {
        return txtTienCocThucTe;
    }
    public ComboBox getCboPhuongThucTT() {
        return cboPhuongThucTT;
    }
    public VBox getVbQrCodeContainer() {
        return vbQrCodeContainer;
    }
    public ImageView getImgQrCode() {
        return imgQrCode;
    }
    public Button getBtnXacNhan() {
        return btnXacNhan;
    }
    public Button getBtnHuy() {
        return btnHuy;
    }
    private void handleXacNhan(ActionEvent e) {
        // TODO: implement handler
    }
    private void handleHuy(ActionEvent e) {
        // TODO: implement handler
    }
}
