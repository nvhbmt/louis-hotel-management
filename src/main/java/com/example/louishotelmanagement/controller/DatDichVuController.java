package com.example.louishotelmanagement.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.louishotelmanagement.dao.CTHoaDonDichVuDAO;
import com.example.louishotelmanagement.dao.CTHoaDonPhongDAO;
import com.example.louishotelmanagement.dao.DichVuDAO;
import com.example.louishotelmanagement.dao.HoaDonDAO;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.PhieuDatPhongDAO;
import com.example.louishotelmanagement.dao.PhieuDichVuDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.CTHoaDonDichVu;
import com.example.louishotelmanagement.model.CTHoaDonPhong;
import com.example.louishotelmanagement.model.DichVu;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.model.PhieuDatPhong;
import com.example.louishotelmanagement.model.PhieuDichVu;
import com.example.louishotelmanagement.model.TrangThaiPhieuDatPhong;
import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.util.Refreshable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class DatDichVuController implements Initializable,Refreshable {



    public TableView tblChiTietTam;
    public TableColumn colDVMa;
    public TableColumn colDVTen;
    public TableColumn colDVSL;
    public TableColumn colDVGia;
    public ComboBox cboDichVu;
    public TextField txtSoLuong;
    public Button btnThemDV;
    public TextArea txtGhiChu;
    public VBox dsDichVuDaDat;
    public Label lblTongTienTam;
    public Button btnXacNhanLapPhieu;
    public ComboBox dsPhong;
    public ComboBox dsKhachHang;
    public TableColumn colDVMoTa;
    public TableColumn colDVConKinhDoanh;
    public TextField maNV;
    @FXML
    private Label tieuDeLabel;
    public KhachHangDAO kDao;
    public PhongDAO pDao;
    public CTHoaDonDichVuDAO cthddvDao;
    public CTHoaDonPhongDAO cthddphongDao;
    public PhieuDichVuDAO pdvDao;
    public DichVuDAO dvDao;
    public PhieuDatPhongDAO phieuDatPhongDAO;
    public CTHoaDonPhongDAO ctHoaDondao;
    public HoaDonDAO hdDao;
    public ArrayList<String> dsMaKH;
    public ArrayList<String> dsMaDV;
    // Danh sách TẠM THỜI lưu trữ các dịch vụ đã chọn
    private List<CTHoaDonDichVu> danhSachDichVuTam = new ArrayList<>();
    private String maPhieuDV;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        kDao = new KhachHangDAO();
        pDao = new PhongDAO();
        cthddvDao = new CTHoaDonDichVuDAO();
        pdvDao = new PhieuDichVuDAO();
        dvDao = new DichVuDAO();
        cthddphongDao = new CTHoaDonPhongDAO();
        phieuDatPhongDAO = new PhieuDatPhongDAO();
        ctHoaDondao = new CTHoaDonPhongDAO();
        hdDao = new HoaDonDAO();
        try{
            laydsKh();
            laydsPhongTheoKhachHang();
            khoiTaoTableView();
            layDsDichVu();
            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    laydsPhongTheoKhachHang();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            tblChiTietTam.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue != null){
                    DichVu dv = (DichVu) newValue;
                    cboDichVu.getSelectionModel().select(dv.getTenDV());
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void laydsKh() throws SQLException {
        ArrayList<KhachHang> khachhangs = kDao.layDSKhachHang();
        dsMaKH = new ArrayList<>();
        for (KhachHang khachHang : khachhangs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }
        dsKhachHang.getSelectionModel().selectFirst();
    }
    public void laydsPhongTheoKhachHang() throws SQLException {
        dsPhong.getItems().clear();
        ArrayList<PhieuDatPhong> dsPhieu = phieuDatPhongDAO.layDSPhieuDatPhongTheoKhachHang(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
        if (dsPhieu.size() > 0) {
            for (PhieuDatPhong p : dsPhieu) {
                if (p.getTrangThai() != null && p.getTrangThai().equalsIgnoreCase(TrangThaiPhieuDatPhong.DANG_SU_DUNG.toString())) {
                    ArrayList<CTHoaDonPhong> dsCTP = ctHoaDondao.getCTHoaDonPhongTheoMaPhieu(p.getMaPhieu());
                    for (CTHoaDonPhong ctp : dsCTP) {
                        dsPhong.getItems().add(ctp.getMaPhong());
                    }
                }

            }
            dsPhong.getSelectionModel().selectFirst();
        }else{
            dsPhong.getItems().clear();
        }

    }
    public void khoiTaoTableView() throws Exception {
        colDVMa.setCellValueFactory(new PropertyValueFactory<>("maDV"));
        colDVTen.setCellValueFactory(new PropertyValueFactory<>("tenDV"));
        colDVSL.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colDVGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        colDVMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        colDVConKinhDoanh.setCellValueFactory(new PropertyValueFactory<>("conKinhDoanh"));
        colDVConKinhDoanh.setCellFactory(column->new TableCell<DichVu, Boolean>(){
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null){
                    setText(null);
                }else{
                    setText("Còn kinh doanh");
                    setStyle("-fx-text-fill: #198754; -fx-font-weight: bold;");
                }
            }
        });
        List<DichVu> listDv = dvDao.layTatCaDichVu(true);
        ObservableList<DichVu> list =  FXCollections.observableArrayList(listDv);
        tblChiTietTam.setItems(list);
    }
    public void layDsDichVu() throws Exception {
        List<DichVu> listDv = dvDao.layTatCaDichVu(true);
        dsMaDV = new ArrayList<>();
        for(DichVu dv : listDv){
            cboDichVu.getItems().add(dv.getTenDV());
            dsMaDV.add(dv.getMaDV());
        }
    }


    public void handleXacNhanLapPhieu(ActionEvent actionEvent) throws Exception {

        // Bỏ isSuccess = false;

        // 1. Kiểm tra an toàn trước
        if (dsPhong.getSelectionModel().getSelectedItem() == null || danhSachDichVuTam.isEmpty()) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn phòng và thêm dịch vụ.");
            return;
        }

        // Lấy thông tin đặt phòng
        CTHoaDonPhong cthdp = cthddphongDao.getDSCTHoaDonPhongTheoMaPhong(dsPhong.getSelectionModel().getSelectedItem().toString()).getLast();
        PhieuDatPhong phieuDatPhong = phieuDatPhongDAO.layPhieuDatPhongTheoMa(cthdp.getMaPhieu());

        if (phieuDatPhong.getNgayDi().isAfter(LocalDate.now())&&phieuDatPhong.getTrangThai().equalsIgnoreCase(TrangThaiPhieuDatPhong.DANG_SU_DUNG.toString())) {

            // Setup và lưu Phiếu Dịch Vụ chính
            AuthService auth = AuthService.getInstance();
            PhieuDichVu pdv = new PhieuDichVu(maPhieuDV, cthdp.getMaHD(), LocalDate.now(), auth.getCurrentUser().getNhanVien().getMaNV(), txtGhiChu.getText(), auth.getCurrentUser().getNhanVien(), hdDao.timHoaDonTheoMa(cthdp.getMaHD()));
            pdvDao.themPhieuDichVu(pdv);

            // Biến cờ này dùng để kiểm tra xem có cần rollback (nếu có hệ thống giao dịch)
            boolean allItemsSuccessful = true;

            for (CTHoaDonDichVu cthddv : danhSachDichVuTam) {

                // Khởi tạo các biến cần thiết cho giao dịch
                String maHD = cthdp.getMaHD();
                String maDV = cthddv.getMaDV();

                cthddv.setMaHD(maHD);
                DichVu dv = dvDao.timDichVuTheoMa(maDV);
                CTHoaDonDichVu ctHoaDonDichVuHienCo = cthddvDao.timCTDVTheoMaHDMaDV(maHD, maDV);

                if (ctHoaDonDichVuHienCo == null) {
                    // Thêm mới
                    cthddv.setMaPhieuDV(pdv.getMaPhieuDV());
                    if (!cthddvDao.themCTHoaDonDichVu(cthddv)) {
                        allItemsSuccessful = false; break; // Thất bại
                    }
                } else {
                    // Cập nhật
                    int soLuongMoi = ctHoaDonDichVuHienCo.getSoLuong() + cthddv.getSoLuong();
                    if (!cthddvDao.capNhatSoLuongCTHDDV(ctHoaDonDichVuHienCo.getMaHD(), ctHoaDonDichVuHienCo.getMaDV(), soLuongMoi)) {
                        allItemsSuccessful = false; break; // Thất bại
                    }
                }

                // Cập nhật tồn kho (Chỉ chạy khi INSERT/UPDATE thành công)
                dvDao.capNhatSoLuongTonKho(dv.getMaDV(), dv.getSoLuong() - cthddv.getSoLuong());
            }

            // 3. Xử lý kết quả tổng thể
            if (allItemsSuccessful) {
                ThongBaoUtil.hienThiThongBao("Thành công", "Đã ghi nhận tất cả dịch vụ vào hóa đơn.");
                refreshData();
            } else {
                // Nếu có lỗi, thông báo lỗi chung
                ThongBaoUtil.hienThiLoi("Lỗi Thao Tác", "Một số dịch vụ không thể ghi nhận do lỗi hệ thống.");
                // Lưu ý: Nếu xảy ra lỗi nghiêm trọng, bạn nên thực hiện Rollback cho tất cả các giao dịch đã thực hiện trong vòng lặp.
            }

        } else {
            ThongBaoUtil.hienThiLoi("Lỗi đặt dịch vụ", "Phòng đã bị quá hạn.");
        }

    }

    public void handleThemDichVu(ActionEvent actionEvent) throws Exception {
        DichVu selectedDvGoc = (DichVu) tblChiTietTam.getSelectionModel().getSelectedItem();

        if (selectedDvGoc == null || txtSoLuong.getText().isEmpty()) {
            System.err.println("Vui lòng chọn dịch vụ và nhập số lượng.");
            return;
        }

        int soLuongCanThem;
        try {
            soLuongCanThem = Integer.parseInt(txtSoLuong.getText());
            if (soLuongCanThem <= 0) {
                System.err.println("Số lượng phải lớn hơn 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("Số lượng không hợp lệ.");
            return;
        }

        // --- LOGIC QUAN TRỌNG: TÌM KIẾM VÀ CẬP NHẬT ---
        CTHoaDonDichVu ctDvHienTai = null;

        // 1. Kiểm tra xem dịch vụ đã có trong danh sách tạm chưa
        for (CTHoaDonDichVu ct : danhSachDichVuTam) {
            if (ct.getMaDV().equals(selectedDvGoc.getMaDV())) {
                ctDvHienTai = ct;
                break;
            }
        }

        if (ctDvHienTai != null) {
            // 2. Dịch vụ ĐÃ TỒN TẠI: Cập nhật số lượng
            int soLuongMoi = ctDvHienTai.getSoLuong() + soLuongCanThem;
            ctDvHienTai.setSoLuong(soLuongMoi);
            // Cập nhật lại thành tiền tạm tính (nếu cần)
            ctDvHienTai.setThanhTien(ctDvHienTai.tinhThanhTien());

        } else {
            // 3. Dịch vụ CHƯA TỒN TẠI: Tạo mục mới và thêm vào danh sách
            CTHoaDonDichVu newCtDv = new CTHoaDonDichVu(
                    null,
                    null,
                    selectedDvGoc.getMaDV(),
                    soLuongCanThem,
                    BigDecimal.valueOf(selectedDvGoc.getDonGia())
            );
            danhSachDichVuTam.add(newCtDv);
        }
        // --------------------------------------------------

        // 4. Cập nhật số lượng tồn kho (Áp dụng ngay lập tức)
        selectedDvGoc.setSoLuong(selectedDvGoc.getSoLuong() - soLuongCanThem);
        // dvDao.capNhatSoLuongTonKho(selectedDvGoc.getMaDV(), selectedDvGoc.getSoLuong()); // Cập nhật vào DB

        // 5. Cập nhật lại giao diện người dùng
        capNhatDSDichVuDaDatUI();

        capNhatTongTienTam();

        // (Tùy chọn) Xóa nội dung
        txtSoLuong.clear();
    }
    private void capNhatTongTienTam() {
        BigDecimal tongTien = BigDecimal.ZERO;

        // Tính tổng tiền
        for (CTHoaDonDichVu ctDv : danhSachDichVuTam) {
            // Giả sử CTHoaDonDichVu có một phương thức getThanhTien() hoặc bạn tính lại tại đây.
            // Dựa vào logic trong handleThemDichVu, bạn đã tính được thành tiền của CTHoaDonDichVu.
            // Nếu không có hàm getThanhTien() hoặc hàm tính tự động trong model:
            BigDecimal donGia = ctDv.getDonGia();
            int soLuong = ctDv.getSoLuong();
            BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(soLuong));

            tongTien = tongTien.add(thanhTien);
        }

        // Định dạng và hiển thị lên Label
        // Sử dụng định dạng tiền tệ Việt Nam (VND)
        // Lưu ý: Cần import thư viện hoặc sử dụng định dạng đơn giản nếu không muốn dùng Locale phức tạp.
        String tongTienStr = String.format("%,.0f VND", tongTien);
        lblTongTienTam.setText(tongTienStr);
    }
    // Phương thức riêng để cập nhật giao diện dsDichVuDaDat
    private void capNhatDSDichVuDaDatUI() throws Exception {
        dsDichVuDaDat.getChildren().clear(); // Xóa tất cả các mục cũ

        for (CTHoaDonDichVu ctDv : danhSachDichVuTam) {
            HBox itemBox = new HBox(10);
            itemBox.setAlignment(Pos.CENTER_LEFT);
            itemBox.setPadding(new Insets(2, 5, 2, 5));
            HBox.setHgrow(itemBox, Priority.ALWAYS); // Quan trọng: cho HBox mở rộng

            // 1. Label hiển thị thông tin
            // Tạm thời hiển thị MaDV, bạn nên dùng TenDV nếu có thể
            Label infoLabel = new Label(dvDao.timDichVuTheoMa(ctDv.getMaDV()).getTenDV() + " (x" + ctDv.getSoLuong() + ")");
            infoLabel.setStyle("-fx-font-weight: bold;");

            // 2. Nút Xóa
            Button btnXoa = new Button("Xóa");
            btnXoa.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 10px;");

            // 3. Gán hành động cho nút Xóa
            btnXoa.setOnAction(event -> {
                // Lấy số lượng dịch vụ bị xóa
                int soLuongHoanLai = ctDv.getSoLuong();
                String maDVHoanLai = ctDv.getMaDV();

                // Xóa dịch vụ này khỏi danh sách tạm thời
                danhSachDichVuTam.remove(ctDv);

                // Hoàn lại số lượng tồn kho (giả sử bạn có thể tìm lại đối tượng DichVu gốc)
                // Cần hàm tìm kiếm DV gốc: DichVu dvGoc = dvDao.layDichVuTheoMa(maDVHoanLai);
                // dvGoc.setSoLuong(dvGoc.getSoLuong() + soLuongHoanLai);
                // dvDao.capNhatSoLuongTonKho(dvGoc.getMaDV(), dvGoc.getSoLuong());

                // Cập nhật lại giao diện
                try {
                    capNhatDSDichVuDaDatUI();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            // 4. Spacer để căn chỉnh nút Xóa sang phải
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            itemBox.getChildren().addAll(infoLabel, spacer, btnXoa);
            dsDichVuDaDat.getChildren().add(itemBox);
        }
    }


    @Override
    public void refreshData() throws Exception {
        laydsKh();
        laydsPhongTheoKhachHang();
        khoiTaoTableView();
        cboDichVu.getItems().clear();
        layDsDichVu();
        danhSachDichVuTam.clear();
        AuthService auth = AuthService.getInstance();
        maNV.setText(auth.getCurrentUser().getNhanVien().getMaNV());
        maPhieuDV = pdvDao.layMaPhieuDichVuTiepTheo();
        capNhatDSDichVuDaDatUI();
        capNhatTongTienTam();
        txtGhiChu.setText(null);
    }
}
