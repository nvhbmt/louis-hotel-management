// File: PhieuDatPhongPDFController.java

package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.model.PhieuDatPhong;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PhieuDatPhongPDFController {

    @FXML private VBox rootContainer; // Container chính chứa tất cả nội dung phiếu
    @FXML private Label lblMaPhieu;
    @FXML private Label lblNgayLap;
    @FXML private Label lblKhachHang;
    @FXML private Label lblNgayDen;
    @FXML private Label lblNgayDi;
    @FXML private VBox containerChiTietPhong; // VBox để thêm các Label/HBox chi tiết phòng
    @FXML private Label lblTongTien;
    @FXML private Button btnIn;

    private PhieuDatPhong pdp;
    private ArrayList<Phong> danhSachPhong;

    public void setPhieuDatPhongData(PhieuDatPhong pdp, ArrayList<Phong> dsPhong) {
        this.pdp = pdp;
        this.danhSachPhong = dsPhong;
        hienThiChiTiet();
    }

    private void hienThiChiTiet() {
        if (pdp == null || danhSachPhong == null) return;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        lblMaPhieu.setText("Mã Phiếu: " + pdp.getMaPhieu());
        lblNgayLap.setText("Ngày Lập: " + pdp.getNgayDat().format(formatter));
        // Lấy tên khách hàng từ DAO hoặc đối tượng KhachHang liên quan
        lblKhachHang.setText("Khách Hàng: " + pdp.getMaKH());
        lblNgayDen.setText("Ngày Đến: " + pdp.getNgayDen().format(formatter));
        lblNgayDi.setText("Ngày Đi: " + pdp.getNgayDi().format(formatter));

        double tongTien = 0;
        containerChiTietPhong.getChildren().clear();

        for (Phong p : danhSachPhong) {
            double donGia = p.getLoaiPhong().getDonGia();
            tongTien += donGia;

            Label maPhongLabel = new Label(p.getMaPhong());
            Label loaiPhongLabel = new Label(p.getLoaiPhong().getTenLoai());
            Label donGiaLabel = new Label(String.format("%,.0f VNĐ/đêm", donGia));

            // ✅ Đặt độ rộng cố định cho từng cột
            maPhongLabel.setPrefWidth(80);   // cột 1
            loaiPhongLabel.setPrefWidth(129); // cột 2
            donGiaLabel.setPrefWidth(150);    // cột 3

            // Căn trái, phải cho đẹp
            maPhongLabel.setAlignment(Pos.CENTER_LEFT);
            loaiPhongLabel.setAlignment(Pos.CENTER_LEFT);
            donGiaLabel.setAlignment(Pos.CENTER_RIGHT);

            // Gom 3 label vào 1 hàng
            HBox row = new HBox(20, maPhongLabel, loaiPhongLabel, donGiaLabel);
            row.setAlignment(Pos.CENTER_LEFT);

            containerChiTietPhong.getChildren().add(row);
        }


        lblTongTien.setText(String.format("TỔNG CỘNG (Tạm tính): %,.0f VNĐ", tongTien));
    }

    @FXML
    private void handleIn() {
        chupManHinhVaLuu(rootContainer);
    }

    /**
     * Hàm chụp màn hình Node (thường là VBox/BorderPane) và lưu dưới dạng ảnh PNG.
     * Sau đó người dùng có thể dùng ảnh này để in ra PDF.
     */
    private void chupManHinhVaLuu(Node node) {
        // Chụp ảnh giao diện của Node
        WritableImage image = node.snapshot(null, null);

        // Mở hộp thoại chọn nơi lưu file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG file (*.png)", "*.png"));
        fileChooser.setInitialFileName("XacNhanDatPhong_" + pdp.getMaPhieu() + ".png");

        // Lấy Stage từ Node để mở FileChooser
        Stage stage = (Stage) node.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                // Lưu ảnh vào file
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                ThongBaoUtil.hienThiThongBao("Thành công", "Đã lưu phiếu xác nhận dưới dạng ảnh PNG tại:\n" + file.getAbsolutePath());

                // Hướng dẫn in ra PDF
                ThongBaoUtil.hienThiThongBao("Hướng dẫn", "Bạn có thể mở file PNG này và chọn 'In' để xuất ra PDF (sử dụng máy in ảo như 'Microsoft Print to PDF' hoặc tương đương).");
            } catch (IOException e) {
                ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi khi lưu ảnh: " + e.getMessage());
            }
        }
    }

    public void handleDong(ActionEvent actionEvent) {
        Stage stage = (Stage) btnIn.getScene().getWindow();
        stage.close();
    }
}