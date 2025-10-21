package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.utils.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class QuanLyKhachHangController implements Initializable {

    private KhachHangDAO KhachHangDAO;
    @FXML
    private ComboBox<String> cmbHang;
    @FXML
    private ComboBox<String> cmbTrangThai;
    @FXML
    private TableView<KhachHang> tableViewKhachHang;
    @FXML
    private TableColumn<KhachHang, String> colMaKH;
    @FXML
    private TableColumn<KhachHang, String> colHoTen;
    @FXML
    private TableColumn<KhachHang, String> colSoDT;
    @FXML
    private TableColumn<KhachHang, String> colEmail;
    @FXML
    private TableColumn<KhachHang, String> colHangKhach;
    private ArrayList<KhachHang> dsKhachHang;
    @FXML
    private Label lblSoKhachHang;
    @FXML
    private Label lblKhachVIP;
    @FXML
    private TextField txtTimKiem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            KhachHangDAO = new KhachHangDAO();

            //Khởi tạo dữ liệu
            setupComboBox();
            setupTableColumns();
            setupListner();
            loadData();

        }catch (Exception e){
            UIUtils.hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu" + e.getMessage());
            e.printStackTrace();
        }
    }

    //Khởi tạo combobox
    private void setupComboBox() {
        ObservableList<String> dsHang = FXCollections.observableArrayList(
                "Tất cả hạng",
                "Khách VIP",
                "Khách quen",
                "Khách doanh nghiệp"
        );
        cmbHang.setItems(dsHang);
        cmbHang.setValue("Tất cả hạng");

        ObservableList<String> dsTrangThai = FXCollections.observableArrayList(
                "Tất cả trạng thái",
                "Đang lưu trú",
                "Check-out",
                "Đã đặt"
        );
        cmbTrangThai.setItems(dsTrangThai);
        cmbTrangThai.setValue("Tất cả trạng thái");
    }

    //Nạp dữ liệu vào table
    private void setupTableColumns() {
        colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colSoDT.setCellValueFactory(new PropertyValueFactory<>("soDT"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colHangKhach.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
    }

    //Đăng ký sự kiện
    private void setupListner() {
        cmbHang.valueProperty().addListener((observable, oldValue, newValue) -> {
            apDungFilterKhachHang();
        });
        txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> {
            apDungFilterKhachHang();
        });
    }

    //Load dữ liệu, cập nhật thống kê
    private  void loadData() {
        try {
            dsKhachHang = KhachHangDAO.layDSKhachHang();
            ObservableList<KhachHang> danhSach =  FXCollections.observableArrayList(dsKhachHang);

            tableViewKhachHang.setItems(danhSach);

            capNhatThongKe();
        }catch (Exception e){
            UIUtils.hienThiThongBao("Lỗi","Lỗi lấy dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Cập nhật các ô thống kê
    void capNhatThongKe() {
        try {
            int tongSoKhach = dsKhachHang.size();
            int soKhachVip = 0;

            for (KhachHang kh : dsKhachHang) {
                if("Khách VIP".equals(kh.getGhiChu())){
                    soKhachVip++;
                }
            }

            lblSoKhachHang.setText(String.valueOf(tongSoKhach));
            lblKhachVIP.setText(String.valueOf(soKhachVip));

        } catch (Exception e) {
            UIUtils.hienThiThongBao("Lỗi", "Không thể cập nhật thống kê: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void apDungFilterKhachHang() {
        // Lấy text tìm kiếm và giá trị combo box
        String timKiem = (txtTimKiem.getText() != null) ? txtTimKiem.getText().toLowerCase() : "";
        String hangFilter = cmbHang.getValue();

        // Tạo danh sách lọc
        ObservableList<KhachHang> filteredList = FXCollections.observableArrayList();

        for (KhachHang kh : dsKhachHang) {
            boolean matchText = timKiem.isEmpty() ||
                    (kh.getHoTen() != null && kh.getHoTen().toLowerCase().contains(timKiem)) ||
                    (kh.getSoDT() != null && kh.getSoDT().toLowerCase().contains(timKiem)) ||
                    (kh.getEmail() != null && kh.getEmail().toLowerCase().contains(timKiem));

            boolean matchHang = hangFilter == null || hangFilter.equals("Tất cả hạng") ||
                    (kh.getGhiChu() != null && kh.getGhiChu().equalsIgnoreCase(hangFilter));

            if (matchText && matchHang) {
                filteredList.add(kh);
            }
        }

        tableViewKhachHang.setItems(filteredList);
    }
}
