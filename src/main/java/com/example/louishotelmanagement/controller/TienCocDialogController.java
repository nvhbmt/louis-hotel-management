package com.example.louishotelmanagement.controller;// package com.example.louishotelmanagement.controller;
// File: TienCocDialogController.java

import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox; 
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image; 
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class TienCocDialogController implements Initializable {

    @FXML
    private Label lblTongTienPhong;
    @FXML
    private Label lblTienCocDeXuat;
    @FXML
    private TextField txtTienCocThucTe;
    @FXML
    private ComboBox<String> cboPhuongThucTT; 
    @FXML
    private VBox vbQrCodeContainer; 
    @FXML
    private ImageView imgQrCode; 
    @FXML
    private Button btnXacNhan;
    @FXML
    private Button btnHuy;

    private BigDecimal tongTienPhong;
    private BigDecimal tienCocDeXuat;
    private BigDecimal tienCocThucTe;
    private String phuongThucTT; 
    private boolean confirmed = false;

    private static final DecimalFormat currencyFormat = new DecimalFormat("#,##0 VND");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khởi tạo ComboBox
        cboPhuongThucTT.getItems().addAll("Tiền mặt", "Chuyển khoản");
        cboPhuongThucTT.getSelectionModel().selectFirst(); // Mặc định chọn Tiền mặt

        // Cài đặt listener để ẩn/hiện QR Code
        cboPhuongThucTT.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isChuyenKhoan = "Chuyển khoản".equals(newVal);
            vbQrCodeContainer.setVisible(isChuyenKhoan);
            vbQrCodeContainer.setManaged(isChuyenKhoan);
        });

        // Mặc định ẩn container QR (vì Tiền mặt được chọn)
        vbQrCodeContainer.setVisible(false);
        vbQrCodeContainer.setManaged(false);

        // Nạp ảnh QR (Đảm bảo đường dẫn này là chính xác!)
        try {
            // Đảm bảo bạn đã đặt file 'qr_code.png' trong thư mục resources/images
            Image qrImage = new Image(getClass().getResourceAsStream("/com/example/louishotelmanagement/image/QR.jpg"));
            imgQrCode.setImage(qrImage);
        } catch (Exception e) {
            System.err.println("Lỗi tải hình ảnh QR Code: " + e.getMessage());
            imgQrCode.setImage(null);
        }
    }

    public void setTongTien(double tongTien) {
        this.tongTienPhong = BigDecimal.valueOf(tongTien);
        this.tienCocDeXuat = this.tongTienPhong.multiply(new BigDecimal("0.2"));

        lblTongTienPhong.setText(currencyFormat.format(tongTienPhong));
        lblTienCocDeXuat.setText(currencyFormat.format(tienCocDeXuat));
        txtTienCocThucTe.setText(tienCocDeXuat.toPlainString());
    }

    // Getter mới cho Phương thức thanh toán
    public String getPhuongThucTT() {
        return phuongThucTT;
    }

    public BigDecimal getTienCoc() {
        return tienCocThucTe;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    @FXML
    private void handleXacNhan() {
        try {
            // 1. Kiểm tra và lấy tiền cọc
            String input = txtTienCocThucTe.getText().replace(",", "");
            this.tienCocThucTe = new BigDecimal(input);

            if (tienCocThucTe.compareTo(BigDecimal.ZERO) < 0) {
                ThongBaoUtil.hienThiLoi("Lỗi", "Tiền cọc không được là số âm.");
                return;
            }

            // 2. Lấy Phương thức thanh toán
            this.phuongThucTT = cboPhuongThucTT.getValue();
            if (this.phuongThucTT == null) {
                ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn phương thức thanh toán.");
                return;
            }

            confirmed = true;
            closeStage();
        } catch (NumberFormatException e) {
            ThongBaoUtil.hienThiLoi("Lỗi nhập liệu", "Vui lòng nhập số tiền cọc hợp lệ.");
        }
    }

    @FXML
    private void handleHuy() {
        confirmed = false;
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }
}