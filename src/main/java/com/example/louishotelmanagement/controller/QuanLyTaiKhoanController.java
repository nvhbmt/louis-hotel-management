package com.example.louishotelmanagement.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.example.louishotelmanagement.dao.NhanVienDAO;
import com.example.louishotelmanagement.dao.TaiKhoanDAO;
import com.example.louishotelmanagement.model.TaiKhoan;
import com.example.louishotelmanagement.util.PasswordUtil;
import com.example.louishotelmanagement.util.ThongBaoUtil;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class QuanLyTaiKhoanController implements Initializable {

    @FXML
    private TableView<TaiKhoan> taiKhoanTable;
    
    @FXML
    private TableColumn<TaiKhoan, String> maTKColumn;
    
    @FXML
    private TableColumn<TaiKhoan, String> tenDangNhapColumn;
    
    @FXML
    private TableColumn<TaiKhoan, String> maNVColumn;
    
    @FXML
    private TableColumn<TaiKhoan, String> quyenColumn;
    
    @FXML
    private TableColumn<TaiKhoan, String> trangThaiColumn;
    
    @FXML
    private TableColumn<TaiKhoan, Void> thaoTacColumn;
    
    @FXML
    private TextField timKiemField;
    
    @FXML
    private Button themTaiKhoanBtn;
    
    @FXML
    private Button lamMoiBtn;
    
    @FXML
    private ComboBox<String> quyenFilterComboBox;
    
    @FXML
    private ComboBox<String> trangThaiFilterComboBox;
    
    @FXML
    private Label lblTongTaiKhoan;
    
    @FXML
    private Label lblTaiKhoanHoatDong;
    
    @FXML
    private Label lblTaiKhoanBiKhoa;
    
    @FXML
    private Label lblTaiKhoanManager;
    
    private ObservableList<TaiKhoan> taiKhoanList;
    private TaiKhoanDAO taiKhoanDAO;
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taiKhoanDAO = new TaiKhoanDAO();
        taiKhoanList = FXCollections.observableArrayList();
        
        setupTable();
        loadTaiKhoanData();
        setupEventHandlers();
    }
    
    private void setupTable() {
        maTKColumn.setCellValueFactory(new PropertyValueFactory<>("maTK"));
        tenDangNhapColumn.setCellValueFactory(new PropertyValueFactory<>("tenDangNhap"));
        maNVColumn.setCellValueFactory(cellData -> {
            TaiKhoan tk = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                tk.getNhanVien() != null ? tk.getNhanVien().getMaNV() : "N/A"
            );
        });
        quyenColumn.setCellValueFactory(new PropertyValueFactory<>("quyen"));
        trangThaiColumn.setCellValueFactory(cellData -> {
            TaiKhoan tk = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(tk.getTrangThaiDisplay());
        });
        
        // Cột thao tác với các nút
        thaoTacColumn.setCellFactory(_ -> new TableCell<TaiKhoan, Void>() {
            private final Button btnEdit = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");
            private final Button btnDoiMatKhau = new Button("Đổi MK");

            {
                // Chỉ nút Edit có màu
                btnEdit.getStyleClass().addAll("btn", "btn-xs", "btn-info", "btn-table-edit");
                btnDelete.getStyleClass().addAll("btn", "btn-xs", "btn-danger", "btn-table-delete");
                btnDoiMatKhau.getStyleClass().addAll("btn", "btn-xs", "btn-info", "btn-table-edit");
                
                // Tăng chiều rộng cho nút Delete
                btnDelete.setPrefWidth(60);
                btnEdit.setPrefWidth(70);
                btnDoiMatKhau.setPrefWidth(100); // Gấp đôi chiều rộng

                btnEdit.setOnAction(_ -> {
                    TaiKhoan taiKhoan = getTableView().getItems().get(getIndex());
                    showSuaTaiKhoanDialog(taiKhoan);
                });

                btnDelete.setOnAction(_ -> {
                    TaiKhoan taiKhoan = getTableView().getItems().get(getIndex());
                    xoaTaiKhoan(taiKhoan);
                });
                
                btnDoiMatKhau.setOnAction(_ -> {
                    TaiKhoan taiKhoan = getTableView().getItems().get(getIndex());
                    showDoiMatKhauDialog(taiKhoan);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(4, btnEdit, btnDoiMatKhau, btnDelete);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        thaoTacColumn.setCellValueFactory(_ -> new ReadOnlyObjectWrapper<>(null));
        
        taiKhoanTable.setItems(taiKhoanList);
    }
    
    private void loadTaiKhoanData() {
        try {
            List<TaiKhoan> dsTaiKhoan = taiKhoanDAO.layDSTaiKhoan();
            taiKhoanList.clear();
            taiKhoanList.addAll(dsTaiKhoan);
            updateStatistics();
        } catch (SQLException e) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể tải danh sách tài khoản: " + e.getMessage());
        }
    }
    
    private void updateStatistics() {
        int tongTaiKhoan = taiKhoanList.size();
        int taiKhoanHoatDong = (int) taiKhoanList.stream().filter(TaiKhoan::getTrangThai).count();
        int taiKhoanBiKhoa = tongTaiKhoan - taiKhoanHoatDong;
        int taiKhoanManager = (int) taiKhoanList.stream().filter(tk -> "Manager".equals(tk.getQuyen())).count();
        
        lblTongTaiKhoan.setText(String.valueOf(tongTaiKhoan));
        lblTaiKhoanHoatDong.setText(String.valueOf(taiKhoanHoatDong));
        lblTaiKhoanBiKhoa.setText(String.valueOf(taiKhoanBiKhoa));
        lblTaiKhoanManager.setText(String.valueOf(taiKhoanManager));
    }
    
    private void filterByQuyen() {
        String selectedQuyen = quyenFilterComboBox.getValue();
        if (selectedQuyen == null || "Tất cả".equals(selectedQuyen)) {
            taiKhoanTable.setItems(taiKhoanList);
        } else {
            ObservableList<TaiKhoan> filteredList = taiKhoanList.filtered(tk -> selectedQuyen.equals(tk.getQuyen()));
            taiKhoanTable.setItems(filteredList);
        }
    }
    
    private void filterByTrangThai() {
        String selectedTrangThai = trangThaiFilterComboBox.getValue();
        if (selectedTrangThai == null || "Tất cả".equals(selectedTrangThai)) {
            taiKhoanTable.setItems(taiKhoanList);
        } else {
            boolean isHoatDong = "Hoạt động".equals(selectedTrangThai);
            ObservableList<TaiKhoan> filteredList = taiKhoanList.filtered(tk -> tk.getTrangThai() == isHoatDong);
            taiKhoanTable.setItems(filteredList);
        }
    }
    
    private void setupEventHandlers() {
        themTaiKhoanBtn.setOnAction(v -> showThemTaiKhoanDialog());
        lamMoiBtn.setOnAction(e -> loadTaiKhoanData());
        
        timKiemField.textProperty().addListener((_, _, newValue) -> {
            filterTaiKhoan(newValue);
        });
        
        quyenFilterComboBox.setOnAction(_ -> filterByQuyen());
        trangThaiFilterComboBox.setOnAction(_ -> filterByTrangThai());
        
        // Setup ComboBox items
        quyenFilterComboBox.getItems().addAll("Tất cả", "Manager", "Staff");
        trangThaiFilterComboBox.getItems().addAll("Tất cả", "Hoạt động", "Khóa");
        
        // Double click on table row to edit
        taiKhoanTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                TaiKhoan selectedTaiKhoan = taiKhoanTable.getSelectionModel().getSelectedItem();
                if (selectedTaiKhoan != null) {
                    showSuaTaiKhoanDialog(selectedTaiKhoan);
                }
            }
        });
    }
    
    private void showThemTaiKhoanDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/tai-khoan-form-dialog.fxml"));
            Parent root = loader.load();
            
            TaiKhoanDialogController controller = loader.getController();
            controller.setMode("ADD");
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm Tài Khoản Mới");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            
            // Đặt kích thước dialog
            dialogStage.setWidth(700);
            dialogStage.setHeight(500);
            dialogStage.setResizable(false);
            
            dialogStage.showAndWait();
            
            // Làm mới danh sách sau khi đóng dialog
            loadTaiKhoanData();
            updateStatistics();
            
        } catch (IOException e) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể tải dialog thêm tài khoản: " + e.getMessage());
        }
    }
    
    private void showSuaTaiKhoanDialog(TaiKhoan taiKhoan) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/tai-khoan-form-dialog.fxml"));
            Parent root = loader.load();
            
            TaiKhoanDialogController controller = loader.getController();
            controller.setMode("EDIT");
            controller.setTaiKhoan(taiKhoan);
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sửa Tài Khoản");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            
            // Đặt kích thước dialog
            dialogStage.setWidth(700);
            dialogStage.setHeight(500);
            dialogStage.setResizable(false);
            
            dialogStage.showAndWait();
            
            // Làm mới danh sách sau khi đóng dialog
            loadTaiKhoanData();
            updateStatistics();
            
        } catch (IOException e) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể tải dialog sửa tài khoản: " + e.getMessage());
        }
    }
    
    private void xoaTaiKhoan(TaiKhoan taiKhoan) {
        if (ThongBaoUtil.hienThiXacNhan("Xác nhận xóa", "Bạn có chắc chắn muốn xóa tài khoản này?")) {
            try {
                if (taiKhoanDAO.xoaTaiKhoan(taiKhoan.getMaTK())) {
                    ThongBaoUtil.hienThiThongBao("Thành công", "Xóa tài khoản thành công!");
                    loadTaiKhoanData();
                } else {
                    ThongBaoUtil.hienThiLoi("Lỗi", "Không thể xóa tài khoản!");
                }
            } catch (SQLException e) {
                ThongBaoUtil.hienThiLoi("Lỗi", "Lỗi khi xóa tài khoản: " + e.getMessage());
            }
        }
    }
    
    private void showDoiMatKhauDialog(TaiKhoan taiKhoan) {
        
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Đổi Mật Khẩu");
        dialog.setHeaderText("Đổi mật khẩu cho tài khoản: " + taiKhoan.getTenDangNhap());
        
        PasswordField matKhauMoiField = new PasswordField();
        matKhauMoiField.setPromptText("Mật khẩu mới");
        
        PasswordField xacNhanMatKhauField = new PasswordField();
        xacNhanMatKhauField.setPromptText("Xác nhận mật khẩu mới");
        
        VBox content = new VBox(10);
        content.getChildren().addAll(matKhauMoiField, xacNhanMatKhauField);
        dialog.getDialogPane().setContent(content);
        
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String matKhauMoi = matKhauMoiField.getText();
                String xacNhanMatKhau = xacNhanMatKhauField.getText();
                
                if (matKhauMoi.isEmpty() || xacNhanMatKhau.isEmpty()) {
                    ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng nhập đầy đủ thông tin!");
                    return null;
                }
                
                if (!matKhauMoi.equals(xacNhanMatKhau)) {
                    ThongBaoUtil.hienThiLoi("Lỗi", "Mật khẩu xác nhận không khớp!");
                    return null;
                }
                
                return PasswordUtil.hashPassword(matKhauMoi);
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(matKhauHash -> {
            try {
                if (taiKhoanDAO.capNhatTaiKhoan(
                    taiKhoan.getMaTK(),
                    taiKhoan.getNhanVien(),
                    taiKhoan.getTenDangNhap(),
                    matKhauHash,
                    taiKhoan.getQuyen(),
                    taiKhoan.getTrangThaiDisplay()
                )) {
                    ThongBaoUtil.hienThiThongBao("Thành công", "Đổi mật khẩu thành công!");
                } else {
                    ThongBaoUtil.hienThiLoi("Lỗi", "Không thể đổi mật khẩu!");
                }
            } catch (SQLException e) {
                ThongBaoUtil.hienThiLoi("Lỗi", "Lỗi khi đổi mật khẩu: " + e.getMessage());
            }
        });
    }
    
    private void filterTaiKhoan(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            loadTaiKhoanData();
            return;
        }
        
        taiKhoanList.clear();
        try {
            List<TaiKhoan> dsTaiKhoan = taiKhoanDAO.layDSTaiKhoan();
            for (TaiKhoan tk : dsTaiKhoan) {
                if (tk.getTenDangNhap().toLowerCase().contains(searchText.toLowerCase()) ||
                    tk.getMaTK().toLowerCase().contains(searchText.toLowerCase()) ||
                    (tk.getNhanVien() != null && tk.getNhanVien().getMaNV().toLowerCase().contains(searchText.toLowerCase()))) {
                    taiKhoanList.add(tk);
                }
            }
        } catch (SQLException e) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể tìm kiếm tài khoản: " + e.getMessage());
        }
    }
}