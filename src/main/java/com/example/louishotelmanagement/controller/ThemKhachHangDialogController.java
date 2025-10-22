package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class ThemKhachHangDialogController implements Initializable {

    public TextField hoTen;
    public TextField soDT;
    public TextField email;
    public TextField diaChi;
    public DatePicker ngaySinh;
    public TextField ghiChu;
    public TextField cccd;
    public KhachHangDAO kDao;
    private String maKH;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kDao = new KhachHangDAO();
    }

    public void handleThem(ActionEvent actionEvent) throws SQLException {
        String hoTenStr = hoTen.getText();
        String soDTStr = soDT.getText();
        String emailStr = email.getText();
        String diaChiStr = diaChi.getText();
        LocalDate ngaySinhDate = ngaySinh.getValue();
        String ghiChuStr = ghiChu.getText();
        String cccdStr = cccd.getText();
        // Các trường bắt buộc không được để trống
        if (hoTenStr.isEmpty() || soDTStr.isEmpty() || cccdStr.isEmpty() || ngaySinhDate == null) {
            UIUtils.hienThiLoi("Lỗi nhập liệu", "Vui lòng nhập đầy đủ Họ Tên, SĐT, CCCD và Ngày Sinh.");
            return;
        }

        // Kiểm tra định dạng SĐT (ví dụ: 10 chữ số, có thể điều chỉnh theo định dạng bạn muốn)
        if (!soDTStr.matches("^\\d{10}$")) {
            UIUtils.hienThiLoi("Lỗi SĐT", "Số điện thoại phải gồm 10 chữ số.");
            return;
        }

        // Kiểm tra tính logic của Ngày Sinh
        if (ngaySinhDate.isAfter(LocalDate.now())) {
            UIUtils.hienThiLoi("Lỗi ngày sinh", "Ngày sinh không thể là ngày trong tương lai.");
            return;
        }
        Random ran = new Random();
        ArrayList<KhachHang> listKhachHang = kDao.layDSKhachHang();
        ArrayList<String> dsMaKh = new ArrayList<>();
        for(KhachHang k: listKhachHang){
            dsMaKh.add(k.getMaKH());
        }
        do{
            maKH = "KH"+ran.nextInt(90);
        }while(dsMaKh.contains(maKH));
        KhachHang kh =  new KhachHang(maKH,hoTenStr,soDTStr,emailStr,diaChiStr,ngaySinhDate,ghiChuStr,cccdStr);
        kDao.themKhachHang(kh);
        UIUtils.hienThiThongBao("Thông báo","Thêm khách hàng thành công");
        // Đóng cửa sổ hiện tại
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void handleHuy(ActionEvent actionEvent) {
        // Lấy Stage (cửa sổ) hiện tại từ sự kiện và đóng nó
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
