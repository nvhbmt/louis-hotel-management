package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.NhanVienDAO;
import com.example.louishotelmanagement.model.NhanVien;
import com.example.louishotelmanagement.ui.components.CustomButton;
import com.example.louishotelmanagement.ui.models.ButtonVariant;
import com.example.louishotelmanagement.view.NhanVienDialogView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class QuanLyNhanVienController implements Initializable {

    // --- FXML Components ---
    @FXML
    private Label lblTongTaiKhoan;
    @FXML
    private Label lblTaiKhoanHoatDong; // Sẽ dùng cho Lễ tân
    @FXML
    private Label lblTaiKhoanBiKhoa;   // Sẽ dùng cho Nhân viên khác
    @FXML
    private Label lblTaiKhoanManager;  // Sẽ dùng cho Quản lý
    @FXML
    private Button btnThemNV;
    @FXML
    private TextField timKiemField;
    @FXML
    private ComboBox<String> cbxChucVu;
    @FXML
    private Button btnLamMoi;
    @FXML
    private TableView<NhanVien> nhanVienTable;
    @FXML
    private TableColumn<NhanVien, String> colMaNV;
    @FXML
    private TableColumn<NhanVien, String> colHoTen;
    @FXML
    private TableColumn<NhanVien, LocalDate> colNgaySinh; // Kiểu LocalDate
    @FXML
    private TableColumn<NhanVien, String> colDiaChi;
    @FXML
    private TableColumn<NhanVien, String> colSDT;
    @FXML
    private TableColumn<NhanVien, String> colChucVu;
    @FXML
    private TableColumn<NhanVien, Void> colThaoTac;

    // --- Data and DAO ---
    private NhanVienDAO nhanVienDAO;
    private ObservableList<NhanVien> danhSachNhanVien; // Danh sách gốc
    private FilteredList<NhanVien> filteredNhanVienList; // Danh sách hiển thị

    // Định dạng ngày
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nhanVienDAO = new NhanVienDAO();

        cauHinhBang();
        khoiTaoComboBox();
        thietLapBoLoc();

        // Thêm sự kiện cho các nút
        btnThemNV.setOnAction(this::handleThem);
        btnLamMoi.setOnAction(this::handleLamMoi);

        taiDuLieuNhanVien();
    }

    /**
     * Cấu hình TableView, các cột, định dạng và nút thao tác.
     */
    private void cauHinhBang() {
        danhSachNhanVien = FXCollections.observableArrayList();
        filteredNhanVienList = new FilteredList<>(danhSachNhanVien, p -> true);
        nhanVienTable.setItems(filteredNhanVienList);

        // Liên kết các cột dữ liệu
        colMaNV.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colNgaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("soDT")); // Đổi tên thuộc tính model cho khớp
        colChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));

        // Định dạng cột Ngày sinh
        colNgaySinh.setCellFactory(column -> new TableCell<NhanVien, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });

        // Cấu hình cột Thao tác (Nút Sửa / Xóa)
        colThaoTac.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = CustomButton.createButton("Sửa", ButtonVariant.INFO);
            private final Button btnDelete = CustomButton.createButton("Xóa", ButtonVariant.DANGER);
            private final HBox pane = new HBox(5, btnEdit, btnDelete);

            {
                pane.setAlignment(Pos.CENTER);

                btnEdit.setOnAction(event -> {
                    NhanVien nv = getTableView().getItems().get(getIndex());
                    handleSua(nv);
                });
                btnDelete.setOnAction(event -> {
                    NhanVien nv = getTableView().getItems().get(getIndex());
                    handleXoa(nv);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    /**
     * Khởi tạo ComboBox Chức vụ.
     */
    private void khoiTaoComboBox() {
        // Tạm thời dùng danh sách cố định, bạn có thể lấy từ CSDL nếu muốn
        ObservableList<String> chucVuList = FXCollections.observableArrayList(
                "Tất cả chức vụ", "Quản lý", "Lễ tân", "Nhân viên" // Thêm các chức vụ khác nếu có
        );
        cbxChucVu.setItems(chucVuList);
        cbxChucVu.setValue("Tất cả chức vụ");
    }

    /**
     * Thiết lập bộ lọc tự động cho TableView.
     */
    private void thietLapBoLoc() {
        filteredNhanVienList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            String timKiem = timKiemField.getText().toLowerCase().trim();
            String chucVu = cbxChucVu.getValue();

            return nhanVien -> {
                // Điều kiện lọc 1: Tìm kiếm (Mã, Tên, SĐT)
                boolean timKiemMatch = timKiem.isEmpty() ||
                        nhanVien.getMaNV().toLowerCase().contains(timKiem) ||
                        nhanVien.getHoTen().toLowerCase().contains(timKiem) ||
                        nhanVien.getSoDT().toLowerCase().contains(timKiem);

                // Điều kiện lọc 2: Chức vụ
                boolean chucVuMatch = (chucVu == null || chucVu.equals("Tất cả chức vụ")) ||
                        (nhanVien.getChucVu() != null && nhanVien.getChucVu().equals(chucVu));

                return timKiemMatch && chucVuMatch;
            };
        }, timKiemField.textProperty(), cbxChucVu.valueProperty()));
    }

    /**
     * Tải dữ liệu nhân viên từ CSDL và cập nhật bảng, thống kê.
     */
    private void taiDuLieuNhanVien() {
        try {
            ArrayList<NhanVien> ds = nhanVienDAO.layDSNhanVien();
            danhSachNhanVien.setAll(ds);
            capNhatThongKe();
        } catch (SQLException e) {
            e.printStackTrace();
            hienThiLoi("Lỗi Tải Dữ Liệu", "Không thể tải danh sách nhân viên: " + e.getMessage());
        }
    }

    /**
     * Cập nhật các Label thống kê.
     */
    private void capNhatThongKe() {
        long tongSo = danhSachNhanVien.size();
        // Đếm dựa trên giá trị cột 'chucVu' (chú ý chữ hoa/thường)
        long soLeTan = danhSachNhanVien.stream().filter(nv -> "Lễ tân".equalsIgnoreCase(nv.getChucVu())).count();
        long soQuanLy = danhSachNhanVien.stream().filter(nv -> "Quản lý".equalsIgnoreCase(nv.getChucVu())).count();
        long soKhac = tongSo - soLeTan - soQuanLy; // Số còn lại là nhân viên khác

        lblTongTaiKhoan.setText(String.valueOf(tongSo));
        lblTaiKhoanHoatDong.setText(String.valueOf(soLeTan)); // Dùng cho Lễ tân
        lblTaiKhoanBiKhoa.setText(String.valueOf(soKhac));    // Dùng cho Nhân viên khác
        lblTaiKhoanManager.setText(String.valueOf(soQuanLy));  // Dùng cho Quản lý
    }

    // --- Event Handlers ---

    @FXML
    private void handleThem(ActionEvent event) {
        // Mở dialog/cửa sổ thêm nhân viên
        moDialogNhanVien(null); // Truyền null để biết là chế độ THÊM
    }

    private void handleSua(NhanVien nhanVien) {
        // Mở dialog/cửa sổ sửa nhân viên, truyền dữ liệu nhân viên vào
        moDialogNhanVien(nhanVien); // Truyền nhân viên để biết là chế độ SỬA
    }

    private void handleXoa(NhanVien nhanVien) {
        // Hiển thị hộp thoại xác nhận
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác Nhận Xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa nhân viên này?");
        confirmAlert.setContentText("Nhân viên: " + nhanVien.getHoTen() + " (" + nhanVien.getMaNV() + ")");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean success = nhanVienDAO.XoaNhanVien(nhanVien.getMaNV());
                if (success) {
                    hienThiThongBao("Thành Công", "Đã xóa nhân viên thành công.");
                    taiDuLieuNhanVien(); // Tải lại dữ liệu sau khi xóa
                } else {
                    hienThiLoi("Thất Bại", "Không thể xóa nhân viên.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                hienThiLoi("Lỗi CSDL", "Không thể xóa nhân viên: " + e.getMessage());
                // Cần kiểm tra xem có ràng buộc khóa ngoại không (ví dụ: nhân viên đã tạo hóa đơn)
            }
        }
    }

    @FXML
    private void handleLamMoi(ActionEvent event) {
        timKiemField.clear();
        cbxChucVu.setValue("Tất cả chức vụ");
        taiDuLieuNhanVien(); // Tải lại dữ liệu gốc
    }

    /**
     * Mở Dialog để thêm hoặc sửa thông tin nhân viên.
     *
     * @param nhanVienToEdit Nhân viên cần sửa (null nếu là thêm mới).
     */
    private void moDialogNhanVien(NhanVien nhanVienToEdit) {
        NhanVienDialogView view = new NhanVienDialogView();
        NhanVienDialogController controller = new NhanVienDialogController(view);
        Parent root = view.getRoot();
        controller.setNhanVien(nhanVienToEdit); // Truyền dữ liệu vào dialog

        Stage dialogStage = new Stage();
        dialogStage.setTitle(nhanVienToEdit == null ? "Thêm Nhân Viên Mới" : "Sửa Thông Tin Nhân Viên");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        // dialogStage.initOwner(btnThemNV.getScene().getWindow()); // Đặt cửa sổ cha (tùy chọn)
        dialogStage.setScene(new Scene(root));

        dialogStage.showAndWait(); // Hiển thị dialog và đợi đóng

        // Sau khi dialog đóng, tải lại dữ liệu nếu có thay đổi
        // (Bạn có thể làm logic này phức tạp hơn để chỉ tải lại nếu có thay đổi thực sự)
        taiDuLieuNhanVien();

    }

    // --- Utility Methods ---

    private void hienThiThongBao(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void hienThiLoi(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}