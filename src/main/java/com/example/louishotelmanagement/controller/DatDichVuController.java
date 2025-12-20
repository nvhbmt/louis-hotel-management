package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DatDichVuController implements Initializable {

    public ComboBox dsKhachHang;
    public ComboBox dsPhong;
    public TextField maNV;
    public TextArea txtGhiChu;
    public Label lblTongTienTam;
    @FXML
    private FlowPane pnDanhSachDichVu; // Panel chứa các thẻ dịch vụ (Bên trái)
    @FXML
    private TextField txtTimKiem;

    @FXML
    private TableView<CTHoaDonDichVu> tblGioHang;
    @FXML
    private TableColumn<CTHoaDonDichVu, String> colGH_Ten;
    @FXML
    private TableColumn<CTHoaDonDichVu, Integer> colGH_SL;
    @FXML
    private TableColumn<CTHoaDonDichVu, String> colGH_ThanhTien;
    @FXML
    private TableColumn<CTHoaDonDichVu, Void> colGH_Xoa;



    public KhachHangDAO kDao;
    public PhongDAO pDao;
    public CTHoaDonDichVuDAO cthddvDao;
    public CTHoaDonPhongDAO cthddphongDao;
    public PhieuDichVuDAO pdvDao;
    public DichVuDAO dvDao;
    public PhieuDatPhongDAO phieuDatPhongDAO;
    public HoaDonDAO hdDao;

    public ArrayList<String> dsMaKH;
    private ObservableList<CTHoaDonDichVu> gioHangList = FXCollections.observableArrayList(); // List bind vào bảng giỏ hàng
    private List<DichVu> allDichVu; // Danh sách gốc để lọc/hiển thị
    private String maPhieuDV;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Khởi tạo DAO
        kDao = new KhachHangDAO();
        pDao = new PhongDAO();
        cthddvDao = new CTHoaDonDichVuDAO();
        pdvDao = new PhieuDichVuDAO();
        dvDao = new DichVuDAO();
        cthddphongDao = new CTHoaDonPhongDAO();
        phieuDatPhongDAO = new PhieuDatPhongDAO();
        hdDao = new HoaDonDAO();

        try {
            // Load dữ liệu ban đầu
            laydsKh();
            laydsPhongTheoKhachHang();

            // Setup giao diện POS
            setupGioHangTable(); // Cấu hình bảng giỏ hàng
            loadVaHienThiDichVu(); // Load danh sách món lên FlowPane

            // Listener
            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    laydsPhongTheoKhachHang();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            // Tìm kiếm dịch vụ
            txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> {
                filterDichVu(newValue);
            });

            // Hiển thị mã nhân viên
            AuthService auth = AuthService.getInstance();
            if(auth.getCurrentUser() != null && auth.getCurrentUser().getNhanVien() != null)
                maNV.setText(auth.getCurrentUser().getNhanVien().getMaNV());

            maPhieuDV = pdvDao.layMaPhieuDichVuTiepTheo();

        } catch (Exception e) {
            e.printStackTrace();
            ThongBaoUtil.hienThiLoi("Lỗi khởi tạo", e.getMessage());
        }
    }

    private void loadVaHienThiDichVu() throws Exception {
        allDichVu = dvDao.layTatCaDichVu(true); // Lấy dịch vụ còn kinh doanh
        renderServiceCards(allDichVu);
    }

    // Hàm quan trọng: Vẽ các thẻ dịch vụ
    private void renderServiceCards(List<DichVu> listDv) {
        pnDanhSachDichVu.getChildren().clear();

        // Cài đặt khoảng cách giữa các ô (Gap) cho FlowPane nếu chưa set trong FXML
        pnDanhSachDichVu.setHgap(15);
        pnDanhSachDichVu.setVgap(15);

        for (DichVu dv : listDv) {
            VBox card = new VBox(5);
            card.prefWidthProperty().bind(pnDanhSachDichVu.widthProperty().subtract(80).divide(4));

            card.setPrefHeight(200);
            card.setMinWidth(150);

            card.setAlignment(Pos.TOP_CENTER);

            // CSS cho thẻ
            card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2); " +
                    "-fx-cursor: hand; -fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10;");

            ImageView imgView = new ImageView();
            try {
                Image img = new Image(getClass().getResourceAsStream("/com/example/louishotelmanagement/image/trolley.png"));
                imgView.setImage(img);
            } catch (Exception e) {
            }
            imgView.setFitWidth(100);
            imgView.setFitHeight(100);

            Label lblTen = new Label(dv.getTenDV());
            lblTen.setWrapText(true);
            lblTen.setAlignment(Pos.CENTER);
            lblTen.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label lblGia = new Label(String.format("%,.0f đ", dv.getDonGia()));
            lblGia.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 13px;");

            Label lblTon = new Label("Sẵn có: " + dv.getSoLuong());
            lblTon.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");

            //Add vào card
            card.getChildren().addAll(imgView, lblTen, lblGia, lblTon);

            // Thêm vào giỏ
            card.setOnMouseClicked(event -> themVaoGioHang(dv));

            // Hiệu ứng Hover
            card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 10; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,123,255,0.3), 8, 0, 0, 2); " +
                    "-fx-cursor: hand; -fx-padding: 10; -fx-border-color: #2196f3; -fx-border-radius: 10;"));
            card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2); " +
                    "-fx-cursor: hand; -fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10;"));

            pnDanhSachDichVu.getChildren().add(card);
        }
    }

    private void filterDichVu(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            renderServiceCards(allDichVu);
            return;
        }
        List<DichVu> filtered = new ArrayList<>();
        for (DichVu dv : allDichVu) {
            if (dv.getTenDV().toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(dv);
            }
        }
        renderServiceCards(filtered);
    }

    private void setupGioHangTable() {
        colGH_Ten.setCellValueFactory(cellData -> {
            try {
                DichVu dv = dvDao.timDichVuTheoMa(cellData.getValue().getMaDV());
                return new SimpleStringProperty(dv != null ? dv.getTenDV() : cellData.getValue().getMaDV());
            } catch (Exception e) { return new SimpleStringProperty("Unk"); }
        });

        colGH_SL.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        colGH_ThanhTien.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%,.0f", cellData.getValue().getThanhTien()))
        );

        // Cấu hình cột Xóa (Button)
        colGH_Xoa.setCellFactory(param -> new TableCell<>() {
            private final Button btnDelete = new Button("X");

            {
                btnDelete.setStyle("-fx-background-color: #ffcdd2; -fx-text-fill: #c62828; -fx-font-weight: bold; -fx-background-radius: 15;");
                btnDelete.setOnAction(event -> {
                    CTHoaDonDichVu item = getTableView().getItems().get(getIndex());
                    gioHangList.remove(item);
                    capNhatTongTien();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); }
                else { setGraphic(btnDelete); }
            }
        });

        tblGioHang.setItems(gioHangList);
    }

    private void themVaoGioHang(DichVu dv) {
        if (dv.getSoLuong() <= 0) {
            ThongBaoUtil.hienThiLoi("Hết hàng", "Dịch vụ này tạm thời hết hàng.");
            return;
        }

        CTHoaDonDichVu itemInCart = null;
        for (CTHoaDonDichVu item : gioHangList) {
            if (item.getMaDV().equals(dv.getMaDV())) {
                itemInCart = item;
                break;
            }
        }

        if (itemInCart != null) {
            if (itemInCart.getSoLuong() + 1 > dv.getSoLuong()) {
                ThongBaoUtil.hienThiLoi("Cảnh báo", "Không đủ số lượng tồn kho!");
                return;
            }
            itemInCart.setSoLuong(itemInCart.getSoLuong() + 1);
            itemInCart.setThanhTien(itemInCart.tinhThanhTien());
            tblGioHang.refresh();
        } else {
            CTHoaDonDichVu newItem = new CTHoaDonDichVu(
                    null, null, dv.getMaDV(), 1, BigDecimal.valueOf(dv.getDonGia())
            );
            gioHangList.add(newItem);
        }
        capNhatTongTien();
    }

    private void capNhatTongTien() {
        BigDecimal total = BigDecimal.ZERO;
        for (CTHoaDonDichVu item : gioHangList) {
            total = total.add(item.getThanhTien());
        }
        lblTongTienTam.setText(String.format("%,.0f VND", total));
    }

    public void handleXacNhanLapPhieu(ActionEvent actionEvent) throws Exception {
        if (dsPhong.getSelectionModel().getSelectedItem() == null || gioHangList.isEmpty()) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn phòng và chọn ít nhất 1 dịch vụ.");
            return;
        }

        CTHoaDonPhong cthdp = cthddphongDao.getDSCTHoaDonPhongTheoMaPhong(dsPhong.getSelectionModel().getSelectedItem().toString()).getLast();
        PhieuDatPhong phieuDatPhong = phieuDatPhongDAO.layPhieuDatPhongTheoMa(cthdp.getMaPhieu());

        if (phieuDatPhong.getTrangThai().equals(TrangThaiPhieuDatPhong.DANG_SU_DUNG)) {
            // Setup Phiếu Dịch Vụ
            AuthService auth = AuthService.getInstance();
            PhieuDichVu pdv = new PhieuDichVu(maPhieuDV, cthdp.getMaHD(), LocalDate.now(), auth.getCurrentUser().getNhanVien().getMaNV(), txtGhiChu.getText(), auth.getCurrentUser().getNhanVien(), hdDao.timHoaDonTheoMa(cthdp.getMaHD()));
            pdvDao.themPhieuDichVu(pdv);

            boolean allItemsSuccessful = true;

            // DUYỆT QUA LIST TRONG GIỎ HÀNG
            for (CTHoaDonDichVu cthddv : gioHangList) {
                String maHD = cthdp.getMaHD();
                String maDV = cthddv.getMaDV();
                cthddv.setMaHD(maHD);

                DichVu dv = dvDao.timDichVuTheoMa(maDV);
                CTHoaDonDichVu ctHoaDonDichVuHienCo = cthddvDao.timCTDVTheoMaHDMaDV(maHD, maDV);

                if (ctHoaDonDichVuHienCo == null) {
                    cthddv.setMaPhieuDV(pdv.getMaPhieuDV());
                    if (!cthddvDao.themCTHoaDonDichVu(cthddv)) {
                        allItemsSuccessful = false; break;
                    }
                } else {
                    int soLuongMoi = ctHoaDonDichVuHienCo.getSoLuong() + cthddv.getSoLuong();
                    if (!cthddvDao.capNhatSoLuongCTHDDV(ctHoaDonDichVuHienCo.getMaHD(), ctHoaDonDichVuHienCo.getMaDV(), soLuongMoi)) {
                        allItemsSuccessful = false; break;
                    }
                }
                dvDao.capNhatSoLuongTonKho(dv.getMaDV(), dv.getSoLuong() - cthddv.getSoLuong());
            }

            if (allItemsSuccessful) {
                ThongBaoUtil.hienThiThongBao("Thành công", "Đã đặt dịch vụ thành công!");
                refreshData();
            } else {
                ThongBaoUtil.hienThiLoi("Lỗi", "Có lỗi xảy ra khi lưu chi tiết dịch vụ.");
            }
        } else {
            ThongBaoUtil.hienThiLoi("Lỗi", "Phòng này không khả dụng.");
        }
    }

    public void laydsKh() throws SQLException {
        ArrayList<KhachHang> khachhangs = kDao.layDSKhachHang();
        dsMaKH = new ArrayList<>();
        dsKhachHang.getItems().clear();
        for (KhachHang khachHang : khachhangs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }
        // if(!dsKhachHang.getItems().isEmpty()) dsKhachHang.getSelectionModel().selectFirst();
    }

    public void laydsPhongTheoKhachHang() throws SQLException {
        dsPhong.getItems().clear();
        if(dsKhachHang.getSelectionModel().getSelectedIndex() < 0) return;

        ArrayList<PhieuDatPhong> dsPhieu = phieuDatPhongDAO.layDSPhieuDatPhongTheoKhachHang(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
        if (dsPhieu.size() > 0) {
            for (PhieuDatPhong p : dsPhieu) {
                if (p.getTrangThai() != null && p.getTrangThai().equals(TrangThaiPhieuDatPhong.DANG_SU_DUNG)) {
                    ArrayList<CTHoaDonPhong> dsCTP = cthddphongDao.getCTHoaDonPhongTheoMaPhieu(p.getMaPhieu());
                    for (CTHoaDonPhong ctp : dsCTP) {
                        dsPhong.getItems().add(ctp.getMaPhong());
                    }
                }
            }
            if(!dsPhong.getItems().isEmpty()) dsPhong.getSelectionModel().selectFirst();
        }
    }


    public void refreshData() throws Exception {
        laydsKh();
        dsPhong.getItems().clear();
        gioHangList.clear();
        loadVaHienThiDichVu();
        capNhatTongTien();

        AuthService auth = AuthService.getInstance();
        if(auth.getCurrentUser() != null) maNV.setText(auth.getCurrentUser().getNhanVien().getMaNV());
        maPhieuDV = pdvDao.layMaPhieuDichVuTiepTheo();
        txtGhiChu.clear();
    }
}