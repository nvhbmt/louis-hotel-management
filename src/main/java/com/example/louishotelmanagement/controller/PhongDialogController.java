package com.example.louishotelmanagement.controller;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.example.louishotelmanagement.dao.LoaiPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;
import com.example.louishotelmanagement.utils.UIUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class PhongDialogController implements Initializable {

    @FXML
    private Label lblTieuDe;
    @FXML
    private VBox formContainer;
    @FXML
    private Button btnLuu;

    private PhongDAO phongDAO;
    private LoaiPhongDAO loaiPhongDAO;
    private String mode = "ADD";

    // Form fields
    private StringField maPhongField;
    private IntegerField tangField;
    private SingleSelectionField<TrangThaiPhong> trangThaiField;
    private SingleSelectionField<LoaiPhong> loaiPhongField;
    private StringField moTaField;
    private Form form;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            khoiTaoDAO();
            String maPhongTiepTheo = layMaPhongTiepTheoNeuThemMoi();
            List<LoaiPhong> dsLoaiPhong = loaiPhongDAO.layDSLoaiPhong();
            
            taoForm(maPhongTiepTheo, 1, null, dsLoaiPhong, "");
            hienThiForm();
            
        } catch (SQLException e) {
            UIUtils.hienThiThongBao("Lỗi", "Không thể khởi tạo form: " + e.getMessage());
        }
    }

    private void khoiTaoDAO() throws SQLException {
        phongDAO = new PhongDAO();
        loaiPhongDAO = new LoaiPhongDAO();
    }

    private String layMaPhongTiepTheoNeuThemMoi() {
        if (!"ADD".equals(mode)) return "";
        
        try {
            return phongDAO.layMaPhongTiepTheo();
        } catch (SQLException e) {
            UIUtils.hienThiThongBao("Lỗi", "Không thể lấy mã phòng tiếp theo: " + e.getMessage());
            return "";
        }
    }

    private void taoForm(String maPhong, Integer tang, TrangThaiPhong trangThai, 
                           List<LoaiPhong> dsLoaiPhong, String moTa) {
        // Tạo các field
        maPhongField = taoFieldMaPhong(maPhong);
        tangField = taoFieldTang(tang);
        trangThaiField = taoFieldTrangThai(trangThai);
        loaiPhongField = taoFieldLoaiPhong(dsLoaiPhong);
        moTaField = taoFieldMoTa(moTa);

        // Tạo form
        form = Form.of(Group.of(maPhongField, tangField, trangThaiField, loaiPhongField, moTaField))
                .title("Thông tin phòng");
    }

    private StringField taoFieldMaPhong(String maPhong) {
        StringField field = Field.ofStringType(maPhong)
                .label("Mã Phòng")
                .placeholder("VD: P101")
                .validate(
                        StringLengthValidator.exactly(4, "Mã phòng phải đúng 4 ký tự"),
                        RegexValidator.forPattern("^P\\d{3}$", "Mã phòng phải theo định dạng 'Pxxx'")
                )
                .required("Mã phòng không được để trống");
        
        if ("ADD".equals(mode)) {
            field.editable(false);
        }
        return field;
    }

    private IntegerField taoFieldTang(Integer tang) {
        return Field.ofIntegerType(tang)
                .label("Tầng")
                .required("Tầng không được để trống");
    }

    private SingleSelectionField<TrangThaiPhong> taoFieldTrangThai(TrangThaiPhong trangThai) {
        return Field.ofSingleSelectionType(List.of(
                TrangThaiPhong.TRONG, 
                TrangThaiPhong.DA_DAT, 
                TrangThaiPhong.DANG_SU_DUNG, 
                TrangThaiPhong.BAO_TRI))
                .label("Trạng thái")
                .required("Vui lòng chọn trạng thái phòng");
    }

    private SingleSelectionField<LoaiPhong> taoFieldLoaiPhong(List<LoaiPhong> dsLoaiPhong) {
        return Field.ofSingleSelectionType(dsLoaiPhong)
                .label("Loại phòng")
                .required("Vui lòng chọn loại phòng");
    }

    private StringField taoFieldMoTa(String moTa) {
        return Field.ofStringType(moTa)
                .label("Mô tả")
                .placeholder("Nhập mô tả phòng...");
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
            lblTieuDe.setText("Sửa Thông Tin Phòng");
            btnLuu.setText("Cập nhật");
        } else {
            lblTieuDe.setText("Thêm Phòng Mới");
            btnLuu.setText("Lưu");
            if (maPhongField != null) {
                maPhongField.editable(false);
            }
        }
    }

    public void setPhong(Phong phong) {
        if (phong == null) return;
        
        // Set values
        maPhongField.valueProperty().set(phong.getMaPhong());
        maPhongField.editable(false);
        
        if (phong.getTang() != null) {
            tangField.valueProperty().set(phong.getTang());
        }
        
        trangThaiField.selectionProperty().set(phong.getTrangThai());
        moTaField.valueProperty().set(phong.getMoTa());
        loaiPhongField.selectionProperty().set(phong.getLoaiPhong());
    }

    @FXML
    private void handleLuu() {
        if (!form.isValid()) {
            UIUtils.hienThiThongBao("Lỗi", "Vui lòng kiểm tra lại thông tin phòng!");
            return;
        }
        
        try {
            Phong phong = taoPhongTuForm();
            boolean thanhCong = luuPhong(phong);
            
            if (thanhCong) {
                hienThiThongBaoThanhCong();
                dongDialog();
            }
            
        } catch (SQLException e) {
            UIUtils.hienThiThongBao("Lỗi", "Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private Phong taoPhongTuForm() {
        Phong phong = new Phong();
        phong.setMaPhong(maPhongField.valueProperty().get().trim());
        phong.setTang(tangField.valueProperty().get());
        phong.setTrangThai(trangThaiField.getSelection());
        phong.setMoTa(moTaField.valueProperty().get().trim());
        
        LoaiPhong loaiPhong = loaiPhongField.getSelection();
        if (loaiPhong != null) {
            phong.setLoaiPhong(loaiPhong);
        }
        
        return phong;
    }

    private boolean luuPhong(Phong phong) throws SQLException {
        boolean thanhCong;
        
        if ("ADD".equals(mode)) {
            thanhCong = phongDAO.themPhong(phong);
        } else {
            thanhCong = phongDAO.capNhatPhong(phong);
        }
        
        return thanhCong;
    }

    private void hienThiThongBaoThanhCong() {
        String thongBao = "ADD".equals(mode) ? "Đã thêm phòng thành công!" : "Đã cập nhật phòng thành công!";
        UIUtils.hienThiThongBao("Thành công", thongBao);
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
