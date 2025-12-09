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
    private boolean needNavigateToTraPhong = false;

    // Khai báo các biến lưu trữ kết quả tính toán cuối cùng (ở cấp độ Controller)
    private BigDecimal finalTienPhat = BigDecimal.ZERO;
    private BigDecimal finalGiamGia = BigDecimal.ZERO;
    private BigDecimal finalVat = BigDecimal.ZERO;

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
    // Getter mới
    public boolean isNeedNavigateToTraPhong() {
        return needNavigateToTraPhong;
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
            final BigDecimal TI_LE_PHAT_TRA_TRE = new BigDecimal("0.10"); // 10%
            final BigDecimal TI_LE_PHAT_TRA_SOM = new BigDecimal("0.30");
            final BigDecimal TI_LE_PHAT_NHAN_PHONG_TRE = new BigDecimal("0.50");
            final int SCALE = 2; // Làm tròn 2 chữ số thập phân (ví dụ: 0.00)
            final int SCALE_DISPLAY = 0; // Làm tròn 0 chữ số cho hiển thị
            BigDecimal tongTienPhaiTra = BigDecimal.ZERO;
            List<CTHoaDonPhong> dsctHoaDonPhong = cthddpDao.getCTHoaDonPhongTheoMaHD(hoaDon.getMaHD());

            for(CTHoaDonPhong cthdp : dsctHoaDonPhong){
                // Lấy thông tin cần thiết
                PhieuDatPhong pdp = pdpDao.layPhieuDatPhongTheoMa(cthdp.getMaPhieu());

                // Tiền cọc (Giả định lấy từ phiếu đầu tiên)
                if (pdp != null && pdp.getTienCoc() != null) {
                    tienCoc = pdp.getTienCoc();
                }

                // Tiền gốc cần thanh toán
                BigDecimal tienGoc = cthdp.getThanhTien();

                BigDecimal phiPhatNhanTre = BigDecimal.ZERO;
                if (pdp != null && cthdp.getNgayDen().isAfter(pdp.getNgayDen())) {
                    // Tính số ngày nhận phòng trễ
                    long soNgayNhanTre = tinhSoNgayTraTre(pdp.getNgayDen(), cthdp.getNgayDen());
                    BigDecimal soNgay = new BigDecimal(soNgayNhanTre);

                    // Lấy giá phòng một ngày (Giả định cthdp.getThanhTien() là tổng tiền,
                    // bạn cần chia cho số ngày để có giá 1 ngày)
                    // Cần đảm bảo hàm tinhSoNgayTraTre() trả về số ngày đúng, hoặc lấy số ngày đặt từ PhieuDatPhong

                    // GIẢ ĐỊNH: cthdp.getGiaPhong() là giá phòng 1 đêm
                    BigDecimal giaPhongMotDem = cthdp.getGiaPhong();

                    phiPhatNhanTre = giaPhongMotDem
                            .multiply(TI_LE_PHAT_NHAN_PHONG_TRE) // 50%
                            .multiply(soNgay)
                            .setScale(SCALE, RoundingMode.HALF_UP);

                    tienPhat = tienPhat.add(phiPhatNhanTre); // Cộng dồn vào tiền phạt chung
                }

                tienPhong = tienPhong.add(tienGoc);
                if(cthdp.getNgayDi().isAfter(LocalDate.now())){
                    long soNgayTraSom = tinhSoNgayTraTre(LocalDate.now(), cthdp.getNgayDi());
                    BigDecimal soNgay = new BigDecimal(soNgayTraSom);
                    // 2. Tính Phí phạt
                    BigDecimal phiPhat = tienGoc
                            .multiply(TI_LE_PHAT_TRA_SOM)
                            .multiply(soNgay)
                            .setScale(SCALE, RoundingMode.HALF_UP);

                    tienPhat = tienPhat.add(phiPhat); // Cộng dồn tiền phạt

                    // 3. Tổng tiền phải trả = Tiền gốc + Phí phạt
                    tongTienPhaiTra = tienPhong.add(phiPhat);

                }else {
                    // Trường hợp 2: Trả phòng trễ (NgayDi đã qua LocalDate.now())
                    long soNgayTraTre = tinhSoNgayTraTre(cthdp.getNgayDi(), LocalDate.now());
                    BigDecimal soNgay = new BigDecimal(soNgayTraTre);

                    // 2. Tính Phí phạt
                    BigDecimal phiPhat = tienGoc
                            .multiply(TI_LE_PHAT_TRA_TRE)
                            .multiply(soNgay)
                            .setScale(SCALE, RoundingMode.HALF_UP);

                    tienPhat = tienPhat.add(phiPhat); // Cộng dồn tiền phạt

                    // 3. Tổng tiền phải trả = Tiền gốc + Phí phạt
                    tongTienPhaiTra = tienPhong.add(phiPhat);

                }
            }
            // Hiển thị Tiền phạt đã tính
            lblTienPhat.setText(String.format("%,." + SCALE_DISPLAY + "f ₫", tienPhat));

            // Tính giảm giá nếu có mã giảm
            BigDecimal tienPhongGocChoGG = tongTienPhaiTra.subtract(tienPhat); // Tiền phòng gốc (trước phạt)
            if (hoaDon.getMaGG() != null) {
                MaGiamGia mgg = maGiamGiaDAO.layMaGiamGiaThepMa(hoaDon.getMaGG());
                if (mgg != null) {
                    if (mgg.getKieuGiamGia() == KieuGiamGia.PERCENT) {
                        giamGia = tienPhongGocChoGG.multiply(BigDecimal.valueOf((mgg.getGiamGia() / 100.0)) ).setScale(SCALE, RoundingMode.HALF_UP) ;
                    } else if (mgg.getKieuGiamGia() == KieuGiamGia.AMOUNT) {
                        giamGia = BigDecimal.valueOf(mgg.getGiamGia()).setScale(SCALE, RoundingMode.HALF_UP);
                    }
                }
            }
            tienDichVu = cthddv.tinhTongTienDichVu(hoaDon.getMaHD()).setScale(SCALE, RoundingMode.HALF_UP);

            // VAT 10% chỉ tính trên phần còn lại sau khi giảm giá
            BigDecimal baseAmount = tongTienPhaiTra.add(tienDichVu).subtract(giamGia); // Tiền phòng(gốc+phạt) + Dịch vụ - GG Mã GG
            vat = baseAmount.multiply(new BigDecimal("0.1")).setScale(SCALE, RoundingMode.HALF_UP);

            // Tổng tạm thời (trước khi trừ GG Hạng Khách và Cọc)
            tongThanhToan = baseAmount.add(vat);

            // Tính giảm giá theo hạng khách hàng
            BigDecimal tienHangkhach =  BigDecimal.ZERO;
            KhachHang kh = khachHangDAO.layKhachHangTheoMa(hoaDon.getMaKH());
            if (kh != null) {
                if(kh.getHangKhach().equals(HangKhach.KHACH_QUEN)){
                    tienHangkhach = tongThanhToan.multiply(BigDecimal.valueOf(0.05)).setScale(SCALE, RoundingMode.HALF_UP);
                }else if(kh.getHangKhach().equals(HangKhach.KHACH_DOANH_NGHIEP)){
                    tienHangkhach = tongThanhToan.multiply(BigDecimal.valueOf(0.1)).setScale(SCALE, RoundingMode.HALF_UP);
                }else if(kh.getHangKhach().equals(HangKhach.KHACH_VIP)){
                    tienHangkhach = tongThanhToan.multiply(BigDecimal.valueOf(0.15)).setScale(SCALE, RoundingMode.HALF_UP);
                }
            }

            // Tổng Giảm giá cuối cùng
            giamGia = giamGia.add(tienHangkhach);

            // Tổng Thanh Toán Cuối Cùng
            tongThanhToan = tongThanhToan.subtract(tienHangkhach).subtract(tienCoc);

            // Lưu trữ kết quả tính toán vào biến của Controller để dùng trong thanhToan()
            finalTienPhat = tienPhat;
            finalGiamGia = giamGia;
            finalVat = vat;

            // Cập nhật tongTien trong đối tượng hoaDon (không cập nhật DB ở đây)
            hoaDon.setTongTien(tongThanhToan.setScale(SCALE, RoundingMode.HALF_UP));

            // Hiển thị lên UI
            lblTongTienPhong.setText(String.format("%,." + SCALE_DISPLAY + "f ₫", tienPhong));
            lblTongTienDichVu.setText(String.format("%,." + SCALE_DISPLAY + "f ₫", tienDichVu));
            lblVat.setText(String.format("%,." + SCALE_DISPLAY + "f ₫", vat));
            lblTongGiamGia.setText(String.format("%,." + SCALE_DISPLAY + "f ₫", giamGia));
            lblTienCoc.setText(String.format("%,." + SCALE_DISPLAY + "f ₫", tienCoc));
            lblTongThanhToan.setText(String.format("%,." + SCALE_DISPLAY + "f ₫", tongThanhToan));

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
                txtHangkhach.setText(kh.getHangKhach().toString());
            }

            // Lấy thông tin phòng
            txtSoPhong.setText(hoaDon.getSoPhong() != null ? hoaDon.getSoPhong() : "N/A");
            txtNgayNhan.setText(hoaDon.getNgayLap() != null ? hoaDon.getNgayLap().toString() : "N/A");

            // Lấy Ngày trả phòng đã lưu (nếu có)
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
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == rbtnTienMat) {
                // Trường hợp 1: Tiền Mặt
                btnThanhToan.setDisable(false); // Kích hoạt ngay
            } else if (newValue == rbtnTheNganHang) {
                // Trường hợp 2: Chuyển Khoản/Thẻ
                btnThanhToan.setDisable(true);
                moManHinhQRCode();
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
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Xác nhận chuyển khoản thành công.");
                successAlert.setHeaderText(null);
                successAlert.showAndWait();
            } else {
                // Hủy giao dịch (hoặc đóng) -> Vô hiệu hóa nút và bỏ chọn RadioButton
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
        hoaDon.setMaGG(null);
        tinhTongThanhToan();
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
            // Lấy tổng tiền đã tính toán (đã lưu tạm trong hoaDon.tongTien)
            String tongThanhToanText = lblTongThanhToan.getText()
                    .replaceAll("[^0-9]", "");
            BigDecimal tongThanhToan = new BigDecimal(tongThanhToanText);

            // Cập nhật các trường cần thiết
            hoaDon.setPhuongThuc(phuongThuc);
            hoaDon.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);
            hoaDon.setNgayCheckOut(LocalDate.now());
            hoaDon.setTongTien(tongThanhToan);

            // ************ LƯU KẾT QUẢ TÍNH TOÁN CHI TIẾT VÀO HOA ĐƠN ************
            // Lấy kết quả từ các biến đã lưu trong Controller
            hoaDon.setTienPhat(finalTienPhat);
            hoaDon.setTongGiamGia(finalGiamGia);
            hoaDon.setTongVAT(finalVat);
            // *******************************************************************

            // Cập nhật vào cơ sở dữ liệu (DAO phải xử lý các trường mới)
            hoaDonDAO2.capNhatHoaDon(hoaDon);

            // 3. Thông báo thành công và đóng form
            ThongBaoUtil.hienThiThongBao("Thành công", "Thanh toán hóa đơn #" + hoaDon.getMaHD() + " đã hoàn tất.");
            // Đóng form thanh toán
            dongForm();

        } catch (Exception e) {
            e.printStackTrace();
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể lưu thông tin thanh toán vào hệ thống.");
        }
    }
    @FXML
    private void handleChonGiamGia() {
        try{
            // ... (Phần load FXML và stage giữ nguyên) ...
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/khuyen-mai-screen.fxml"));
            Parent parent = loader.load();
            ChonKhuyenMaiController khuyenMaiController = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Chọn mã giảm giá");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            MaGiamGia maDuocChon = khuyenMaiController.getMaGiamGiaDuocChon();

            if (maDuocChon != null) {
                // 3. Nếu có mã giảm giá được chọn, cập nhật vào TextField
                txtMaGiamGia.setText(maDuocChon.getMaGG());
                hoaDon.setMaGG(txtMaGiamGia.getText());
                tinhTongThanhToan();
            }

        }catch (IOException e){
            e.printStackTrace();
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
            System.out.println(cthoaDonPhong.tinhThanhTien());
        }
    }
}