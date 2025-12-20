package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.util.ContentSwitcher;
import com.example.louishotelmanagement.util.ContentManager;
import javafx.fxml.FXML;

public class TrangChuController {
    private ContentSwitcher switcher;

    @FXML
    public void moQuanLyPhong() {
         ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/quan-ly-phong-view.fxml");
    }

    @FXML
    public void moQuanLyDichVu() {
        ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/quan-ly-dich-vu-view.fxml");
    }

    @FXML
    public void moQuanLyKhachHang() {
        ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/quan-ly-khach-hang-view.fxml");
    }

    @FXML
    public void moThongKe() {
        ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/thong-ke-view.fxml");
    }

    @FXML
    public void moDatPhong() {
        ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/dat-phong-truc-tiep-view.fxml");
    }

    @FXML
    public void moNhanPhong() {
        ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/nhan-phong-view.fxml");
    }

    @FXML
    public void moTraPhong() {
        ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/tra-phong-view.fxml");
    }

    public void setContentSwitcher(ContentSwitcher switcher) {
        this.switcher = switcher;
    }
}
