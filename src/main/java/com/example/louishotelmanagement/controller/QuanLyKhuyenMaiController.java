package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.KhuyenMaiDAO;
import com.example.louishotelmanagement.model.KhuyenMai;
import com.example.louishotelmanagement.model.KieuGiamGia;
import com.example.louishotelmanagement.ui.components.Badge;
import com.example.louishotelmanagement.ui.components.CustomButton;
import com.example.louishotelmanagement.ui.models.BadgeVariant;
import com.example.louishotelmanagement.ui.models.ButtonVariant;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.view.KhuyenMaiFormDialogView;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class QuanLyKhuyenMaiController implements Initializable {
    @FXML
    public Label lblTongSoKhuyenMai;
    @FXML
    public Label lblSoMaDangSuDung;
    @FXML
    public Label lblSoMaHetHan;
    @FXML
    public Label lblSoMaChuaBatDau;
    @FXML
    public Label lblSoMaVoHieuHoa;
    @FXML
    public BorderPane rootPane;
    @FXML
    public Label lblTongSoPhieu;
    @FXML
    public Button btnLamMoi;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private ComboBox<KieuGiamGia> cbKieuGiamGia;
    @FXML
    private ComboBox<String> cbTrangThai;

    @FXML
    private TableView<KhuyenMai> tableViewKhuyenMai;
    @FXML
    private TableColumn<KhuyenMai, String> colMaKM;
    @FXML
    private TableColumn<KhuyenMai, String> colCode;
    @FXML
    private TableColumn<KhuyenMai, Double> colGiamGia;
    @FXML
    private TableColumn<KhuyenMai, KieuGiamGia> colKieuGiamGia;
    @FXML
    private TableColumn<KhuyenMai, LocalDate> colNgayBatDau;
    @FXML
    private TableColumn<KhuyenMai, LocalDate> colNgayKetThuc;
    @FXML
    private TableColumn<KhuyenMai, Double> colTongTienToiThieu;
    @FXML
    private TableColumn<KhuyenMai, String> colTrangThai;
    @FXML
    private TableColumn<KhuyenMai, String> colMoTa;
    @FXML
    public TableColumn<KhuyenMai, Void> colThaoTac;

    private KhuyenMaiDAO khuyenMaiDAO;
    private ObservableList<KhuyenMai> danhSachKhuyenMai;
    private ObservableList<KhuyenMai> danhSachKhuyenMaiFiltered;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            khuyenMaiDAO = new KhuyenMaiDAO();

            // Khởi tạo dữ liệu
            khoiTaoDuLieu();
            khoiTaoTableView();
            khoiTaoComboBox();

            // Load dữ liệu
            taiDuLieu();

        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void capNhatThongKe() throws SQLException {
        List<KhuyenMai> dsKhuyenMai = khuyenMaiDAO.layDSKhuyenMai();
        List<KhuyenMai> dsMaDangHoatDong = khuyenMaiDAO.layKhuyenMaiDangHoatDong();

        int tongSoKhuyenMai = dsKhuyenMai.size();
        int soMaDangSuDung = 0;
        int soMaHetHan = 0;
        int soMaChuaBatDau = 0;
        int soMaVoHieuHoa = 0;

        for (KhuyenMai km : dsKhuyenMai) {
            String trangThai = km.getTrangThai();
            switch (trangThai) {
                case "Đang sử dụng" -> soMaDangSuDung++;
                case "Đã hết hạn" -> soMaHetHan++;
                case "Chưa bắt đầu" -> soMaChuaBatDau++;
                case "Vô hiệu hóa" -> soMaVoHieuHoa++;
            }
        }

        lblTongSoKhuyenMai.setText(String.valueOf(tongSoKhuyenMai));
        lblSoMaDangSuDung.setText(String.valueOf(soMaDangSuDung));
        lblSoMaHetHan.setText(String.valueOf(soMaHetHan));
        lblSoMaChuaBatDau.setText(String.valueOf(soMaChuaBatDau));
        lblSoMaVoHieuHoa.setText(String.valueOf(soMaVoHieuHoa));
    }

    private void khoiTaoDuLieu() {
        danhSachKhuyenMai = FXCollections.observableArrayList();
        danhSachKhuyenMaiFiltered = FXCollections.observableArrayList();
    }

    private void khoiTaoTableView() {
        // Thiết lập các cột
        colMaKM.setCellValueFactory(new PropertyValueFactory<>("maKM"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colGiamGia.setCellValueFactory(new PropertyValueFactory<>("giamGia"));
        colKieuGiamGia.setCellValueFactory(new PropertyValueFactory<>("kieuGiamGia"));
        colNgayBatDau.setCellValueFactory(new PropertyValueFactory<>("ngayBatDau"));
        colNgayKetThuc.setCellValueFactory(new PropertyValueFactory<>("ngayKetThuc"));
        colTongTienToiThieu.setCellValueFactory(new PropertyValueFactory<>("tongTienToiThieu"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));

        // Format ngày tháng
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colNgayBatDau.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        colNgayKetThuc.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        // Format số tiền
        colGiamGia.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f", item));
                }
            }
        });

        colTongTienToiThieu.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f", item));
                }
            }
        });

        // Format trạng thái với màu sắc
        colTrangThai.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                if (empty || item == null) {
                    setText(null);
                } else {
                    switch (item.toLowerCase()) {
                        case "đang sử dụng" -> setGraphic(Badge.createBadge(item, BadgeVariant.SUCCESS));
                        case "đã hết hạn" -> setGraphic(Badge.createBadge(item, BadgeVariant.DANGER));
                        case "chưa bắt đầu" -> setGraphic(Badge.createBadge(item, BadgeVariant.WARNING));
                        case "vô hiệu hóa" -> setGraphic(Badge.createBadge(item, BadgeVariant.DEFAULT));
                        default -> setText(item);
                    }
                }
            }
        });

        colThaoTac.setCellFactory(_ -> new TableCell<>() {
            private final Button btnEdit = CustomButton.createButton("Sửa", ButtonVariant.INFO);
            private final Button btnDelete = CustomButton.createButton("Xóa", ButtonVariant.DANGER);

            {
                btnEdit.setOnAction(_ -> {
                    KhuyenMai khuyenMai = getTableView().getItems().get(getIndex());
                    handleSuaKhuyenMai(khuyenMai);
                });

                btnDelete.setOnAction(_ -> {
                    KhuyenMai khuyenMai = getTableView().getItems().get(getIndex());
                    handleXoaKhuyenMai(khuyenMai);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(8, btnEdit, btnDelete);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        colThaoTac.setCellValueFactory(_ -> new ReadOnlyObjectWrapper<>(null));

        // Thiết lập TableView
        tableViewKhuyenMai.setItems(danhSachKhuyenMaiFiltered);

        // Cho phép chọn nhiều dòng
        tableViewKhuyenMai.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void khoiTaoComboBox() {
        // Khởi tạo ComboBox kiểu giảm giá
        List<KieuGiamGia> danhSachKieuGiamGia = List.of(KieuGiamGia.PERCENT, KieuGiamGia.AMOUNT);
        cbKieuGiamGia.setItems(FXCollections.observableArrayList(danhSachKieuGiamGia));
        cbKieuGiamGia.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(KieuGiamGia item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn kiểu giảm giá");
                } else {
                    setText(item == KieuGiamGia.PERCENT ? "Phần trăm (%)" : "Số tiền (VNĐ)");
                }
            }
        });
        cbKieuGiamGia.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(KieuGiamGia item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn kiểu giảm giá");
                } else {
                    setText(item == KieuGiamGia.PERCENT ? "Phần trăm (%)" : "Số tiền (VNĐ)");
                }
            }
        });

        // Khởi tạo ComboBox trạng thái cho filter (hiển thị tất cả trạng thái có thể)
        List<String> danhSachTrangThai = List.of("Đang sử dụng", "Đã hết hạn", "Chưa bắt đầu", "Vô hiệu hóa");
        cbTrangThai.setItems(FXCollections.observableArrayList(danhSachTrangThai));
        cbTrangThai.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn trạng thái");
                } else {
                    setText(item);
                }
            }
        });
        cbTrangThai.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn trạng thái");
                } else {
                    setText(item);
                }
            }
        });
    }

    private void taiDuLieu() {
        try {
            // Lấy danh sách mã giảm giá từ database
            List<KhuyenMai> dsKhuyenMai = khuyenMaiDAO.layDSKhuyenMai();

            LocalDate ngayHienTai = LocalDate.now();

            for (KhuyenMai km : dsKhuyenMai) {
                LocalDate batDau = km.getNgayBatDau();
                LocalDate ketThuc = km.getNgayKetThuc();
                String trangThaiDB = km.getTrangThai();

                // Tính toán trạng thái hiển thị dựa trên trạng thái DB và thời gian
                String trangThaiHienThi;
                if ("Kích hoạt".equals(trangThaiDB)) {
                    if (ketThuc.isBefore(ngayHienTai)) {
                        trangThaiHienThi = "Đã hết hạn";
                    } else if (batDau.isAfter(ngayHienTai)) {
                        trangThaiHienThi = "Chưa bắt đầu";
                    } else {
                        trangThaiHienThi = "Đang sử dụng";
                    }
                } else {
                    trangThaiHienThi = "Vô hiệu hóa";
                }

                km.setTrangThai(trangThaiHienThi);
            }

            danhSachKhuyenMai.clear();
            danhSachKhuyenMai.addAll(dsKhuyenMai);
            capNhatThongKe();

            // Áp dụng filter hiện tại
            apDungFilter();
        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải dữ liệu khuyến mãi: " + e.getMessage());
        }
    }

    private void apDungFilter() {
        danhSachKhuyenMaiFiltered.clear();

        List<KhuyenMai> filtered = danhSachKhuyenMai.stream()
                .filter(khuyenMai -> {
                    // Filter theo tìm kiếm
                    String timKiem = txtTimKiem.getText().toLowerCase();
                    if (!timKiem.isEmpty()) {
                        boolean matchMaKM = khuyenMai.getMaKM().toLowerCase().contains(timKiem);
                        boolean matchCode = khuyenMai.getCode().toLowerCase().contains(timKiem);
                        boolean matchMoTa = khuyenMai.getMoTa() != null &&
                                khuyenMai.getMoTa().toLowerCase().contains(timKiem);
                        if (!matchMaKM && !matchCode && !matchMoTa) {
                            return false;
                        }
                    }

                    // Filter theo kiểu giảm giá
                    KieuGiamGia kieuGiamGiaFilter = cbKieuGiamGia.getValue();
                    if (kieuGiamGiaFilter != null && !khuyenMai.getKieuGiamGia().equals(kieuGiamGiaFilter)) {
                        return false;
                    }

                    // Filter theo trạng thái
                    String trangThaiFilter = cbTrangThai.getValue();
                    if (trangThaiFilter != null && (khuyenMai.getTrangThai() == null ||
                            !khuyenMai.getTrangThai().equalsIgnoreCase(trangThaiFilter))) {
                        return false;
                    }

                    return true;
                })
                .toList();

        danhSachKhuyenMaiFiltered.addAll(filtered);
    }

    @FXML
    private void handleThemKhuyenMai() {
        KhuyenMaiFormDialogView view = new KhuyenMaiFormDialogView();
        KhuyenMaiDialogController controller = new KhuyenMaiDialogController(view);
        Parent root = view.getRoot();
        Stage dialog = new Stage();
        dialog.setTitle("Thêm Khuyến Mãi Mới");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root));
        // Thiết lập controller và dữ liệu
        controller.setMode("ADD");

        dialog.showAndWait();

        // Làm mới dữ liệu sau khi thêm
        taiDuLieu();

    }

    @FXML
    private void handleSuaKhuyenMai(KhuyenMai khuyenMai) {
        try {
            // Load lại dữ liệu đầy đủ từ database
            KhuyenMai khuyenMaiDayDu = khuyenMaiDAO.layKhuyenMaiTheoMa(khuyenMai.getMaKM());
            KhuyenMaiFormDialogView view = new KhuyenMaiFormDialogView();
            KhuyenMaiDialogController controller = new KhuyenMaiDialogController(view);
            Parent root = view.getRoot();
            Stage dialog = new Stage();
            dialog.setTitle("Sửa Thông Tin Khuyến Mãi");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            // Thiết lập controller và dữ liệu
            controller.setMode("EDIT");

            // Set dữ liệu sau khi dialog hiển thị
            javafx.application.Platform.runLater(() -> controller.setKhuyenMai(khuyenMaiDayDu));
            dialog.showAndWait();

            // Làm mới dữ liệu sau khi sửa
            taiDuLieu();

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi khi load dữ liệu khuyến mãi: " + e.getMessage());
        }
    }

    @FXML
    private void handleXoaKhuyenMai(KhuyenMai khuyenMai) {
        // Xác nhận xóa
        String message = "Bạn có chắc chắn muốn xóa khuyến mãi này?\nMã: " + khuyenMai.getMaKM() + " - Code: " + khuyenMai.getCode();
        if (ThongBaoUtil.hienThiXacNhan("Xác nhận xóa", message)) {
            try {
                // Xóa khuyến mãi
                if (khuyenMaiDAO.xoaKhuyenMai(khuyenMai.getMaKM())) {
                    ThongBaoUtil.hienThiThongBao("Thành công", "Đã xóa khuyến mãi thành công!");
                    taiDuLieu();
                } else {
                    ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể xóa khuyến mãi!");
                }

            } catch (SQLException e) {
                ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi khi xóa khuyến mãi: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleLamMoi() {
        taiDuLieu();
        txtTimKiem.clear();
        cbKieuGiamGia.setValue(null);
        cbTrangThai.setValue(null);
    }

    @FXML
    private void handleTimKiem() {
        apDungFilter();
    }

    @FXML
    private void handleLocKieuGiamGia() {
        apDungFilter();
    }

    @FXML
    private void handleLocTrangThai() {
        apDungFilter();
    }
}