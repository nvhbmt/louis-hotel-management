package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.HoaDonDAO2;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.MaGiamGiaDAO;
import com.example.louishotelmanagement.model.HoaDon;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.model.KieuGiamGia;
import com.example.louishotelmanagement.model.MaGiamGia;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class ThanhToanDialogController {

    @FXML private TextField txtMaKhachHang;
    @FXML private TextField txtHoTen;
    @FXML private TextField txtSoDienThoai;
    @FXML private TextField txtEmail;
    @FXML private TextField txtHangkhach;
    @FXML private TextField txtSoPhong;
    @FXML private TextField txtNgayNhan;
    @FXML private TextField txtNgayTra;
    @FXML private TextField txtMaGiamGia;

    @FXML private RadioButton rbtnTienMat;
    @FXML private RadioButton rbtnTheNganHang;
    @FXML private RadioButton rbtnViDienTu;

    @FXML private Button btnThanhToan;
    @FXML private Button btnHuy;
    @FXML private Button btnLamMoi;

    @FXML private Label lblTongTienPhong;
    @FXML private Label lblTongTienDichVu;
    @FXML private Label lblVat;
    @FXML private Label lblTongGiamGia;
    @FXML private Label lblTongThanhToan;


    private HoaDon hoaDon;

    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final MaGiamGiaDAO maGiamGiaDAO = new MaGiamGiaDAO();
    private final HoaDonDAO2 hoaDonDAO2 = new HoaDonDAO2();

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
            double tienPhong = 0.0;
            double tienDichVu = 0.0;
            double vat = 0.0;
            double giamGia = 0.0;
            double tongThanhToan = 0.0;

            // Lấy danh sách hóa đơn từ DAO (tìm hóa đơn theo mã phòng hoặc mã KH)
            for (HoaDon hd : hoaDonDAO2.layDanhSachHoaDon()) {
                if (hd.getSoPhong().equals(hoaDon.getSoPhong()) ||
                        hd.getMaKH().equals(hoaDon.getMaKH())) {
                    tienPhong = hd.getTongTien().doubleValue(); // Tổng tiền phòng gốc
                    break;
                }
            }

            // Tính giảm giá nếu có mã giảm
            if (hoaDon.getMaGG() != null) {
                MaGiamGia mgg = maGiamGiaDAO.layMaGiamGiaThepMa(hoaDon.getMaGG());
                if (mgg != null) {
                    if (mgg.getKieuGiamGia() == KieuGiamGia.PERCENT) {
                        giamGia = tienPhong * (mgg.getGiamGia() / 100.0);
                    } else if (mgg.getKieuGiamGia() == KieuGiamGia.AMOUNT) {
                        giamGia = mgg.getGiamGia();
                    }
                }
            }

            // VAT 10% chỉ tính trên phần còn lại sau khi giảm giá
            vat = (tienPhong - giamGia) * 0.1;

            // Tổng thanh toán cuối cùng
            tongThanhToan = tienPhong + tienDichVu + vat - giamGia;

            // Hiển thị lên UI
            lblTongTienPhong.setText(String.format("%,.0f ₫", tienPhong));
            lblTongTienDichVu.setText(String.format("%,.0f đ", tienDichVu));
            lblVat.setText(String.format("%,.0f ₫", vat));
            lblTongGiamGia.setText(String.format("%,.0f ₫", giamGia));
            lblTongThanhToan.setText(String.format("%,.0f ₫", tongThanhToan));

        } catch (Exception e) {
            e.printStackTrace();
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
                    case VI_DIEN_TU -> rbtnViDienTu.setSelected(true);
                    default -> {
                        rbtnTienMat.setSelected(false);
                        rbtnTheNganHang.setSelected(false);
                        rbtnViDienTu.setSelected(false);
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
        rbtnViDienTu.setToggleGroup(group);

        btnHuy.setOnAction(e -> dongForm());
        btnLamMoi.setOnAction(e -> lamMoiForm());
    }

    private void dongForm() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }

    private void lamMoiForm() {
        txtMaGiamGia.clear();
        rbtnTienMat.setSelected(false);
        rbtnTheNganHang.setSelected(false);
        rbtnViDienTu.setSelected(false);
    }
}
