package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.CTHoaDonPhongDAO;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.PhieuDatPhongDAO;
import com.example.louishotelmanagement.model.CTHoaDonPhong;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.model.PhieuDatPhong;
import com.example.louishotelmanagement.model.TrangThaiPhieuDatPhong;
import com.example.louishotelmanagement.ui.components.Badge;
import com.example.louishotelmanagement.ui.components.CustomButton;
import com.example.louishotelmanagement.ui.models.BadgeVariant;
import com.example.louishotelmanagement.ui.models.ButtonVariant;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.view.ChiTietPhongTrongPhieuView;
import com.example.louishotelmanagement.view.PhieuDatPhongFormDialogView;
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
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class QuanLyPhieuDatPhongController implements Initializable {

    @FXML
    public BorderPane rootPane;
    @FXML
    public Label lblTongSoPhieu;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private ComboBox<String> cbTrangThai;

    @FXML
    private TableView<PhieuDatPhong> tableViewPhieuDatPhong;
    @FXML
    public TableColumn<PhieuDatPhong, String> colMaPDP;
    @FXML
    public TableColumn<PhieuDatPhong, String> colKH;
    @FXML
    public TableColumn<PhieuDatPhong, LocalDate> colNgayDat;
    @FXML
    public TableColumn<PhieuDatPhong, LocalDate> colNgayDen;
    @FXML
    public TableColumn<PhieuDatPhong, LocalDate> colNgayDi;
    @FXML
    public TableColumn<PhieuDatPhong, BigDecimal> colTienCoc;
    @FXML
    public Label lblPhieuHoanThanh;
    @FXML
    public Label lblPhieuDangSuDung;
    @FXML
    public Label lblSoPhieuDaDat;
    @FXML
    private TableColumn<PhieuDatPhong, TrangThaiPhieuDatPhong> colTrangThai;
    @FXML
    public TableColumn<PhieuDatPhong, Void> colThaoTac;

    private PhieuDatPhongDAO phieuDatPhongDAO;
    private KhachHangDAO khachHangDAO;
    private CTHoaDonPhongDAO cthdpDao;
    private ObservableList<PhieuDatPhong> danhSachPhieuDatPhong;
    private ObservableList<PhieuDatPhong> danhSachPhieuDatPhongFiltered;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            phieuDatPhongDAO = new PhieuDatPhongDAO();
            khachHangDAO = new KhachHangDAO();
            cthdpDao = new CTHoaDonPhongDAO();
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
        List<PhieuDatPhong> dsPhieuDatPhong = phieuDatPhongDAO.layDSPhieuDatPhong();

        int tongPhieuDatPhong = dsPhieuDatPhong.size();
        long tongPhieuDatPhongDaDat = dsPhieuDatPhong.stream()
                .filter(PDP -> PDP.getTrangThai().equals(TrangThaiPhieuDatPhong.DA_DAT))
                .count();
        long tongPhieuDatPhongDangSuDung = dsPhieuDatPhong.stream()
                .filter(PDP -> PDP.getTrangThai().equals(TrangThaiPhieuDatPhong.DANG_SU_DUNG))
                .count();
        long tongPhieuDatPhongDaHoanThanh = dsPhieuDatPhong.stream()
                .filter(PDP -> PDP.getTrangThai().equals(TrangThaiPhieuDatPhong.HOAN_THANH))
                .count();
        lblTongSoPhieu.setText(Long.toString(tongPhieuDatPhong));
        lblSoPhieuDaDat.setText(String.valueOf(tongPhieuDatPhongDaDat));
        lblPhieuDangSuDung.setText(String.valueOf(tongPhieuDatPhongDangSuDung));
        lblPhieuHoanThanh.setText(String.valueOf(tongPhieuDatPhongDaHoanThanh));
    }

    private void khoiTaoDuLieu() {
        danhSachPhieuDatPhong = FXCollections.observableArrayList();
        danhSachPhieuDatPhongFiltered = FXCollections.observableArrayList();
    }

    private void khoiTaoTableView() {
        // Thiết lập các cột
        colMaPDP.setCellValueFactory(new PropertyValueFactory<>("maPhieu"));
        colKH.setCellValueFactory(cellData -> {
            PhieuDatPhong phieuDatPhong = cellData.getValue();
            String maKH = phieuDatPhong.getMaKH(); // Lấy mã khách hàng từ phiếu đặt phòng
            try {
                // Sử dụng KhachHangDAO để tìm kiếm khách hàng
                KhachHang khachHang = khachHangDAO.layKhachHangTheoMa(maKH);
                if (khachHang != null) {
                    // Trả về Tên Khách Hàng
                    return new ReadOnlyObjectWrapper<>(khachHang.getHoTen());
                }
            } catch (SQLException e) {
                // Xử lý lỗi SQL nếu cần (ví dụ: in ra console)
                System.err.println("Lỗi khi tìm kiếm khách hàng có mã " + maKH + ": " + e.getMessage());
            }
            // Trả về chuỗi rỗng nếu không tìm thấy hoặc có lỗi
            return new ReadOnlyObjectWrapper<>("Không rõ");
        });
        colNgayDat.setCellValueFactory(new PropertyValueFactory<>("ngayDat"));
        colNgayDen.setCellValueFactory(new PropertyValueFactory<>("ngayDen"));
        colNgayDi.setCellValueFactory(new PropertyValueFactory<>("ngayDi"));
        colTienCoc.setCellValueFactory(new PropertyValueFactory<>("tienCoc"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        // Format ngày tháng
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colNgayDi.setCellFactory(column -> new TableCell<>() {
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

        colNgayDen.setCellFactory(column -> new TableCell<>() {
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
        colNgayDat.setCellFactory(column -> new TableCell<>() {
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

        colTienCoc.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0fđ", item));
                }
            }
        });

        // Format trạng thái với màu sắc sử dụng BadgeUtil
        colTrangThai.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(TrangThaiPhieuDatPhong item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    switch (item) {
                        case DA_DAT -> setGraphic(Badge.createBadge(item.toString(), BadgeVariant.INFO));
                        case HOAN_THANH -> setGraphic(Badge.createBadge(item.toString(), BadgeVariant.SUCCESS));
                        case DANG_SU_DUNG -> setGraphic(Badge.createBadge(item.toString(), BadgeVariant.WARNING));
                        case DA_HUY -> setGraphic(Badge.createBadge(item.toString(), BadgeVariant.DANGER));
                        default -> setGraphic(Badge.createBadge(item.toString(), BadgeVariant.DEFAULT));
                    }
                }
            }
        });

        colThaoTac.setCellFactory(_ -> new TableCell<>() {
            private final Button btnEdit = CustomButton.createButton("Sửa", ButtonVariant.INFO);
            private final Button btnDelete = CustomButton.createButton("Xóa", ButtonVariant.DANGER);
            private final Button btnView = CustomButton.createButton("Xem chi tiết", ButtonVariant.SECONDARY);

            {
                btnView.setOnAction(_ -> {
                    PhieuDatPhong phieuDatPhong = getTableView().getItems().get(getIndex());
                    handleXemChiTiet(phieuDatPhong);
                });
                btnEdit.setOnAction(_ -> {
                    PhieuDatPhong phieuDatPhong = getTableView().getItems().get(getIndex());
                    handleSuaPhieuDatPhong(phieuDatPhong);
                });

                btnDelete.setOnAction(_ -> {
                    PhieuDatPhong phieuDatPhong = getTableView().getItems().get(getIndex());
                    handleXoaPhieuDatPhong(phieuDatPhong);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(8, btnView, btnEdit, btnDelete);

                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        colThaoTac.setCellValueFactory(_ -> new ReadOnlyObjectWrapper<>(null));

        // Thiết lập TableView
        tableViewPhieuDatPhong.setItems(danhSachPhieuDatPhongFiltered);

        // Cho phép chọn nhiều dòng
        tableViewPhieuDatPhong.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void khoiTaoComboBox() {
        // Khởi tạo ComboBox trạng thái
        List<String> danhSachTrangThai = List.of("Đã đặt", "Đang sử dụng", "Hoàn thành");
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
            ArrayList<PhieuDatPhong> dsPhieuDatPhong = phieuDatPhongDAO.layDSPhieuDatPhong();
            danhSachPhieuDatPhong.clear();
            danhSachPhieuDatPhong.addAll(dsPhieuDatPhong);
            capNhatThongKe();

            // Áp dụng filter hiện tại
            apDungFilter();
        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải dữ liệu mã giảm giá: " + e.getMessage());
        }
    }

    private void apDungFilter() {
        danhSachPhieuDatPhongFiltered.clear();

        List<PhieuDatPhong> filtered = danhSachPhieuDatPhong.stream()
                .filter(phieuDatPhong -> {
                    // Filter theo tìm kiếm
                    String timKiem = txtTimKiem.getText().toLowerCase();
                    if (!timKiem.isEmpty()) {
                        boolean matchPhieu = phieuDatPhong.getMaPhieu().toLowerCase().contains(timKiem);
                        String tenKH = "";
                        try {
                            KhachHang kh = khachHangDAO.layKhachHangTheoMa(phieuDatPhong.getMaKH());
                            if (kh != null) {
                                tenKH = kh.getHoTen();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        boolean matchTenKH = tenKH.toLowerCase().contains(timKiem);
                        if (!matchPhieu && !matchTenKH) {
                            return false;
                        }
                    }


                    // Filter theo trạng thái
                    String trangThaiFilter = cbTrangThai.getValue();
                    if (trangThaiFilter != null && (phieuDatPhong.getTrangThai() == null ||
                            !phieuDatPhong.getTrangThai().equals(trangThaiFilter))) {
                        return false;
                    }

                    return true;
                })
                .toList();

        danhSachPhieuDatPhongFiltered.addAll(filtered);
    }


    // Trong QuanLyPhieuDatPhongController

    @FXML
    private void handleSuaPhieuDatPhong(PhieuDatPhong phieuDatPhong) {
        try {
            if (phieuDatPhong.getTrangThai().equals(TrangThaiPhieuDatPhong.DA_DAT)) {
                PhieuDatPhongFormDialogView view = new PhieuDatPhongFormDialogView();
                PhieuDatPhongFormDialogController controller = new PhieuDatPhongFormDialogController(view);
                Parent root = view.getRoot();
                Stage dialog = new Stage();
                dialog.setTitle("Sửa Thông Tin Phiếu Đặt Phòng"); // Sửa tiêu đề
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.setScene(new Scene(root));
                // Thiết lập controller và dữ liệu
                controller.setPhieuDatPhong(phieuDatPhong); // Truyền đối tượng Phiếu Đặt Phòng
                dialog.showAndWait();

                // Làm mới dữ liệu sau khi sửa
                taiDuLieu();
            } else {
                ThongBaoUtil.hienThiLoi("Lỗi", "Chỉ được cập nhật phiếu đã đặt");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleXoaPhieuDatPhong(PhieuDatPhong phieuDatPhong) {
        // Xác nhận xóa
        String message = "Bạn có chắc chắn muốn xóa mã giảm giá này?\nMã: " + phieuDatPhong.getMaPhieu();
        if (ThongBaoUtil.hienThiXacNhan("Xác nhận xóa", message)) {
            try {
                // Xóa mã giảm giá
                if (phieuDatPhongDAO.xoaPhieuDatPhong(phieuDatPhong.getMaPhieu())) {
                    ThongBaoUtil.hienThiThongBao("Thành công", "Đã xóa mã giảm giá thành công!");
                    taiDuLieu();
                } else {
                    ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể xóa mã giảm giá!");
                }

            } catch (SQLException e) {
                ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi khi xóa mã giảm giá: " + e.getMessage());
            }
        }
    }

    private void handleXemChiTiet(PhieuDatPhong phieuDatPhong) {
        if (phieuDatPhong == null) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn một phiếu đặt phòng để xem chi tiết.");
            return;
        }

        try {
            // 2. Lấy danh sách chi tiết phòng (CTHoaDonPhong) từ DAO
            ArrayList<CTHoaDonPhong> dsCTP = cthdpDao.getCTHoaDonPhongTheoMaPhieu(phieuDatPhong.getMaPhieu());

            if (dsCTP.isEmpty()) {
                ThongBaoUtil.hienThiLoi("Thông báo", "Phiếu này không chứa thông tin chi tiết phòng nào.");
                return;
            }

            // 3. Load FXML của màn hình chi tiết
            // Đảm bảo đường dẫn FXML là chính xác theo cấu trúc dự án của bạn!
            ChiTietPhongTrongPhieuView view = new ChiTietPhongTrongPhieuView();
            ChiTietPhongTrongPhieuController controller = new ChiTietPhongTrongPhieuController(view);
            Parent root = view.getRoot();

            // 5. Truyền dữ liệu sang Controller mới
            // Hàm setChiTietData sẽ lấy MaPhieu và danh sách CTHoaDonPhong để hiển thị
            controller.setChiTietData(phieuDatPhong.getMaPhieu(), dsCTP);

            // 6. Tạo Stage và hiển thị
            Stage stage = new Stage();
            stage.setTitle("Chi Tiết Phòng Đặt - Phiếu " + phieuDatPhong.getMaPhieu());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với cửa sổ cha
            stage.showAndWait(); // Hiển thị và chờ người dùng đóng cửa sổ

        } catch (SQLException e) {
            // Lỗi xảy ra khi truy vấn DB trong quá trình lấy chi tiết phòng
            ThongBaoUtil.hienThiLoi("Lỗi dữ liệu", "Lỗi khi truy xuất chi tiết phòng: " + e.getMessage());
            System.err.println("Lỗi SQL: ");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLamMoi() {
        taiDuLieu();
        txtTimKiem.clear();
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
