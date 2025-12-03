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
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ThanhToanDialogController {

    public Label lblTienPhat;
    public Label lblTienCoc;
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

    private PhieuDatPhongDAO pdpDao = new PhieuDatPhongDAO();
    private CTHoaDonPhongDAO cthddpDao = new CTHoaDonPhongDAO();
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
            BigDecimal tienPhong = BigDecimal.ZERO;
            BigDecimal tienDichVu = BigDecimal.ZERO;
            BigDecimal vat =BigDecimal.ZERO;
            BigDecimal giamGia = BigDecimal.ZERO;
            BigDecimal tongThanhToan = BigDecimal.ZERO;
            BigDecimal tienPhat = BigDecimal.ZERO;
            BigDecimal tienCoc = BigDecimal.ZERO;

            // Khai báo tỷ lệ phạt và độ chính xác (Scaling)
            final BigDecimal TI_LE_PHAT = new BigDecimal("0.10"); // 10%
            final int SCALE = 2; // Làm tròn 2 chữ số thập phân (ví dụ: 0.00)

            List<CTHoaDonPhong> dsctHoaDonPhong = cthddpDao.getCTHoaDonPhongTheoMaHD(hoaDon.getMaHD());

            for(CTHoaDonPhong cthdp : dsctHoaDonPhong){
                // Lấy thông tin cần thiết
                PhieuDatPhong pdp = pdpDao.layPhieuDatPhongTheoMa(cthdp.getMaPhieu());
                tienCoc = pdp.getTienCoc();

                // Tiền gốc cần thanh toán
                BigDecimal tienGoc = cthdp.getThanhTien();

                if((pdp.getNgayDi().isAfter(LocalDate.now())) || (pdp.getNgayDi().isEqual(LocalDate.now()))){
                    // Trường hợp 1: Trả phòng đúng hạn (hoặc chưa đến hạn)
                    tienPhong = tienPhong.add(tienGoc);
                }else{
                    // Trường hợp 2: Trả phòng trễ (NgayDi đã qua LocalDate.now())

                    // 1. Tính số ngày trả trễ (ví dụ: so sánh NgayDi và ngày thanh toán/tính toán hiện tại)
                    // Lưu ý: Cần đảm bảo hàm này trả về số nguyên dương >= 1
                    long soNgayTraTre = tinhSoNgayTraTre(pdp.getNgayDi(), LocalDate.now());

                    // Chuyển số ngày sang BigDecimal để tính toán
                    BigDecimal soNgay = new BigDecimal(soNgayTraTre);

                    // 2. Tính Phí phạt: Tiền gốc * Tỷ lệ phạt * Số ngày trễ
                    // Phí phạt = tienGoc.multiply(TI_LE_PHAT).multiply(soNgay)
                    BigDecimal phiPhat = tienGoc
                            .multiply(TI_LE_PHAT)
                            .multiply(soNgay)
                            .setScale(SCALE, RoundingMode.HALF_UP); // Làm tròn phí phạt
                    lblTienPhat.setText(String.format("%,.0f ₫", phiPhat));
                    // 3. Tổng tiền phải trả = Tiền gốc + Phí phạt
                    BigDecimal tongTienPhaiTra = tienGoc.add(phiPhat);

                    // 4. Cập nhật tổng tiền phòng
                    tienPhong = tienPhong.add(tongTienPhaiTra);
                }
            }



            // Tính giảm giá nếu có mã giảm
            if (hoaDon.getMaGG() != null) {
                MaGiamGia mgg = maGiamGiaDAO.layMaGiamGiaThepMa(hoaDon.getMaGG());
                if (mgg != null) {
                    if (mgg.getKieuGiamGia() == KieuGiamGia.PERCENT) {
                        giamGia = tienPhong.multiply(BigDecimal.valueOf((mgg.getGiamGia() / 100.0)) ) ;
                    } else if (mgg.getKieuGiamGia() == KieuGiamGia.AMOUNT) {
                        giamGia = BigDecimal.valueOf(mgg.getGiamGia());
                    }
                }
            }
            tienDichVu = cthddv.tinhTongTienDichVu(hoaDon.getMaHD());
            // VAT 10% chỉ tính trên phần còn lại sau khi giảm giá
            BigDecimal baseAmount = tienPhong.subtract(giamGia);
            vat = baseAmount.multiply(new BigDecimal("0.1"));

            // Tổng thanh toán cuối cùng
            tongThanhToan = baseAmount.add(vat).add(tienDichVu).subtract(tienCoc);


            // Hiển thị lên UI
            lblTongTienPhong.setText(String.format("%,.0f ₫", tienPhong));
            lblTongTienDichVu.setText(String.format("%,.0f đ", tienDichVu));
            lblVat.setText(String.format("%,.0f ₫", vat));
            lblTongGiamGia.setText(String.format("%,.0f ₫", giamGia));
            lblTienCoc.setText(String.format("%,.0f ₫", tienCoc));
            lblTongThanhToan.setText(String.format("%,.0f ₫", tongThanhToan));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // -------------------------------------------------------------
    // Hàm mẫu để tính số ngày trả trễ (Cần đặt ngoài vòng lặp hoặc trong class)
    // Giả định: NgayDi là ngày phải trả phòng, ngayHienTai là ngày thanh toán
    public long tinhSoNgayTraTre(LocalDate ngayDi, LocalDate ngayHienTai) {
        // Chỉ tính ngày trễ nếu ngayHienTai là SAU ngayDi
        if (ngayHienTai.isAfter(ngayDi)) {
            // Cộng 1 ngày nếu bạn muốn ngày trễ đầu tiên là 1 (ví dụ: trễ 1 ngày)
            return java.time.temporal.ChronoUnit.DAYS.between(ngayDi, ngayHienTai);
        }
        return 0; // Không trễ
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

        // Logic xử lý khi chọn phương thức thanh toán
        // Logic xử lý khi chọn phương thức thanh toán
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == rbtnTienMat) {
                // Trường hợp 1: Tiền Mặt
                btnThanhToan.setDisable(false); // Kích hoạt ngay
                // anQRCode(); // Gọi hàm ẩn nếu có hiển thị QR trước đó
            } else if (newValue == rbtnTheNganHang) {
                // Trường hợp 2: Chuyển Khoản/Thẻ

                // 1. Vô hiệu hóa nút Thanh Toán (chỉ kích hoạt lại sau khi XÁC NHẬN QR)
                btnThanhToan.setDisable(true);

                // 2. Mở màn hình QR Code
                moManHinhQRCode();

                // LƯU Ý: Logic kích hoạt nút nằm bên trong moManHinhQRCode() sau khi nó đóng.
            } else {
                // Trường hợp 3: Không chọn gì
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
            // ... (Phần load FXML và stage giữ nguyên) ...
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/ma-qr-view.fxml"));
            Parent parent = loader.load();
            QRController qrController = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Mã QR Thanh Toán");
            stage.setScene(new Scene(parent));

            stage.showAndWait(); // Chờ cửa sổ QR đóng

            // === LOGIC KIỂM TRA KẾT QUẢ TỪ QR CONTROLLER ===
            if (qrController.isTransactionConfirmed()) {
                // CHỈ kích hoạt nút nếu người dùng đã Xác nhận
                btnThanhToan.setDisable(false);
                rbtnTienMat.setDisable(true);
                // Sử dụng ThongBaoUtil.hienThiThongBao nếu nó chấp nhận 2 tham số:
                // ThongBaoUtil.hienThiThongBao("Thành công", "Xác nhận chuyển khoản thành công. Vui lòng bấm THANH TOÁN để hoàn tất.");
                // Hoặc dùng Alert tiêu chuẩn nếu ThongBaoUtil không phù hợp:
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Xác nhận chuyển khoản thành công.");
                successAlert.setHeaderText(null);
                successAlert.showAndWait();
            } else {
                // Hủy giao dịch (hoặc đóng) -> Vô hiệu hóa nút và bỏ chọn RadioButton
                btnThanhToan.setDisable(true);
                // Quan trọng: Bỏ chọn phương thức chuyển khoản để buộc người dùng chọn lại
                rbtnTienMat.getToggleGroup().selectToggle(null);
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Thay thế ThongBaoUtil.hienThiLoi bằng phương thức phù hợp nếu cần:
            // ThongBaoUtil.hienThiLoi("Lỗi", "Không thể mở màn hình QR Code.");
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
        // 1. Kiểm tra lại phương thức thanh toán
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

        // 2. Cập nhật thông tin hóa đơn
        try {
            // Cập nhật các trường cần thiết
            hoaDon.setPhuongThuc(phuongThuc);
            hoaDon.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);
            hoaDon.setNgayCheckOut(LocalDate.now());
            String tongThanhToanText = lblTongThanhToan.getText()
                    .replaceAll("[^0-9]", ""); // Loại bỏ tất cả trừ số (0-9)
            // Chuyển chuỗi đã làm sạch thành BigDecimal
            hoaDon.setTongTien(new BigDecimal(tongThanhToanText));
            hoaDonDAO2.capNhatHoaDon(hoaDon);
            // **LƯU VÀO CƠ SỞ DỮ LIỆU (DAO)**
            // Giả sử HoaDonDAO2 có phương thức cập nhật
            // hoaDonDAO2.capNhatHoaDon(hoaDon);

            // 3. Thông báo thành công và đóng form
            ThongBaoUtil.hienThiThongBao("Thành công", "Thanh toán hóa đơn #" + hoaDon.getMaHD() + " đã hoàn tất.");

            // Đóng form thanh toán
            dongForm();

        } catch (Exception e) {
            e.printStackTrace();
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể lưu thông tin thanh toán vào hệ thống.");
        }
    }

    static void main() throws SQLException {
        String maCTHDP = "HD006";
        CTHoaDonPhongDAO cthoaDonPhongDAO = new CTHoaDonPhongDAO();
        PhieuDatPhongDAO phieuDatPhongDAO = new PhieuDatPhongDAO();
        List<CTHoaDonPhong> list = cthoaDonPhongDAO.getCTHoaDonPhongTheoMaHD(maCTHDP);
        for(CTHoaDonPhong cthoaDonPhong : list) {
            PhieuDatPhong pdp = phieuDatPhongDAO.layPhieuDatPhongTheoMa(cthoaDonPhong.getMaPhieu());
            System.out.println(cthoaDonPhong);
        }
    }
}
