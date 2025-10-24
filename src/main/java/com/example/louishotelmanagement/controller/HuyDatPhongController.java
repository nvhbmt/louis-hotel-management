package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.CTHoaDonPhongDAO;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.PhieuDatPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HuyDatPhongController implements Initializable, Refreshable {

    public TextField searchTextField;
    @FXML
    public TableView tablePhieu;
    @FXML
    public TableColumn<PhieuDatPhong, String> colMaPhieu;
    @FXML
    public TableColumn<PhieuDatPhong, LocalDate> colNgayDat;
    @FXML
    public TableColumn<PhieuDatPhong, LocalDate> colNgayDen;
    @FXML
    public TableColumn<PhieuDatPhong, LocalDate> colNgayDi;
    @FXML
    public TableColumn<PhieuDatPhong, TrangThaiPhieuDatPhong> colTrangThai;
    @FXML
    public TableColumn<PhieuDatPhong, String> colGhiChu;
    @FXML
    public TableColumn<PhieuDatPhong, String> colTenKhachHang;
    @FXML
    public TableColumn<PhieuDatPhong, String> colMaNhanVien;
    public TextField txtMaPhong;
    public TextField txtLoaiPhong;
    public TextField txtTang;
    public TextField txtTrangThai;
    public TextField txtMoTa;
    public TextField txtDonGia;
    public KhachHangDAO kDao;
    public PhongDAO phDao;
    public PhieuDatPhongDAO pDao;
    public CTHoaDonPhongDAO cthdpDao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kDao = new KhachHangDAO();
        pDao = new PhieuDatPhongDAO();
        cthdpDao = new CTHoaDonPhongDAO();
        phDao = new PhongDAO();
        try {
            KhoiTaoTableView();
            tablePhieu.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        loadCTThongTinPhong();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void KhoiTaoTableView() throws SQLException {
        colMaPhieu.setCellValueFactory(new PropertyValueFactory<>("maPhieu"));
        colNgayDat.setCellValueFactory(new PropertyValueFactory<>("ngayDat"));
        colNgayDen.setCellValueFactory(new PropertyValueFactory<>("ngayDen"));
        colNgayDi.setCellValueFactory(new PropertyValueFactory<>("ngayDi"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        colTrangThai.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(TrangThaiPhieuDatPhong item, boolean empty) {
                super.updateItem(item, empty);

                // Luôn xóa style class cũ trước khi thiết lập
                getStyleClass().removeAll("status-hoan-thanh", "status-da-dat", "status-dang-su-dung", "status-da-huy");

                if (empty || item == null) {
                    setText(null);
                    // Đã xóa style class ở trên
                } else {
                    setText(item.toString());

                    // 💡 Ánh xạ trạng thái Enum sang tên class CSS phù hợp
                    switch (item) {
                        case HOAN_THANH -> getStyleClass().add("status-hoan-thanh");
                        case DA_DAT -> getStyleClass().add("status-da-dat");
                        case DANG_SU_DUNG -> getStyleClass().add("status-dang-su-dung");
                        case DA_HUY -> getStyleClass().add("status-da-huy");
                        default -> {
                            // Để phòng trường hợp có trạng thái mới chưa được xử lý
                        }
                    }
                }
            }
        });

        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        colTenKhachHang.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        colTenKhachHang.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);

                } else {
                    try {
                        KhachHang khachHang = kDao.layKhachHangTheoMa(item);

                        if (khachHang != null) {
                            // Lấy Tên Khách Hàng và set nó vào ô
                            String tenKh = khachHang.getHoTen();
                            setText(tenKh);
                        } else {
                            // Xử lý trường hợp không tìm thấy khách hàng
                            setText("Không tìm thấy");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        colMaNhanVien.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        ArrayList<PhieuDatPhong> dsPhieu = pDao.layDSPhieuDatPhong();
        ObservableList<PhieuDatPhong> observableListPhieu = FXCollections.observableArrayList(dsPhieu);
        tablePhieu.setItems(observableListPhieu);
        // Cho phép chọn nhiều dòng
        tablePhieu.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void loadCTThongTinPhong() throws SQLException {
        if (tablePhieu.getSelectionModel().getSelectedItem() == null) {
            ThongBaoUtil.hienThiLoi("Lỗi thông tin", "Không tìm thấy thông tin phòng");
        } else {
            PhieuDatPhong phieuTam = (PhieuDatPhong) tablePhieu.getSelectionModel().getSelectedItem();
            ArrayList<CTHoaDonPhong> dsCTP = cthdpDao.getCTHoaDonPhongTheoMaPhieu(phieuTam.getMaPhieu());
            Phong pTam = phDao.layPhongTheoMa(dsCTP.getLast().getMaPhong());
            txtMaPhong.setText(pTam.getMaPhong());
            txtLoaiPhong.setText(pTam.getLoaiPhong().getTenLoai());
            txtTang.setText(String.valueOf(pTam.getTang()));
            txtTrangThai.setText(phieuTam.getTrangThai().toString());
            txtDonGia.setText(String.valueOf(pTam.getLoaiPhong().getDonGia()));
            txtMoTa.setText(pTam.getMoTa());
        }
    }


    public void handleTim(ActionEvent actionEvent) throws SQLException {
        String searchtxt = searchTextField.getText();
        if (searchtxt == null || searchtxt.isEmpty()) {
            ThongBaoUtil.hienThiLoi("Không thể tìm", "Không được để trống nội dung tìm kiếm");
        } else {
            PhieuDatPhong phieuDatPhong = pDao.layPhieuDatPhongTheoMa(searchtxt);
            if (phieuDatPhong != null) {
                ObservableList<PhieuDatPhong> ketQuaTimKiem = FXCollections.observableArrayList(phieuDatPhong);
                tablePhieu.setItems(ketQuaTimKiem);
                tablePhieu.refresh();
            } else {
                tablePhieu.setItems(FXCollections.observableArrayList());
                ThongBaoUtil.hienThiLoi("Lỗi tìm kiếm", "Không tìm thấy bất kì phiếu đặt phòng");
            }
        }
    }

    public void handleLamMoi(ActionEvent actionEvent) throws SQLException {
        ArrayList<PhieuDatPhong> dsPhieu = pDao.layDSPhieuDatPhong();
        ObservableList<PhieuDatPhong> observableListPhieu = FXCollections.observableArrayList(dsPhieu);
        tablePhieu.setItems(observableListPhieu);
        tablePhieu.getSelectionModel().clearSelection();
        tablePhieu.refresh();
        searchTextField.setText("");
    }

    public void handleHuyPhieuDat(ActionEvent actionEvent) throws SQLException {
        if (tablePhieu.getSelectionModel().getSelectedIndex() != -1) {
            PhieuDatPhong phieuTam = (PhieuDatPhong) tablePhieu.getSelectionModel().getSelectedItem();
            if (phieuTam.getTrangThai().equals(TrangThaiPhieuDatPhong.DA_DAT)) {
                phieuTam.setTrangThai(TrangThaiPhieuDatPhong.DA_HUY);
                pDao.capNhatTrangThaiPhieuDatPhong(phieuTam.getMaPhieu(), phieuTam.getTrangThai().toString());
                ArrayList<CTHoaDonPhong> dsCTP = cthdpDao.getCTHoaDonPhongTheoMaPhieu(phieuTam.getMaPhieu());
                for (CTHoaDonPhong ctp : dsCTP) {
                    boolean check = phDao.capNhatTrangThaiPhong(ctp.getMaPhong(), TrangThaiPhong.TRONG.toString());
                    if (check == false) {
                        ThongBaoUtil.hienThiLoi("Lỗi hủy đặt phòng", "Không thể cập nhật trạng thái phòng");
                        return;
                    }
                }
                ThongBaoUtil.hienThiThongBao("Thông báo", "Hủy đặt phòng thành công");
                tablePhieu.refresh();
            } else {
                ThongBaoUtil.hienThiLoi("Lỗi hủy đặt phòng", "Phòng đang trong trạng thái " + phieuTam.getTrangThai().toString() + " không thể hủy!");
            }

        } else {
            ThongBaoUtil.hienThiLoi("Lỗi hủy đặt phòng", "Vui lòng chọn phiếu để hủy");
        }
    }

    @Override
    public void refreshData() throws SQLException {
        searchTextField.setText("");
        KhoiTaoTableView();
        txtMaPhong.setText(null);
        txtLoaiPhong.setText(null);
        txtMoTa.setText(null);
        txtTang.setText(null);
        txtDonGia.setText(null);
        txtTrangThai.setText(null);
    }
}
