package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.CTHoaDonPhongDAO;
import com.example.louishotelmanagement.dao.PhieuDatPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PhieuDatPhongFormDialogController implements Initializable {

    // ===== FORM =====
    @FXML private TextField txtMaPhieu;
    @FXML private DatePicker dpNgayDen;
    @FXML private DatePicker dpNgayDi;
    @FXML private TextField txtTienCocCu;

    // ===== TABLE =====
    @FXML private TableView<CTHoaDonPhong> tblPhong;
    @FXML private TableColumn<CTHoaDonPhong, String> colMaPhong;
    @FXML private TableColumn<CTHoaDonPhong, LocalDate> colNgayDen;
    @FXML private TableColumn<CTHoaDonPhong, LocalDate> colNgayDi;
    @FXML private TableColumn<CTHoaDonPhong, BigDecimal> colGiaPhong;
    @FXML private TableColumn<CTHoaDonPhong, Void> colAction;

    // ===== THANH TOÁN =====
    @FXML private HBox boxThanhToan;
    @FXML private ComboBox<PhuongThucThanhToan> cbThanhToan;
    @FXML private Label lblSoTienThuThem;

    private final PhieuDatPhongDAO phieuDatPhongDAO = new PhieuDatPhongDAO();
    private final CTHoaDonPhongDAO ctHoaDonPhongDAO = new CTHoaDonPhongDAO();
    private PhongDAO phongDAO = new PhongDAO();

    private PhieuDatPhong phieuDatPhongHienTai;
    private BigDecimal soTienCanThuThem = BigDecimal.ZERO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        phongDAO = new PhongDAO();
        cbThanhToan.setItems(FXCollections.observableArrayList(
                PhuongThucThanhToan.TIEN_MAT,
                PhuongThucThanhToan.CHUYEN_KHOAN
        ));
        boxThanhToan.setVisible(false);
    }

    // ================= INIT DATA =================
    public void setPhieuDatPhong(PhieuDatPhong pdp) throws SQLException {
        this.phieuDatPhongHienTai = pdp;
        loadDuLieu();
    }

    private void loadDuLieu() throws SQLException {
        txtMaPhieu.setText(phieuDatPhongHienTai.getMaPhieu());
        dpNgayDen.setValue(phieuDatPhongHienTai.getNgayDen());
        dpNgayDi.setValue(phieuDatPhongHienTai.getNgayDi());
        txtTienCocCu.setText(formatTien(phieuDatPhongHienTai.getTienCoc()));

        colMaPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        colNgayDen.setCellValueFactory(new PropertyValueFactory<>("ngayDen"));
        colNgayDi.setCellValueFactory(new PropertyValueFactory<>("ngayDi"));
        colGiaPhong.setCellValueFactory(new PropertyValueFactory<>("giaPhong"));

        themNutDoiPhong();

        tblPhong.setItems(FXCollections.observableArrayList(
                ctHoaDonPhongDAO.getCTHoaDonPhongTheoMaPhieu(
                        phieuDatPhongHienTai.getMaPhieu()
                )
        ));
    }

    // ================= TABLE ACTION =================
    private void themNutDoiPhong() {
        colAction.setCellFactory(col -> new TableCell<>() {

            private final Button btnDoi = new Button("Đổi");
            private final Button btnXoa = new Button("Xóa");
            private final HBox box = new HBox(5, btnDoi, btnXoa);

            {
                btnDoi.setOnAction(e -> {
                    CTHoaDonPhong ct =
                            getTableView().getItems().get(getIndex());
                    xuLyDoiPhong(ct);
                });

                btnXoa.setOnAction(e -> {
                    CTHoaDonPhong ct =
                            getTableView().getItems().get(getIndex());
                    xuLyXoaPhong(ct);
                });

                // (tùy chọn) style nhẹ
                btnXoa.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void xuLyXoaPhong(CTHoaDonPhong ct) {
        try {
            // Lấy danh sách phòng còn hiệu lực
            var danhSachPhong = ctHoaDonPhongDAO
                    .getCTHoaDonPhongTheoMaPhieu(phieuDatPhongHienTai.getMaPhieu())
                    .stream()
                    .filter(CTHoaDonPhong::isConHieuLuc)
                    .toList();

            if (danhSachPhong.size() <= 1) {
                ThongBaoUtil.hienThiCanhBao(
                        "Không thể xóa",
                        "Phiếu đặt phòng phải có ít nhất 1 phòng"
                );
                return;
            }

            boolean dongY = ThongBaoUtil.hienThiXacNhan(
                    "Xác nhận",
                    "Bạn có chắc muốn hủy phòng " + ct.getMaPhong() + " ?"
            );

            if (!dongY) return;

            boolean thanhCong = ctHoaDonPhongDAO.huyCTHoaDonPhong(
                    ct.getMaHD(),
                    ct.getMaPhong()
            );


            if (!thanhCong) {
                ThongBaoUtil.hienThiCanhBao(
                        "Không thành công",
                        "Phòng đã được hủy trước đó"
                );
                return;
            }

            phongDAO.capNhatTrangThaiPhong(
                    ct.getMaPhong(),
                    TrangThaiPhong.TRONG.toString()
            );


            // cập nhật local object (cho UI)
            ct.huyPhong();

            loadDuLieu();

            ThongBaoUtil.hienThiThongBao(
                    "Thành công",
                    "Đã hủy phòng " + ct.getMaPhong()
            );

        } catch (Exception e) {
            ThongBaoUtil.hienThiLoi("Lỗi", e.getMessage());
        }
    }



    private BigDecimal tinhTongTienTuCTHDP() throws SQLException {
        long soDem = java.time.temporal.ChronoUnit.DAYS.between(
                phieuDatPhongHienTai.getNgayDen(),
                phieuDatPhongHienTai.getNgayDi()
        );

        if (soDem <= 0) return BigDecimal.ZERO;

        BigDecimal tongTien = BigDecimal.ZERO;

        // Load lại CTHDP sau khi đã đổi phòng
        var dsCTHDP = ctHoaDonPhongDAO.getCTHoaDonPhongTheoMaPhieu(
                phieuDatPhongHienTai.getMaPhieu()
        );

        for (CTHoaDonPhong ct : dsCTHDP) {
            if (ct.getGiaPhong() != null) {
                tongTien = tongTien.add(
                        ct.getGiaPhong().multiply(BigDecimal.valueOf(soDem))
                );
            }
        }

        return tongTien;
    }


    private void xuLyDoiPhong(CTHoaDonPhong ct) {
        try {
            String maPhongCu = ct.getMaPhong();
            Phong phongMoi = DialogChonPhong();
            if (phongMoi == null) return;

            // 1. Update CTHDP trong DB
            ctHoaDonPhongDAO.capNhatMaPhongVaGia(
                    ct.getMaPhieu(),
                    ct.getMaPhong(),
                    phongMoi.getMaPhong(),
                    BigDecimal.valueOf(phongMoi.getLoaiPhong().getDonGia())
            );

            // 2. Tính lại tổng tiền từ CTHDP
            BigDecimal tongTienMoi = tinhTongTienTuCTHDP();

            // 3. Tính tiền cọc mới = 20%
            BigDecimal tienCocMoi = tongTienMoi.multiply(BigDecimal.valueOf(0.2));

            // 4. So với tiền cọc cũ
            BigDecimal tienCocCu = phieuDatPhongHienTai.getTienCoc();
            BigDecimal chenhLech = tienCocMoi.subtract(tienCocCu);

            if (chenhLech.compareTo(BigDecimal.ZERO) > 0) {
                soTienCanThuThem = chenhLech;
                lblSoTienThuThem.setText(formatTien(chenhLech));
                boxThanhToan.setVisible(true);
            } else {
                boxThanhToan.setVisible(false);
            }

            // 5. Update tiền cọc trong object
            phieuDatPhongHienTai.setTienCoc(tienCocMoi);
            txtTienCocCu.setText(formatTien(tienCocMoi));

            // Phòng cũ → trống
            phongDAO.capNhatTrangThaiPhong(
                    maPhongCu,
                    TrangThaiPhong.TRONG.toString()
            );

            // Phòng mới → đang được đặt
            phongDAO.capNhatTrangThaiPhong(
                    phongMoi.getMaPhong(),
                    TrangThaiPhong.DA_DAT.toString()
            );

            // 6. Load lại table
            loadDuLieu();

        } catch (Exception e) {
            ThongBaoUtil.hienThiLoi("Lỗi", e.getMessage());
        }
    }

    private Phong DialogChonPhong() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/example/louishotelmanagement/fxml/chon-phong-dialog.fxml"
                )
        );

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);

        DialogChonPhongController controller = loader.getController();
        controller.setDanhSachPhong(phongDAO.layDSPhongTrong());
        stage.showAndWait();

        Phong phongMoi = controller.getPhongDuocChon();
        if (phongMoi != null) {
            BigDecimal gia = BigDecimal.valueOf(phongMoi.getLoaiPhong().getDonGia());
            BigDecimal tienCoc = gia.multiply(BigDecimal.valueOf(0.2));
        }
        return phongMoi;
    }
    // ================= THANH TOÁN =================
    @FXML
    private void handleThanhToan() {
        if (cbThanhToan.getValue() == PhuongThucThanhToan.CHUYEN_KHOAN) {
            moManHinhQRCode();
        } else {
            xuLyThanhToan(PhuongThucThanhToan.TIEN_MAT);
        }
    }

    private void xuLyThanhToan(PhuongThucThanhToan pttt) {
        ThongBaoUtil.hienThiThongBao(
                "Thanh toán",
                "Đã thu thêm " + formatTien(soTienCanThuThem)
        );
        boxThanhToan.setVisible(false);
    }

    private void moManHinhQRCode() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/louishotelmanagement/fxml/ma-qr-view.fxml")
            );
            Parent parent = loader.load();
            QRController qrController = loader.getController();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Mã QR Thanh Toán");
            stage.setScene(new Scene(parent));
            stage.showAndWait();

            if (qrController.isTransactionConfirmed()) {
                xuLyThanhToan(PhuongThucThanhToan.CHUYEN_KHOAN);
            }

        } catch (IOException e) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể mở màn hình QR");
        }
    }

    // ================= SAVE =================
    @FXML
    private void handleLuu() {
        try {
            phieuDatPhongHienTai.setNgayDen(dpNgayDen.getValue());
            phieuDatPhongHienTai.setNgayDi(dpNgayDi.getValue());

            phieuDatPhongDAO.capNhatPhieuDatPhong(phieuDatPhongHienTai);

            ThongBaoUtil.hienThiThongBao("Thành công", "Đã cập nhật phiếu đặt phòng");
            ((Stage) txtMaPhieu.getScene().getWindow()).close();

        } catch (SQLException e) {
            ThongBaoUtil.hienThiLoi("Lỗi SQL", e.getMessage());
        }
    }

    @FXML
    private void handleHuy() {
        ((Stage) txtMaPhieu.getScene().getWindow()).close();
    }

    private String formatTien(BigDecimal tien) {
        return String.format("%,.0f VNĐ", tien);
    }
}
