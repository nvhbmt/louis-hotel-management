package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.util.ContentSwitcher;
import com.example.louishotelmanagement.util.Refreshable;
import com.example.louishotelmanagement.util.ThongBaoUtil;
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
    @FXML
    public Label SoPhongDaChon;
    @FXML
    public Label TongTien;
    @FXML
    public Label lbSoDem;
    @FXML
    public Label lbLoaiDatPhong;
    @FXML
    public Label lbLoadingPhong;
    @FXML
    public Label lbSoPhongTrong;
    @FXML
    public Label lbTongSoDem;
    @FXML
    public VBox vbEmptyState;
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
    private ContentSwitcher contentSwitcher;

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
            // Không gọi taiDuLieu() ngay, chờ user chọn ngày
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

// Trong lớp DatPhongController.java

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
        ngayDen.setConverter(converter);
        ngayDi.setConverter(converter);

        // *Tùy chọn:* Đảm bảo DatePicker có thể hiển thị ngày hôm nay nếu người dùng chưa chọn
        // ngayDen.setValue(LocalDate.now());
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
        colDaChon.setCellFactory(_ -> new TableCell<>() {
            private CheckBox checkBox = new CheckBox();

            {

            }

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
            // Chỉ gọi khi cả ngayDen và ngayDi đã được chọn
            if (ngayDen.getValue() == null || ngayDi.getValue() == null) {
                danhSachPhong.clear();
                danhSachPhongFiltered.clear();
                capNhatUIKhiKhongCoDuLieu();
                return;
            }

            // Validate ngày
            if (!validateNgay()) {
                danhSachPhong.clear();
                danhSachPhongFiltered.clear();
                capNhatUIKhiKhongCoDuLieu();
                return;
            }

            // Hiển thị loading indicator
            hienThiLoading(true);

            // Lấy danh sách phòng trống theo khoảng thời gian
            List<Phong> dsPhong = Pdao.layDSPhongTrongTheoKhoangThoiGian(
                    ngayDen.getValue(),
                    ngayDi.getValue()
            );

            danhSachPhong.clear();
            danhSachPhong.addAll(dsPhong);

            // Áp dụng filter hiện tại
            apDungFilter();

            // Cập nhật UI
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

    /**
     * Hiển thị hoặc ẩn loading indicator khi đang tải dữ liệu phòng.
     *
     * @param isLoading true để hiển thị loading, false để ẩn
     */
    private void hienThiLoading(boolean isLoading) {
        if (lbLoadingPhong != null) {
            lbLoadingPhong.setVisible(isLoading);
            lbLoadingPhong.setText(isLoading ? "Đang tìm phòng trống..." : "");
        }
    }

    /**
     * Tính số đêm giữa ngày đến và ngày đi.
     *
     * @return Số đêm, hoặc 0 nếu ngày không hợp lệ hoặc null
     */
    private long tinhSoDem() {
        if (ngayDen.getValue() == null || ngayDi.getValue() == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(ngayDen.getValue(), ngayDi.getValue());
    }

    /**
     * Tính tổng tiền cho danh sách phòng dựa trên số đêm.
     *
     * @param danhSachPhong Danh sách phòng cần tính tiền
     * @param soDem         Số đêm lưu trú
     * @return Tổng tiền (VNĐ)
     */
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

    /**
     * Cập nhật tất cả các thông tin UI dựa trên trạng thái hiện tại.
     * Bao gồm: số đêm, loại đặt phòng, số phòng trống, empty state, và tổng tiền.
     */
    private void capNhatUIThongTin() {
        // Cập nhật số đêm
        long soDem = tinhSoDem();
        if (soDem > 0) {
            if (lbSoDem != null) {
                lbSoDem.setText("(" + soDem + " đêm)");
            }
            if (lbTongSoDem != null) {
                lbTongSoDem.setText(String.valueOf(soDem));
            }
        } else {
            if (lbSoDem != null) {
                lbSoDem.setText("");
            }
            if (lbTongSoDem != null) {
                lbTongSoDem.setText("0");
            }
        }

        // Cập nhật loại đặt phòng
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

        // Cập nhật số phòng trống
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

        // Hiển thị/ẩn empty state
        if (vbEmptyState != null && tablePhong != null) {
            boolean coPhong = !danhSachPhongFiltered.isEmpty();
            tablePhong.setVisible(coPhong);
            vbEmptyState.setVisible(!coPhong);
        }

        // Cập nhật tổng tiền với số đêm
        capNhatTongTien();
    }

    /**
     * Cập nhật UI khi không có dữ liệu (ngày chưa chọn hoặc không hợp lệ).
     * Ẩn các thông tin và hiển thị empty state.
     */
    private void capNhatUIKhiKhongCoDuLieu() {
        if (lbSoDem != null) {
            lbSoDem.setText("");
        }
        if (lbLoaiDatPhong != null) {
            lbLoaiDatPhong.setVisible(false);
        }
        if (lbSoPhongTrong != null) {
            lbSoPhongTrong.setText("");
        }
        if (lbTongSoDem != null) {
            lbTongSoDem.setText("0");
        }
        if (vbEmptyState != null && tablePhong != null) {
            tablePhong.setVisible(false);
            vbEmptyState.setVisible(true);
        }
    }

    /**
     * Cập nhật tổng tiền và số phòng đã chọn trên UI.
     * Tính tổng tiền dựa trên số đêm thực tế nếu có ngày đến/đi,
     * ngược lại tính theo giá 1 đêm.
     */
    private void capNhatTongTien() {
        if (listPhongDuocDat.isEmpty()) {
            if (TongTien != null) {
                TongTien.setText("0 VNĐ");
            }
            if (SoPhongDaChon != null) {
                SoPhongDaChon.setText("0");
            }
            // lbTongSoDem đã được cập nhật trong capNhatUIThongTin(), không cần update lại
            return;
        }

        // Tính tổng tiền dựa trên số đêm
        long soDem = tinhSoDem();
        double tongTien;
        if (soDem > 0) {
            tongTien = tinhTongTienTheoSoDem(listPhongDuocDat, soDem);
        } else {
            // Fallback: tính theo giá phòng (1 đêm)
            tongTien = TinhTongTien(listPhongDuocDat);
        }

        if (TongTien != null) {
            TongTien.setText(String.format("%,.0f VNĐ", tongTien));
        }
        if (SoPhongDaChon != null) {
            SoPhongDaChon.setText(String.valueOf(listPhongDuocDat.size()));
        }
    }

    private boolean validateNgay() {
        LocalDate ngayDenValue = ngayDen.getValue();
        LocalDate ngayDiValue = ngayDi.getValue();

        if (ngayDenValue == null || ngayDiValue == null) {
            return false;
        }

        // Kiểm tra ngày đến không được trong quá khứ
        if (ngayDenValue.isBefore(LocalDate.now())) {
            ThongBaoUtil.hienThiLoi("Lỗi",
                    "Không được chọn ngày đến trước ngày hôm nay");
            return false;
        }

        // Kiểm tra ngày đi phải sau ngày đến
        if (ngayDiValue.isBefore(ngayDenValue) || ngayDiValue.isEqual(ngayDenValue)) {
            ThongBaoUtil.hienThiLoi("Lỗi",
                    "Ngày đi phải sau ngày đến ít nhất 1 ngày");
            return false;
        }

        return true;
    }

    private void khoiTaoDatePickerListeners() {
        // Disable các ngày trong quá khứ cho ngayDen (chỉ set một lần)
        ngayDen.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date != null && date.isBefore(LocalDate.now()));
            }
        });

        // Listener cho ngayDen
        ngayDen.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Cập nhật UI thông tin
                capNhatUIThongTin();
                // Validate và cập nhật danh sách phòng
                if (ngayDi.getValue() != null) {
                    taiDuLieu();
                }
            } else {
                danhSachPhong.clear();
                danhSachPhongFiltered.clear();
                capNhatUIKhiKhongCoDuLieu();
            }
        });

        // Listener cho ngayDi
        ngayDi.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && ngayDen.getValue() != null) {
                // Validate ngayDi > ngayDen
                if (newValue.isBefore(ngayDen.getValue()) ||
                        newValue.isEqual(ngayDen.getValue())) {
                    ThongBaoUtil.hienThiLoi("Lỗi",
                            "Ngày đi phải sau ngày đến ít nhất 1 ngày");
                    ngayDi.setValue(oldValue);
                    return;
                }

                // Cập nhật UI thông tin
                capNhatUIThongTin();
                taiDuLieu();
            } else if (newValue == null) {
                danhSachPhong.clear();
                danhSachPhongFiltered.clear();
                capNhatUIKhiKhongCoDuLieu();
            }
        });
    }

    /**
     * Kiểm tra danh sách phòng có còn trống không (tránh N+1 query problem).
     * Gọi stored procedure MỘT LẦN để check tất cả phòng.
     *
     * @param danhSachPhongCanKiemTra Danh sách phòng cần kiểm tra
     * @return Danh sách phòng còn trống
     * @throws SQLException Nếu có lỗi khi truy vấn database
     */
    private ArrayList<Phong> kiemTraDanhSachPhongTrong(ArrayList<Phong> danhSachPhongCanKiemTra) throws SQLException {
        if (danhSachPhongCanKiemTra == null || danhSachPhongCanKiemTra.isEmpty()) {
            return new ArrayList<>();
        }

        if (ngayDen.getValue() == null || ngayDi.getValue() == null) {
            return new ArrayList<>();
        }

        // Gọi stored procedure MỘT LẦN để lấy tất cả phòng trống
        List<Phong> phongTrong = Pdao.layDSPhongTrongTheoKhoangThoiGian(
                ngayDen.getValue(),
                ngayDi.getValue()
        );

        // Tạo Set để lookup nhanh O(1) thay vì O(n)
        Set<String> maPhongTrong = phongTrong.stream()
                .filter(p -> p != null && p.getMaPhong() != null)
                .map(Phong::getMaPhong)
                .collect(Collectors.toSet());

        // Filter phòng còn trống từ danh sách cần kiểm tra
        ArrayList<Phong> ketQua = new ArrayList<>();
        for (Phong phong : danhSachPhongCanKiemTra) {
            if (phong != null && phong.getMaPhong() != null &&
                    maPhongTrong.contains(phong.getMaPhong())) {
                ketQua.add(phong);
            }
        }

        return ketQua;
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

    public double TinhTongTien(ArrayList<Phong> ls) {
        double tongTien = 0;
        for (Phong phong : ls) {
            tongTien += phong.getLoaiPhong().getDonGia();
        }
        return tongTien;
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
        listPhongDuocDat.clear();
        cbTang.setValue(null);
        cbLocLoaiPhong.setValue(null);
        capNhatUIKhiKhongCoDuLieu();
        // Không gọi taiDuLieu() vì chưa có ngày
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

        // 0.5. DOUBLE-CHECK PHÒNG TRƯỚC KHI ĐẶT (Race condition protection)
        // Fix N+1 query: Check tất cả phòng một lần thay vì từng phòng
        ArrayList<Phong> phongConTrong;
        try {
            phongConTrong = kiemTraDanhSachPhongTrong(listPhongDuocDat);
        } catch (SQLException e) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể kiểm tra phòng trống. Vui lòng thử lại.");
            e.printStackTrace();
            return;
        }

        // Nếu số phòng còn trống khác số phòng đã chọn -> có phòng đã bị đặt
        if (phongConTrong.size() != listPhongDuocDat.size()) {
            ArrayList<Phong> phongKhongTrong = new ArrayList<>(listPhongDuocDat);
            phongKhongTrong.removeAll(phongConTrong);

            StringBuilder danhSachPhong = new StringBuilder();
            for (int i = 0; i < phongKhongTrong.size(); i++) {
                if (i > 0) danhSachPhong.append(", ");
                danhSachPhong.append(phongKhongTrong.get(i).getMaPhong());
            }
            ThongBaoUtil.hienThiLoi("Lỗi",
                    "Các phòng sau đã được đặt bởi khách hàng khác: " + danhSachPhong.toString() +
                            ". Vui lòng chọn phòng khác.");
            // Refresh danh sách phòng
            taiDuLieu();
            return;
        }

        // 1. GỌI DIALOG TIỀN CỌC
        // Tính tổng tiền dựa trên số đêm
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

        // 2. PHÂN BIỆT ĐẶT TRỰC TIẾP VÀ ĐẶT TRƯỚC
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

        // 3. TẠO VÀ LƯU PHIẾU ĐẶT PHÒNG GỐC (CHỈ 1 LẦN)
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
        pdp.setTienCoc(result.tienCoc); // Gán tiền cọc đã nhập từ dialog

        pdpDao.themPhieuDatPhong(pdp); // 👈 LƯU PHIẾU GỐC

        // 3. TẠO VÀ LƯU HÓA ĐƠN GỐC (CHỈ 1 LẦN)
        HoaDon hd = new HoaDon(hDao.taoMaHoaDonTiepTheo(), LocalDate.now(), null, TrangThaiHoaDon.CHUA_THANH_TOAN, null, newKh.getMaKH(), maNV, null);
        hDao.themHoaDon(hd); // 👈 LƯU HÓA ĐƠN

        // 4. LẶP QUA TỪNG PHÒNG ĐỂ TẠO CHI TIẾT VÀ CẬP NHẬT TRẠNG THÁI
        // Xử lý lỗi nếu có phòng bị conflict trong lúc insert (race condition)
        ArrayList<String> phongBiConflict = new ArrayList<>();
        for (Phong p : listPhongDuocDat) {
            try {
                ThemChiTietPhong(pdp, hd, p);
            } catch (SQLException e) {
                // Nếu lỗi là conflict (phòng đã được đặt), ghi lại để thông báo
                if (e.getMessage() != null && e.getMessage().contains("đã được đặt")) {
                    phongBiConflict.add(p.getMaPhong());
                } else {
                    // Lỗi khác, throw lại
                    throw e;
                }
            }
        }

        // Nếu có phòng bị conflict, rollback và thông báo
        if (!phongBiConflict.isEmpty()) {
            // TODO: Implement rollback transaction nếu cần
            StringBuilder danhSachPhong = new StringBuilder();
            for (int i = 0; i < phongBiConflict.size(); i++) {
                if (i > 0) danhSachPhong.append(", ");
                danhSachPhong.append(phongBiConflict.get(i));
            }
            ThongBaoUtil.hienThiLoi("Lỗi",
                    "Các phòng sau đã được đặt bởi khách hàng khác trong lúc xử lý: " +
                            danhSachPhong.toString() + ". Vui lòng thử lại.");
            // Refresh và return
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

    // Hàm mới để xử lý chi tiết (thay thế logic trong DatPhong cũ)
    public void ThemChiTietPhong(PhieuDatPhong pdp, HoaDon hd, Phong p) throws SQLException {
        CTHoaDonPhong cthdp = new CTHoaDonPhong(
                hd.getMaHD(),
                pdp.getMaPhieu(),
                p.getMaPhong(),
                pdp.getNgayDen(), // Ngày đến từ phiếu đặt phòng
                pdp.getNgayDi(),  // Ngày đi từ phiếu đặt phòng
                BigDecimal.valueOf(p.getLoaiPhong().getDonGia())
        );

        // Cập nhật trạng thái phòng dựa trên loại đặt phòng
        if (pdp.getTrangThai() != null &&
                pdp.getTrangThai().equals(TrangThaiPhieuDatPhong.DANG_SU_DUNG)) {
            // Đặt trực tiếp -> Cập nhật thành "Đang sử dụng"
            Pdao.capNhatTrangThaiPhong(p.getMaPhong(), TrangThaiPhong.DANG_SU_DUNG.toString());
        } else {
            // Đặt trước -> Cập nhật thành "Đã đặt"
            Pdao.capNhatTrangThaiPhong(p.getMaPhong(), TrangThaiPhong.DA_DAT.toString());
        }

        cthdpDao.themCTHoaDonPhong(cthdp);
    }

    public void handleRefresh(ActionEvent actionEvent) throws SQLException {
        refreshData();
    }

    public void handleThemKhachHang(ActionEvent actionEvent) {
        try {
            // 1. Tải FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/khach-hang-form-dialog.fxml"));
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

    /**
     * Thiết lập ContentSwitcher để điều hướng giữa các màn hình
     *
     * @param contentSwitcher ContentSwitcher instance
     */
    public void setContentSwitcher(ContentSwitcher contentSwitcher) {
        this.contentSwitcher = contentSwitcher;
    }

    /**
     * Nhận dữ liệu phòng đã chọn và khoảng thời gian từ PhongController
     *
     * @param phongDaChon Danh sách phòng đã chọn
     * @param tuNgay      Ngày bắt đầu
     * @param denNgay     Ngày kết thúc
     */
    public void nhanDuLieuTuPhongView(ArrayList<Phong> phongDaChon, LocalDate tuNgay, LocalDate denNgay) {
        // Set ngày
        if (tuNgay != null) {
            ngayDen.setValue(tuNgay);
        }
        if (denNgay != null) {
            ngayDi.setValue(denNgay);
        }

        // Load dữ liệu phòng trống theo ngày
        taiDuLieu();

        // Tự động thêm các phòng đã chọn vào list
        if (phongDaChon != null && !phongDaChon.isEmpty()) {
            listPhongDuocDat.clear();
            for (Phong phong : phongDaChon) {
                // Tìm phòng trong danh sách hiện tại
                Phong phongTrongList = danhSachPhong.stream()
                        .filter(p -> p.getMaPhong().equals(phong.getMaPhong()))
                        .findFirst()
                        .orElse(null);

                if (phongTrongList != null) {
                    listPhongDuocDat.add(phongTrongList);
                }
            }

            // Cập nhật UI
            capNhatTongTien();
            tablePhong.refresh();

            ThongBaoUtil.hienThiThongBao("Thành công",
                    "Đã chọn " + listPhongDuocDat.size() + " phòng. Vui lòng chọn khách hàng!");
        }
    }
}
