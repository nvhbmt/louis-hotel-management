package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class DatPhongTaiQuayController implements Initializable {

    public TextField maNhanVien;
    public DatePicker ngayDi;
    public ComboBox dsKhachHang;
    public TableView tablePhong;
    public Button btnDatPhong;
    @FXML
    public TableColumn<Phong, Void> colDaChon;
    public Label SoPhongDaChon;
    public Label TongTien;
    @FXML
    private TableColumn<Phong, String> colMaPhong;
    @FXML
    private TableColumn<Phong, Integer> colTang;
    @FXML
    private TableColumn<Phong, TrangThaiPhong> colTrangThai;
    @FXML
    private TableColumn<Phong, String> colMoTa;
    @FXML
    private TableColumn<Phong, String> colTenLoaiPhong;
    @FXML
    private TableColumn<Phong, Double> colDonGia;
    @FXML
    public TableColumn<Phong, Void> colThaoTac;
    private PhongDAO Pdao;
    private KhachHangDAO Kdao;
    private PhieuDatPhongDAO pdpDao;
    private CTPhieuDatPhongDAO ctpDao;
    private ArrayList<String> dsMaKH;
    private NhanVienDAO nvdao;
    private String maPhieu;
    private ArrayList<Phong> listPhongDuocDat;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pdao = new PhongDAO();
        Kdao = new KhachHangDAO();
        pdpDao = new PhieuDatPhongDAO();
        nvdao = new NhanVienDAO();
        ctpDao = new CTPhieuDatPhongDAO();
        listPhongDuocDat = new ArrayList<>();
        tablePhong.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try {
                khoiTaoTableView();
                laydsKhachHang();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            dsKhachHang.getSelectionModel().selectFirst();
    }
    private void khoiTaoTableView() throws SQLException {
        // Thiết lập các cột
        colMaPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        colTang.setCellValueFactory(new PropertyValueFactory<>("tang"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai" ));
        colTrangThai.setCellFactory(_ -> new TableCell<>() {
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
                    getStyleClass().add("status-trong");
                }
            }
        });
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        colTenLoaiPhong.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();

            // 2. Trả về StringBinding chứa Tên Loại.
            // Nếu LoaiPhong không null, liên kết (bind) với thuộc tính TenLoai.
            return loaiPhong != null ?
                    javafx.beans.binding.Bindings.createStringBinding(loaiPhong::getTenLoai) :
                    javafx.beans.binding.Bindings.createStringBinding(() -> ""); // Xử lý trường hợp null
        });
        colDonGia.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();
            return loaiPhong != null ?
                    javafx.beans.binding.Bindings.createObjectBinding(loaiPhong::getDonGia) :
                    javafx.beans.binding.Bindings.createObjectBinding(() -> -0.0);
        });
        colThaoTac.setCellFactory(_ -> new TableCell<>() {

            private final Button btnThem = new Button("Thêm");

            {
                btnThem.getStyleClass().addAll("btn", "btn-xs", "btn-info", "btn-table-edit");


                btnThem.setOnAction(_ -> {
                    Phong phong = getTableView().getItems().get(getIndex());
                    tablePhong.getSelectionModel().select(phong);
                    boolean isContain = listPhongDuocDat.contains(phong);
                    if(isContain) {
                        listPhongDuocDat.remove(phong);
                        SoPhongDaChon.setText(String.valueOf(listPhongDuocDat.size()));
                        TongTien.setText(String.valueOf(TinhTongTien(listPhongDuocDat)));
                    }else{
                        listPhongDuocDat.add(phong);
                        SoPhongDaChon.setText(String.valueOf(listPhongDuocDat.size()));
                        TongTien.setText(String.valueOf(TinhTongTien(listPhongDuocDat)));
                    }
                    getTableView().refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    Phong phong = getTableView().getItems().get(getIndex());
                    boolean isAdded = listPhongDuocDat.contains(phong);
                    btnThem.getStyleClass().removeAll("btn", "btn-xs", "btn-info", "btn-table-add", "btn-danger","btn-table-remove");
                    if(isAdded) {
                        btnThem.setText("Bỏ chọn");
                        btnThem.getStyleClass().addAll("btn", "btn-xs", "btn-danger","btn-table-remove");
                    }else{
                        btnThem.setText("Thêm");
                        btnThem.getStyleClass().addAll("btn", "btn-xs", "btn-info", "btn-table-add");
                    }
                    HBox box = new HBox(10, btnThem);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        colDaChon.setCellFactory(_ -> new TableCell<>() {
            private CheckBox checkBox = new CheckBox();
            {

            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }else{
                    Phong phong = getTableView().getItems().get(getIndex());
                    boolean isChecked = listPhongDuocDat.contains(phong);
                    checkBox.setSelected(isChecked);
                    HBox box = new HBox(8, checkBox);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });


        // Thiết lập TableView
        ArrayList<Phong> dsPhongTrong = Pdao.layDSPhongTrong();
        ObservableList<Phong> observableListPhong = FXCollections.observableArrayList(dsPhongTrong);
        tablePhong.setItems(observableListPhong);

        // Cho phép chọn nhiều dòng
        tablePhong.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public double TinhTongTien(ArrayList<Phong> ls){
        double tongTien = 0;
        for(Phong phong : ls){
            tongTien += phong.getLoaiPhong().getDonGia();
        }
        return tongTien;
    }
    public void laydsKhachHang() throws  SQLException{
        ArrayList<KhachHang> khs = Kdao.layDSKhachHang();
        dsMaKH = new ArrayList<>();
        for(KhachHang khachHang : khs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }
    }
    public void showAlertError(String header,String message){
        Alert alert = new  Alert(Alert.AlertType.ERROR);
        alert.setTitle("Đã xảy ra lỗi");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void refresh() throws SQLException {
        dsKhachHang.getSelectionModel().selectFirst();
        ngayDi.setValue(null);
        tablePhong.getSelectionModel().clearSelection();
        SoPhongDaChon.setText(null);
        TongTien.setText(null);
        listPhongDuocDat.clear();
        ArrayList<Phong> dsPhongTrong = Pdao.layDSPhongTrong();
        ObservableList<Phong> observableListPhong = FXCollections.observableArrayList(dsPhongTrong);
        tablePhong.setItems(observableListPhong);
        tablePhong.refresh();
    }
    public void showAlert(String header,String message){
        Alert alert = new  Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean checkMaPhieu(String maPhieu) throws SQLException {
        ArrayList<PhieuDatPhong> dsPhieuDatPhong = pdpDao.layDSPhieuDatPhong();
        ArrayList<String> dsMaPhieu = new ArrayList<>();
        for(PhieuDatPhong pdph : dsPhieuDatPhong) {
            dsMaPhieu.add(pdph.getMaPhieu());
        }
        if(dsMaPhieu.contains(maPhieu)) {
            return true;
        }
        return false;
    }
    public void DatPhong(KhachHang newKh,Phong p,String maPhieu) throws SQLException {
        PhieuDatPhong pdp = new PhieuDatPhong(maPhieu, LocalDate.now(),LocalDate.now(),ngayDi.getValue(),TrangThaiPhieuDatPhong.DANG_SU_DUNG,"Đặt trực tiếp tại quầy",newKh.getMaKH(),"NV01");
        pdpDao.themPhieuDatPhong(pdp);
        CTPhieuDatPhong ctpdp = new CTPhieuDatPhong(maPhieu,p.getMaPhong(),LocalDate.now(),ngayDi.getValue(), BigDecimal.valueOf(p.getLoaiPhong().getDonGia()));
        Pdao.capNhatTrangThaiPhong(p.getMaPhong(),TrangThaiPhong.DANG_SU_DUNG.toString());
        ctpDao.themCTPhieuDatPhong(ctpdp);
    }
    public void handleDatPhong(ActionEvent actionEvent) throws SQLException {
        if(ngayDi.getValue()==null) {
            showAlertError("Lỗi ngày","Không được bỏ trống ngày đi");
        }else{
            if(ngayDi.getValue().isAfter(LocalDate.now())||ngayDi.getValue().isEqual(LocalDate.now())){
                KhachHang newKh = Kdao.layKhachHangTheoMa(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
                for(Phong p:listPhongDuocDat){
                    Random ran =  new Random();
                    do {
                        maPhieu = "PD"+String.valueOf(ran.nextInt(90)+ran.nextInt(9));
                    }while(checkMaPhieu(maPhieu));
                    DatPhong(newKh,p,maPhieu);
                }
                refresh();
                showAlert("Thành Công","Bạn đã đặt phòng thành công");
            }else{
                showAlertError("lỖI NGÀY","không được chọn ngày đi trước ngày hôm nay");
            }
        }


    }

    public void handleRefresh(ActionEvent actionEvent) throws SQLException {
        refresh();
    }
}
