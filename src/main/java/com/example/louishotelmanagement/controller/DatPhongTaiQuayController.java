package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.StringConverter;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class DatPhongTaiQuayController implements Initializable,Refreshable {

    public TextField maNhanVien;
    public DatePicker ngayDi;
    public ComboBox dsKhachHang;
    public TableView tablePhong;
    public Button btnDatPhong;
    @FXML
    public TableColumn<Phong, Void> colDaChon;
    public Label SoPhongDaChon;
    public Label TongTien;
    @FXML
    private TableColumn<Phong, String> colMaPhong;
    @FXML
    private TableColumn<Phong, Integer> colTang;
    @FXML
    private TableColumn<Phong, TrangThaiPhong> colTrangThai;
    @FXML
    private TableColumn<Phong, String> colMoTa;
    @FXML
    private TableColumn<Phong, String> colTenLoaiPhong;
    @FXML
    private TableColumn<Phong, Double> colDonGia;
    @FXML
    public TableColumn<Phong, Void> colThaoTac;
    private PhongDAO Pdao;
    private KhachHangDAO Kdao;
    private PhieuDatPhongDAO pdpDao;
    private ArrayList<String> dsMaKH;
    private NhanVienDAO nvdao;
    private String maPhieu;
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
        cthdpDao =  new CTHoaDonPhongDAO();
        listPhongDuocDat = new ArrayList<>();
        loaiPhongDAO =  new LoaiPhongDAO();
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
    }
    private void khoiTaoDinhDangNgay() {
        // Định dạng ngày tháng mong muốn (ví dụ: 25/10/2025)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Tạo StringConverter tùy chỉnh cho DatePicker
        StringConverter<LocalDate> converter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                // Chuyển LocalDate sang String để hiển thị
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                // Chuyển String nhập vào (hoặc từ FXML) sang LocalDate
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, formatter);
                    } catch (java.time.format.DateTimeParseException e) {
                        // Xử lý lỗi nếu người dùng nhập sai định dạng
                        System.err.println("Lỗi định dạng ngày: " + string);
                        return null;
                    }
                }
                return null;
            }
        };

        // Áp dụng converter cho cả hai DatePicker
        ngayDi.setConverter(converter);

        // *Tùy chọn:* Đảm bảo DatePicker có thể hiển thị ngày hôm nay nếu người dùng chưa chọn
        // ngayDen.setValue(LocalDate.now());
    }
    private void khoiTaoTableView() throws SQLException {
        // Thiết lập các cột
        colMaPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        colTang.setCellValueFactory(new PropertyValueFactory<>("tang"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai" ));
        colTrangThai.setCellFactory(_ -> new TableCell<>() {
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

            // 2. Trả về StringBinding chứa Tên Loại.
            // Nếu LoaiPhong không null, liên kết (bind) với thuộc tính TenLoai.
            return loaiPhong != null ?
                    javafx.beans.binding.Bindings.createStringBinding(loaiPhong::getTenLoai) :
                    javafx.beans.binding.Bindings.createStringBinding(() -> ""); // Xử lý trường hợp null
        });
        colDonGia.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();
            return loaiPhong != null ?
                    javafx.beans.binding.Bindings.createObjectBinding(loaiPhong::getDonGia) :
                    javafx.beans.binding.Bindings.createObjectBinding(() -> -0.0);
        });
        colThaoTac.setCellFactory(_ -> new TableCell<>() {

            private final Button btnThem = new Button("Thêm");

            {
                btnThem.getStyleClass().addAll("btn", "btn-xs", "btn-info", "btn-table-edit");


                btnThem.setOnAction(_ -> {
                    Phong phong = getTableView().getItems().get(getIndex());
                    tablePhong.getSelectionModel().select(phong);
                    boolean isContain = listPhongDuocDat.contains(phong);
                    if(isContain) {
                        listPhongDuocDat.remove(phong);
                        SoPhongDaChon.setText(String.valueOf(listPhongDuocDat.size()));
                        TongTien.setText(String.valueOf(TinhTongTien(listPhongDuocDat)));
                    }else{
                        listPhongDuocDat.add(phong);
                        SoPhongDaChon.setText(String.valueOf(listPhongDuocDat.size()));
                        TongTien.setText(String.valueOf(TinhTongTien(listPhongDuocDat)));
                    }
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
                    btnThem.getStyleClass().removeAll("btn", "btn-xs", "btn-info", "btn-table-add", "btn-danger","btn-table-remove");
                    if(isAdded) {
                        btnThem.setText("Bỏ chọn");
                        btnThem.getStyleClass().addAll("btn", "btn-xs", "btn-danger","btn-table-remove");
                    }else{
                        btnThem.setText("Thêm");
                        btnThem.getStyleClass().addAll("btn", "btn-xs", "btn-info", "btn-table-add");
                    }
                    HBox box = new HBox(10, btnThem);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        colDaChon.setCellFactory(_ -> new TableCell<>() {
            private CheckBox checkBox = new CheckBox();
            {

            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }else{
                    Phong phong = getTableView().getItems().get(getIndex());
                    boolean isChecked = listPhongDuocDat.contains(phong);
                    checkBox.setSelected(isChecked);
                    HBox box = new HBox(8, checkBox);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });


        // Thiết lập TableView
        tablePhong.setItems(danhSachPhongFiltered);

        // Cho phép chọn nhiều dòng
        tablePhong.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    private void khoiTaoDuLieu() {
        danhSachPhong = FXCollections.observableArrayList();
        danhSachPhongFiltered = FXCollections.observableArrayList();
    }
    private void taiDuLieu() {
        try {
            // Lấy danh sách phòng từ database
            List<Phong> dsPhong = Pdao.layDSPhongTrong();

            danhSachPhong.clear();
            danhSachPhong.addAll(dsPhong);

            // Áp dụng filter hiện tại
            apDungFilter();
        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải dữ liệu phòng: " + e.getMessage());
        }
    }
    private void apDungFilter() {
        danhSachPhongFiltered.clear();

        List<Phong> filtered = danhSachPhong.stream()
                .filter(phong -> {
                    // Filter theo tầng
                    Integer tangFilter = cbTang.getValue();
                    if (tangFilter != null && (phong.getTang() == null || !phong.getTang().equals(tangFilter))) {
                        return false;
                    }

                    // Filter theo loại phòng
                    LoaiPhong loaiPhongFilter = cbLocLoaiPhong.getValue();
                    return loaiPhongFilter == null || (phong.getLoaiPhong() != null &&
                            phong.getLoaiPhong().getMaLoaiPhong().equals(loaiPhongFilter.getMaLoaiPhong()));
                })
                .toList();

        danhSachPhongFiltered.addAll(filtered);
    }
    private void khoiTaoComboBox() {
        // Khởi tạo ComboBox tầng
        List<Integer> danhSachTang = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            danhSachTang.add(i);
        }
        cbTang.setItems(FXCollections.observableArrayList(danhSachTang));
        cbTang.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn tầng");
                } else {
                    setText("Tầng " + item);
                }
            }
        });
        cbTang.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn tầng");
                } else {
                    setText("Tầng " + item);
                }
            }
        });


        // Khởi tạo ComboBox loại phòng để filter
        khoiTaoComboBoxLoaiPhong();
    }
    private void khoiTaoComboBoxLoaiPhong() {
        try {
            List<LoaiPhong> danhSachLoaiPhong = loaiPhongDAO.layDSLoaiPhong();

            // Thiết lập ComboBox để hiển thị tên loại phòng
            cbLocLoaiPhong.setCellFactory(_ -> new ListCell<>() {
                @Override
                protected void updateItem(LoaiPhong item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Chọn loại phòng");
                    } else {
                        setText(item.getTenLoai());
                    }
                }
            });

            cbLocLoaiPhong.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(LoaiPhong item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Chọn loại phòng");
                    } else {
                        setText(item.getTenLoai());
                    }
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
    public void laydsKhachHang() throws  SQLException{
        dsKhachHang.getItems().clear();
        ArrayList<KhachHang> khs = Kdao.layDSKhachHang();
        dsMaKH = new ArrayList<>();
        for(KhachHang khachHang : khs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }
    }
    public void showAlertError(String header,String message){
        Alert alert = new  Alert(Alert.AlertType.ERROR);
        alert.setTitle("Đã xảy ra lỗi");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @Override // 👈 Thêm @Override
    public void refreshData() throws SQLException { // 👈 Đổi tên từ refresh() sang refreshData()
        laydsKhachHang();
        dsKhachHang.getSelectionModel().selectFirst();
        AuthService authService = AuthService.getInstance();
        maNhanVien.setText(authService.getCurrentUser().getNhanVien().getMaNV());
        ngayDi.setValue(null);
        tablePhong.getSelectionModel().clearSelection();
        SoPhongDaChon.setText(null);
        TongTien.setText(null);
        listPhongDuocDat.clear();
        cbTang.setValue(null);
        cbLocLoaiPhong.setValue(null);
        taiDuLieu();
    }

    // Trong DatPhongController.java

// Nó chỉ còn xử lý việc tạo Chi tiết và cập nhật trạng thái phòng.
    public void ThemChiTietPhong(PhieuDatPhong pdp, HoaDon hd, Phong p) throws SQLException {
        CTHoaDonPhong cthdp = new CTHoaDonPhong(
                hd.getMaHD(),
                pdp.getMaPhieu(),
                p.getMaPhong(),
                LocalDate.now(), // ngayDen: Sử dụng ngayDen của pdp nếu cần, hoặc null nếu chỉ dùng NgayNhanPhong
                null, // ngayDi: Sử dụng ngayDi của pdp nếu cần, hoặc null nếu chỉ dùng NgayTraPhong
                BigDecimal.valueOf(p.getLoaiPhong().getDonGia())
        );
        // Cập nhật trạng thái phòng thành ĐÃ ĐẶT (DA_DAT)
        Pdao.capNhatTrangThaiPhong(p.getMaPhong(), TrangThaiPhong.DA_DAT.toString());
        cthdpDao.themCTHoaDonPhong(cthdp);
    }

// -------------------------------------------------------------

    public void handleDatPhong(ActionEvent actionEvent) throws SQLException {
        if (listPhongDuocDat.isEmpty()&&ngayDi.getValue() == null) {
            showAlertError("Lỗi ngày", "Không được vui lòng không bỏ bất kì thông tin nào");
            return;
        }

        if (ngayDi.getValue().isAfter(LocalDate.now()) || ngayDi.getValue().isEqual(LocalDate.now())) {

            KhachHang newKh = Kdao.layKhachHangTheoMa(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));


            AuthService authService = AuthService.getInstance();
            String maNV = authService.getCurrentUser().getNhanVien().getMaNV();

            // 2. TẠO VÀ LƯU PHIẾU ĐẶT PHÒNG GỐC (CHỈ 1 LẦN)
            PhieuDatPhong pdp = new PhieuDatPhong(
                    pdpDao.sinhMaPhieuTiepTheo(),
                    LocalDate.now(),           // Ngay Lap
                    LocalDate.now(),           // Ngay Den (dự kiến/thực tế đặt)
                    ngayDi.getValue(),         // Ngay Di
                    TrangThaiPhieuDatPhong.DANG_SU_DUNG, // 👈 Trạng thái phải là ĐÃ ĐẶT (DA_DAT)
                    "Đặt trực tiếp",
                    newKh.getMaKH(),
                    maNV,
                    null
            );
            pdpDao.themPhieuDatPhong(pdp);

            // 3. TẠO VÀ LƯU HÓA ĐƠN GỐC (CHỈ 1 LẦN)
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

            // 4. LẶP VÀ TẠO CHI TIẾT CHO TỪNG PHÒNG
            for (Phong p : listPhongDuocDat) {
                ThemChiTietPhong(pdp, hd, p); // Gọi hàm xử lý chi tiết
            }

            refreshData();
            ThongBaoUtil.hienThiThongBao("Thành Công", "Bạn đã đặt phòng thành công");

        } else {
            showAlertError("LỖI NGÀY", "Không được chọn ngày đi trước ngày hôm nay");
        }
    }

    public void handleRefresh(ActionEvent actionEvent) throws SQLException {
        refreshData();
    }

    public void handleThemKhachHang(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/khach-hang-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Thêm Khách Hàng Mới");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));

            KhachHangDialogController controller = loader.getController();
            controller.setMode("ADD");

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
}
