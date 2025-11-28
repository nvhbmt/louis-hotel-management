package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class ThanhToanDialogController {

    @FXML
    private TextField txtMaKhachHang;
    @FXML
    private TextField txtHoTen;
    @FXML
    private TextField txtSoDienThoai;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtHangkhach;
    @FXML
    private TextField txtSoPhong;
    @FXML
    private TextField txtNgayNhan;
    @FXML
    private TextField txtNgayTra;
    @FXML
    private TextField txtMaGiamGia;

    @FXML
    private RadioButton rbtnTienMat;
    @FXML
    private RadioButton rbtnTheNganHang;


    @FXML
    private Button btnThanhToan;
    @FXML
    private Button btnHuy;
    @FXML
    private Button btnLamMoi;

    @FXML
    private Label lblTongTienPhong;
    @FXML
    private Label lblTongTienDichVu;
    @FXML
    private Label lblVat;
    @FXML
    private Label lblTongGiamGia;
    @FXML
    private Label lblTongThanhToan;

    private CTHoaDonDichVuDAO cthddv;


    private HoaDon hoaDon;

    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final MaGiamGiaDAO maGiamGiaDAO = new MaGiamGiaDAO();
    private final HoaDonDAO2 hoaDonDAO2 = new HoaDonDAO2();
    private final CTHoaDonPhongDAO cthdPhongDAO = new CTHoaDonPhongDAO();
    private BigDecimal valTienPhong;
    private BigDecimal valTienDichVu;
    private BigDecimal valGiamGia;
    private BigDecimal valVAT;
    private BigDecimal valTongThanhToan;

    // Nhận hóa đơn từ controller trước
    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;

        try {
            // Tìm hóa đơn đầy đủ trong danh sách
            for (HoaDon hd : hoaDonDAO2.layDanhSachHoaDon()) {
                if (hd.getMaHD().equals(hoaDon.getMaHD())) {
                    this.hoaDon = hd;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        hienThiThongTinHoaDon();
        tinhTongThanhToan();
    }
    private void tinhTongThanhToan() {
        try {

            // Hàm này trả về double đổi sang BigDecimal
            double tongTienPhongDB = cthdPhongDAO.tinhTongTienPhongTheoHD(hoaDon.getMaHD());

            valTienPhong = BigDecimal.valueOf(tongTienPhongDB);

            // -----------------------------------------------------------

            valTienDichVu = cthddv.tinhTongTienDichVu(hoaDon.getMaHD());
            if (valTienDichVu == null) valTienDichVu = BigDecimal.ZERO;

            valGiamGia = BigDecimal.ZERO;
            if (hoaDon.getMaGG() != null && !hoaDon.getMaGG().isEmpty()) {
                MaGiamGia mgg = maGiamGiaDAO.layMaGiamGiaThepMa(hoaDon.getMaGG());
                if (mgg != null) {
                    if (mgg.getKieuGiamGia() == KieuGiamGia.PERCENT) {
                        // Giảm % trên tổng tiền phòng
                        valGiamGia = valTienPhong.multiply(BigDecimal.valueOf(mgg.getGiamGia() / 100.0));
                    } else if (mgg.getKieuGiamGia() == KieuGiamGia.AMOUNT) {
                        // Giảm số tiền cụ thể
                        valGiamGia = BigDecimal.valueOf(mgg.getGiamGia());
                    }
                }
            }

            BigDecimal tongTruocThue = valTienPhong.add(valTienDichVu).subtract(valGiamGia);

            if (tongTruocThue.compareTo(BigDecimal.ZERO) < 0) tongTruocThue = BigDecimal.ZERO;

            // Tính thuế
            valVAT = tongTruocThue.multiply(BigDecimal.valueOf(0.1));

            valTongThanhToan = tongTruocThue.add(valVAT);

            lblTongTienPhong.setText(String.format("%,.0f ₫", valTienPhong));
            lblTongTienDichVu.setText(String.format("%,.0f ₫", valTienDichVu));
            lblVat.setText(String.format("%,.0f ₫", valVAT));
            lblTongGiamGia.setText(String.format("%,.0f ₫", valGiamGia));

            lblTongThanhToan.setText(String.format("%,.0f ₫", valTongThanhToan));

        } catch (Exception e) {
            e.printStackTrace();
            ThongBaoUtil.hienThiLoi("Lỗi tính tiền", "Chi tiết: " + e.getMessage());
        }
    }


    // Hiển thị thông tin hóa đơn ra form
    private void hienThiThongTinHoaDon() {
        if (hoaDon == null) return;

        try {
            // Lấy thông tin khách hàng
            KhachHang kh = khachHangDAO.layKhachHangTheoMa(hoaDon.getMaKH());
            if (kh != null) {
                txtMaKhachHang.setText(kh.getMaKH());
                txtHoTen.setText(kh.getHoTen());
                txtSoDienThoai.setText(kh.getSoDT());
                txtEmail.setText(kh.getEmail());
                txtHangkhach.setText(kh.getHangKhach());
            }

            // Lấy thông tin phòng
            txtSoPhong.setText(hoaDon.getSoPhong() != null ? hoaDon.getSoPhong() : "N/A");
            txtNgayNhan.setText(hoaDon.getNgayLap() != null ? hoaDon.getNgayLap().toString() : "N/A");

            if (hoaDon.getNgayCheckOut() == null) {
                txtNgayTra.setText(java.time.LocalDate.now().toString());
            } else {
                txtNgayTra.setText(hoaDon.getNgayCheckOut().toString());
            }

            // Lấy mã giảm giá
            if (hoaDon.getMaGG() != null) {
                MaGiamGia mgg = maGiamGiaDAO.layMaGiamGiaThepMa(hoaDon.getMaGG());
                if (mgg != null)
                    txtMaGiamGia.setText(mgg.getCode());
            } else {
                txtMaGiamGia.setText("");
            }

            if (hoaDon.getPhuongThuc() != null) {
                switch (hoaDon.getPhuongThuc()) {
                    case TIEN_MAT -> rbtnTienMat.setSelected(true);
                    case CHUYEN_KHOAN -> rbtnTheNganHang.setSelected(true);
                    default -> {
                        rbtnTienMat.setSelected(false);
                        rbtnTheNganHang.setSelected(false);
                    }
                }
            } else {
                rbtnTienMat.setSelected(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void initialize() {
        ToggleGroup group = new ToggleGroup();
        rbtnTienMat.setToggleGroup(group);
        rbtnTheNganHang.setToggleGroup(group);
        // Vô hiệu hóa nút thanh toán lúc ban đầu
        btnThanhToan.setDisable(true);

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == rbtnTienMat) {
                btnThanhToan.setDisable(false);
            } else if (newValue == rbtnTheNganHang) {
                btnThanhToan.setDisable(true);

                moManHinhQRCode();

            } else {
                btnThanhToan.setDisable(true);
            }
        });
        cthddv = new CTHoaDonDichVuDAO();
        btnHuy.setOnAction(e -> dongForm());
        btnLamMoi.setOnAction(e -> lamMoiForm());
        btnThanhToan.setOnAction(e -> thanhToan());
    }
    private void moManHinhQRCode() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/ma-qr-view.fxml"));
            Parent parent = loader.load();
            QRController qrController = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Mã QR Thanh Toán");
            stage.setScene(new Scene(parent));

            stage.showAndWait();

            if (qrController.isTransactionConfirmed()) {
                btnThanhToan.setDisable(false);
                rbtnTienMat.setDisable(true);

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Xác nhận chuyển khoản thành công.");
                successAlert.setHeaderText(null);
                successAlert.showAndWait();
            } else {
                btnThanhToan.setDisable(true);
                rbtnTienMat.getToggleGroup().selectToggle(null);
            }

        } catch (IOException e) {
            e.printStackTrace();
            btnThanhToan.setDisable(true);
        }
    }
    private void dongForm() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }

    private void lamMoiForm() {
        txtMaGiamGia.clear();
        rbtnTienMat.setSelected(false);
        rbtnTheNganHang.setSelected(false);
    }
    private void thanhToan() {
        PhuongThucThanhToan phuongThuc;

        if(rbtnTienMat.isSelected()) {
            phuongThuc = PhuongThucThanhToan.TIEN_MAT;
        } else if (rbtnTheNganHang.isSelected()) {
            phuongThuc = PhuongThucThanhToan.CHUYEN_KHOAN;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn phương thức thanh toán trước khi xác nhận.");
            alert.showAndWait();
            return;
        }

        try {
            hoaDon.setPhuongThuc(phuongThuc);
            hoaDon.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);
            hoaDon.setNgayCheckOut(LocalDate.now());
            String tongThanhToanText = lblTongThanhToan.getText()
                    .replaceAll("[^0-9]", "");
            hoaDon.setTongTien(new BigDecimal(tongThanhToanText));
            hoaDonDAO2.capNhatHoaDon(hoaDon);

            ThongBaoUtil.hienThiThongBao("Thành công", "Thanh toán hóa đơn #" + hoaDon.getMaHD() + " đã hoàn tất.");

            dongForm();

        } catch (Exception e) {
            e.printStackTrace();
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể lưu thông tin thanh toán vào hệ thống.");
        }
    }

}
