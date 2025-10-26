package com.example.louishotelmanagement.controller;// package com.example.louishotelmanagement.controller;
// File: ChiTietPhongTrongPhieuController.java

import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.CTHoaDonPhong;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChiTietPhongTrongPhieuController implements Initializable {

    @FXML
    private Label lblMaPhieu;
    @FXML
    private TableView<Phong> tblChiTietPhong;
    @FXML
    private TableColumn<Phong, String> colMaPhong;
    @FXML
    private TableColumn<Phong, String> colTenLoaiPhong;
    @FXML
    private TableColumn<Phong, Double> colGia;

    // 💡 KHAI BÁO MỚI: Thêm cột cho Tầng
    @FXML
    private TableColumn<Phong, Integer> colTang; // Kiểu Integer cho tầng

    private PhongDAO phDao = new PhongDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colMaPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        colTenLoaiPhong.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();

            // 2. Trả về StringBinding chứa Tên Loại.
            // Nếu LoaiPhong không null, liên kết (bind) với thuộc tính TenLoai.
            return loaiPhong != null ?
                    javafx.beans.binding.Bindings.createStringBinding(loaiPhong::getTenLoai) :
                    javafx.beans.binding.Bindings.createStringBinding(() -> ""); // Xử lý trường hợp null
        });
        colGia.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();
            return loaiPhong != null ?
                    javafx.beans.binding.Bindings.createObjectBinding(loaiPhong::getDonGia) :
                    javafx.beans.binding.Bindings.createObjectBinding(() -> -0.0);
        });

        // 💡 THAY ĐỔI: Thiết lập CellValueFactory cho cột Tầng
        colTang.setCellValueFactory(new PropertyValueFactory<>("tang")); // Lấy giá trị từ trường 'tang' trong đối tượng Phong
    }

    /**
     * Phương thức nhận dữ liệu từ Controller cha và hiển thị.
     */
    public void setChiTietData(String maPhieu, ArrayList<CTHoaDonPhong> dsCTP) throws SQLException {
        lblMaPhieu.setText("Chi Tiết Phòng Phiếu: " + maPhieu);

        ArrayList<Phong> dsPhong = new ArrayList<>();
        for (CTHoaDonPhong ctp : dsCTP) {
            // Lấy thông tin chi tiết của phòng
            Phong phong = phDao.layPhongTheoMa(ctp.getMaPhong());
            if (phong != null) {
                // Bạn có thể cần gán thêm TenLoaiPhong và Gia cho đối tượng Phong
                // từ các DAO khác nếu nó không có sẵn.
                // Ví dụ: phong.setTenLoaiPhong(phDao.layTenLoaiPhong(phong.getMaLoaiPhong()));
                dsPhong.add(phong);
            }
        }

        ObservableList<Phong> observableListPhong = FXCollections.observableArrayList(dsPhong);
        tblChiTietPhong.setItems(observableListPhong);
    }

    @FXML
    private void handleClose() {
        // Lấy Stage (cửa sổ) hiện tại từ bất kỳ thành phần nào trong Scene
        Stage stage = (Stage) lblMaPhieu.getScene().getWindow();

        // Đóng Stage
        stage.close();
    }
}