package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.controller.LayoutController;
import com.example.louishotelmanagement.util.ContentSwitcher;
import com.example.louishotelmanagement.util.ContentManager;
import javafx.fxml.FXML;

public class TrangChuController {

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
         switcher.switchContent("/com/example/louishotelmanagement/fxml/quan-ly-phong-view.fxml");
    }

    @FXML
    public void moQuanLyDichVu() {
        switcher.switchContent("/com/example/louishotelmanagement/fxml/quan-ly-dich-vu-view.fxml");
    }

    @FXML
    public void moQuanLyKhachHang() {
        switcher.switchContent("/com/example/louishotelmanagement/fxml/quan-ly-khach-hang-view.fxml");
    }

    @FXML
    public void moThongKe() {
        switcher.switchContent("/com/example/louishotelmanagement/fxml/thong-ke-view.fxml");
    }

    @FXML
    public void moDatPhong() {
        switcher.switchContent("/com/example/louishotelmanagement/fxml/dat-phong-truc-tiep-view.fxml");
    }

    @FXML
    public void moNhanPhong() {
        switcher.switchContent("/com/example/louishotelmanagement/fxml/nhan-phong-view.fxml");
    }

    @FXML
    public void moTraPhong() {
        switcher.switchContent("/com/example/louishotelmanagement/fxml/tra-phong-view.fxml");
    }

    public void setContentSwitcher(ContentSwitcher switcher) {
        this.switcher = switcher;
    }
}
