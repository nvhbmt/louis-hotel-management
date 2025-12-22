package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.ui.components.CustomButton;
import com.example.louishotelmanagement.ui.models.ButtonVariant;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.util.Refreshable;
import com.example.louishotelmanagement.view.KhachHangFormDialogView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DatPhongTaiQuayController implements Initializable, Refreshable {

    public TextField maNhanVien;
    public DatePicker ngayDi;
    public ComboBox dsKhachHang;
    public TableView<Phong> tablePhong;
    public Button btnDatPhong;

    @FXML public TableColumn<Phong, Void> colDaChon;
    public Label SoPhongDaChon;
    public Label TongTien;

    @FXML private TableColumn<Phong, String> colMaPhong;
    @FXML private TableColumn<Phong, Integer> colTang;
    @FXML private TableColumn<Phong, TrangThaiPhong> colTrangThai;
    @FXML private TableColumn<Phong, String> colMoTa;
    @FXML private TableColumn<Phong, String> colTenLoaiPhong;
    @FXML private TableColumn<Phong, Double> colDonGia;
    @FXML public TableColumn<Phong, Void> colThaoTac;

    private PhongDAO Pdao;
    private KhachHangDAO Kdao;
    private PhieuDatPhongDAO pdpDao;
    private ArrayList<String> dsMaKH;
    private NhanVienDAO nvdao;
    private ArrayList<Phong> listPhongDuocDat;
    private HoaDonDAO hDao;
    private CTHoaDonPhongDAO cthdpDao;
    private ObservableList<Phong> danhSachPhong;
    private ObservableList<Phong> danhSachPhongFiltered;
    private LoaiPhongDAO loaiPhongDAO;
    public ComboBox<Integer> cbTang;
    public ComboBox<LoaiPhong> cbLocLoaiPhong;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pdao = new PhongDAO();
        Kdao = new KhachHangDAO();
        pdpDao = new PhieuDatPhongDAO();
        nvdao = new NhanVienDAO();
        hDao = new HoaDonDAO();
        cthdpDao = new CTHoaDonPhongDAO();
        listPhongDuocDat = new ArrayList<>();
        loaiPhongDAO = new LoaiPhongDAO();

        tablePhong.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try {
            khoiTaoDuLieu();
            khoiTaoTableView();
            khoiTaoComboBox();
            khoiTaoDinhDangNgay();
            laydsKhachHang();
            taiDuLieu();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dsKhachHang.getSelectionModel().selectFirst();

        AuthService authService = AuthService.getInstance();
        if(authService.getCurrentUser() != null && authService.getCurrentUser().getNhanVien() != null) {
            maNhanVien.setText(authService.getCurrentUser().getNhanVien().getMaNV());
        }
    }

    private void khoiTaoDinhDangNgay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringConverter<LocalDate> converter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, formatter);
                    } catch (java.time.format.DateTimeParseException e) {
                        return null;
                    }
                }
                return null;
            }
        };
        ngayDi.setConverter(converter);
    }

    private void khoiTaoTableView() throws SQLException {
        colMaPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        colTang.setCellValueFactory(new PropertyValueFactory<>("tang"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        colTrangThai.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(TrangThaiPhong item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    getStyleClass().clear();
                } else {
                    setText(item.toString());
                    setAlignment(Pos.CENTER);
                    getStyleClass().clear();
                    getStyleClass().add("status-trong");
                }
            }
        });
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        colTenLoaiPhong.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();
            return loaiPhong != null ?
                    Bindings.createStringBinding(loaiPhong::getTenLoai) :
                    Bindings.createStringBinding(() -> "");
        });
        colDonGia.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();
            return loaiPhong != null ?
                    Bindings.createObjectBinding(loaiPhong::getDonGia) :
                    Bindings.createObjectBinding(() -> -0.0);
        });
        colThaoTac.setCellFactory(param -> new TableCell<>() {
            private Button btnThem;

            {
                btnThem = CustomButton.createButton("Thêm", ButtonVariant.INFO);
                btnThem.setOnAction(event -> {
                    Phong phong = getTableView().getItems().get(getIndex());
                    tablePhong.getSelectionModel().select(phong);

                    Phong phongDaChon = listPhongDuocDat.stream()
                            .filter(p -> p.getMaPhong().equals(phong.getMaPhong()))
                            .findFirst()
                            .orElse(null);

                    if(phongDaChon != null) {
                        listPhongDuocDat.remove(phongDaChon);
                    } else {
                        listPhongDuocDat.add(phong);
                    }
                    capNhatTongTien();
                    getTableView().refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Phong phong = getTableView().getItems().get(getIndex());
                    boolean isAdded = listPhongDuocDat.stream()
                            .anyMatch(p -> p.getMaPhong().equals(phong.getMaPhong()));

                    if(isAdded) {
                        btnThem.setText("Bỏ chọn");
                        btnThem.setStyle(CustomButton.createButton("", ButtonVariant.DANGER).getStyle());
                    } else {
                        btnThem.setText("Thêm");
                        btnThem.setStyle(CustomButton.createButton("", ButtonVariant.INFO).getStyle());
                    }
                    HBox box = new HBox(10, btnThem);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        colDaChon.setCellFactory(param -> new TableCell<>() {
            private CheckBox checkBox = new CheckBox();
            {
                checkBox.setDisable(true);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Phong phong = getTableView().getItems().get(getIndex());
                    boolean isChecked = listPhongDuocDat.stream()
                            .anyMatch(p -> p.getMaPhong().equals(phong.getMaPhong()));
                    checkBox.setSelected(isChecked);
                    HBox box = new HBox(8, checkBox);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        tablePhong.setItems(danhSachPhongFiltered);
        tablePhong.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void khoiTaoDuLieu() {
        danhSachPhong = FXCollections.observableArrayList();
        danhSachPhongFiltered = FXCollections.observableArrayList();
    }

    private void taiDuLieu() {
        try {
            List<Phong> dsPhong = Pdao.layDSPhongTrong();
            danhSachPhong.clear();
            danhSachPhong.addAll(dsPhong);
            apDungFilter();
        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải dữ liệu phòng: " + e.getMessage());
        }
    }

    private void apDungFilter() {
        danhSachPhongFiltered.clear();
        List<Phong> filtered = danhSachPhong.stream()
                .filter(phong -> {
                    Integer tangFilter = cbTang.getValue();
                    if (tangFilter != null && (phong.getTang() == null || !phong.getTang().equals(tangFilter))) {
                        return false;
                    }
                    LoaiPhong loaiPhongFilter = cbLocLoaiPhong.getValue();
                    return loaiPhongFilter == null || (phong.getLoaiPhong() != null &&
                            phong.getLoaiPhong().getMaLoaiPhong().equals(loaiPhongFilter.getMaLoaiPhong()));
                })
                .toList();
        danhSachPhongFiltered.addAll(filtered);
    }

    private void khoiTaoComboBox() {
        List<Integer> danhSachTang = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            danhSachTang.add(i);
        }
        cbTang.setItems(FXCollections.observableArrayList(danhSachTang));
        cbTang.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText("Chọn tầng");
                else setText("Tầng " + item);
            }
        });
        cbTang.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText("Chọn tầng");
                else setText("Tầng " + item);
            }
        });
        khoiTaoComboBoxLoaiPhong();
    }

    private void khoiTaoComboBoxLoaiPhong() {
        try {
            List<LoaiPhong> danhSachLoaiPhong = loaiPhongDAO.layDSLoaiPhong();
            cbLocLoaiPhong.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(LoaiPhong item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) setText("Chọn loại phòng");
                    else setText(item.getTenLoai());
                }
            });
            cbLocLoaiPhong.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(LoaiPhong item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) setText("Chọn loại phòng");
                    else setText(item.getTenLoai());
                }
            });
            cbLocLoaiPhong.setItems(FXCollections.observableArrayList(danhSachLoaiPhong));
        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải danh sách loại phòng: " + e.getMessage());
        }
    }

    public double TinhTongTien(ArrayList<Phong> ls){
        double tongTien = 0;
        for(Phong phong : ls){
            tongTien += phong.getLoaiPhong().getDonGia();
        }
        return tongTien;
    }

    private void capNhatTongTien() {
        if (SoPhongDaChon != null) {
            SoPhongDaChon.setText(String.valueOf(listPhongDuocDat.size()));
        }
        if (TongTien != null) {
            TongTien.setText(String.format("%,.0f VNĐ", TinhTongTien(listPhongDuocDat)));
        }
    }

    public void laydsKhachHang() throws SQLException{
        dsKhachHang.getItems().clear();
        ArrayList<KhachHang> khs = Kdao.layDSKhachHang();
        dsMaKH = new ArrayList<>();
        for(KhachHang khachHang : khs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }
    }

    @Override
    public void refreshData() throws SQLException {
        laydsKhachHang();
        dsKhachHang.getSelectionModel().selectFirst();
        AuthService authService = AuthService.getInstance();
        if(authService.getCurrentUser() != null && authService.getCurrentUser().getNhanVien() != null){
            maNhanVien.setText(authService.getCurrentUser().getNhanVien().getMaNV());
        }
        ngayDi.setValue(null);
        tablePhong.getSelectionModel().clearSelection();
        SoPhongDaChon.setText(null);
        TongTien.setText(null);
        listPhongDuocDat.clear();
        cbTang.setValue(null);
        cbLocLoaiPhong.setValue(null);
        taiDuLieu();
    }

    public void ThemChiTietPhong(PhieuDatPhong pdp, HoaDon hd, Phong p) throws SQLException {
        CTHoaDonPhong cthdp = new CTHoaDonPhong(
                hd.getMaHD(),
                pdp.getMaPhieu(),
                p.getMaPhong(),
                pdp.getNgayDen(),
                pdp.getNgayDi(),
                BigDecimal.valueOf(p.getLoaiPhong().getDonGia())
        );
        Pdao.capNhatTrangThaiPhong(p.getMaPhong(), TrangThaiPhong.DANG_SU_DUNG.toString());
        cthdpDao.themCTHoaDonPhong(cthdp);
    }

    public void handleDatPhong(ActionEvent actionEvent) throws SQLException {
        if (ngayDi.getValue() == null) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn ngày đi trước khi đặt phòng.");
            return;
        }
        if (listPhongDuocDat.isEmpty()) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn phòng trước khi đặt");
            return;
        }

        if (ngayDi.getValue().isAfter(LocalDate.now()) || ngayDi.getValue().isEqual(LocalDate.now())) {
            KhachHang newKh = Kdao.layKhachHangTheoMa(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
            AuthService authService = AuthService.getInstance();
            String maNV = authService.getCurrentUser().getNhanVien().getMaNV();

            PhieuDatPhong pdp = new PhieuDatPhong(
                    pdpDao.sinhMaPhieuTiepTheo(),
                    LocalDate.now(),
                    LocalDate.now(),
                    ngayDi.getValue(),
                    TrangThaiPhieuDatPhong.DANG_SU_DUNG,
                    "Đặt trực tiếp",
                    newKh.getMaKH(),
                    maNV,
                    BigDecimal.ZERO
            );
            pdpDao.themPhieuDatPhong(pdp);

            HoaDon hd = new HoaDon(
                    hDao.taoMaHoaDonTiepTheo(),
                    LocalDate.now(),
                    null,
                    TrangThaiHoaDon.CHUA_THANH_TOAN,
                    null,
                    newKh.getMaKH(),
                    maNV,
                    null
            );
            hDao.themHoaDon(hd);

            for (Phong p : listPhongDuocDat) {
                ThemChiTietPhong(pdp, hd, p);
            }
            Kdao.capNhatTrangThaiKhachHang(newKh.getMaKH(),TrangThaiKhachHang.DANG_LUU_TRU);

            refreshData();
            ThongBaoUtil.hienThiThongBao("Thành Công", "Bạn đã đặt phòng thành công");
        } else {
            ThongBaoUtil.hienThiLoi("LỖI NGÀY", "Không được chọn ngày đi trước ngày hôm nay");
        }
    }

    public void handleRefresh(ActionEvent actionEvent) throws SQLException {
        refreshData();
    }

    public void handleThemKhachHang(ActionEvent actionEvent) {
        try {
            KhachHangFormDialogView view = new KhachHangFormDialogView();
            KhachHangDialogController controller = new KhachHangDialogController(view);
            Parent root = view.getRoot();
            Stage dialog = new Stage();
            dialog.setTitle("Thêm Khách Hàng Mới");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
            refreshData();
        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLocTang() {
        apDungFilter();
    }
    @FXML
    private void handleLocLoaiPhong() {
        apDungFilter();
    }

    public void nhanDanhSachPhongDaChon(ArrayList<Phong> dsPhongNhan) {
        if (dsPhongNhan != null && !dsPhongNhan.isEmpty()) {
            listPhongDuocDat.clear();
            for (Phong pNhan : dsPhongNhan) {
                for (Phong pTrong : danhSachPhong) {
                    if (pTrong.getMaPhong().equals(pNhan.getMaPhong())) {
                        listPhongDuocDat.add(pTrong);
                        break;
                    }
                }
            }
            capNhatTongTien();
            if (tablePhong != null) {
                tablePhong.refresh();
            }
        }
    }
}