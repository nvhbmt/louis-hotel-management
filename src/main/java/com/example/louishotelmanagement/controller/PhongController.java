package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import com.example.louishotelmanagement.util.ContentSwitcher;


public class PhongController implements Initializable {

    // --- FXML Components ---
    @FXML
    private Label tieuDeLabel;
    @FXML
    private Button btnNhanPhong, btnDatTT, btnDoiPhong, btnHuyDat, btnDichVu, btnThanhToan;
    @FXML
    private ComboBox<String> cbxTang;
    @FXML
    private ComboBox<String> cbxTrangThai;
    @FXML
    private DatePicker dpTuNgay;
    @FXML
    private DatePicker dpDenNgay;
    @FXML
    private TableView<Phong> tableViewPhong;
    @FXML
    private TableColumn<Phong, Boolean> chonPhong;
    @FXML
    private TableColumn<Phong, String> maPhong;
    @FXML
    private TableColumn<Phong, String> loaiPhong;
    @FXML
    private TableColumn<Phong, Double> donGia;
    @FXML
    private TableColumn<Phong, TrangThaiPhong> trangThai;
    @FXML
    private Label lblTongPhong;
    @FXML
    private Label lblPhongTrong;
    @FXML
    private Label lblPhongSuDung;
    @FXML
    private Label lblPhongBaoTri;

    // --- Data and DAO ---
    private PhongDAO phongDAO;
    private ObservableList<Phong> phongObservableList; // Danh sách gốc (chứa tất cả)
    private FilteredList<Phong> filteredPhongList;   // Danh sách đã lọc (để hiển thị)

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        phongDAO = new PhongDAO();
        System.out.println("Phong page initialized");

