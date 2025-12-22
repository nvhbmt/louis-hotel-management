package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.LoaiPhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.ui.components.CustomButton;
import com.example.louishotelmanagement.ui.components.StatsCard;
import com.example.louishotelmanagement.ui.models.ButtonVariant;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.view.LoaiPhongFormDialogView;
import com.example.louishotelmanagement.view.QuanLyLoaiPhongView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class QuanLyLoaiPhongController {

    private StatsCard totalRoomTypesCard;
    private StatsCard lowestPriceCard;
    private StatsCard highestPriceCard;
    private StatsCard averagePriceCard;
    private TextField txtTimKiem;
    private ComboBox<String> cbLocGia;
    private TableView<LoaiPhong> tableViewLoaiPhong;
    private TableColumn<LoaiPhong, String> colMaLoaiPhong;
    private TableColumn<LoaiPhong, String> colTenLoai;
    private TableColumn<LoaiPhong, Double> colDonGia;
    private TableColumn<LoaiPhong, String> colMoTa;
    private TableColumn<LoaiPhong, Void> colThaoTac;

    private LoaiPhongDAO loaiPhongDAO;
    private ObservableList<LoaiPhong> danhSachLoaiPhong;
    private FilteredList<LoaiPhong> filteredLoaiPhongList;

    public QuanLyLoaiPhongController(QuanLyLoaiPhongView view) {
        this.totalRoomTypesCard = view.getTotalRoomTypesCard();
        this.lowestPriceCard = view.getLowestPriceCard();
        this.highestPriceCard = view.getHighestPriceCard();
        this.averagePriceCard = view.getAveragePriceCard();
        this.cbLocGia = view.getCbLocGia();
        this.txtTimKiem = view.getTxtTimKiem();
        this.tableViewLoaiPhong = view.getTableViewLoaiPhong();
        this.colMaLoaiPhong = view.getColMaLoaiPhong();
        this.colTenLoai = view.getColTenLoai();
        this.colDonGia = view.getColDonGia();
        this.colMoTa = view.getColMoTa();
        this.colThaoTac = view.getColThaoTac();
        initialize();
    }

    public void initialize() {
        try {
            loaiPhongDAO = new LoaiPhongDAO();

            // Khởi tạo dữ liệu
            khoiTaoDuLieu();
            khoiTaoTableView();
            khoiTaoComboBox();
            cauHinhLoc();

            // Load dữ liệu
            taiDuLieu();

        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void capNhatThongKe() throws SQLException {
        List<LoaiPhong> dsLoaiPhong = loaiPhongDAO.layDSLoaiPhong();

        int tongSoLoaiPhong = dsLoaiPhong.size();
        totalRoomTypesCard.setValue(String.valueOf(tongSoLoaiPhong));

        if (tongSoLoaiPhong > 0) {
            Double giaThapNhat = dsLoaiPhong.stream()
                    .map(LoaiPhong::getDonGia)
                    .min(Double::compareTo)
                    .orElse(0.0);

            Double giaCaoNhat = dsLoaiPhong.stream()
                    .map(LoaiPhong::getDonGia)
                    .max(Double::compareTo)
                    .orElse(0.0);

            Double giaTrungBinh = dsLoaiPhong.stream()
                    .map(LoaiPhong::getDonGia)
                    .reduce(0.0, Double::sum) / tongSoLoaiPhong;

            lowestPriceCard.setValue(formatCurrency(giaThapNhat));
            highestPriceCard.setValue(formatCurrency(giaCaoNhat));
            averagePriceCard.setValue(formatCurrency(giaTrungBinh));
        } else {
            lowestPriceCard.setValue("0 VNĐ");
            highestPriceCard.setValue("0 VNĐ");
            averagePriceCard.setValue("0 VNĐ");
        }
    }

    private String formatCurrency(Double amount) {
        if (amount == null) return "0 VNĐ";
        return String.format("%,.0f VNĐ", amount);
    }

    private void khoiTaoDuLieu() {
        danhSachLoaiPhong = FXCollections.observableArrayList();
        filteredLoaiPhongList = new FilteredList<>(danhSachLoaiPhong, p -> true);
    }

    private void khoiTaoTableView() {
        // Thiết lập các cột
        colMaLoaiPhong.setCellValueFactory(new PropertyValueFactory<>("maLoaiPhong"));
        colTenLoai.setCellValueFactory(new PropertyValueFactory<>("tenLoai"));
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));

        // Format cột đơn giá
        colDonGia.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f VNĐ", item));
                }
            }
        });

        // Cột thao tác
        colThaoTac.setCellFactory(_ -> new TableCell<>() {
            private final Button btnEdit = CustomButton.createButton("Sửa", ButtonVariant.INFO);
            private final Button btnDelete = CustomButton.createButton("Xóa", ButtonVariant.DANGER);

            {
                btnEdit.setOnAction(_ -> {
                    LoaiPhong loaiPhong = getTableView().getItems().get(getIndex());
                    handleSuaLoaiPhong(loaiPhong);
                });

                btnDelete.setOnAction(_ -> {
                    LoaiPhong loaiPhong = getTableView().getItems().get(getIndex());
                    handleXoaLoaiPhong(loaiPhong);
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

        // Thiết lập TableView với FilteredList
        tableViewLoaiPhong.setItems(filteredLoaiPhongList);

        // Cho phép chọn một dòng
        tableViewLoaiPhong.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void khoiTaoComboBox() {
        // Khởi tạo ComboBox lọc giá
        List<String> danhSachLocGia = List.of(
                "Dưới 1 triệu",
                "1-2 triệu",
                "2-5 triệu",
                "5-10 triệu",
                "Trên 10 triệu"
        );
        cbLocGia.setItems(FXCollections.observableArrayList(danhSachLocGia));
        cbLocGia.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn khoảng giá");
                } else {
                    setText(item);
                }
            }
        });
        cbLocGia.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Tất cả khoảng giá");
                } else {
                    setText(item);
                }
            }
        });

        // Set default values
        cbLocGia.setValue("Tất cả khoảng giá");

        // Thiết lập reactive filtering
        cauHinhLoc();
    }

    private void cauHinhLoc() {
        filteredLoaiPhongList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            String timKiemText = txtTimKiem.getText();
            String giaFilter = cbLocGia.getValue();

            return loaiPhong -> {
                // Filter theo tìm kiếm
                boolean timKiemMatch = timKiemText == null || timKiemText.trim().isEmpty() ||
                        (loaiPhong.getMaLoaiPhong() != null && loaiPhong.getMaLoaiPhong().toLowerCase().contains(timKiemText.toLowerCase())) ||
                        (loaiPhong.getTenLoai() != null && loaiPhong.getTenLoai().toLowerCase().contains(timKiemText.toLowerCase()));

                // Filter theo giá
                boolean giaMatch = (giaFilter == null || giaFilter.equals("Tất cả khoảng giá")) ||
                        giaMatchesFilter(loaiPhong.getDonGia(), giaFilter);

                return timKiemMatch && giaMatch;
            };
        }, txtTimKiem.textProperty(), cbLocGia.valueProperty()));
    }

    /**
     * Helper method to check if a price matches the filter string
     */
    private boolean giaMatchesFilter(Double donGia, String filterString) {
        if (donGia == null || filterString == null) {
            return false;
        }

        switch (filterString) {
            case "Dưới 1 triệu":
                return donGia < 1000000.0;
            case "1-2 triệu":
                return donGia >= 1000000.0 && donGia <= 2000000.0;
            case "2-5 triệu":
                return donGia > 2000000.0 && donGia <= 5000000.0;
            case "5-10 triệu":
                return donGia > 5000000.0 && donGia <= 10000000.0;
            case "Trên 10 triệu":
                return donGia > 10000000.0;
            default:
                return false;
        }
    }

    private void taiDuLieu() {
        try {
            // Lấy danh sách loại phòng từ database
            List<LoaiPhong> dsLoaiPhong = loaiPhongDAO.layDSLoaiPhong();

            danhSachLoaiPhong.clear();
            danhSachLoaiPhong.addAll(dsLoaiPhong);
            capNhatThongKe();

            // FilteredList will automatically apply filters

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải dữ liệu loại phòng: " + e.getMessage());
        }
    }


    public void handleThemLoaiPhong() {
        LoaiPhongFormDialogView view = new LoaiPhongFormDialogView();
        LoaiPhongDialogController controller = new LoaiPhongDialogController(view);
        Parent root = view.getRoot();
        Stage dialog = new Stage();
        dialog.setTitle("Thêm Loại Phòng Mới");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root));
        // Thiết lập controller và dữ liệu
        controller.setMode("ADD");

        dialog.showAndWait();

        // Làm mới dữ liệu sau khi thêm
        taiDuLieu();

    }

    public void handleSuaLoaiPhong(LoaiPhong loaiPhong) {
        LoaiPhongFormDialogView view = new LoaiPhongFormDialogView();
        LoaiPhongDialogController controller = new LoaiPhongDialogController(view);
        Parent root = view.getRoot();
        Stage dialog = new Stage();
        dialog.setTitle("Sửa Thông Tin Loại Phòng");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root));
        // Thiết lập controller và dữ liệu
        controller.setMode("EDIT");
        controller.setLoaiPhong(loaiPhong);
        dialog.showAndWait();

        // Làm mới dữ liệu sau khi sửa
        taiDuLieu();

    }

    public void handleXoaLoaiPhong(LoaiPhong loaiPhong) {
        // Xác nhận xóa
        String message = "Bạn có chắc chắn muốn xóa loại phòng này?\nLoại phòng: " + loaiPhong.getTenLoai() + " - Mã: " + loaiPhong.getMaLoaiPhong();
        if (ThongBaoUtil.hienThiXacNhan("Xác nhận xóa", message)) {
            try {
                // Kiểm tra xem loại phòng có đang được sử dụng không
                if (loaiPhongDAO.kiemTraLoaiPhongDuocSuDung(loaiPhong.getMaLoaiPhong())) {
                    ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể xóa loại phòng này vì đang được sử dụng!");
                    return;
                }

                // Xóa loại phòng
                if (loaiPhongDAO.xoaLoaiPhong(loaiPhong.getMaLoaiPhong())) {
                    ThongBaoUtil.hienThiThongBao("Thành công", "Đã xóa loại phòng thành công!");
                    taiDuLieu();
                } else {
                    ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể xóa loại phòng!");
                }

            } catch (SQLException e) {
                ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi khi xóa loại phòng: " + e.getMessage());
            }
        }
    }

    public void handleLamMoi() {
        taiDuLieu();
        txtTimKiem.clear();
        cbLocGia.setValue("Tất cả khoảng giá");
    }

    public void handleTimKiem() {
        // Filtering is now automatic via reactive binding
    }

    public void handleLocGia() {
        // Filtering is now automatic via reactive binding
    }


}