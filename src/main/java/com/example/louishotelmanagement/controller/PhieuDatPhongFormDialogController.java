package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.PhieuDatPhongDAO;
import com.example.louishotelmanagement.model.PhieuDatPhong;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PhieuDatPhongFormDialogController implements Initializable {

    @FXML
    private TextField txtMaPhieu; // maPhieu trong FXML
    @FXML
    private DatePicker dpNgayDen; // dpNgayDen trong FXML
    @FXML
    private DatePicker dpNgayDi;   // dpNgayDi trong FXML

    // Các nút (tùy chọn, bạn có thể xử lý trực tiếp)
    // @FXML private Button btnLuu;
    // @FXML private Button btnHuy;

    private PhieuDatPhongDAO phieuDatPhongDAO;
    private PhieuDatPhong phieuDatPhongHienTai; // Phiếu được truyền vào để sửa

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        phieuDatPhongDAO = new PhieuDatPhongDAO();
        // Cấu hình ban đầu nếu cần
    }

    /**
     * Phương thức được gọi từ controller mẹ (QuanLyPhieuDatPhongController)
     * để truyền dữ liệu phiếu đặt phòng vào form.
     */
    public void setPhieuDatPhong(PhieuDatPhong phieuDatPhong) {
        this.phieuDatPhongHienTai = phieuDatPhong;
        // Load dữ liệu lên UI khi dữ liệu được set
        loadDuLieu();
    }

    /**
     * Đổ dữ liệu từ model lên các control của form.
     */
    private void loadDuLieu() {
        if (phieuDatPhongHienTai != null) {
            // 1. Trường không được sửa (Mã phiếu)
            txtMaPhieu.setText(phieuDatPhongHienTai.getMaPhieu());

            // 2. Các trường cho phép sửa
            dpNgayDen.setValue(phieuDatPhongHienTai.getNgayDen());
            dpNgayDi.setValue(phieuDatPhongHienTai.getNgayDi());

            // Bạn có thể thêm logic load các trường khác như maKH, tienCoc, etc. ở đây
        }
    }

    // -------------------------------------------------------------------------
    // XỬ LÝ SỰ KIỆN CỦA NÚT
    // -------------------------------------------------------------------------

    @FXML
    private void handleLuu() {
        if (phieuDatPhongHienTai == null) return;

        // 1. Lấy dữ liệu mới từ UI
        LocalDate ngayDenMoi = dpNgayDen.getValue();
        LocalDate ngayDiMoi = dpNgayDi.getValue();

        // 2. Validate (Kiểm tra hợp lệ)
        if (ngayDenMoi == null || ngayDiMoi == null) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Ngày đến và Ngày đi không được để trống.");
            return;
        }
        if (ngayDiMoi.isBefore(ngayDenMoi) || ngayDiMoi.isEqual(ngayDenMoi)) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Ngày đi phải sau Ngày đến.");
            return;
        }

        // 3. Cập nhật Model và Database
        try {
            // Cập nhật các giá trị mới vào đối tượng hiện tại
            phieuDatPhongHienTai.setNgayDen(ngayDenMoi);
            phieuDatPhongHienTai.setNgayDi(ngayDiMoi);

            // Gọi DAO để cập nhật vào database
            if (phieuDatPhongDAO.capNhatPhieuDatPhong(phieuDatPhongHienTai)) {
                ThongBaoUtil.hienThiThongBao("Thành công", "Cập nhật phiếu đặt phòng thành công.");

                // Đóng dialog sau khi lưu thành công
                Stage stage = (Stage) txtMaPhieu.getScene().getWindow();
                stage.close();
            } else {
                ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể cập nhật phiếu đặt phòng.");
            }

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi SQL", "Lỗi khi cập nhật dữ liệu: " + e.getMessage());
        }
    }

    @FXML
    private void handleHuy() {
        // Đóng dialog khi nhấn Hủy
        Stage stage = (Stage) txtMaPhieu.getScene().getWindow();
        stage.close();
    }
}