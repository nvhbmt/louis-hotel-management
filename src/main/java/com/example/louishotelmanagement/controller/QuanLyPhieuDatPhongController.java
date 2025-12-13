package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.MaGiamGiaDAO;
import com.example.louishotelmanagement.dao.PhieuDatPhongDAO;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
    private TableColumn<PhieuDatPhong, String> colTrangThai;
    @FXML
    public TableColumn<PhieuDatPhong, Void> colThaoTac;

    private PhieuDatPhongDAO phieuDatPhongDAO;
    private KhachHangDAO khachHangDAO;
    private ObservableList<PhieuDatPhong> danhSachPhieuDatPhong;
    private ObservableList<PhieuDatPhong> danhSachPhieuDatPhongFiltered;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            phieuDatPhongDAO = new PhieuDatPhongDAO();
            khachHangDAO = new KhachHangDAO();
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
                .filter(PDP->PDP.getTrangThai().equalsIgnoreCase(TrangThaiPhieuDatPhong.DA_DAT.toString()))
                .count();
        long tongPhieuDatPhongDangSuDung = dsPhieuDatPhong.stream()
                .filter(PDP->PDP.getTrangThai().equalsIgnoreCase(TrangThaiPhieuDatPhong.DANG_SU_DUNG.toString()))
                .count();
        long tongPhieuDatPhongDaHoanThanh = dsPhieuDatPhong.stream()
                .filter(PDP->PDP.getTrangThai().equalsIgnoreCase(TrangThaiPhieuDatPhong.HOAN_THANH.toString()))
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

        // Format trạng thái với màu sắc
        colTrangThai.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    getStyleClass().clear();
                } else {
                    setText(item);
                    getStyleClass().clear();
                    switch (item.toLowerCase()) {
                        case "Đã đặt" -> getStyleClass().add("status-dadat");
                        case "Hoàn thành" -> getStyleClass().add("status-hoanthanh");
                        case "Đang sử dụng" -> getStyleClass().add("status-trong");
                        default -> {
                            // Không thêm style class nào
                        }
                    }
                }
            }
        });

        colThaoTac.setCellFactory(_ -> new TableCell<>() {
            private final Button btnEdit = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");

            {
                btnEdit.getStyleClass().addAll("btn", "btn-xs", "btn-info", "btn-table-edit");
                btnDelete.getStyleClass().addAll("btn", "btn-xs", "btn-danger", "btn-table-delete");

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
                    HBox box = new HBox(8, btnEdit, btnDelete);
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
                        try{
                            KhachHang kh = khachHangDAO.layKhachHangTheoMa(phieuDatPhong.getMaKH());
                            if(kh != null){
                                tenKH = kh.getHoTen();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        boolean matchTenKH = tenKH.toLowerCase().contains(timKiem);
                        if (!matchPhieu&&!matchTenKH) {
                            return false;
                        }
                    }


                    // Filter theo trạng thái
                    String trangThaiFilter = cbTrangThai.getValue();
                    if (trangThaiFilter != null && (phieuDatPhong.getTrangThai() == null ||
                            !phieuDatPhong.getTrangThai().equalsIgnoreCase(trangThaiFilter))) {
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
            if(phieuDatPhong.getTrangThai().equalsIgnoreCase(TrangThaiPhieuDatPhong.DA_DAT.toString())){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/phieu-dat-phong-form-dialog.fxml"));
                Stage dialog = new Stage();
                dialog.setTitle("Sửa Thông Tin Phiếu Đặt Phòng"); // Sửa tiêu đề
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.setScene(new Scene(loader.load()));

                // Thiết lập controller và dữ liệu
                PhieuDatPhongFormDialogController controller = loader.getController(); // Lấy đúng controller
                controller.setPhieuDatPhong(phieuDatPhong); // Truyền đối tượng Phiếu Đặt Phòng
                dialog.showAndWait();

                // Làm mới dữ liệu sau khi sửa
                taiDuLieu();
            }
            else{
                ThongBaoUtil.hienThiLoi("Lỗi","Chỉ được cập nhật phiếu đã đặt");
            }

        } catch (IOException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể mở form sửa phiếu đặt phòng: " + e.getMessage());
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
