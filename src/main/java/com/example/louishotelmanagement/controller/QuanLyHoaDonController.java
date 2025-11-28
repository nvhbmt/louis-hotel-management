package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.HoaDonDAO2;
import com.example.louishotelmanagement.model.HoaDon;
import com.example.louishotelmanagement.model.KhachHang;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class QuanLyHoaDonController implements Initializable {

    // --- FXML Components ---
    @FXML private TextField txtTimKiem;
    @FXML private ComboBox<String> cbNgayTao;
    @FXML private TableView<HoaDon> hoaDonTable;
    @FXML private TableColumn<HoaDon, String> colMaHD;
    @FXML private TableColumn<HoaDon, String> colHoTen;
    @FXML private TableColumn<HoaDon, String> colSoPhong;
    @FXML private TableColumn<HoaDon, String> colCheckOut;
    @FXML private TableColumn<HoaDon, Void> colThaoTac;

    // --- Data and DAO ---
    private HoaDonDAO2 hoaDonDAO2;
    private ObservableList<HoaDon> danhSachHoaDon;
    private FilteredList<HoaDon> filteredHoaDonList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hoaDonDAO2 = new HoaDonDAO2();
        khoiTaoComboBox();
        cauHinhBang();
        thietLapBoLoc();
        taiDuLieuHoaDon();
    }

    private void khoiTaoComboBox() {
        cbNgayTao.setItems(FXCollections.observableArrayList(
                "Tất cả", "Hôm nay", "7 ngày qua", "Tháng này"
        ));
        cbNgayTao.setValue("Tất cả");
    }

    private void cauHinhBang() {
        danhSachHoaDon = FXCollections.observableArrayList();
        filteredHoaDonList = new FilteredList<>(danhSachHoaDon, p -> true);
        hoaDonTable.setItems(filteredHoaDonList);

        colMaHD.setCellValueFactory(new PropertyValueFactory<>("maHD"));

        colHoTen.setCellValueFactory(cellData -> {
            KhachHang kh = cellData.getValue().getKhachHang();
            return Bindings.createStringBinding(() -> (kh != null && kh.getHoTen() != null) ? kh.getHoTen() : "N/A");
        });

        colSoPhong.setCellValueFactory(new PropertyValueFactory<>("soPhong"));

        colCheckOut.setCellValueFactory(cellData -> {
            LocalDate ngayDi = cellData.getValue().getNgayCheckOut();
            if (ngayDi == null) return Bindings.createStringBinding(() -> "N/A");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return Bindings.createStringBinding(() -> ngayDi.format(formatter));
        });

        colThaoTac.setCellFactory(param -> new TableCell<>() {
            private final Button btnPrint = new Button("In");
            private final Button btnView = new Button("Xem");
            private final HBox pane = new HBox(10, btnPrint, btnView);
            {
                pane.setAlignment(Pos.CENTER);
                double buttonWidth = 70;
                btnPrint.setPrefWidth(buttonWidth);
                btnView.setPrefWidth(buttonWidth);
                btnPrint.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000; -fx-border-color: #c0c0c0;-fx-border-radius: 10;");
                btnView.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-border-radius: 10;");
                btnPrint.setOnAction(event -> handleInHoaDon(getTableView().getItems().get(getIndex())));
                btnView.setOnAction(event -> handleXemChiTiet(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void thietLapBoLoc() {
        filteredHoaDonList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            String tuKhoa = txtTimKiem.getText().toLowerCase();
            String locNgay = cbNgayTao.getValue();

            return hoaDon -> {
                boolean timKiemMatch = tuKhoa.isEmpty() ||
                        (hoaDon.getMaHD() != null && hoaDon.getMaHD().toLowerCase().contains(tuKhoa)) ||
                        (hoaDon.getKhachHang() != null && hoaDon.getKhachHang().getHoTen() != null && hoaDon.getKhachHang().getHoTen().toLowerCase().contains(tuKhoa)) ||
                        (hoaDon.getSoPhong() != null && hoaDon.getSoPhong().toLowerCase().contains(tuKhoa));

                boolean locNgayMatch = false;
                LocalDate ngayHoaDon = hoaDon.getNgayLap();
                if (locNgay.equals("Tất cả") || ngayHoaDon == null) {
                    locNgayMatch = true;
                } else {
                    LocalDate now = LocalDate.now();
                    switch (locNgay) {
                        case "Hôm nay": locNgayMatch = ngayHoaDon.isEqual(now); break;
                        case "7 ngày qua": locNgayMatch = !ngayHoaDon.isBefore(now.minusDays(7)) && !ngayHoaDon.isAfter(now); break;
                        case "Tháng này": locNgayMatch = ngayHoaDon.getMonth() == now.getMonth() && ngayHoaDon.getYear() == now.getYear(); break;
                    }
                }
                return timKiemMatch && locNgayMatch;
            };
        }, txtTimKiem.textProperty(), cbNgayTao.valueProperty()));
    }

    private void taiDuLieuHoaDon() {
        try {
            List<HoaDon> ds = hoaDonDAO2.layDanhSachHoaDon();
            danhSachHoaDon.setAll(ds);



        } catch (Exception e) {


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi Tải Dữ Liệu Hóa Đơn");
            alert.setHeaderText("Không thể tải danh sách hóa đơn!");
            alert.setContentText("Lỗi chi tiết: " + e.getMessage());

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            alert.getDialogPane().setExpandableContent(textArea);

            alert.showAndWait();
        }
    }


    private void handleInHoaDon(HoaDon hoaDon) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText("Chức năng đang phát triển");
        alert.setContentText("Logic in hóa đơn cho hóa đơn " + hoaDon.getMaHD() + " sẽ được triển khai ở đây.");
        alert.showAndWait();
    }


    private void handleXemChiTiet(HoaDon hoaDon) {


        try {
            com.example.louishotelmanagement.controller.HoaDonTxtGenerator generator = new com.example.louishotelmanagement.controller.HoaDonTxtGenerator();
            String noiDungHoaDon = generator.taoNoiDungHoaDon(hoaDon);


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/xem-hoa-don-txt.fxml"));
            Parent root = loader.load();

            XemHoaDonTxtController controller = loader.getController();
            controller.initData(noiDungHoaDon);

            Stage stage = new Stage();
            stage.setTitle("Chi Tiết Hóa Đơn (Xem file TXT): " + hoaDon.getMaHD());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi Truy Vấn Dữ Liệu");
            alert.setHeaderText("Không thể tạo chi tiết hóa đơn.");
            alert.setContentText("Lỗi SQL: " + e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            // Đã xóa e.printStackTrace()
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi Giao Diện");
            alert.setHeaderText("Không thể mở màn hình xem hóa đơn.");
            alert.setContentText("Lỗi FXML (kiểm tra lại đường dẫn): " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void handleLamMoi(javafx.event.ActionEvent actionEvent) {

        txtTimKiem.clear();


        cbNgayTao.setValue("Tất cả");


        taiDuLieuHoaDon();
    }

    @FXML
    public void handleAutoRefreshOnMouseEntered() {
        taiDuLieuHoaDon();
        // Auto refresh cập nhật mới thông tin hóa đơn mỗi khi di chuyển chuột qua
    }
}