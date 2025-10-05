package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.LoaiPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class RoomFormDialogController implements Initializable {

    @FXML private Label lblTieuDe;
    @FXML private TextField txtMaPhong;
    @FXML private Spinner<Integer> spinnerTang;
    @FXML private ComboBox<String> cbTrangThai;
    @FXML private ComboBox<LoaiPhong> cbLoaiPhong;
    @FXML private TextArea txtMoTa;
    @FXML private Button btnHuy;
    @FXML private Button btnLuu;

    private PhongDAO phongDAO;
    private LoaiPhongDAO loaiPhongDAO;
    private String mode = "ADD"; // ADD hoặc EDIT

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo spinner tầng
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        spinnerTang.setValueFactory(valueFactory);
        
        // Khởi tạo ComboBox trạng thái
        List<String> danhSachTrangThai = List.of("Trống", "Đã đặt", "Đang sử dụng", "Bảo trì", "Dọn dẹp");
        cbTrangThai.setItems(FXCollections.observableArrayList(danhSachTrangThai));
        
        // Thiết lập ComboBox loại phòng để hiển thị tên loại phòng
        cbLoaiPhong.setCellFactory(_ -> new ListCell<LoaiPhong>() {
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
        
        cbLoaiPhong.setButtonCell(new ListCell<LoaiPhong>() {
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
    }

    public void setPhongDAO(PhongDAO phongDAO) {
        this.phongDAO = phongDAO;
    }

    public void setLoaiPhongDAO(LoaiPhongDAO loaiPhongDAO) {
        this.loaiPhongDAO = loaiPhongDAO;
        taiDuLieuLoaiPhong();
    }

    public void setMode(String mode) {
        this.mode = mode;
        if ("EDIT".equals(mode)) {
            lblTieuDe.setText("Sửa Thông Tin Phòng");
            btnLuu.setText("Cập nhật");
        } else {
            lblTieuDe.setText("Thêm Phòng Mới");
            btnLuu.setText("Lưu");
        }
    }

    public void setPhong(Phong phong) {
        if (phong != null) {
            txtMaPhong.setText(phong.getMaPhong());
            txtMaPhong.setDisable(true); // Không cho sửa mã phòng khi edit
            
            if (phong.getTang() != null) {
                spinnerTang.getValueFactory().setValue(phong.getTang());
            }
            
            cbTrangThai.setValue(phong.getTrangThai());
            txtMoTa.setText(phong.getMoTa());
            
            // Tìm và chọn loại phòng
            if (phong.getMaLoaiPhong() != null && !phong.getMaLoaiPhong().isEmpty()) {
                for (LoaiPhong loaiPhong : cbLoaiPhong.getItems()) {
                    if (loaiPhong.getMaLoaiPhong().equals(phong.getMaLoaiPhong())) {
                        cbLoaiPhong.setValue(loaiPhong);
                        break;
                    }
                }
            }
        }
    }

    private void taiDuLieuLoaiPhong() {
        try {
            List<LoaiPhong> danhSachLoaiPhong = loaiPhongDAO.layDSLoaiPhong();
            cbLoaiPhong.setItems(FXCollections.observableArrayList(danhSachLoaiPhong));
        } catch (SQLException e) {
            hienThiThongBao("Lỗi", "Không thể tải danh sách loại phòng: " + e.getMessage());
        }
    }

    @FXML
    private void handleLuu() {
        if (!kiemTraDuLieuHopLe()) {
            return;
        }

        try {
            Phong phong = new Phong();
            phong.setMaPhong(txtMaPhong.getText().trim());
            phong.setTang(spinnerTang.getValue());
            phong.setTrangThai(cbTrangThai.getValue());
            phong.setMoTa(txtMoTa.getText().trim());
            
            LoaiPhong loaiPhongChon = cbLoaiPhong.getValue();
            if (loaiPhongChon != null) {
                phong.setMaLoaiPhong(loaiPhongChon.getMaLoaiPhong());
            }

            boolean thanhCong = false;
            
            if ("ADD".equals(mode)) {
                thanhCong = phongDAO.themPhong(phong);
                if (thanhCong) {
                    hienThiThongBao("Thành công", "Đã thêm phòng thành công!");
                } else {
                    hienThiThongBao("Lỗi", "Không thể thêm phòng! Có thể mã phòng đã tồn tại.");
                }
            } else if ("EDIT".equals(mode)) {
                thanhCong = phongDAO.capNhatPhong(phong);
                if (thanhCong) {
                    hienThiThongBao("Thành công", "Đã cập nhật phòng thành công!");
                } else {
                    hienThiThongBao("Lỗi", "Không thể cập nhật phòng!");
                }
            }

            if (thanhCong) {
                dongDialog();
            }

        } catch (SQLException e) {
            hienThiThongBao("Lỗi", "Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    @FXML
    private void handleHuy() {
        dongDialog();
    }

    private boolean kiemTraDuLieuHopLe() {
        StringBuilder loi = new StringBuilder();

        if (txtMaPhong.getText().trim().isEmpty()) {
            loi.append("• Mã phòng không được để trống\n");
        }

        if (cbTrangThai.getValue() == null) {
            loi.append("• Vui lòng chọn trạng thái phòng\n");
        }

        if (cbLoaiPhong.getValue() == null) {
            loi.append("• Vui lòng chọn loại phòng\n");
        }

        if (loi.length() > 0) {
            hienThiThongBao("Dữ liệu không hợp lệ", loi.toString());
            return false;
        }

        return true;
    }

    private void dongDialog() {
        Stage stage = (Stage) btnLuu.getScene().getWindow();
        stage.close();
    }

    private void hienThiThongBao(String tieuDe, String noiDung) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tieuDe);
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.showAndWait();
    }
}
