package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TraPhongController implements Initializable,Refreshable{

    public Button btnCheck;
    public DatePicker ngayDi;
    public ComboBox dsPhong;
    public ComboBox dsKhachHang;
    public Button btnTraPhong;
    public KhachHangDAO khDao;
    public PhongDAO phDao;
    public CTHoaDonPhongDAO cthdpDao;
    public PhieuDatPhongDAO phieuDatPhongDAO;
    public DatePicker ngayTraPhong;
    public TextField hoTen;
    public TextField maPhieuThue;
    public DatePicker ngayDen;
    public TextField soLuongPhong;
    public Label lblCheDo;
    public Button btnXemChiTiet;
    public ComboBox dsPhieu;
    private ArrayList<String> dsMaKH = new ArrayList<>();
    private ArrayList<PhieuDatPhong> dspdp;
    private HoaDonDAO hdDao;
    private List<CTHoaDonPhong> listCTHoaDonPhong = new ArrayList<>();
    private ContentSwitcher switcher;
    public void setContentSwitcher(ContentSwitcher switcher) {
        this.switcher = switcher;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        khDao = new KhachHangDAO();
        phDao = new PhongDAO();
        cthdpDao = new CTHoaDonPhongDAO();
        phieuDatPhongDAO = new PhieuDatPhongDAO();
        hdDao = new HoaDonDAO();
        btnXemChiTiet.setDisable(true);
        try{
            laydsKhachHang();
            laydsPhieuTheoKhachHang();
            laydsPhongTheoPhieu();
            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue!=null){
                    try {
                        laydsPhieuTheoKhachHang();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            dsPhieu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue!=null){
                    try {
                        laydsPhongTheoPhieu();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void laydsKhachHang() throws  SQLException{
        ArrayList<KhachHang> khs = khDao.layDSKhachHang();
        for(KhachHang khachHang : khs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }
        dsKhachHang.getSelectionModel().selectFirst();
    }
    public void laydsPhieuTheoKhachHang() throws SQLException {
        dsPhieu.getItems().clear();
        ArrayList<PhieuDatPhong> listpdp = phieuDatPhongDAO.layDSPhieuDatPhongTheoKhachHang(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
        for(PhieuDatPhong phieuDatPhong : listpdp) {
            dsPhieu.getItems().add(phieuDatPhong.getMaPhieu());
        }
        dsPhieu.getSelectionModel().selectFirst();
    }
    public void laydsPhongTheoPhieu() throws SQLException {
        dsPhong.getItems().clear();
        ArrayList<CTHoaDonPhong> ctHoaDonPhongs = cthdpDao.getCTHoaDonPhongTheoMaPhieu(dsPhieu.getSelectionModel().getSelectedItem().toString());
        for(CTHoaDonPhong ctHoaDonPhong : ctHoaDonPhongs) {
            dsPhong.getItems().add(ctHoaDonPhong.getMaPhong());
        }
        dsPhong.getSelectionModel().selectFirst();
    }
    public void handleCheck(javafx.event.ActionEvent actionEvent) throws Exception {
        boolean IsCheck = false;
        if(dsPhieu.getSelectionModel().getSelectedItem()!=null){
            PhieuDatPhong phieuDatPhong = phieuDatPhongDAO.layPhieuDatPhongTheoMa(dsPhieu.getSelectionModel().getSelectedItem().toString());
            if(phieuDatPhong!=null){
                KhachHang khachHang = khDao.layKhachHangTheoMa(phieuDatPhong.getMaKH());
                hoTen.setText(khachHang.getHoTen());
                maPhieuThue.setText(phieuDatPhong.getMaPhieu());
                ngayDen.setValue(phieuDatPhong.getNgayDen());
                ArrayList<CTHoaDonPhong> listCTHDPTheoPhieu = cthdpDao.getCTHoaDonPhongTheoMaPhieu(phieuDatPhong.getMaPhieu());
                soLuongPhong.setText(String.valueOf(listCTHDPTheoPhieu.size()));
                IsCheck = true;
            }else{
                ThongBaoUtil.hienThiLoi("Lỗi kiểm tra","Không tìm thấy bất kì phiếu đặt phòng nào");
                refreshData();
            }
        }else{
            ThongBaoUtil.hienThiLoi("Lỗi kiểm tra","Vui lòng chọn phiếu muốn kiểm tra");
            refreshData();
        }
        btnXemChiTiet.setDisable(!IsCheck);
    }

    // Trong com.example.louishotelmanagement.controller.TraPhongController.java

    // Trong com.example.louishotelmanagement.controller.TraPhongController.java

    public void handleTraPhong(ActionEvent actionEvent) throws Exception {

        // 0. Kiểm tra xem đã có thông tin phiếu được hiển thị chưa
        String maPhieu = maPhieuThue.getText();
        if (maPhieu == null || maPhieu.isEmpty()) {
            ThongBaoUtil.hienThiLoi("Lỗi trả phòng", "Vui lòng Kiểm tra phiếu đặt phòng trước khi Trả phòng.");
            return;
        }

        // 1. Lấy danh sách chi tiết hóa đơn phòng theo mã phiếu
        ArrayList<CTHoaDonPhong> listCTHDP = cthdpDao.getCTHoaDonPhongTheoMaPhieu(maPhieu);

        // 2. Kiểm tra danh sách có rỗng không
        if (listCTHDP.isEmpty()) {
            ThongBaoUtil.hienThiLoi("Lỗi trả phòng", "Không tìm thấy bất kì chi tiết hóa đơn phòng nào liên kết.");
            return;
        }

        // 3. Lấy Chi tiết đầu tiên để tìm MaHD
        CTHoaDonPhong ctHoaDonPhong = listCTHDP.getFirst();

        // 4. Kiểm tra và xử lý Hóa đơn
        if (ctHoaDonPhong != null) {
            HoaDon hd = hdDao.timHoaDonTheoMa(ctHoaDonPhong.getMaHD());

            if (hd != null) {
                if (hd.getTrangThai().equals(TrangThaiHoaDon.CHUA_THANH_TOAN)) {

                    // 💡 TRẠNG THÁI 1: CHƯA THANH TOÁN -> CHUYỂN MÀN HÌNH THANH TOÁN (KHÔNG TRUYỀN DỮ LIỆU)
                    if (switcher != null) {
                        switcher.switchContent("/com/example/louishotelmanagement/fxml/thanh-toan-view.fxml");
                    } else {
                        ThongBaoUtil.hienThiLoi("Lỗi hệ thống", "Không tìm thấy bộ chuyển đổi nội dung (switcher).");
                    }

                } else {

                    // 💡 TRẠNG THÁI 2: ĐÃ THANH TOÁN -> HOÀN TẤT VÀ DỌN PHÒNG
                    phieuDatPhongDAO.capNhatTrangThaiPhieuDatPhong(maPhieu, TrangThaiPhieuDatPhong.HOAN_THANH.toString());

                    // Cập nhật trạng thái từng phòng thành TRỐNG
                    for (CTHoaDonPhong ctHd : listCTHDP) {
                        phDao.capNhatTrangThaiPhong(ctHd.getMaPhong(), TrangThaiPhong.TRONG.toString());
                    }

                    ThongBaoUtil.hienThiThongBao("Thành công", "Trả phòng và dọn phòng thành công!");
                    refreshData();
                }
            } else {
                ThongBaoUtil.hienThiLoi("Lỗi trả phòng", "Không tìm thấy Hóa đơn phòng có mã: " + ctHoaDonPhong.getMaHD());
            }
        }
    }


    public void handleXemChiTiet(ActionEvent actionEvent) throws SQLException {
// 1. Lấy phiếu đặt phòng được chọn
        PhieuDatPhong selectedPhieu = phieuDatPhongDAO.layPhieuDatPhongTheoMa(dsPhieu.getSelectionModel().getSelectedItem().toString());

        if (selectedPhieu == null) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn một phiếu đặt phòng để xem chi tiết.");
            return;
        }

        try {
            // 2. Lấy danh sách chi tiết phòng (CTHoaDonPhong) từ DAO
            ArrayList<CTHoaDonPhong> dsCTP = cthdpDao.getCTHoaDonPhongTheoMaPhieu(selectedPhieu.getMaPhieu());

            if (dsCTP.isEmpty()) {
                ThongBaoUtil.hienThiLoi("Thông báo", "Phiếu này không chứa thông tin chi tiết phòng nào.");
                return;
            }

            // 3. Load FXML của màn hình chi tiết
            // Đảm bảo đường dẫn FXML là chính xác theo cấu trúc dự án của bạn!
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/ChiTietPhongTrongPhieuView.fxml"));
            Parent root = loader.load();

            // 4. Truy cập Controller của màn hình mới
            ChiTietPhongTrongPhieuController chiTietController = loader.getController();

            // 5. Truyền dữ liệu sang Controller mới
            // Hàm setChiTietData sẽ lấy MaPhieu và danh sách CTHoaDonPhong để hiển thị
            chiTietController.setChiTietData(selectedPhieu.getMaPhieu(), dsCTP);

            // 6. Tạo Stage và hiển thị
            Stage stage = new Stage();
            stage.setTitle("Chi Tiết Phòng Đặt - Phiếu " + selectedPhieu.getMaPhieu());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với cửa sổ cha
            stage.showAndWait(); // Hiển thị và chờ người dùng đóng cửa sổ

        } catch (IOException e) {
            // Lỗi khi không tìm thấy hoặc không load được file FXML
            ThongBaoUtil.hienThiLoi("Lỗi mở màn hình", "Không tìm thấy file FXML Chi Tiết Phòng hoặc lỗi tải: " + e.getMessage());
            System.err.println("Lỗi FXML: ");
            e.printStackTrace();
        } catch (SQLException e) {
            // Lỗi xảy ra khi truy vấn DB trong quá trình lấy chi tiết phòng
            ThongBaoUtil.hienThiLoi("Lỗi dữ liệu", "Lỗi khi truy xuất chi tiết phòng: " + e.getMessage());
            System.err.println("Lỗi SQL: ");
            e.printStackTrace();
        }
    }

    @Override
    public void refreshData() throws SQLException, Exception {
        laydsKhachHang();
        laydsPhieuTheoKhachHang();
        laydsPhongTheoPhieu();
        ngayTraPhong.setValue(LocalDate.now());
        hoTen.setText(null);
        maPhieuThue.setText(null);
        ngayDen.setValue(null);
        soLuongPhong.setText(null);
    }
}
