package com.example.louishotelmanagement.controller;

import java.sql.SQLException;
import java.util.List;
import com.example.louishotelmanagement.dao.LoaiPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;
import com.example.louishotelmanagement.ui.components.Badge;
import com.example.louishotelmanagement.ui.components.CustomButton;
import com.example.louishotelmanagement.ui.models.BadgeVariant;
import com.example.louishotelmanagement.ui.models.ButtonVariant;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.view.PhongFormDialogView;
import com.example.louishotelmanagement.view.QuanLyPhongView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class QuanLyPhongController  {
    @FXML
    public Label lblsoPhongTrong;
    @FXML
    public Label lblSoPhongTrong;
    @FXML
    public Label lblSoPhongSuDung;
    @FXML
    public Label lblSoPhongBaoTri;
    @FXML
    public BorderPane rootPane;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private ComboBox<String> cbTang;
    @FXML
    private ComboBox<String> cbTrangThai;
    @FXML
    private ComboBox<LoaiPhong> cbLocLoaiPhong;

    @FXML
    private TableView<Phong> tableViewPhong;
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
    public TableColumn<Phong, Void> colThaoTac;

    private PhongDAO phongDAO;
    private LoaiPhongDAO loaiPhongDAO;
    private ObservableList<Phong> danhSachPhong;
    private ObservableList<Phong> danhSachPhongFiltered;
    private FilteredList<Phong> filteredPhongList;

    public QuanLyPhongController(QuanLyPhongView view) {
        this.lblsoPhongTrong = view.getLblsoPhongTrong();
        this.lblSoPhongTrong = view.getLblSoPhongTrong();
        this.lblSoPhongSuDung = view.getLblSoPhongSuDung();
        this.lblSoPhongBaoTri = view.getLblSoPhongBaoTri();
        this.cbTrangThai = view.getCbTrangThai();
        this.cbTang = view.getCbTang();
        this.cbLocLoaiPhong = view.getCbLocLoaiPhong();
        this.txtTimKiem = view.getTxtTimKiem();
        this.tableViewPhong = view.getTableViewPhong();
        this.colMaPhong = view.getColMaPhong();
        this.colTenLoaiPhong = view.getColTenLoaiPhong();
        this.colTang = view.getColTang();
        this.colTrangThai = view.getColTrangThai();
        this.colMoTa = view.getColMoTa();
        this.colThaoTac = view.getColThaoTac();
        initialize();
      }

    public void initialize() {
        try {
            phongDAO = new PhongDAO();
            loaiPhongDAO = new LoaiPhongDAO();

            // Khởi tạo dữ liệu
            khoiTaoDuLieu();
            khoiTaoTableView();
            khoiTaoComboBox();

            // Load dữ liệu
            taiDuLieu();

        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void capNhatThongKe() throws SQLException {
        int tongSoPhong = phongDAO.layDSPhong().size();
        int soPhongTrong = phongDAO.layDSPhongTheoTrangThai(TrangThaiPhong.TRONG).size();
        int soPhongSuDung = phongDAO.layDSPhongTheoTrangThai(TrangThaiPhong.DANG_SU_DUNG).size();
        int soPhongBaoTri = phongDAO.layDSPhongTheoTrangThai(TrangThaiPhong.BAO_TRI).size();

        lblsoPhongTrong.setText(String.valueOf(tongSoPhong));
        lblSoPhongTrong.setText(String.valueOf(soPhongTrong));
        lblSoPhongSuDung.setText(String.valueOf(soPhongSuDung));
        lblSoPhongBaoTri.setText(String.valueOf(soPhongBaoTri));
    }

    private void khoiTaoDuLieu() {
        danhSachPhong = FXCollections.observableArrayList();
        danhSachPhongFiltered = FXCollections.observableArrayList();
        filteredPhongList = new FilteredList<>(danhSachPhong, p -> true);
    }

    private void khoiTaoTableView() {
        // Thiết lập các cột
        colMaPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        colTang.setCellValueFactory(new PropertyValueFactory<>("tang"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));

        colTrangThai.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(TrangThaiPhong item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-alignment: CENTER;");
                } else {
                    // Tạo badge label
                    BadgeVariant variant = switch (item) {
                        case TRONG -> BadgeVariant.SUCCESS;
                        case DA_DAT -> BadgeVariant.DANGER;
                        case DANG_SU_DUNG -> BadgeVariant.WARNING;
                        case BAO_TRI -> BadgeVariant.INFO;
                        default -> BadgeVariant.DEFAULT;
                    };
                    Label badge = Badge.createBadge(item.toString(), variant);

                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        // Cột tên loại phòng và đơn giá sẽ được thiết lập trong taiDuLieu()
        colTenLoaiPhong.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();
            return loaiPhong != null ?
                    javafx.beans.binding.Bindings.createStringBinding(loaiPhong::getTenLoai) :
                    javafx.beans.binding.Bindings.createStringBinding(() -> "");
        });


        colThaoTac.setCellFactory(_ -> new TableCell<>() {

            private final Button btnEdit = CustomButton.createButton("Sửa", ButtonVariant.INFO);
            private final Button btnDelete = CustomButton.createButton("Xóa", ButtonVariant.DANGER);

            {
                btnEdit.setOnAction(_ -> {
                    Phong phong = getTableView().getItems().get(getIndex());
                    handleSuaPhong(phong);
                });

                btnDelete.setOnAction(_ -> {
                    Phong phong = getTableView().getItems().get(getIndex());
                    handleXoaPhong(phong);
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
        tableViewPhong.setItems(filteredPhongList);

        // Cho phép chọn nhiều dòng
        tableViewPhong.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void khoiTaoComboBox() {
        // Khởi tạo ComboBox tầng
        ObservableList<String> danhSachTang = FXCollections.observableArrayList();
        danhSachTang.add("Tất cả các tầng");
        for (int i = 1; i <= 4; i++) danhSachTang.add("Tầng " + i);
        cbTang.setItems(danhSachTang);
        cbTang.setValue("Tất cả các tầng");

        ObservableList<String> danhSachTrangThai = FXCollections.observableArrayList(
            "Tất cả trạng thái",
            TrangThaiPhong.TRONG.toString(),
            TrangThaiPhong.DANG_SU_DUNG.toString(),
            TrangThaiPhong.DA_DAT.toString(),
            TrangThaiPhong.BAO_TRI.toString()
        );
        cbTrangThai.setItems(danhSachTrangThai);
        cbTrangThai.setValue("Tất cả trạng thái");

        cbLocLoaiPhong.setValue(null);
        cbTrangThai.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    cbTrangThai.setValue("Tất cả trạng thái");
                } else {
                    setText(item);
                }
            }
        });
        cbTrangThai.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn trạng thái");
                } else {
                    setText(item);
                }
            }
        });

        // Khởi tạo ComboBox loại phòng để filter
        khoiTaoComboBoxLoaiPhong();

        // Thiết lập reactive filtering
        cauHinhLoc();
    }

    private void cauHinhLoc() {
        filteredPhongList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            String trangThaiDaChon = cbTrangThai.getValue();
            String timKiemText = txtTimKiem.getText();
            String tangDaChon = cbTang.getValue();
            LoaiPhong loaiPhongDaChon = cbLocLoaiPhong.getValue();

            return phong -> {
                // Filter theo tìm kiếm
                boolean timKiemMatch = timKiemText == null || timKiemText.trim().isEmpty() ||
                        phong.getMaPhong().toLowerCase().contains(timKiemText.toLowerCase()) ||
                        (phong.getTang() != null && phong.getTang().toString().contains(timKiemText));

                // Filter theo tầng
                boolean tangMatch = (tangDaChon == null || tangDaChon.equals("Tất cả các tầng")) ||
                (phong.getTang() != null && phong.getTang() == Integer.parseInt(tangDaChon.split("\\s+")[1]));

                // Filter theo trạng thái
                boolean trangThaiMatch = (trangThaiDaChon == null || trangThaiDaChon.equals("Tất cả trạng thái")) ||
                        trangThaiMatchesFilter(phong.getTrangThai(), trangThaiDaChon);

                // Filter theo loại phòng
                boolean loaiPhongMatch = (loaiPhongDaChon == null) ||
                        (phong.getLoaiPhong() != null &&
                                phong.getLoaiPhong().getMaLoaiPhong().equals(loaiPhongDaChon.getMaLoaiPhong()));

                return timKiemMatch && tangMatch && trangThaiMatch && loaiPhongMatch;
            };
        }, cbTang.valueProperty(), cbTrangThai.valueProperty(), txtTimKiem.textProperty(), cbLocLoaiPhong.valueProperty()));
    }

    private void khoiTaoComboBoxLoaiPhong() {
        try {
            List<LoaiPhong> danhSachLoaiPhong = loaiPhongDAO.layDSLoaiPhong();

            // Thiết lập ComboBox để hiển thị tên loại phòng
            cbLocLoaiPhong.setCellFactory(_ -> new ListCell<>() {
                @Override
                protected void updateItem(LoaiPhong item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Chọn loại phòng");
                    } else {
                        setText(item.getTenLoai());
                    }
                }
            });

            cbLocLoaiPhong.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(LoaiPhong item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Chọn loại phòng");
                    } else {
                        setText(item.getTenLoai());
                    }
                }
            });

            cbLocLoaiPhong.setItems(FXCollections.observableArrayList(danhSachLoaiPhong));

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải danh sách loại phòng: " + e.getMessage());
        }
    }

    private void taiDuLieu() {
        try {
            // Lấy danh sách phòng từ database
            List<Phong> dsPhong = phongDAO.layDSPhong();

            danhSachPhong.clear();
            danhSachPhong.addAll(dsPhong);
            capNhatThongKe();

            // FilteredList will automatically apply filters
        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải dữ liệu phòng: " + e.getMessage());
        }
    }


    /**
     * Helper method to check if a room status matches the filter string
     */
    private boolean trangThaiMatchesFilter(TrangThaiPhong trangThai, String filterString) {
        if (trangThai == null || filterString == null) {
            return false;
        }

        return    (filterString == null || filterString.equals("Tất cả trạng thái")) ||
        (trangThai!= null && trangThai.toString().equals(filterString));
    }

    public void handleThemPhong() {
        PhongFormDialogView view = new PhongFormDialogView();
        PhongDialogController controller = new PhongDialogController(view);
        Parent root = view.getRoot();
        Stage dialog = new Stage();
        dialog.setTitle("Thêm Phòng Mới");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root));
        // Thiết lập controller và dữ liệu
        controller.setMode("ADD");

        dialog.showAndWait();

        // Làm mới dữ liệu sau khi thêm
        taiDuLieu();

    }

    public void handleSuaPhong(Phong phong) {

        PhongFormDialogView view = new PhongFormDialogView();
        PhongDialogController controller = new PhongDialogController(view);
        Parent root = view.getRoot();
        Stage dialog = new Stage();
        dialog.setTitle("Sửa Thông Tin Phòng");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root));
        // Thiết lập controller và dữ liệu
        controller.setMode("EDIT");
        controller.setPhong(phong);
        dialog.showAndWait();

        // Làm mới dữ liệu sau khi sửa
        taiDuLieu();

    }

    public void handleXoaPhong(Phong phong) {

        // Xác nhận xóa
        String message = "Bạn có chắc chắn muốn xóa phòng này?\nPhòng: " + phong.getMaPhong() + " - Tầng: " + phong.getTang();
        if (ThongBaoUtil.hienThiXacNhan("Xác nhận xóa", message)) {
            try {
                // Xóa phòng (stored procedure sẽ tự kiểm tra phòng có tồn tại và có đang được sử dụng không)
                phongDAO.xoaPhong(phong.getMaPhong());
                ThongBaoUtil.hienThiThongBao("Thành công", "Đã xóa phòng thành công!");
                taiDuLieu();
            } catch (SQLException e) {
                // Hiển thị thông báo lỗi từ stored procedure (RAISERROR)
                String errorMessage = e.getMessage();
                if (errorMessage != null && !errorMessage.isEmpty()) {
                    ThongBaoUtil.hienThiThongBao("Lỗi", errorMessage);
                } else {
                    ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể xóa phòng!");
                }
            }
        }
    }

    public void handleLamMoi() {
        taiDuLieu();
        txtTimKiem.clear();
        cbTang.setValue("Tất cả các tầng");
        cbTrangThai.setValue("Tất cả trạng thái");
        cbLocLoaiPhong.setValue(null);
    }

    public void handleTimKiem() {
        // Filtering is now automatic via reactive binding
    }

    public void handleLocTang() {
        // Filtering is now automatic via reactive binding
    }

    public void handleLocTrangThai() {
        // Filtering is now automatic via reactive binding
    }

    public void handleLocLoaiPhong() {
        // Filtering is now automatic via reactive binding
    }
}
