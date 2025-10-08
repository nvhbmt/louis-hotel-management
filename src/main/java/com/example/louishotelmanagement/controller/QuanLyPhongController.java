package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.LoaiPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class QuanLyPhongController implements Initializable {

    @FXML private Button btnThemPhong;
    @FXML private Button btnSuaPhong;
    @FXML private Button btnXoaPhong;
    @FXML private Button btnLamMoi;
    
    @FXML private TextField txtTimKiem;
    @FXML private ComboBox<Integer> cbTang;
    @FXML private ComboBox<String> cbTrangThai;
    @FXML private ComboBox<LoaiPhong> cbLocLoaiPhong;
    
    @FXML private TableView<Phong> tableViewPhong;
    @FXML private TableColumn<Phong, String> colMaPhong;
    @FXML private TableColumn<Phong, Integer> colTang;
    @FXML private TableColumn<Phong, String> colTrangThai;
    @FXML private TableColumn<Phong, String> colMoTa;
    @FXML private TableColumn<Phong, String> colMaLoaiPhong;
    @FXML private TableColumn<Phong, String> colTenLoaiPhong;
    @FXML private TableColumn<Phong, BigDecimal> colDonGia;
    
    @FXML private Label lblTrangThai;
    @FXML private Label lblSoLuong;

    private PhongDAO phongDAO;
    private LoaiPhongDAO loaiPhongDAO;
    private ObservableList<Phong> danhSachPhong;
    private ObservableList<Phong> danhSachPhongFiltered;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            phongDAO = new PhongDAO();
            loaiPhongDAO = new LoaiPhongDAO();
            
            // Khởi tạo dữ liệu
            khoiTaoDuLieu();
            khoiTaoTableView();
            khoiTaoComboBox();
            
            // Load dữ liệu
            taiDuLieu();
            
        } catch (Exception e) {
            hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void khoiTaoDuLieu() {
        danhSachPhong = FXCollections.observableArrayList();
        danhSachPhongFiltered = FXCollections.observableArrayList();
    }

    private void khoiTaoTableView() {
        // Thiết lập các cột
        colMaPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        colTang.setCellValueFactory(new PropertyValueFactory<>("tang"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        colMaLoaiPhong.setCellValueFactory(new PropertyValueFactory<>("maLoaiPhong"));
        
        // Cột tên loại phòng và đơn giá sẽ được thiết lập trong taiDuLieu()
        colTenLoaiPhong.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();
            return loaiPhong != null ? 
                javafx.beans.binding.Bindings.createStringBinding(() -> loaiPhong.getTenLoai()) : 
                javafx.beans.binding.Bindings.createStringBinding(() -> "");
        });
        
        colDonGia.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();
            return loaiPhong != null ? 
                javafx.beans.binding.Bindings.createObjectBinding(() -> loaiPhong.getDonGia()) : 
                javafx.beans.binding.Bindings.createObjectBinding(() -> BigDecimal.ZERO);
        });

        // Thiết lập TableView
        tableViewPhong.setItems(danhSachPhongFiltered);
        
        // Cho phép chọn nhiều dòng
        tableViewPhong.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void khoiTaoComboBox() {
        // Khởi tạo ComboBox tầng
        List<Integer> danhSachTang = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            danhSachTang.add(i);
        }
        cbTang.setItems(FXCollections.observableArrayList(danhSachTang));
        
        // Khởi tạo ComboBox trạng thái với các trạng thái phù hợp
        List<String> danhSachTrangThai = List.of("Trống", "Đã đặt", "Đang sử dụng", "Bảo trì", "Dọn dẹp");
        cbTrangThai.setItems(FXCollections.observableArrayList(danhSachTrangThai));
        
        // Khởi tạo ComboBox loại phòng để filter
        khoiTaoComboBoxLoaiPhong();
    }
    
    private void khoiTaoComboBoxLoaiPhong() {
        try {
            List<LoaiPhong> danhSachLoaiPhong = loaiPhongDAO.layDSLoaiPhong();
            
            // Thiết lập ComboBox để hiển thị tên loại phòng
            cbLocLoaiPhong.setCellFactory(_ -> new ListCell<LoaiPhong>() {
                @Override
                protected void updateItem(LoaiPhong item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getTenLoai() + " (" + item.getMaLoaiPhong() + ")");
                    }
                }
            });
            
            cbLocLoaiPhong.setButtonCell(new ListCell<LoaiPhong>() {
                @Override
                protected void updateItem(LoaiPhong item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getTenLoai() + " (" + item.getMaLoaiPhong() + ")");
                    }
                }
            });
            
            cbLocLoaiPhong.setItems(FXCollections.observableArrayList(danhSachLoaiPhong));
            
        } catch (SQLException e) {
            hienThiThongBao("Lỗi", "Không thể tải danh sách loại phòng: " + e.getMessage());
        }
    }

    private void taiDuLieu() {
        try {
            // Lấy danh sách phòng từ database
            List<Phong> phongs = phongDAO.layDSPhong();
            
            // Lấy thông tin loại phòng cho mỗi phòng
            for (Phong phong : phongs) {
                if (phong.getMaLoaiPhong() != null && !phong.getMaLoaiPhong().isEmpty()) {
                    LoaiPhong loaiPhong = loaiPhongDAO.layLoaiPhongTheoMa(phong.getMaLoaiPhong());
                    phong.setLoaiPhong(loaiPhong);
                }
            }
            
            danhSachPhong.clear();
            danhSachPhong.addAll(phongs);
            
            // Áp dụng filter hiện tại
            apDungFilter();
            
            capNhatTrangThai("Đã tải " + phongs.size() + " phòng");
            
        } catch (SQLException e) {
            hienThiThongBao("Lỗi", "Không thể tải dữ liệu phòng: " + e.getMessage());
        }
    }

    private void apDungFilter() {
        danhSachPhongFiltered.clear();
        
        List<Phong> filtered = danhSachPhong.stream()
            .filter(phong -> {
                // Filter theo tìm kiếm
                String timKiem = txtTimKiem.getText().toLowerCase();
                if (!timKiem.isEmpty()) {
                    boolean matchMaPhong = phong.getMaPhong().toLowerCase().contains(timKiem);
                    boolean matchTang = phong.getTang() != null && 
                        phong.getTang().toString().contains(timKiem);
                    if (!matchMaPhong && !matchTang) {
                        return false;
                    }
                }
                
                // Filter theo tầng
                Integer tangFilter = cbTang.getValue();
                if (tangFilter != null && (phong.getTang() == null || !phong.getTang().equals(tangFilter))) {
                    return false;
                }
                
                // Filter theo loại phòng
                LoaiPhong loaiPhongFilter = cbLocLoaiPhong.getValue();
                if (loaiPhongFilter != null && (phong.getLoaiPhong() == null || 
                    !phong.getLoaiPhong().getMaLoaiPhong().equals(loaiPhongFilter.getMaLoaiPhong()))) {
                    return false;
                }
                
                return true;
            })
            .collect(Collectors.toList());
        
        danhSachPhongFiltered.addAll(filtered);
        lblSoLuong.setText("Tổng số phòng: " + danhSachPhongFiltered.size());
    }

    @FXML
    private void handleThemPhong() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/room-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Thêm Phòng Mới");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));
            
            // Thiết lập controller và dữ liệu
            RoomFormDialogController controller = loader.getController();
            controller.setMode("ADD");
            
            dialog.showAndWait();
            
            // Làm mới dữ liệu sau khi thêm
            taiDuLieu();
            
        } catch (IOException e) {
            hienThiThongBao("Lỗi", "Không thể mở form thêm phòng: " + e.getMessage());
        }
    }

    @FXML
    private void handleSuaPhong() {
        Phong phongChon = tableViewPhong.getSelectionModel().getSelectedItem();
        if (phongChon == null) {
            hienThiThongBao("Cảnh báo", "Vui lòng chọn phòng cần sửa!");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/room-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Sửa Thông Tin Phòng");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));
            
            // Thiết lập controller và dữ liệu
            RoomFormDialogController controller = loader.getController();
            controller.setPhongDAO(phongDAO);
            controller.setLoaiPhongDAO(loaiPhongDAO);
            controller.setMode("EDIT");
            controller.setPhong(phongChon);
            
            dialog.showAndWait();
            
            // Làm mới dữ liệu sau khi sửa
            taiDuLieu();
            
        } catch (IOException e) {
            hienThiThongBao("Lỗi", "Không thể mở form sửa phòng: " + e.getMessage());
        }
    }

    @FXML
    private void handleXoaPhong() {
        Phong phongChon = tableViewPhong.getSelectionModel().getSelectedItem();
        if (phongChon == null) {
            hienThiThongBao("Cảnh báo", "Vui lòng chọn phòng cần xóa!");
            return;
        }
        
        // Xác nhận xóa
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa phòng này?");
        alert.setContentText("Phòng: " + phongChon.getMaPhong() + " - Tầng: " + phongChon.getTang());
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Kiểm tra xem phòng có đang được sử dụng không
                    if (phongDAO.kiemTraPhongDuocSuDung(phongChon.getMaPhong())) {
                        hienThiThongBao("Lỗi", "Không thể xóa phòng này vì đang được sử dụng!");
                        return;
                    }
                    
                    // Xóa phòng
                    if (phongDAO.xoaPhong(phongChon.getMaPhong())) {
                        hienThiThongBao("Thành công", "Đã xóa phòng thành công!");
                        taiDuLieu();
                    } else {
                        hienThiThongBao("Lỗi", "Không thể xóa phòng!");
                    }
                    
                } catch (SQLException e) {
                    hienThiThongBao("Lỗi", "Lỗi khi xóa phòng: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleLamMoi() {
        taiDuLieu();
        txtTimKiem.clear();
        cbTang.setValue(null);
        cbTrangThai.setValue(null);
        cbLocLoaiPhong.setValue(null);
    }

    @FXML
    private void handleTimKiem() {
        apDungFilter();
    }

    @FXML
    private void handleLocTang() {
        apDungFilter();
    }

    @FXML
    private void handleLocTrangThai() {
        apDungFilter();
    }

    @FXML
    private void handleLocLoaiPhong() {
        apDungFilter();
    }

    private void capNhatTrangThai(String message) {
        lblTrangThai.setText(message);
    }

    private void hienThiThongBao(String tieuDe, String noiDung) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tieuDe);
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.showAndWait();
    }
}
