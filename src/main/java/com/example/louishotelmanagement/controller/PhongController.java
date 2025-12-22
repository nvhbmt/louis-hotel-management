package com.example.louishotelmanagement.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;
import com.example.louishotelmanagement.ui.components.Badge;
import com.example.louishotelmanagement.ui.models.BadgeVariant;
import com.example.louishotelmanagement.util.ContentSwitcher;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PhongController implements Initializable {

    // --- FXML Components ---
    @FXML private Label tieuDeLabel;
    @FXML private Button btnNhanPhong, btnDatTT, btnDoiPhong, btnHuyDat, btnDichVu;
    @FXML private ComboBox<String> cbxTang;
    @FXML private ComboBox<String> cbxTrangThai;
    @FXML private DatePicker dpTuNgay;
    @FXML private DatePicker dpDenNgay;

    @FXML private TableView<Phong> tableViewPhong;
    @FXML private TableColumn<Phong, Boolean> chonPhong;
    @FXML private TableColumn<Phong, String> maPhong;
    @FXML private TableColumn<Phong, String> loaiPhong;
    @FXML private TableColumn<Phong, Double> donGia;
    @FXML private TableColumn<Phong, TrangThaiPhong> trangThai;

    @FXML private Label lblTongPhong;
    @FXML private Label lblPhongTrong;
    @FXML private Label lblPhongSuDung;
    @FXML private Label lblPhongBaoTri;

    // --- Data and DAO ---
    private PhongDAO phongDAO;
    private ObservableList<Phong> phongObservableList;
    private FilteredList<Phong> filteredPhongList;

    private ContentSwitcher switcher;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        phongDAO = new PhongDAO();

        cauHinhBang();
        cauHinhCBX();
        cauHinhDatePicker();
        cauHinhLoc();
        taiBang();
        
        ContentSwitcher switcher = LayoutController.getInstance();
        if (switcher != null) {
            this.switcher = switcher;
        }
    }

    private void cauHinhBang() {
        phongObservableList = FXCollections.observableArrayList();
        filteredPhongList = new FilteredList<>(phongObservableList, p -> true);
        tableViewPhong.setItems(filteredPhongList);

        // 1. Cột Checkbox
        chonPhong.setCellValueFactory(new PropertyValueFactory<>("selected"));
        chonPhong.setCellFactory(column -> new TableCell<Phong, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(event -> {
                    Phong phong = getTableView().getItems().get(getIndex());
                    phong.setSelected(checkBox.isSelected());
                });
                setStyle("-fx-alignment: CENTER;");
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    Phong phong = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(phong.isSelected());

                    boolean coTheChon = kiemTraCoTheChonPhong(phong);
                    checkBox.setDisable(!coTheChon);
                    if (!coTheChon && phong.isSelected()) {
                        phong.setSelected(false);
                        checkBox.setSelected(false);
                    }
                    setGraphic(checkBox);
                }
            }
        });

        // 2. Các cột thông tin
        maPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        maPhong.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-weight: bold;");

        loaiPhong.setCellValueFactory(cellData -> {
            LoaiPhong lp = cellData.getValue().getLoaiPhong();
            return new SimpleStringProperty(lp != null ? lp.getTenLoai() : "N/A");
        });

        donGia.setCellValueFactory(cellData -> {
            LoaiPhong lp = cellData.getValue().getLoaiPhong();
            Double gia = (lp != null) ? lp.getDonGia() : 0.0;
            return new SimpleObjectProperty<>(gia);
        });
        donGia.setCellFactory(column -> new TableCell<Phong, Double>() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(currencyFormat.format(item));
            }
        });

        // 3. Cột Trạng Thái (Badge)
        trangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        trangThai.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(TrangThaiPhong item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Phong phong = getTableView().getItems().get(getIndex());

                    boolean isHighlighted = phong.getTrangThai() != TrangThaiPhong.TRONG;

                    BadgeVariant variant = switch (phong.getTrangThai()) {
                        case TRONG -> BadgeVariant.SUCCESS;
                        case DANG_SU_DUNG -> BadgeVariant.WARNING;
                        case DA_DAT -> BadgeVariant.DANGER;
                        case BAO_TRI -> BadgeVariant.INFO;
                        default -> BadgeVariant.DEFAULT;
                    };

                    Label badge = isHighlighted
                            ? Badge.createBadge(phong.getTrangThai().toString(), variant, "6px 16px", true)
                            : Badge.createBadge(phong.getTrangThai().toString(), variant);

                    setGraphic(badge);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }

    public void taiBang() {
        try {
            ArrayList<Phong> dsPhong = new ArrayList<>();
            if (dpTuNgay.getValue() != null && dpDenNgay.getValue() != null) {
                if (dpDenNgay.getValue().isBefore(dpTuNgay.getValue())) {
                    ThongBaoUtil.hienThiLoi("Lỗi", "Ngày kết thúc phải sau ngày bắt đầu!");
                    return;
                }
                dsPhong = phongDAO.layDSPhongTrongTheoKhoangThoiGian(dpTuNgay.getValue(),
                        dpDenNgay.getValue());
            } else {
                dsPhong = phongDAO.layDSPhong();
            }
            phongObservableList.clear();
            phongObservableList.addAll(dsPhong);
            capNhatThongKe();
            tableViewPhong.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể tải dữ liệu phòng: " + e.getMessage());
        }
    }


    private void chuyenSangManHinhTraPhong(Phong phong) {
        if (switcher != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/tra-phong-view.fxml"));
                Parent root = loader.load();

                TraPhongController ctrl = loader.getController();
                ctrl.setContentSwitcher(this.switcher);
                ctrl.truyenDuLieuTuPhong(phong.getMaPhong());

                switcher.switchContent(root);
            } catch (Exception e) {
                e.printStackTrace();
                ThongBaoUtil.hienThiLoi("Lỗi", "Không thể mở màn hình trả phòng: " + e.getMessage());
            }
        }
    }

    private void cauHinhCBX() {
        ObservableList<String> danhSachTang = FXCollections.observableArrayList();
        danhSachTang.add("Tất cả các tầng");
        for (int i = 1; i <= 4; i++) danhSachTang.add("Tầng " + i);
        cbxTang.setItems(danhSachTang);
        cbxTang.setValue("Tất cả các tầng");

        ObservableList<String> danhSachTrangThai = FXCollections.observableArrayList(
                "Tất cả trạng thái",
                TrangThaiPhong.TRONG.toString(),
                TrangThaiPhong.DANG_SU_DUNG.toString(),
                TrangThaiPhong.DA_DAT.toString(),
                TrangThaiPhong.BAO_TRI.toString()
        );
        cbxTrangThai.setItems(danhSachTrangThai);
        cbxTrangThai.setValue("Tất cả trạng thái");
    }

    private void cauHinhDatePicker() {
        dpTuNgay.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && dpDenNgay.getValue() != null && dpDenNgay.getValue().isBefore(newVal)) {
                ThongBaoUtil.hienThiCanhBao("Cảnh báo", "Ngày kết thúc phải sau ngày bắt đầu!");
                dpTuNgay.setValue(oldVal);
                return;
            }
            taiBang();
        });
        dpDenNgay.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && dpTuNgay.getValue() != null && newVal.isBefore(dpTuNgay.getValue())) {
                ThongBaoUtil.hienThiCanhBao("Cảnh báo", "Ngày kết thúc phải sau ngày bắt đầu!");
                dpDenNgay.setValue(oldVal);
                return;
            }
            taiBang();
        });
    }

    private void cauHinhLoc() {
        filteredPhongList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            String tangDaChon = cbxTang.getValue();
            String trangThaiDaChon = cbxTrangThai.getValue();

            return phong -> {
                boolean tangMatch = (tangDaChon == null || tangDaChon.equals("Tất cả các tầng")) ||
                        (phong.getTang() != null && phong.getTang() == Integer.parseInt(tangDaChon.split("\\s+")[1]));

                boolean trangThaiMatch = (trangThaiDaChon == null || trangThaiDaChon.equals("Tất cả trạng thái")) ||
                        (phong.getTrangThai() != null && phong.getTrangThai().toString().equals(trangThaiDaChon));

                return tangMatch && trangThaiMatch;
            };
        }, cbxTang.valueProperty(), cbxTrangThai.valueProperty()));
    }

    private void capNhatThongKe() {
        long tongSo = phongObservableList.size();
        long soTrong = phongObservableList.stream().filter(p -> p.getTrangThai() == TrangThaiPhong.TRONG).count();
        long soSuDung = phongObservableList.stream().filter(p -> p.getTrangThai() == TrangThaiPhong.DANG_SU_DUNG).count();
        long soBaoTri = phongObservableList.stream().filter(p -> p.getTrangThai() == TrangThaiPhong.BAO_TRI).count();

        lblTongPhong.setText(String.valueOf(tongSo));
        lblPhongTrong.setText(String.valueOf(soTrong));
        lblPhongSuDung.setText(String.valueOf(soSuDung));
        lblPhongBaoTri.setText(String.valueOf(soBaoTri));
    }

    @FXML
    private void handleLamMoi(ActionEvent event) {
        cbxTang.setValue("Tất cả các tầng");
        cbxTrangThai.setValue("Tất cả trạng thái");
        dpTuNgay.setValue(null);
        dpDenNgay.setValue(null);
        phongObservableList.forEach(phong -> phong.setSelected(false));
        taiBang();
    }

    private boolean kiemTraCoTheChonPhong(Phong phong) {
        if (dpTuNgay.getValue() == null || dpDenNgay.getValue() == null)
            return phong.getTrangThai() == TrangThaiPhong.TRONG;
        try {
            return phongDAO.kiemTraPhongTrongTheoKhoangThoiGian(phong.getMaPhong(), dpTuNgay.getValue(), dpDenNgay.getValue());
        } catch (SQLException e) {
            return false;
        }
    }

    // Hàm thông minh để lấy danh sách phòng (ưu tiên Checkbox, nếu rỗng thì lấy dòng Highlight)
    private ArrayList<Phong> layDanhSachPhongTarget() {
        ArrayList<Phong> ketQua = new ArrayList<>();
        // 1. Lấy từ Checkbox
        for (Phong phong : phongObservableList) {
            if (phong.isSelected()) {
                ketQua.add(phong);
            }
        }
        // 2. Nếu Checkbox rỗng, kiểm tra dòng đang bôi đen (SelectionModel)
        if (ketQua.isEmpty()) {
            Phong selectedRow = tableViewPhong.getSelectionModel().getSelectedItem();
            if (selectedRow != null) {
                ketQua.add(selectedRow);
            }
        }
        return ketQua;
    }

    // --- CÁC HÀM SỰ KIỆN NÚT BẤM (ĐÃ CẬP NHẬT LOGIC MỚI) ---

    @FXML
    private void moNhanPhong(ActionEvent actionEvent) {
        ArrayList<Phong> dsTarget = layDanhSachPhongTarget();
        String maPhongCanNhan = dsTarget.isEmpty() ? null : dsTarget.get(0).getMaPhong();

        if (switcher != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/nhan-phong-view.fxml"));
                Parent root = loader.load();

                if (maPhongCanNhan != null) {
                    NhanPhongController controller = loader.getController();
                    controller.setContentSwitcher(this.switcher);
                    controller.nhanDuLieuTuPhong(maPhongCanNhan);
                }
                switcher.switchContent(root);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @FXML
    private void moDoiPhong(ActionEvent actionEvent) throws SQLException {
        ArrayList<Phong> dsTarget = layDanhSachPhongTarget();

        // 1. Kiểm tra nếu chưa chọn phòng nào
        if (dsTarget.isEmpty()) {
            ThongBaoUtil.hienThiCanhBao("Lỗi", "Vui lòng chọn một phòng để đổi!");
            return;
        }

        String maPhongCanDoi = dsTarget.get(0).getMaPhong();
        TrangThaiPhong tt = phongDAO.layPhongTheoMa(maPhongCanDoi).getTrangThai();

        // 2. SỬA TẠI ĐÂY: Nếu trạng thái KHÁC "DANG_SU_DUNG" thì mới báo lỗi
        if (!tt.equals(TrangThaiPhong.DANG_SU_DUNG)) {
            ThongBaoUtil.hienThiCanhBao("Lỗi", "Vui lòng chọn phòng đang sử dụng để đổi phòng");
            return;
        }

        // 3. Logic chuyển màn hình nếu thỏa mãn điều kiện
        if (switcher != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/doi-phong-view.fxml"));
                Parent root = loader.load();

                DoiPhongController controller = loader.getController();
                controller.setContentSwitcher(this.switcher);
                controller.nhanDuLieuTuPhong(maPhongCanDoi);

                switcher.switchContent(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML private void moDichVu(ActionEvent actionEvent) { if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/dat-dich-vu-view.fxml"); }
    @FXML private void moHuyDat(ActionEvent actionEvent) { if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/huy-dat-phong-view.fxml"); }
    @FXML private void moThanhToan(ActionEvent actionEvent) { if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/quan-ly-hoa-don-view.fxml"); }

    @FXML
    private void moHuy(ActionEvent actionEvent) { // Nút Trả phòng
        ArrayList<Phong> dsTarget = layDanhSachPhongTarget();

        if (dsTarget.isEmpty()) {
            ThongBaoUtil.hienThiCanhBao("Thông báo", "Vui lòng chọn phòng cần trả!");
            return;
        }

        Phong p = dsTarget.get(0);
        if (p.getTrangThai() != TrangThaiPhong.DANG_SU_DUNG) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Phòng " + p.getMaPhong() + " không đang sử dụng, không thể trả!");
            return;
        }
        chuyenSangManHinhTraPhong(p);
    }

    @FXML
    private void moDatPhong(ActionEvent actionEvent) {
        ArrayList<Phong> dsTarget = layDanhSachPhongTarget();

        if (dsTarget.isEmpty()) {
            ThongBaoUtil.hienThiCanhBao("Thông báo", "Vui lòng chọn phòng để đặt!");
            return;
        }

        LocalDate ngayDenVal = dpTuNgay.getValue() != null ? dpTuNgay.getValue() : LocalDate.now();
        LocalDate ngayDiVal = dpDenNgay.getValue() != null ? dpDenNgay.getValue() : LocalDate.now().plusDays(1);
        if (switcher != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/dat-phong-view.fxml"));
                Parent root = loader.load();

                DatPhongController ctrl = loader.getController();
                ctrl.nhanDuLieuTuPhongView(dsTarget, ngayDenVal, ngayDiVal);

                
                switcher.switchContent(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void moDatTT(ActionEvent actionEvent) {
        ArrayList<Phong> dsTarget = layDanhSachPhongTarget();
        if (switcher != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/dat-phong-truc-tiep-view.fxml"));
                Parent root = loader.load();
                DatPhongTaiQuayController ctrl = loader.getController();
                if (!dsTarget.isEmpty()) ctrl.nhanDanhSachPhongDaChon(dsTarget);
                switcher.switchContent(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}