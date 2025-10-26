package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.DichVuDAO;
import com.example.louishotelmanagement.model.DichVu;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class QuanLyDichVuController implements Initializable {
    @FXML
    public Label lblTongSoDichVu;
    @FXML
    public Label lblSoDichVuConKinhDoanh;
    @FXML
    public Label lblSoDichVuNgungKinhDoanh;
    @FXML
    public Label lblTongGiaTriDichVu;
    @FXML
    public BorderPane rootPane;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private ComboBox<String> cbTrangThaiKinhDoanh;

    @FXML
    private TableView<DichVu> tableViewDichVu;
    @FXML
    private TableColumn<DichVu, String> colMaDV;
    @FXML
    private TableColumn<DichVu, String> colTenDV;
    @FXML
    private TableColumn<DichVu, Integer> colSoLuong;
    @FXML
    private TableColumn<DichVu, Double> colDonGia;
    @FXML
    private TableColumn<DichVu, String> colMoTa;
    @FXML
    private TableColumn<DichVu, Boolean> colTrangThai;
    @FXML
    public TableColumn<DichVu, Void> colThaoTac;

    @FXML
    private Label lblTrangThai;
    @FXML
    private Label lblSoLuong;

    private DichVuDAO dichVuDAO;
    private ObservableList<DichVu> danhSachDichVu;
    private ObservableList<DichVu> danhSachDichVuFiltered;
    private List<DichVu> dsDichVu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            dichVuDAO = new DichVuDAO();

            // Khởi tạo dữ liệu
            khoiTaoDuLieu();
            khoiTaoTableView();
            khoiTaoComboBox();
            khoiTaoSukien();

            // Load dữ liệu
            taiDuLieu();

        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void capNhatThongKe() {
        if (dsDichVu == null) return;

        int tongSo = dsDichVu.size();
        int conKinhDoanh = 0;
        int ngungKinhDoanh = 0;
        double tongGiaTri = 0;

        for (DichVu dv : dsDichVu) {
            if (dv.isConKinhDoanh()) {
                conKinhDoanh++;
            } else {
                ngungKinhDoanh++;
            }
            tongGiaTri += dv.getDonGia() * dv.getSoLuong();
        }

        lblTongSoDichVu.setText(String.valueOf(tongSo));
        lblSoDichVuConKinhDoanh.setText(String.valueOf(conKinhDoanh));
        //lblSoDichVuNgungKinhDoanh.setText(String.valueOf(ngungKinhDoanh));
        //lblTongGiaTriDichVu.setText(String.format("%.0f VNĐ", tongGiaTri));
    }


    private void khoiTaoDuLieu() {
        danhSachDichVu = FXCollections.observableArrayList();
        danhSachDichVuFiltered = FXCollections.observableArrayList();
    }

    private void khoiTaoTableView() {
        // Thiết lập các cột
        colMaDV.setCellValueFactory(new PropertyValueFactory<>("maDV"));
        colTenDV.setCellValueFactory(new PropertyValueFactory<>("tenDV"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));

        // Cột trạng thái hiển thị văn bản dựa trên conKinhDoanh
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("conKinhDoanh"));

        // Cột đơn giá với định dạng tiền tệ
        colDonGia.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f VNĐ", item));
                }
            }
        });

        // Cột trạng thái kinh doanh
        colTrangThai.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().clear();
                if (empty || item == null) {
                    setText(null);
                } else {
                    if (item) {
                        setText("Đang kinh doanh");
                        getStyleClass().add("status-active");
                    } else {
                        setText("Ngừng kinh doanh");
                        getStyleClass().add("status-inactive");
                    }
                }
            }
        });

        colThaoTac.setCellFactory(_ -> new TableCell<>() {
            private final Button btnEdit = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");

            {
                btnEdit.getStyleClass().addAll("btn", "btn-xs", "btn-info", "btn-table-edit");
                btnDelete.getStyleClass().addAll("btn", "btn-xs", "btn-danger", "btn-table-delete");

                btnEdit.setOnAction(_ -> {
                    DichVu dichVu = getTableView().getItems().get(getIndex());
                    handleSuaDichVu(dichVu);
                });

                btnDelete.setOnAction(_ -> {
                    DichVu dichVu = getTableView().getItems().get(getIndex());
                    handleXoaDichVu(dichVu);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(8, btnEdit, btnDelete);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        colThaoTac.setCellValueFactory(_ -> new ReadOnlyObjectWrapper<>(null));

        // Thiết lập TableView
        tableViewDichVu.setItems(danhSachDichVuFiltered);

        // Cho phép chọn nhiều dòng
        tableViewDichVu.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void khoiTaoComboBox() {
        // Khởi tạo ComboBox trạng thái kinh doanh
        List<String> danhSachTrangThai = List.of("Đang kinh doanh", "Ngừng kinh doanh");
        cbTrangThaiKinhDoanh.setItems(FXCollections.observableArrayList(danhSachTrangThai));
        cbTrangThaiKinhDoanh.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Tất cả trạng thái");
                } else {
                    setText(item);
                }
            }
        });
        cbTrangThaiKinhDoanh.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Tất cả trạng thái");
                } else {
                    setText(item);
                }
            }
        });
    }

    private void taiDuLieu() {
        try {
            // Lấy danh sách dịch vụ từ database
            dsDichVu = dichVuDAO.layTatCaDichVu(true);

            danhSachDichVu.clear();
            danhSachDichVu.addAll(dsDichVu);
            capNhatThongKe();

            // Áp dụng filter hiện tại
            apDungFilter();

            //capNhatTrangThai("Đã tải " + dsDichVu.size() + " dịch vụ");

        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải dữ liệu dịch vụ: " + e.getMessage());
        }
    }

    private void apDungFilter() {
        String timKiem = txtTimKiem.getText() != null ? txtTimKiem.getText().trim().toLowerCase() : "";
        String trangThaiFilter = cbTrangThaiKinhDoanh.getValue();

        // Xóa dữ liệu cũ
        danhSachDichVuFiltered.clear();

        for (DichVu dv : dsDichVu) {
            // Lọc theo tìm kiếm (tên hoặc mã)
            boolean matchTimKiem = timKiem.isEmpty() ||
                    (dv.getTenDV() != null && dv.getTenDV().toLowerCase().contains(timKiem)) ||
                    (dv.getMaDV() != null && dv.getMaDV().toLowerCase().contains(timKiem));

            // Lọc theo trạng thái
            boolean matchTrangThai = trangThaiFilter == null || trangThaiFilter.equals("Tất cả trạng thái") ||
                    (trangThaiFilter.equals("Đang kinh doanh") && dv.isConKinhDoanh()) ||
                    (trangThaiFilter.equals("Ngừng kinh doanh") && !dv.isConKinhDoanh());

            // Nếu thỏa cả 2 điều kiện, thêm vào filtered list
            if (matchTimKiem && matchTrangThai) {
                danhSachDichVuFiltered.add(dv);
            }
        }
    }

    @FXML
    private void handleThemDichVu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/dich-vu-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Thêm Dịch Vụ Mới");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));

            // Thiết lập controller và dữ liệu
            DichVuDialogController controller = loader.getController();
            controller.setMode("ADD");

            dialog.showAndWait();

            // Làm mới dữ liệu sau khi thêm
            taiDuLieu();

        } catch (IOException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể mở form thêm dịch vụ: " + e.getMessage());
        }
    }

    @FXML
    private void handleSuaDichVu(DichVu dichVu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/dich-vu-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Sửa Thông Tin Dịch Vụ");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));

            // Thiết lập controller và dữ liệu
            DichVuDialogController controller = loader.getController();
            controller.setMode("EDIT");
            controller.setDichVu(dichVu);
            dialog.showAndWait();

            // Làm mới dữ liệu sau khi sửa
            taiDuLieu();

        } catch (IOException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể mở form sửa dịch vụ: " + e.getMessage());
        }
    }

    @FXML
    private void handleXoaDichVu(DichVu dichVu) {
        // Xác nhận xóa
        String message = "Bạn có chắc chắn muốn ngừng kinh doanh dịch vụ này?\nDịch vụ: " + dichVu.getMaDV() + " - " + dichVu.getTenDV();
        if (ThongBaoUtil.hienThiXacNhan("Xác nhận ngừng kinh doanh", message)) {
            try {
                // Ngừng kinh doanh dịch vụ
                if (dichVuDAO.xoaDichVu(dichVu.getMaDV())) {
                    ThongBaoUtil.hienThiThongBao("Thành công", "Đã ngừng kinh doanh dịch vụ thành công!");
                    taiDuLieu();
                } else {
                    ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể ngừng kinh doanh dịch vụ!");
                }

            } catch (Exception e) {
                ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi khi ngừng kinh doanh dịch vụ: " + e.getMessage());
            }
        }
    }

    private void khoiTaoSukien() {
        txtTimKiem.textProperty().addListener((obs, oldVal, newVal) -> apDungFilter());
        cbTrangThaiKinhDoanh.valueProperty().addListener((obs, oldVal, newVal) -> apDungFilter());
    }

    @FXML
    private void handleLamMoi() {
        taiDuLieu();
        txtTimKiem.clear();
        cbTrangThaiKinhDoanh.setValue(null);
    }



//    private void capNhatTrangThai(String message) {
//        lblTrangThai.setText(message);
//    }
}