        cauHinhBang();
        cauHinhCBX();
        cauHinhDatePicker(); // Thiết lập listener cho DatePicker
        cauHinhLoc(); // Thiết lập bộ lọc tự động
        taiBang();
    }

    /**
     * Cấu hình cột, định dạng ô, và gán danh sách đã lọc cho TableView.
     */
    private void cauHinhBang() {
        phongObservableList = FXCollections.observableArrayList();
        filteredPhongList = new FilteredList<>(phongObservableList, p -> true);
        tableViewPhong.setItems(filteredPhongList);
        
        // Cột Checkbox
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
                    
                    // Disable checkbox nếu phòng không TRONG hoặc không có ngày chọn
                    boolean coTheChon = kiemTraCoTheChonPhong(phong);
                    checkBox.setDisable(!coTheChon);
                    
                    // Nếu disable, bỏ chọn
                    if (!coTheChon && phong.isSelected()) {
                        phong.setSelected(false);
                        checkBox.setSelected(false);
                    }
                    
                    setGraphic(checkBox);
                }
            }
        });
        
        //các cột
        maPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));

        loaiPhong.setCellValueFactory(cellData -> {
            LoaiPhong lp = cellData.getValue().getLoaiPhong();
            return new SimpleStringProperty(lp != null ? lp.getTenLoai() : "N/A");
        });


        donGia.setCellValueFactory(cellData -> {
            LoaiPhong lp = cellData.getValue().getLoaiPhong();
            Double gia = (lp != null) ? lp.getDonGia() : 0.0;
            return new SimpleObjectProperty<>(gia);
        });

        // Định dạng
        donGia.setCellFactory(column -> new TableCell<Phong, Double>() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });

        // Cột Trạng Thái
        trangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        trangThai.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(TrangThaiPhong item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-alignment: CENTER;");
                } else {
                    Phong phong = getTableView().getItems().get(getIndex());

                    // Lấy trạng thái theo khoảng thời gian
                    TrangThaiPhong trangThaiHienThi = layTrangThaiPhongTheoNgay(phong);

                    // Tạo badge label
                    Label badge = new Label(trangThaiHienThi.toString());
                    
                    // Style cho badge theo trạng thái
                    String backgroundColor, textColor;
                    switch (trangThaiHienThi) {
                        case TRONG -> {
                            backgroundColor = "#d1fae5"; // Xanh lá nhạt
                            textColor = "#065f46";       // Xanh lá đậm
                        }
                        case DANG_SU_DUNG -> {
                            backgroundColor = "#fef3c7"; // Vàng nhạt
                            textColor = "#92400e";       // Vàng đậm
                        }
                        case DA_DAT -> {
                            backgroundColor = "#fce7f3"; // Hồng nhạt
                            textColor = "#9f1239";       // Hồng đậm
                        }
                        case BAO_TRI -> {
                            backgroundColor = "#dbeafe"; // Xanh dương nhạt
                            textColor = "#1e40af";       // Xanh dương đậm
                        }
                        default -> {
                            backgroundColor = "#f3f4f6";
                            textColor = "#6b7280";
                        }
                    }
                    
                    // Nếu có chọn ngày và phòng trống, highlight đậm hơn
                    if (dpTuNgay.getValue() != null && dpDenNgay.getValue() != null
                        && trangThaiHienThi == TrangThaiPhong.TRONG) {
                        badge.setStyle(
                            "-fx-background-color: " + backgroundColor + ";" +
                            "-fx-text-fill: " + textColor + ";" +
                            "-fx-padding: 6px 16px;" +
                            "-fx-background-radius: 12px;" +
                            "-fx-font-size: 12px;" +
                            "-fx-font-weight: bold;"
                        );
                    } else {
                        badge.setStyle(
                            "-fx-background-color: " + backgroundColor + ";" +
                            "-fx-text-fill: " + textColor + ";" +
                            "-fx-padding: 6px 16px;" +
                            "-fx-background-radius: 12px;" +
                            "-fx-font-size: 12px;" +
                            "-fx-font-weight: 600;"
                        );
                    }
                    
                    setText(null);
                    setGraphic(badge);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }


    public void taiBang() {
        try {
            // Luôn lấy TẤT CẢ phòng
            ArrayList<Phong> dsPhong = phongDAO.layDSPhong();
            
            // Nếu có chọn khoảng thời gian, validate
            if (dpTuNgay.getValue() != null && dpDenNgay.getValue() != null) {
                if (dpDenNgay.getValue().isBefore(dpTuNgay.getValue())) {
                    ThongBaoUtil.hienThiLoi("Lỗi", "Ngày kết thúc phải sau ngày bắt đầu!");
                    return;
                }
            }
            
            phongObservableList.clear();
            phongObservableList.addAll(dsPhong);
            capNhatThongKe();
            
            // Refresh table để cập nhật checkbox state và trạng thái
            tableViewPhong.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể tải dữ liệu phòng: " + e.getMessage());
        }
    }


    private void cauHinhCBX() {
        ObservableList<String> danhSachTang = FXCollections.observableArrayList();
        danhSachTang.add("Tất cả các tầng"); // Thêm tùy chọn "Tất cả"
        int soTangKhachSan = 4;
        for (int i = 1; i <= soTangKhachSan; i++) {
            danhSachTang.add("Tầng " + i);
        }
        cbxTang.setItems(danhSachTang);
        cbxTang.setValue("Tất cả các tầng");

        // --- Setup ComboBox Trạng Thái ---
        ObservableList<String> danhSachTrangThai = FXCollections.observableArrayList();
        danhSachTrangThai.add("Tất cả trạng thái");
        // Thêm các trạng thái từ Enum
        danhSachTrangThai.add(TrangThaiPhong.TRONG.toString());
        danhSachTrangThai.add(TrangThaiPhong.DANG_SU_DUNG.toString());
        danhSachTrangThai.add(TrangThaiPhong.DA_DAT.toString());
        danhSachTrangThai.add(TrangThaiPhong.BAO_TRI.toString());

        cbxTrangThai.setItems(danhSachTrangThai);
        cbxTrangThai.setValue("Tất cả trạng thái");
    }
    
    /**
     * Thiết lập listener cho DatePicker để tự động lọc khi chọn ngày
     */
    private void cauHinhDatePicker() {
        // Listener cho "Từ ngày"
        dpTuNgay.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && dpDenNgay.getValue() != null) {
                // Kiểm tra ngày hợp lệ
                if (dpDenNgay.getValue().isBefore(newVal)) {
                    ThongBaoUtil.hienThiCanhBao("Cảnh báo", "Ngày kết thúc phải sau ngày bắt đầu!");
                    dpTuNgay.setValue(oldVal);
                    return;
                }
                taiBang(); // Tải lại dữ liệu khi có thay đổi
            }
        });
        
        // Listener cho "Đến ngày"
        dpDenNgay.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && dpTuNgay.getValue() != null) {
                // Kiểm tra ngày hợp lệ
                if (newVal.isBefore(dpTuNgay.getValue())) {
                    ThongBaoUtil.hienThiCanhBao("Cảnh báo", "Ngày kết thúc phải sau ngày bắt đầu!");
                    dpDenNgay.setValue(oldVal);
                    return;
                }
                taiBang(); // Tải lại dữ liệu khi có thay đổi
            }
        });
    }


    private void cauHinhLoc() {
        filteredPhongList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            String tangDaChon = cbxTang.getValue();
            String trangThaiDaChon = cbxTrangThai.getValue();

            return phong -> {
                //tang
                boolean tangMatch = false;
                if (tangDaChon == null || tangDaChon.equals("Tất cả các tầng")) {
                    tangMatch = true;
                } else {
                    try {
                        int soTang = Integer.parseInt(tangDaChon.split("\\s+")[1]);
                        tangMatch = (phong.getTang() != null && phong.getTang() == soTang);
                    } catch (Exception e) {
                        tangMatch = false; // Lỗi parse thì không khớp
                    }
                }

                // trạng thái
                boolean trangThaiMatch = false;
                if (trangThaiDaChon == null || trangThaiDaChon.equals("Tất cả trạng thái")) {
                    trangThaiMatch = true;
                } else {
                    trangThaiMatch = (phong.getTrangThai() != null && phong.getTrangThai().toString().equals(trangThaiDaChon));
                }

                // Chỉ hiển thị khi CẢ HAI điều kiện đều đúng
                return tangMatch && trangThaiMatch;
            };
        }, cbxTang.valueProperty(), cbxTrangThai.valueProperty()));
    }



    private void capNhatThongKe() {
        long tongSo = phongObservableList.size();
        long soTrong = phongObservableList.stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.TRONG)
                .count();
        long soSuDung = phongObservableList.stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.DANG_SU_DUNG)
                .count();
        long soBaoTri = phongObservableList.stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.BAO_TRI)
                .count();

        // Cập nhật giao diện
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
        
        // Bỏ chọn tất cả các checkbox
        phongObservableList.forEach(phong -> phong.setSelected(false));
        
        taiBang();
        
        // Refresh table để cập nhật UI
        tableViewPhong.refresh();
    }
    // huyển trang
    private ContentSwitcher switcher;

    public void setContentSwitcher(ContentSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML
    private void moNhanPhong(ActionEvent actionEvent) {
        if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/nhan-phong-view.fxml");
    }

    /**
     * Lấy trạng thái phòng theo khoảng thời gian đã chọn
     * @param phong Phòng cần kiểm tra
     * @return Trạng thái phòng (Trống hoặc trạng thái gốc)
     */
    private TrangThaiPhong layTrangThaiPhongTheoNgay(Phong phong) {
        // Nếu không chọn khoảng thời gian, trả về trạng thái gốc
        if (dpTuNgay.getValue() == null || dpDenNgay.getValue() == null) {
            return phong.getTrangThai();
        }
        
        // Kiểm tra phòng có trống trong khoảng thời gian không
        try {
            boolean isTrong = phongDAO.kiemTraPhongTrongTheoKhoangThoiGian(
                phong.getMaPhong(), 
                dpTuNgay.getValue(), 
                dpDenNgay.getValue()
            );
            
            return isTrong ? TrangThaiPhong.TRONG : TrangThaiPhong.DA_DAT;
        } catch (SQLException e) {
            e.printStackTrace();
            return phong.getTrangThai(); // Fallback về trạng thái gốc
        }
    }
    
    /**
     * Kiểm tra xem phòng có thể được chọn hay không
     * @param phong Phòng cần kiểm tra
     * @return true nếu có thể chọn, false nếu không
     */
    private boolean kiemTraCoTheChonPhong(Phong phong) {
        // Nếu không chọn khoảng thời gian, chỉ cho phép chọn phòng TRONG
        if (dpTuNgay.getValue() == null || dpDenNgay.getValue() == null) {
            return phong.getTrangThai() == TrangThaiPhong.TRONG;
        }
        
        // Nếu có chọn khoảng thời gian, kiểm tra phòng có trống không
        try {
            return phongDAO.kiemTraPhongTrongTheoKhoangThoiGian(
                phong.getMaPhong(), 
                dpTuNgay.getValue(), 
                dpDenNgay.getValue()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lấy danh sách các phòng đã chọn
     */
    private ArrayList<Phong> layPhongDaChon() {
        ArrayList<Phong> dsPhongDaChon = new ArrayList<>();
        for (Phong phong : phongObservableList) {
            if (phong.isSelected()) {
                dsPhongDaChon.add(phong);
            }
        }
        return dsPhongDaChon;
    }

    @FXML
    private void moDatPhong(ActionEvent actionEvent) {
        ArrayList<Phong> dsPhongDaChon = layPhongDaChon();
        
        if (dsPhongDaChon.isEmpty()) {
            ThongBaoUtil.hienThiCanhBao("Thông báo", "Vui lòng chọn ít nhất một phòng để đặt trước!");
            return;
        }
        
        // Kiểm tra xem có chọn ngày hay không
        if (dpTuNgay.getValue() == null || dpDenNgay.getValue() == null) {
            ThongBaoUtil.hienThiCanhBao("Thông báo", "Vui lòng chọn khoảng thời gian đặt phòng!");
            return;
        }
        
        // Kiểm tra ngày hợp lệ
        if (dpDenNgay.getValue().isBefore(dpTuNgay.getValue())) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Ngày kết thúc phải sau ngày bắt đầu!");
            return;
        }
        
        if (switcher != null) {
            try {
                // Load FXML và truyền dữ liệu
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/dat-phong-view.fxml"));
                Parent root = loader.load();
                
                // Lấy Controller và truyền dữ liệu
                DatPhongController controller = loader.getController();
                controller.setContentSwitcher(switcher);
                
                // Truyền dữ liệu phòng đã chọn và khoảng thời gian
                controller.nhanDuLieuTuPhongView(dsPhongDaChon, dpTuNgay.getValue(), dpDenNgay.getValue());
                
                // Hiển thị
                switcher.switchContent(root);
            } catch (IOException e) {
                e.printStackTrace();
                ThongBaoUtil.hienThiLoi("Lỗi", "Không thể tải giao diện đặt phòng!");
            }
        }
    }

    @FXML
    private void moDatTT(ActionEvent actionEvent) {
        // Lấy danh sách phòng đã chọn (nếu có)
        ArrayList<Phong> dsPhongDaChon = layPhongDaChon();
        
        // Nếu có phòng được chọn và có chọn ngày, kiểm tra ngày phải là hôm nay
        if (!dsPhongDaChon.isEmpty() && dpTuNgay.getValue() != null) {
            if (!dpTuNgay.getValue().equals(LocalDate.now())) {
                ThongBaoUtil.hienThiCanhBao("Thông báo", "Đặt trực tiếp chỉ áp dụng cho ngày hôm nay!");
                return;
            }
        }
        
        if (switcher != null) {
            try {
                // Load FXML thủ công để truyền dữ liệu
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/dat-phong-truc-tiep-view.fxml"));
                Parent root = loader.load();
                
                // Lấy controller
                DatPhongTaiQuayController controller = loader.getController();
                
                // Nếu có phòng đã chọn, truyền sang
                if (!dsPhongDaChon.isEmpty()) {
                    controller.nhanDanhSachPhongDaChon(dsPhongDaChon);
                }
                
                // Hiển thị
                switcher.switchContent(root);
                
            } catch (IOException e) {
                e.printStackTrace();
                ThongBaoUtil.hienThiLoi("Lỗi", "Không thể tải giao diện đặt phòng trực tiếp!");
            }
        }
    }

    @FXML
    private void moDoiPhong(ActionEvent actionEvent) {
        if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/doi-phong-view.fxml");
    }

    @FXML
    private void moHuy(ActionEvent actionEvent) {
        // 1. Lấy phòng đang chọn từ TableView
        Phong selected = tableViewPhong.getSelectionModel().getSelectedItem();
        if (selected == null || !selected.getTrangThai().equals(TrangThaiPhong.DANG_SU_DUNG)) {
            ThongBaoUtil.hienThiLoi("Thông báo", "Vui lòng chọn một phòng đang sử dụng để trả phòng!");
            return;
        }

        if (switcher != null) {
            try {
                // 2. Load FXML thủ công
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/tra-phong-view.fxml"));
                Parent root = loader.load();

                // 3. Lấy Controller và nạp dữ liệu
                TraPhongController controller = loader.getController();

                // QUAN TRỌNG: Gán switcher lại cho màn hình mới để điều hướng menu không bị hỏng
                controller.setContentSwitcher(this.switcher);

                // Nạp dữ liệu phòng vào màn hình trả phòng
                controller.truyenDuLieuTuPhong(selected.getMaPhong());

                // 4. Hiển thị thông qua LayoutController (switcher)
                switcher.switchContent(root);

            } catch (IOException e) {
                e.printStackTrace();
                ThongBaoUtil.hienThiLoi("Lỗi", "Không thể tải giao diện trả phòng!");
            }
        }
    }

    @FXML
    private void moDichVu(ActionEvent actionEvent) {
        if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/dat-dich-vu-view.fxml");
    }

    @FXML
    private void moHuyDat(ActionEvent actionEvent) {
        if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/huy-dat-phong-view.fxml");
    }

    @FXML
    private void moThanhToan(ActionEvent actionEvent) {
        if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/thanh-toan-view.fxml");
    }
}