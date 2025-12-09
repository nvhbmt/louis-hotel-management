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

    public void setContentSwitcher(ContentSwitcher switcher) {
        this.switcher = switcher;
    }
}
