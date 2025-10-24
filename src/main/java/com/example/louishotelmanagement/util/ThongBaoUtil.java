package com.example.louishotelmanagement.util;

import javafx.scene.control.Alert;

/**
 * Utility class for common UI operations
 */
public class ThongBaoUtil {

    /**
     * Hiển thị thông báo thông tin
     *
     * @param tieuDe  Tiêu đề của thông báo
     * @param noiDung Nội dung của thông báo
     */
    public static void hienThiThongBao(String tieuDe, String noiDung) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tieuDe);
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.showAndWait();
    }

    /**
     * Hiển thị thông báo cảnh báo
     *
     * @param tieuDe  Tiêu đề của thông báo
     * @param noiDung Nội dung của thông báo
     */
    public static void hienThiCanhBao(String tieuDe, String noiDung) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tieuDe);
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.showAndWait();
    }

    /**
     * Hiển thị thông báo lỗi
     *
     * @param tieuDe  Tiêu đề của thông báo
     * @param noiDung Nội dung của thông báo
     */
    public static void hienThiLoi(String tieuDe, String noiDung) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(tieuDe);
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.showAndWait();
    }

    /**
     * Hiển thị thông báo xác nhận
     *
     * @param tieuDe  Tiêu đề của thông báo
     * @param noiDung Nội dung của thông báo
     * @return true nếu người dùng chọn OK, false nếu chọn Cancel
     */
    public static boolean hienThiXacNhan(String tieuDe, String noiDung) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tieuDe);
        alert.setHeaderText(null);
        alert.setContentText(noiDung);

        return alert.showAndWait().orElse(javafx.scene.control.ButtonType.CANCEL) == javafx.scene.control.ButtonType.OK;
    }
}
