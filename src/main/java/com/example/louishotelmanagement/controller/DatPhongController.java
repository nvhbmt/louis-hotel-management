package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class DatPhongController implements Initializable, Refreshable{

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
    @FXML
    public Label SoPhongDaChon;
    @FXML
    public Label TongTien;
    public Button handleThemKhachHang;
    public ComboBox<Integer> cbTang;
    public ComboBox<LoaiPhong> cbLocLoaiPhong;
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
    @FXML
    public TableColumn<Phong, Void> colDaChon;
    @FXML
    private TableView<Phong> tablePhong;
    private String maPhieu;
    private CTHoaDonPhongDAO cthdpDao;
    private HoaDonDAO hDao;
    public ArrayList<Phong> listPhongDuocDat = new ArrayList<>();
    private ObservableList<Phong> danhSachPhong;
    private ObservableList<Phong> danhSachPhongFiltered;
    private LoaiPhongDAO loaiPhongDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pdao = new PhongDAO();
        Kdao = new KhachHangDAO();
        pdpDao = new PhieuDatPhongDAO();
        cthdpDao = new CTHoaDonPhongDAO();
        hDao = new HoaDonDAO();
        loaiPhongDAO = new LoaiPhongDAO();
        tablePhong.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try{

            khoiTaoDuLieu();
            khoiTaoTableView();
            khoiTaoComboBox();
            laydsKhachHang();
            taiDuLieu();
        }catch (SQLException e){
            e.printStackTrace();
        }
        dsKhachHang.getSelectionModel().selectFirst();
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


    private void khoiTaoTableView() throws SQLException {
        // Thiết lập các cột
        colMaPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        colTang.setCellValueFactory(new PropertyValueFactory<>("tang"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        colTrangThai.setCellFactory(_ -> new TableCell<>() {
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
                    if (isContain) {
                        listPhongDuocDat.remove(phong);
                        SoPhongDaChon.setText(String.valueOf(listPhongDuocDat.size()));
                        TongTien.setText(String.valueOf(TinhTongTien(listPhongDuocDat)));
                    } else {
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
        tablePhong.setItems(danhSachPhongFiltered);
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
        public void showAlert(String header,String message){
            Alert alert = new  Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(header);
            alert.setContentText(message);
            alert.showAndWait();
        }
    @Override
    public void refreshData() throws SQLException { // 👈 Đổi tên từ refresh() sang refreshData()
        laydsKhachHang();
        dsKhachHang.getSelectionModel().selectFirst();
        AuthService authService = AuthService.getInstance();
        maNhanVien.setText(authService.getCurrentUser().getNhanVien().getMaNV());
        ngayDen.setValue(null);
        ngayDi.setValue(null);
        tablePhong.getSelectionModel().clearSelection();
        SoPhongDaChon.setText(null);
        TongTien.setText(null);
        listPhongDuocDat.clear();
        cbTang.setValue(null);
        cbLocLoaiPhong.setValue(null);
        taiDuLieu();
    }
    public void hienThiPhieuDatPhong(PhieuDatPhong pdp, ArrayList<Phong> dsPhong) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/phieu-dat-phong-pdf-view.fxml"));
            Parent root = loader.load();

            PhieuDatPhongPDFController controller = loader.getController();

            // Truyền dữ liệu sang Controller mới
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
    // Tạo 1 private static inner class để dễ sử dụng các thuộc tính mới
    private static class TienCocResult {
        public final BigDecimal tienCoc;
        public final String phuongThucTT;
        public TienCocResult(BigDecimal tienCoc, String phuongThucTT) {
            this.tienCoc = tienCoc;
            this.phuongThucTT = phuongThucTT;
        }
    }

    /**
     * Hiển thị màn hình xác nhận tiền cọc và lấy kết quả.
     */
    // Trong com.example.louishotelmanagement.controller.DatPhongController.java

    private TienCocResult hienThiTienCocDialog(double tongTienPhong) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/tien-coc-dialog.fxml"));
        Parent root = loader.load();

        TienCocDialogController controller = loader.getController();
        controller.setTongTien(tongTienPhong);

        Stage stage = new Stage();
        stage.setTitle("Xác Nhận Tiền Cọc");

        //Thiết lập kích thước tối thiểu/ban đầu cho Scene
        Scene scene = new Scene(root, 450, 650);

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        if (controller.isConfirmed()) {
            return new TienCocResult(controller.getTienCoc(), controller.getPhuongThucTT());
        } else {
            return null; // Trả về null nếu người dùng Hủy
        }
    }

    public void handleDatPhong(ActionEvent actionEvent) throws SQLException {
        // 0. KIỂM TRA ĐIỀU KIỆN BAN ĐẦU
        if (listPhongDuocDat.isEmpty() || ngayDen.getValue() == null || ngayDi.getValue() == null) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn phòng và nhập đầy đủ ngày đến/ngày đi.");
            return;
        }

        // 1. GỌI DIALOG TIỀN CỌC
        double tongTienPhong = TinhTongTien(listPhongDuocDat);
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

        // 2. TẠO VÀ LƯU PHIẾU ĐẶT PHÒNG GỐC (CHỈ 1 LẦN)
        AuthService authService = AuthService.getInstance();
        KhachHang newKh = Kdao.layKhachHangTheoMa(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
        String maNV = authService.getCurrentUser().getNhanVien().getMaNV();
        String maPhieuMoi = pdpDao.sinhMaPhieuTiepTheo();

        // Ghi chú sẽ là phương thức thanh toán
        String ghiChu = "Đặt trước (" + result.phuongThucTT + ")";

        PhieuDatPhong pdp = new PhieuDatPhong(
                maPhieuMoi,
                LocalDate.now(),
                ngayDen.getValue(),
                ngayDi.getValue(),
                TrangThaiPhieuDatPhong.DA_DAT,
                ghiChu,
                newKh.getMaKH(),
                maNV,
                null
        );
        pdp.setTienCoc(result.tienCoc); // Gán tiền cọc đã nhập từ dialog

        pdpDao.themPhieuDatPhong(pdp); // 👈 LƯU PHIẾU GỐC

        // 3. TẠO VÀ LƯU HÓA ĐƠN GỐC (CHỈ 1 LẦN)
        HoaDon hd = new HoaDon(hDao.taoMaHoaDonTiepTheo(), LocalDate.now(), null, TrangThaiHoaDon.CHUA_THANH_TOAN, null, newKh.getMaKH(), maNV, null);
        hDao.themHoaDon(hd); // 👈 LƯU HÓA ĐƠN

        // 4. LẶP QUA TỪNG PHÒNG ĐỂ TẠO CHI TIẾT VÀ CẬP NHẬT TRẠNG THÁI
        for (Phong p : listPhongDuocDat) {
            ThemChiTietPhong(pdp, hd, p);
        }

        ThongBaoUtil.hienThiThongBao("Thông báo", "Đặt phòng thành công. Tiền cọc: " + result.tienCoc + " VND (" + result.phuongThucTT + ")");

        this.maPhieu = maPhieuMoi;

        PhieuDatPhong phieu = pdpDao.layPhieuDatPhongTheoMa(this.maPhieu);
        if (phieu != null) {
            hienThiPhieuDatPhong(phieu, listPhongDuocDat);
        }
        refreshData();
    }

    // Hàm mới để xử lý chi tiết (thay thế logic trong DatPhong cũ)
    public void ThemChiTietPhong(PhieuDatPhong pdp, HoaDon hd, Phong p) throws SQLException {
        CTHoaDonPhong cthdp = new CTHoaDonPhong(
                hd.getMaHD(),
                pdp.getMaPhieu(),
                p.getMaPhong(),
                null,
                null,
                BigDecimal.valueOf(p.getLoaiPhong().getDonGia())
        );
        Pdao.capNhatTrangThaiPhong(p.getMaPhong(), TrangThaiPhong.DA_DAT.toString());
        cthdpDao.themCTHoaDonPhong(cthdp);
    }

    public void handleRefresh(ActionEvent actionEvent) throws SQLException {
        refreshData();
    }

    public void handleThemKhachHang(ActionEvent actionEvent) {
        try {
            // 1. Tải FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/them-khach-hang-form.fxml"));
            Parent parent = loader.load();

            // 2. Lấy Controller (nếu cần truyền dữ liệu hoặc gọi phương thức)
            // ThemKhachHangDialogController controller = loader.getController();

            // 3. Tạo Stage (Cửa sổ mới)
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm Khách Hàng Mới");

            // Cài đặt làm cửa sổ Modal (bắt buộc phải tương tác trước khi quay lại cửa sổ cũ)
            // Lấy Stage hiện tại từ sự kiện nếu cần
            // Stage ownerStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            // dialogStage.initOwner(ownerStage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            // 4. Thiết lập Scene và hiển thị
            dialogStage.setScene(new Scene(parent));
            dialogStage.showAndWait(); // showAndWait() sẽ chặn luồng cho đến khi hộp thoại đóng lại
            refreshData();
        } catch (IOException e) {
            System.err.println("Lỗi khi tải FXML Thêm Khách Hàng: " + e.getMessage());
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
}
