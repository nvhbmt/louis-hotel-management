package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.util.ContentSwitchable;
import com.example.louishotelmanagement.util.ContentSwitcher;
import com.example.louishotelmanagement.view.QuanLyDichVuView;
import com.example.louishotelmanagement.view.QuanLyKhachHangView;
import com.example.louishotelmanagement.view.QuanLyPhongView;
import com.example.louishotelmanagement.view.DatPhongView;
import com.example.louishotelmanagement.view.NhanPhongView;
import com.example.louishotelmanagement.view.ThongKeView;
import com.example.louishotelmanagement.view.TraPhongView;
import javafx.fxml.FXML;

public class TrangChuController implements ContentSwitchable {

    @FXML
    public void initialize() {
        // Setup ContentSwitcher từ LayoutController instance
        ContentSwitcher switcher = LayoutController.getInstance();
        if (switcher != null) {
            setContentSwitcher(switcher);
        }
    }

    private ContentSwitcher switcher;

    @FXML
    public void moQuanLyPhong() {
         switcher.switchContent(new QuanLyPhongView().getRoot());
    }

    @FXML
    public void moQuanLyDichVu() {
        QuanLyDichVuView quanLyDichVuView = QuanLyDichVuView.getInstance();
        switcher.switchContent(quanLyDichVuView.getRoot());
    }

    @FXML
    public void moQuanLyKhachHang() {
        QuanLyKhachHangView quanLyKhachHangView = QuanLyKhachHangView.getInstance();
        switcher.switchContent(quanLyKhachHangView.getRoot());
    }

    @FXML
    public void moThongKe() {
        switcher.switchContent(ThongKeView.getInstance().getRoot());
    }

    @FXML
    public void moDatPhong() {
        switcher.switchContent(new DatPhongView().getRoot());
    }

    public void moNhanPhong() {
        NhanPhongView view = new NhanPhongView();
        NhanPhongController controller = new NhanPhongController(view);
        switcher.switchContent(view.getRoot());
    }

    @FXML
    public void moTraPhong() {
        if (switcher != null) {
            // 1. Khởi tạo View (Giao diện)
            TraPhongView view = new TraPhongView();

            // 2. Khởi tạo Controller (Logic) và truyền View vào để link các control
            TraPhongController controller = new TraPhongController(view);

            // 3. Truyền bộ chuyển đổi nội dung cho controller (để có thể mở dialog thanh toán)
            controller.setContentSwitcher(this.switcher);

            // 4. Lấy root từ View đã được Controller cấu hình và hiển thị
            switcher.switchContent(view.getRoot());
        }
    }

    public void setContentSwitcher(ContentSwitcher switcher) {
        this.switcher = switcher;
    }
}
