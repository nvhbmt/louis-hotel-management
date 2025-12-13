package com.example.louishotelmanagement.controller;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.example.louishotelmanagement.dao.DichVuDAO;
import com.example.louishotelmanagement.model.DichVu;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DichVuDialogController implements Initializable {

    @FXML
    private Label lblTieuDe;
    @FXML
    private VBox formContainer;
    @FXML
    private Button btnLuu;

    private DichVuDAO dichVuDAO;
    private String mode = "ADD";

    // Form fields
    private StringField maDVField;
    private StringField tenDVField;
    private IntegerField soLuongField;
    private DoubleField donGiaField;
    private StringField moTaField;
    private BooleanField conKinhDoanhField;
    private Form form;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            khoiTaoDAO();
            String maDichVuTiepTheo = layMaDichVuTiepTheoNeuThemMoi();

            taoForm(maDichVuTiepTheo, "", 0, 0.0, "", true);
            hienThiForm();

        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể khởi tạo form: " + e.getMessage());
        }
    }

    private void khoiTaoDAO() throws Exception {
        dichVuDAO = new DichVuDAO();
    }

    private String layMaDichVuTiepTheoNeuThemMoi() {
        if (!"ADD".equals(mode)) return "";

        try {
            long number = System.currentTimeMillis() % 1000;
            return String.format("DV%03d", number);
        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể lấy mã dịch vụ tiếp theo: " + e.getMessage());
            return "";
        }
    }

    private void taoForm(String maDV, String tenDV, Integer soLuong, Double donGia, String moTa, Boolean conKinhDoanh) {
        // Tạo các field
        maDVField = taoFieldMaDV(maDV);
        tenDVField = taoFieldTenDV(tenDV);
        soLuongField = taoFieldSoLuong(soLuong);
        donGiaField = taoFieldDonGia(donGia);
        moTaField = taoFieldMoTa(moTa);
        conKinhDoanhField = taoFieldConKinhDoanh(conKinhDoanh);

        // Tạo form
        form = Form.of(Group.of(maDVField, tenDVField, soLuongField, donGiaField, moTaField, conKinhDoanhField))
                .title("Thông tin dịch vụ");
    }

    private StringField taoFieldMaDV(String maDV) {
        StringField field = Field.ofStringType(maDV)
                .label("Mã Dịch Vụ")
                .placeholder("VD: DV001")
                .validate(
                        StringLengthValidator.atLeast(3, "Mã dịch vụ phải có ít nhất 3 ký tự"),
                        RegexValidator.forPattern("^DV\\d{3}$", "Mã dịch vụ phải theo định dạng 'DVxxx'")
                )
                .required("Mã dịch vụ không được để trống");

        if ("ADD".equals(mode)) {
            field.editable(false);
        }
        return field;
    }

    private StringField taoFieldTenDV(String tenDV) {
        return Field.ofStringType(tenDV)
                .label("Tên Dịch Vụ")
                .placeholder("VD: Dịch vụ massage")
                .validate(StringLengthValidator.atLeast(2, "Tên dịch vụ phải có ít nhất 2 ký tự"))
                .required("Tên dịch vụ không được để trống");
    }

    private IntegerField taoFieldSoLuong(Integer soLuong) {
        return Field.ofIntegerType(soLuong)
                .label("Số Lượng")
                .validate(com.dlsc.formsfx.model.validators.IntegerRangeValidator.atLeast(0, "Số lượng phải >= 0"))
                .required("Số lượng không được để trống");
    }

    private DoubleField taoFieldDonGia(Double donGia) {
        return Field.ofDoubleType(donGia)
                .label("Đơn Giá (VNĐ)")
                .validate(com.dlsc.formsfx.model.validators.DoubleRangeValidator.atLeast(0.0, "Đơn giá phải >= 0"))
                .required("Đơn giá không được để trống");
    }

    private StringField taoFieldMoTa(String moTa) {
        return Field.ofStringType(moTa)
                .label("Mô Tả")
                .placeholder("Nhập mô tả dịch vụ...");
    }

    private BooleanField taoFieldConKinhDoanh(Boolean conKinhDoanh) {
        return Field.ofBooleanType(conKinhDoanh)
                .label("Đang Kinh Doanh");
    }

    private void hienThiForm() {
        FormRenderer formRenderer = new FormRenderer(form);
        formContainer.getChildren().add(formRenderer);
        formContainer.getStyleClass().add("formsfx-form-container");
    }

    public void setMode(String mode) {
        this.mode = mode;
        capNhatUIVaoCheDo();
    }

    private void capNhatUIVaoCheDo() {
        if ("EDIT".equals(mode)) {
            lblTieuDe.setText("Sửa Thông Tin Dịch Vụ");
            btnLuu.setText("Cập nhật");
        } else {
            lblTieuDe.setText("Thêm Dịch Vụ Mới");
            btnLuu.setText("Lưu");
            if (maDVField != null) {
                maDVField.editable(false);
            }
        }
    }

    public void setDichVu(DichVu dichVu) {
        if (dichVu == null) return;

        // Set values
        formContainer.getChildren().clear();
        taoForm(
                dichVu.getMaDV(),
                dichVu.getTenDV(),
                dichVu.getSoLuong(),
                dichVu.getDonGia(),
                dichVu.getMoTa(),
                dichVu.isConKinhDoanh()
        );
        hienThiForm();
    }

    @FXML
    private void handleLuu() {
        if (!form.isValid()) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Vui lòng kiểm tra lại thông tin dịch vụ!");
            return;
        }

        // Đảm bảo đồng bộ giá trị từ UI vào model trước khi đọc
        form.persist();

        try {
            DichVu dichVu = taoDichVuTuForm();
            boolean thanhCong = luuDichVu(dichVu);

            if (thanhCong) {
                hienThiThongBaoThanhCong();
                dongDialog();
            }

        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private DichVu taoDichVuTuForm() {
        DichVu dichVu = new DichVu();
        String ma = maDVField.valueProperty().get();
        String ten = tenDVField.valueProperty().get();
        String moTa = moTaField.valueProperty().get();

        dichVu.setMaDV(ma != null ? ma.trim() : "");
        dichVu.setTenDV(ten != null ? ten.trim() : "");
        dichVu.setSoLuong(soLuongField.valueProperty().get());
        dichVu.setDonGia(donGiaField.valueProperty().get());
        dichVu.setMoTa(moTa != null ? moTa.trim() : "");
        dichVu.setConKinhDoanh(conKinhDoanhField.valueProperty().get());

        return dichVu;
    }

    private boolean luuDichVu(DichVu dichVu) throws Exception {
        boolean thanhCong;

        if ("ADD".equals(mode)) {
            thanhCong = dichVuDAO.themDichVu(dichVu);
        } else {
            thanhCong = dichVuDAO.capNhatDichVu(dichVu);
        }

        return thanhCong;
    }

    private void hienThiThongBaoThanhCong() {
        String thongBao = "ADD".equals(mode) ? "Đã thêm dịch vụ thành công!" : "Đã cập nhật dịch vụ thành công!";
        ThongBaoUtil.hienThiThongBao("Thành công", thongBao);
    }

    @FXML
    private void handleHuy() {
        dongDialog();
    }

    private void dongDialog() {
        Stage stage = (Stage) btnLuu.getScene().getWindow();
        stage.close();
    }
}

