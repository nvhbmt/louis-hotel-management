package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.util.ContentSwitchable;
import com.example.louishotelmanagement.util.ContentSwitcher;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.util.Refreshable;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DoiPhongController implements Initializable, Refreshable,ContentSwitchable {

    @FXML public ComboBox<String> dsKhachHang;
    @FXML public ComboBox<String> dsPhongHienTai;
    @FXML public ComboBox<String> dsPhong;
    @FXML public TableView<Phong> tablePhong;
    @FXML public Button btnDoiPhong;
    @FXML public ComboBox<Integer> cbTang;
    @FXML public ComboBox<LoaiPhong> cbLocLoaiPhong;

    @FXML private TableColumn<Phong, String> maPhong;
    @FXML private TableColumn<Phong, Integer> tang;
    @FXML private TableColumn<Phong, TrangThaiPhong> trangThai;
    @FXML private TableColumn<Phong, String> moTa;
    @FXML private TableColumn<Phong, String> loaiPhong;

    private PhongDAO Pdao;
    private KhachHangDAO Kdao;
    private PhieuDatPhongDAO pdpDao;
    private CTHoaDonPhongDAO cthdpDao;
    private LoaiPhongDAO loaiPhongDAO;

    private ArrayList<String> dsMaKH;
    private ObservableList<Phong> danhSachPhong;
    private ObservableList<Phong> danhSachPhongFiltered;

    private ContentSwitcher switcher;

    public void setContentSwitcher(ContentSwitcher switcher) {
        this.switcher = switcher;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pdao = new PhongDAO();
        Kdao = new KhachHangDAO();
        pdpDao = new PhieuDatPhongDAO();
        cthdpDao = new CTHoaDonPhongDAO();
        loaiPhongDAO = new LoaiPhongDAO();

        VBox.setVgrow(tablePhong, Priority.ALWAYS);

        try {
            khoiTaoDuLieu();
            khoiTaoTableView();
            khoiTaoComboBox();

            refreshData();

            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        laydsPhongHienTaiCuaKhach();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            tablePhong.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    dsPhong.getSelectionModel().select(newValue.getMaPhong());
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void nhanDuLieuTuPhong(String maPhongCanDoi) {
        try {
            ArrayList<CTHoaDonPhong> listCT = cthdpDao.getDSCTHoaDonPhongTheoMaPhong(maPhongCanDoi);
            String maKHTimThay = null;

            for (int i = listCT.size() - 1; i >= 0; i--) {
                CTHoaDonPhong ct = listCT.get(i);
                PhieuDatPhong pdp = pdpDao.layPhieuDatPhongTheoMa(ct.getMaPhieu());

                if (pdp != null && pdp.getTrangThai() == TrangThaiPhieuDatPhong.DANG_SU_DUNG) {
                    maKHTimThay = pdp.getMaKH();
                    break;
                }
            }

            if (maKHTimThay != null) {
                for (int i = 0; i < dsMaKH.size(); i++) {
                    if (dsMaKH.get(i).equals(maKHTimThay)) {
                        dsKhachHang.getSelectionModel().select(i);
                        break;
                    }
                }
                dsPhongHienTai.setValue(maPhongCanDoi);
            } else {
                ThongBaoUtil.hienThiThongBao("Thông báo", "Phòng " + maPhongCanDoi + " không có phiếu thuê đang hoạt động.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void laydsPhongHienTaiCuaKhach() throws SQLException {
        dsPhongHienTai.getItems().clear();

        int selectedIndex = dsKhachHang.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= dsMaKH.size()) return;

        String maKH = dsMaKH.get(selectedIndex);
        ArrayList<PhieuDatPhong> dsPhieu = pdpDao.layDSPhieuDatPhongTheoKhachHang(maKH);

        for (PhieuDatPhong p : dsPhieu) {
            if (p.getTrangThai() == TrangThaiPhieuDatPhong.DANG_SU_DUNG) {
                ArrayList<CTHoaDonPhong> dsCTP = cthdpDao.getCTHoaDonPhongTheoMaPhieu(p.getMaPhieu());
                for (CTHoaDonPhong ctp : dsCTP) {
                    dsPhongHienTai.getItems().add(ctp.getMaPhong());
                }
            }
        }

        if (!dsPhongHienTai.getItems().isEmpty()) {
            dsPhongHienTai.getSelectionModel().selectFirst();
        }
    }

    public void handleDoiPhong(ActionEvent actionEvent) throws SQLException {
        String phongCu = dsPhongHienTai.getValue();
        String phongMoi = dsPhong.getValue();

        if (phongCu == null || phongMoi == null) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn phòng hiện tại và phòng muốn đổi đến.");
            return;
        }

        if (phongCu.equals(phongMoi)) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Phòng mới trùng với phòng hiện tại.");
            return;
        }

        ArrayList<CTHoaDonPhong> dscthdp = cthdpDao.getDSCTHoaDonPhongTheoMaPhong(phongCu);
        CTHoaDonPhong ctCanDoi = null;

        for (int i = dscthdp.size() - 1; i >= 0; i--) {
            CTHoaDonPhong ct = dscthdp.get(i);
            PhieuDatPhong p = pdpDao.layPhieuDatPhongTheoMa(ct.getMaPhieu());
            if (p != null && p.getTrangThai() == TrangThaiPhieuDatPhong.DANG_SU_DUNG) {
                ctCanDoi = ct;
                break;
            }
        }

        if (ctCanDoi != null) {
            Phong pMoiInfo = Pdao.layPhongTheoMa(phongMoi);

            cthdpDao.capNhatMaPhongVaGia(
                    ctCanDoi.getMaPhieu(),
                    phongCu,
                    phongMoi,
                    BigDecimal.valueOf(pMoiInfo.getLoaiPhong().getDonGia())
            );

            Pdao.capNhatTrangThaiPhong(phongCu, TrangThaiPhong.TRONG.toString());
            Pdao.capNhatTrangThaiPhong(phongMoi, TrangThaiPhong.DANG_SU_DUNG.toString());

            ThongBaoUtil.hienThiThongBao("Thành công", "Đổi từ phòng " + phongCu + " sang " + phongMoi + " thành công!");
            refreshData();
        } else {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không tìm thấy dữ liệu thuê của phòng " + phongCu);
        }
    }

    private void khoiTaoTableView() {
        maPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        tang.setCellValueFactory(new PropertyValueFactory<>("tang"));

        trangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        trangThai.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(TrangThaiPhong item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    getStyleClass().clear();
                } else {
                    setText(item.toString());
                    setAlignment(Pos.CENTER);
                    getStyleClass().clear();
                    if (item == TrangThaiPhong.TRONG) getStyleClass().add("status-trong");
                }
            }
        });

        moTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        loaiPhong.setCellValueFactory(cellData -> {
            LoaiPhong lp = cellData.getValue().getLoaiPhong();
            return lp != null ? Bindings.createStringBinding(lp::getTenLoai) : Bindings.createStringBinding(() -> "");
        });

        tablePhong.setItems(danhSachPhongFiltered);
        tablePhong.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void khoiTaoDuLieu() {
        danhSachPhong = FXCollections.observableArrayList();
        danhSachPhongFiltered = FXCollections.observableArrayList();
        dsMaKH = new ArrayList<>();
    }

    public void laydsKhachHang() throws SQLException {
        dsKhachHang.getItems().clear();
        dsMaKH.clear();
        ArrayList<KhachHang> khs = Kdao.layDSKhachHang();
        for (KhachHang khachHang : khs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }

        com.example.louishotelmanagement.util.SearchBoxUtil.makeSearchable(dsKhachHang);
    }

    public void layDsPhongTrong() throws SQLException {
        dsPhong.getItems().clear();
        ArrayList<Phong> phongs = Pdao.layDSPhongTrong();
        for (Phong phong : phongs) {
            dsPhong.getItems().add(phong.getMaPhong());
        }
        danhSachPhong.clear();
        danhSachPhong.addAll(phongs);
        apDungFilter();
    }

    public void refreshData() throws SQLException {
        laydsKhachHang();
        layDsPhongTrong();
        dsKhachHang.getSelectionModel().clearSelection();
        dsPhongHienTai.getItems().clear();
        cbTang.setValue(null);
        cbLocLoaiPhong.setValue(null);
    }

    private void apDungFilter() {
        danhSachPhongFiltered.clear();
        List<Phong> filtered = danhSachPhong.stream()
                .filter(phong -> {
                    Integer tangFilter = cbTang.getValue();
                    if (tangFilter != null && (phong.getTang() == null || !phong.getTang().equals(tangFilter))) return false;
                    LoaiPhong loaiPhongFilter = cbLocLoaiPhong.getValue();
                    return loaiPhongFilter == null || (phong.getLoaiPhong() != null && phong.getLoaiPhong().getMaLoaiPhong().equals(loaiPhongFilter.getMaLoaiPhong()));
                })
                .toList();
        danhSachPhongFiltered.addAll(filtered);
    }

    private void khoiTaoComboBox() {
        List<Integer> danhSachTang = new ArrayList<>();
        for (int i = 1; i <= 10; i++) danhSachTang.add(i);
        cbTang.setItems(FXCollections.observableArrayList(danhSachTang));
        khoiTaoComboBoxLoaiPhong();
    }

    private void khoiTaoComboBoxLoaiPhong() {
        try {
            List<LoaiPhong> danhSachLoaiPhong = loaiPhongDAO.layDSLoaiPhong();
            cbLocLoaiPhong.setItems(FXCollections.observableArrayList(danhSachLoaiPhong));
            Callback<ListView<LoaiPhong>, ListCell<LoaiPhong>> factory = param -> new ListCell<>() {
                @Override protected void updateItem(LoaiPhong item, boolean empty) {
                    super.updateItem(item, empty);
                    setText((empty || item == null) ? "Chọn loại phòng" : item.getTenLoai());
                }
            };
            cbLocLoaiPhong.setCellFactory(factory);
            cbLocLoaiPhong.setButtonCell(factory.call(null));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML private void handleLocTang() { apDungFilter(); }
    @FXML private void handleLocLoaiPhong() { apDungFilter(); }
    public void handleRefresh(ActionEvent actionEvent) throws SQLException { refreshData(); }
}