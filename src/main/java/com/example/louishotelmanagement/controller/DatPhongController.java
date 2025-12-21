package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.util.ContentSwitcher;
import com.example.louishotelmanagement.util.Refreshable;
import com.example.louishotelmanagement.util.ThongBaoUtil;
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class DatPhongController implements Initializable, Refreshable {

    public ComboBox dsPhong;
    public ComboBox dsKhachHang;
    public DatePicker ngayDen;
    public DatePicker ngayDi;
    public Button btnDatPhong;
    public PhongDAO Pdao;
    public TextField maNhanVien;
    public KhachHangDAO Kdao;
    public PhieuDatPhongDAO pdpDao;
    public ArrayList<String> dsMaKH;
    @FXML public Label SoPhongDaChon;
    @FXML public Label TongTien;
    @FXML public Label lbSoDem;
    @FXML public Label lbLoaiDatPhong;
    @FXML public Label lbLoadingPhong;
    @FXML public Label lbSoPhongTrong;
    @FXML public Label lbTongSoDem;
    @FXML public VBox vbEmptyState;
    public Button handleThemKhachHang;
    public ComboBox<Integer> cbTang;
    public ComboBox<LoaiPhong> cbLocLoaiPhong;

    @FXML private TableColumn<Phong, String> colMaPhong;
    @FXML private TableColumn<Phong, Integer> colTang;
    @FXML private TableColumn<Phong, TrangThaiPhong> colTrangThai;
    @FXML private TableColumn<Phong, String> colMoTa;
    @FXML private TableColumn<Phong, String> colTenLoaiPhong;
    @FXML private TableColumn<Phong, Double> colDonGia;
    @FXML public TableColumn<Phong, Void> colThaoTac;
    @FXML public TableColumn<Phong, Void> colDaChon;
    @FXML private TableView<Phong> tablePhong;

    private String maPhieu;
    private CTHoaDonPhongDAO cthdpDao;
    private HoaDonDAO hDao;
    public ArrayList<Phong> listPhongDuocDat = new ArrayList<>();
    private ObservableList<Phong> danhSachPhong;
    private ObservableList<Phong> danhSachPhongFiltered;
    private LoaiPhongDAO loaiPhongDAO;
    private ContentSwitcher contentSwitcher;

    public void setContentSwitcher(ContentSwitcher contentSwitcher) {
        this.contentSwitcher = contentSwitcher;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pdao = new PhongDAO();
        Kdao = new KhachHangDAO();
        pdpDao = new PhieuDatPhongDAO();
        cthdpDao = new CTHoaDonPhongDAO();
        hDao = new HoaDonDAO();
        loaiPhongDAO = new LoaiPhongDAO();

        tablePhong.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try {
            khoiTaoDuLieu();
            khoiTaoTableView();
            khoiTaoComboBox();
            khoiTaoDinhDangNgay();
            khoiTaoDatePickerListeners();
            laydsKhachHang();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dsKhachHang.getSelectionModel().selectFirst();
    }

    public void laydsKhachHang() throws SQLException {
        dsKhachHang.getItems().clear();
        ArrayList<KhachHang> khs = Kdao.layDSKhachHang();
        dsMaKH = new ArrayList<>();
        for (KhachHang khachHang : khs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
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
        ngayDen.setConverter(converter);
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
                    setAlignment(Pos.TOP_CENTER);
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
            private final Button btnThem = new Button("Thêm");
            {
                btnThem.getStyleClass().addAll("btn", "btn-xs", "btn-info", "btn-table-edit");
                btnThem.setOnAction(event -> {
                    Phong phong = getTableView().getItems().get(getIndex());
                    tablePhong.getSelectionModel().select(phong);
                    boolean isContain = listPhongDuocDat.contains(phong);
                    if (isContain) {
                        listPhongDuocDat.remove(phong);
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
                    boolean isAdded = listPhongDuocDat.contains(phong);
                    btnThem.getStyleClass().removeAll("btn", "btn-xs", "btn-info", "btn-table-add", "btn-danger", "btn-table-remove");
                    if (isAdded) {
                        btnThem.setText("Bỏ chọn");
                        btnThem.getStyleClass().addAll("btn", "btn-danger", "btn-table-remove");
                    } else {
                        btnThem.setText("Thêm");
                        btnThem.getStyleClass().addAll("btn", "btn-info", "btn-table-add");
                    }
                    HBox box = new HBox(10, btnThem);
                    box.setAlignment(Pos.TOP_CENTER);
                    setGraphic(box);
                }
            }
        });
        colDaChon.setCellFactory(param -> new TableCell<>() {
            private CheckBox checkBox = new CheckBox();
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Phong phong = getTableView().getItems().get(getIndex());
                    boolean isChecked = listPhongDuocDat.contains(phong);
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
            if (ngayDen.getValue() == null || ngayDi.getValue() == null) {
                danhSachPhong.clear();
                danhSachPhongFiltered.clear();
                capNhatUIKhiKhongCoDuLieu();
                return;
            }

            if (!validateNgay()) {
                danhSachPhong.clear();
                danhSachPhongFiltered.clear();
                capNhatUIKhiKhongCoDuLieu();
                return;
            }

            hienThiLoading(true);

            List<Phong> dsPhong = Pdao.layDSPhongTrongTheoKhoangThoiGian(
                    ngayDen.getValue(),
                    ngayDi.getValue()
            );

            danhSachPhong.clear();
            danhSachPhong.addAll(dsPhong);
            apDungFilter();
            capNhatUIThongTin();
            hienThiLoading(false);
        } catch (SQLException e) {
            hienThiLoading(false);
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể tải dữ liệu phòng. Vui lòng thử lại sau.");
            e.printStackTrace();
            capNhatUIKhiKhongCoDuLieu();
        } catch (Exception e) {
            hienThiLoading(false);
            ThongBaoUtil.hienThiLoi("Lỗi", "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại.");
            e.printStackTrace();
            capNhatUIKhiKhongCoDuLieu();
        }
    }

    private void hienThiLoading(boolean isLoading) {
        if (lbLoadingPhong != null) {
            lbLoadingPhong.setVisible(isLoading);
            lbLoadingPhong.setText(isLoading ? "Đang tìm phòng trống..." : "");
        }
    }

    private long tinhSoDem() {
        if (ngayDen.getValue() == null || ngayDi.getValue() == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(ngayDen.getValue(), ngayDi.getValue());
    }

    private double tinhTongTienTheoSoDem(ArrayList<Phong> danhSachPhong, long soDem) {
        if (danhSachPhong == null || danhSachPhong.isEmpty() || soDem <= 0) {
            return 0;
        }
        double tongTien = 0;
        for (Phong phong : danhSachPhong) {
            if (phong != null && phong.getLoaiPhong() != null) {
                tongTien += phong.getLoaiPhong().getDonGia() * soDem;
            }
        }
        return tongTien;
    }

    private void capNhatUIThongTin() {
        long soDem = tinhSoDem();
        if (soDem > 0) {
            if (lbSoDem != null) lbSoDem.setText("(" + soDem + " đêm)");
            if (lbTongSoDem != null) lbTongSoDem.setText(String.valueOf(soDem));
        } else {
            if (lbSoDem != null) lbSoDem.setText("");
            if (lbTongSoDem != null) lbTongSoDem.setText("0");
        }

        if (ngayDen.getValue() != null && lbLoaiDatPhong != null) {
            if (ngayDen.getValue().equals(LocalDate.now())) {
                lbLoaiDatPhong.setText("[Đặt trực tiếp]");
                lbLoaiDatPhong.getStyleClass().setAll("booking-type-direct");
                lbLoaiDatPhong.setVisible(true);
            } else if (ngayDen.getValue().isAfter(LocalDate.now())) {
                lbLoaiDatPhong.setText("[Đặt trước]");
                lbLoaiDatPhong.getStyleClass().setAll("booking-type-advance");
                lbLoaiDatPhong.setVisible(true);
            } else {
                lbLoaiDatPhong.setVisible(false);
            }
        }

        if (lbSoPhongTrong != null) {
            int soPhongTrong = danhSachPhongFiltered.size();
            if (soPhongTrong > 0) {
                lbSoPhongTrong.setText("✓ Tìm thấy " + soPhongTrong + " phòng trống");
                lbSoPhongTrong.getStyleClass().setAll("info-success");
            } else {
                lbSoPhongTrong.setText("⚠ Không có phòng trống trong khoảng thời gian này");
                lbSoPhongTrong.getStyleClass().setAll("info-error");
            }
        }

        if (vbEmptyState != null && tablePhong != null) {
            boolean coPhong = !danhSachPhongFiltered.isEmpty();
            tablePhong.setVisible(coPhong);
            vbEmptyState.setVisible(!coPhong);
        }

        capNhatTongTien();
    }

    private void capNhatUIKhiKhongCoDuLieu() {
        if (lbSoDem != null) lbSoDem.setText("");
        if (lbLoaiDatPhong != null) lbLoaiDatPhong.setVisible(false);
        if (lbSoPhongTrong != null) lbSoPhongTrong.setText("");
        if (lbTongSoDem != null) lbTongSoDem.setText("0");
        if (vbEmptyState != null && tablePhong != null) {
            tablePhong.setVisible(false);
            vbEmptyState.setVisible(true);
        }
    }

    private void capNhatTongTien() {
        if (listPhongDuocDat.isEmpty()) {
            if (TongTien != null) TongTien.setText("0 VNĐ");
            if (SoPhongDaChon != null) SoPhongDaChon.setText("0");
            return;
        }

        long soDem = tinhSoDem();
        double tongTien;
        if (soDem > 0) {
            tongTien = tinhTongTienTheoSoDem(listPhongDuocDat, soDem);
        } else {
            tongTien = TinhTongTien(listPhongDuocDat);
        }

        if (TongTien != null) TongTien.setText(String.format("%,.0f VNĐ", tongTien));
        if (SoPhongDaChon != null) SoPhongDaChon.setText(String.valueOf(listPhongDuocDat.size()));
    }

    private boolean validateNgay() {
        LocalDate ngayDenValue = ngayDen.getValue();
        LocalDate ngayDiValue = ngayDi.getValue();

        if (ngayDenValue == null || ngayDiValue == null) {
            return false;
        }

        if (ngayDenValue.isBefore(LocalDate.now())) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không được chọn ngày đến trước ngày hôm nay");
            return false;
        }

        if (ngayDiValue.isBefore(ngayDenValue) || ngayDiValue.isEqual(ngayDenValue)) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Ngày đi phải sau ngày đến ít nhất 1 ngày");
            return false;
        }

        return true;
    }

    private void khoiTaoDatePickerListeners() {
        ngayDen.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date != null && date.isBefore(LocalDate.now()));
            }
        });

        ngayDen.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                capNhatUIThongTin();
                if (ngayDi.getValue() != null) {
                    taiDuLieu();
                }
            } else {
                danhSachPhong.clear();
                danhSachPhongFiltered.clear();
                capNhatUIKhiKhongCoDuLieu();
            }
        });

        ngayDi.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && ngayDen.getValue() != null) {
                if (newValue.isBefore(ngayDen.getValue()) || newValue.isEqual(ngayDen.getValue())) {
                    ThongBaoUtil.hienThiLoi("Lỗi", "Ngày đi phải sau ngày đến ít nhất 1 ngày");
                    ngayDi.setValue(oldValue);
                    return;
                }
                capNhatUIThongTin();
                taiDuLieu();
            } else if (newValue == null) {
                danhSachPhong.clear();
                danhSachPhongFiltered.clear();
                capNhatUIKhiKhongCoDuLieu();
            }
        });
    }

    private ArrayList<Phong> kiemTraDanhSachPhongTrong(ArrayList<Phong> danhSachPhongCanKiemTra) throws SQLException {
        if (danhSachPhongCanKiemTra == null || danhSachPhongCanKiemTra.isEmpty()) {
            return new ArrayList<>();
        }
        if (ngayDen.getValue() == null || ngayDi.getValue() == null) {
            return new ArrayList<>();
        }

        List<Phong> phongTrong = Pdao.layDSPhongTrongTheoKhoangThoiGian(
                ngayDen.getValue(),
                ngayDi.getValue()
        );

        Set<String> maPhongTrong = phongTrong.stream()
                .filter(p -> p != null && p.getMaPhong() != null)
                .map(Phong::getMaPhong)
                .collect(Collectors.toSet());

        ArrayList<Phong> ketQua = new ArrayList<>();
        for (Phong phong : danhSachPhongCanKiemTra) {
            if (phong != null && phong.getMaPhong() != null && maPhongTrong.contains(phong.getMaPhong())) {
                ketQua.add(phong);
            }
        }
        return ketQua;
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

    public double TinhTongTien(ArrayList<Phong> ls) {
        double tongTien = 0;
        for (Phong phong : ls) {
            tongTien += phong.getLoaiPhong().getDonGia();
        }
        return tongTien;
    }

    @Override
    public void refreshData() throws SQLException {
        laydsKhachHang();
        dsKhachHang.getSelectionModel().selectFirst();
        AuthService authService = AuthService.getInstance();
        maNhanVien.setText(authService.getCurrentUser().getNhanVien().getMaNV());
        ngayDen.setValue(null);
        ngayDi.setValue(null);
        tablePhong.getSelectionModel().clearSelection();
        listPhongDuocDat.clear();
        cbTang.setValue(null);
        cbLocLoaiPhong.setValue(null);
        capNhatUIKhiKhongCoDuLieu();
    }

    public void hienThiPhieuDatPhong(PhieuDatPhong pdp, ArrayList<Phong> dsPhong) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/phieu-dat-phong-pdf-view.fxml"));
            Parent root = loader.load();
            PhieuDatPhongPDFController controller = loader.getController();
            controller.setPhieuDatPhongData(pdp, dsPhong);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setTitle("Phiếu Xác Nhận Đặt Phòng " + pdp.getMaPhieu());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi giao diện", "Không thể tải giao diện phiếu xác nhận.");
            e.printStackTrace();
        }
    }

    private static class TienCocResult {
        public final BigDecimal tienCoc;
        public final String phuongThucTT;
        public TienCocResult(BigDecimal tienCoc, String phuongThucTT) {
            this.tienCoc = tienCoc;
            this.phuongThucTT = phuongThucTT;
        }
    }

    private TienCocResult hienThiTienCocDialog(double tongTienPhong) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/tien-coc-dialog.fxml"));
        Parent root = loader.load();
        TienCocDialogController controller = loader.getController();
        controller.setTongTien(tongTienPhong);
        Stage stage = new Stage();
        stage.setTitle("Xác Nhận Tiền Cọc");
        Scene scene = new Scene(root, 450, 650);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        if (controller.isConfirmed()) {
            return new TienCocResult(controller.getTienCoc(), controller.getPhuongThucTT());
        } else {
            return null;
        }
    }

    public void handleDatPhong(ActionEvent actionEvent) throws SQLException {
        if (ngayDen.getValue() == null || ngayDi.getValue() == null) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn đầy đủ ngày đến/ngày đi.");
            return;
        }
        if (listPhongDuocDat.isEmpty()) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn phòng trước khi đặt");
            return;
        }
        if (!validateNgay()) {
            return;
        }

        ArrayList<Phong> phongConTrong;
        try {
            phongConTrong = kiemTraDanhSachPhongTrong(listPhongDuocDat);
        } catch (SQLException e) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể kiểm tra phòng trống. Vui lòng thử lại.");
            e.printStackTrace();
            return;
        }

        if (phongConTrong.size() != listPhongDuocDat.size()) {
            ArrayList<Phong> phongKhongTrong = new ArrayList<>(listPhongDuocDat);
            phongKhongTrong.removeAll(phongConTrong);
            StringBuilder danhSachPhong = new StringBuilder();
            for (int i = 0; i < phongKhongTrong.size(); i++) {
                if (i > 0) danhSachPhong.append(", ");
                danhSachPhong.append(phongKhongTrong.get(i).getMaPhong());
            }
            ThongBaoUtil.hienThiLoi("Lỗi", "Các phòng sau đã được đặt bởi khách hàng khác: " + danhSachPhong.toString() + ". Vui lòng chọn phòng khác.");
            taiDuLieu();
            return;
        }

        long soDem = tinhSoDem();
        double tongTienPhong;
        if (soDem > 0) {
            tongTienPhong = tinhTongTienTheoSoDem(listPhongDuocDat, soDem);
        } else {
            tongTienPhong = TinhTongTien(listPhongDuocDat);
        }

        TienCocResult result;
        try {
            result = hienThiTienCocDialog(tongTienPhong);
            if (result == null) {
                ThongBaoUtil.hienThiThongBao("Thông báo", "Đã hủy bỏ thao tác đặt phòng.");
                return;
            }
        } catch (IOException e) {
            ThongBaoUtil.hienThiLoi("Lỗi giao diện", "Không thể mở màn hình xác nhận tiền cọc.");
            e.printStackTrace();
            return;
        }

        boolean datTrucTiep = ngayDen.getValue().equals(LocalDate.now());
        TrangThaiPhieuDatPhong trangThaiPhieu;
        String ghiChu;
        if (datTrucTiep) {
            trangThaiPhieu = TrangThaiPhieuDatPhong.DANG_SU_DUNG;
            ghiChu = "Đặt trực tiếp (" + result.phuongThucTT + ")";
        } else {
            trangThaiPhieu = TrangThaiPhieuDatPhong.DA_DAT;
            ghiChu = "Đặt trước (" + result.phuongThucTT + ")";
        }

        AuthService authService = AuthService.getInstance();
        KhachHang newKh = Kdao.layKhachHangTheoMa(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
        String maNV = authService.getCurrentUser().getNhanVien().getMaNV();
        String maPhieuMoi = pdpDao.sinhMaPhieuTiepTheo();

        PhieuDatPhong pdp = new PhieuDatPhong(
                maPhieuMoi,
                LocalDate.now(),
                ngayDen.getValue(),
                ngayDi.getValue(),
                trangThaiPhieu,
                ghiChu,
                newKh.getMaKH(),
                maNV,
                null
        );
        pdp.setTienCoc(result.tienCoc);
        pdpDao.themPhieuDatPhong(pdp);

        HoaDon hd = new HoaDon(hDao.taoMaHoaDonTiepTheo(), LocalDate.now(), null, TrangThaiHoaDon.CHUA_THANH_TOAN, null, newKh.getMaKH(), maNV, null);
        hDao.themHoaDon(hd);

        ArrayList<String> phongBiConflict = new ArrayList<>();
        for (Phong p : listPhongDuocDat) {
            try {
                ThemChiTietPhong(pdp, hd, p);
            } catch (SQLException e) {
                if (e.getMessage() != null && e.getMessage().contains("đã được đặt")) {
                    phongBiConflict.add(p.getMaPhong());
                } else {
                    throw e;
                }
            }
        }

        if (!phongBiConflict.isEmpty()) {
            StringBuilder danhSachPhong = new StringBuilder();
            for (int i = 0; i < phongBiConflict.size(); i++) {
                if (i > 0) danhSachPhong.append(", ");
                danhSachPhong.append(phongBiConflict.get(i));
            }
            ThongBaoUtil.hienThiLoi("Lỗi", "Các phòng sau đã được đặt bởi khách hàng khác trong lúc xử lý: " + danhSachPhong.toString() + ". Vui lòng thử lại.");
            taiDuLieu();
            return;
        }

        Kdao.capNhatTrangThaiKhachHang(newKh.getMaKH(), TrangThaiKhachHang.DA_DAT);
        ThongBaoUtil.hienThiThongBao("Thông báo", "Đặt phòng thành công. Tiền cọc: " + result.tienCoc + " VND (" + result.phuongThucTT + ")");
        this.maPhieu = maPhieuMoi;
        PhieuDatPhong phieu = pdpDao.layPhieuDatPhongTheoMa(this.maPhieu);
        if (phieu != null) {
            hienThiPhieuDatPhong(phieu, listPhongDuocDat);
        }
        refreshData();
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

        if (pdp.getTrangThai() == TrangThaiPhieuDatPhong.DANG_SU_DUNG) {
            Pdao.capNhatTrangThaiPhong(p.getMaPhong(), TrangThaiPhong.DANG_SU_DUNG.toString());
        } else {
            Pdao.capNhatTrangThaiPhong(p.getMaPhong(), TrangThaiPhong.DA_DAT.toString());
        }
        cthdpDao.themCTHoaDonPhong(cthdp);
    }

    public void handleRefresh(ActionEvent actionEvent) throws SQLException {
        refreshData();
    }

    public void handleThemKhachHang(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/khach-hang-form-dialog.fxml"));
            Parent parent = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm Khách Hàng Mới");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(parent));
            dialogStage.showAndWait();
            refreshData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public void nhanDuLieuTuPhongView(ArrayList<Phong> dsPhongNhan, LocalDate ngayDenVal, LocalDate ngayDiVal) {
        if (ngayDen != null) ngayDen.setValue(ngayDenVal);
        if (ngayDi != null) ngayDi.setValue(ngayDiVal);

        taiDuLieu();

        if (dsPhongNhan != null && !dsPhongNhan.isEmpty()) {
            listPhongDuocDat.clear();
            for (Phong pNhan : dsPhongNhan) {
                boolean phongCoSan = danhSachPhong.stream()
                        .anyMatch(p -> p.getMaPhong().equals(pNhan.getMaPhong()));
                if (phongCoSan) {
                    listPhongDuocDat.add(pNhan);
                }
            }
        }
        capNhatUIThongTin();
        if (tablePhong != null) {
            tablePhong.refresh();
        }
    }
}