package com.example.louishotelmanagement.dao;


import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.PhieuDichVu;
import com.example.louishotelmanagement.model.NhanVien; // Giả định
import com.example.louishotelmanagement.model.HoaDon; // Giả định
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PhieuDichVuDAO {

    // Giả định phương thức getConnection() tồn tại trong CauHinhDatabase

    // --- 1. THÊM PHIẾU DỊCH VỤ (CREATE) - Gọi sp_ThemPhieuDV ---
    /**
     * Thêm một phiếu dịch vụ mới vào CSDL.
     */
    public boolean themPhieuDichVu(PhieuDichVu phieuDV) throws Exception {
        // Gọi SP với 4 tham số: maPhieuDV, maHD, maNV, ghiChu
        String sql = "{CALL sp_ThemPhieuDV(?, ?, ?, ?)}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, phieuDV.getMaPhieuDV());
            stmt.setString(2, phieuDV.getMaHD());
            stmt.setString(3, phieuDV.getMaNV());
            stmt.setString(4, phieuDV.getGhiChu());

            // Trả về true nếu có dòng bị ảnh hưởng (thêm thành công)
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm phiếu dịch vụ: " + e.getMessage());
            // Ném lại ngoại lệ để tầng Controller/Service xử lý thông báo lỗi validation
            throw new Exception("Thêm phiếu dịch vụ thất bại: " + e.getMessage());
        }
    }

    // --- 2. CẬP NHẬT PHIẾU DỊCH VỤ (UPDATE) - Gọi sp_CapNhatPhieuDV ---
    /**
     * Cập nhật ghi chú của phiếu dịch vụ.
     */
    public boolean capNhatPhieuDichVu(PhieuDichVu phieuDV) throws Exception {
        // Gọi SP với 2 tham số: maPhieuDV, ghiChu
        String sql = "{CALL sp_CapNhatPhieuDV(?, ?)}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, phieuDV.getMaPhieuDV());
            stmt.setString(2, phieuDV.getGhiChu());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật phiếu dịch vụ: " + e.getMessage());
            throw new Exception("Cập nhật phiếu dịch vụ thất bại: " + e.getMessage());
        }
    }

    // --- 3. XÓA PHIẾU DỊCH VỤ (DELETE) - Gọi sp_XoaPhieuDV ---
    /**
     * Xóa một phiếu dịch vụ theo mã.
     */
    public boolean xoaPhieuDichVu(String maPhieuDV) throws Exception {
        // Gọi SP với 1 tham số: maPhieuDV
        String sql = "{CALL sp_XoaPhieuDV(?)}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, maPhieuDV);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa phiếu dịch vụ: " + e.getMessage());
            // Lưu ý: Lỗi khóa ngoại (FK) nếu chưa xóa CTPhieuDichVu sẽ bị bắt ở đây
            throw new Exception("Xóa phiếu dịch vụ thất bại: " + e.getMessage());
        }
    }

    // --- 4. LẤY DANH SÁCH PHIẾU DỊCH VỤ (READ) - Gọi sp_LayTatCaPhieuDV ---
    public List<PhieuDichVu> layTatCaPhieuDichVu(String maHD) throws Exception {
        List<PhieuDichVu> list = new ArrayList<>();
        // Gọi SP với 1 tham số tùy chọn: maHD
        String sql = "{CALL sp_LayTatCaPhieuDV(?)}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement stmt = con.prepareCall(sql)) {

            // Xử lý tham số tùy chọn: truyền NULL nếu muốn lấy tất cả
            if (maHD == null || maHD.isEmpty()) {
                stmt.setNull(1, java.sql.Types.NVARCHAR);
            } else {
                stmt.setString(1, maHD);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PhieuDichVu phieuDV = new PhieuDichVu();
                    phieuDV.setMaPhieuDV(rs.getString("maPhieuDV"));
                    phieuDV.setMaHD(rs.getString("maHD"));

                    // Chuyển đổi java.sql.Date sang java.time.LocalDate
                    Date sqlDate = rs.getDate("ngayLap");
                    if (sqlDate != null) {
                        phieuDV.setNgayLap(sqlDate.toLocalDate());
                    }

                    phieuDV.setMaNV(rs.getString("maNV"));
                    phieuDV.setGhiChu(rs.getString("ghiChu"));

                    // Gán các đối tượng liên quan (chỉ gán khóa ngoại để tối giản)
                    phieuDV.setNhanVien(new NhanVien(phieuDV.getMaNV()));
                    phieuDV.setHoaDon(new HoaDon(phieuDV.getMaHD()));
                    list.add(phieuDV);
                }
            }
        }
        return list;
    }
    // --- 5. LẤY MÃ PHIẾU DỊCH VỤ TIẾP THEO - Gọi sp_LayMaPhieuDVTiepTheo ---
    /**
     * Lấy mã phiếu dịch vụ tiếp theo để sử dụng khi thêm mới.
     * @return Mã phiếu dịch vụ tiếp theo (ví dụ: PDV001)
     */
    public String layMaPhieuDichVuTiepTheo() throws Exception {
        // Gọi SP với 1 tham số OUTPUT
        String sql = "{CALL sp_LayMaPhieuDVTiepTheo(?)}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            // Đăng ký tham số 1 là tham số đầu ra (OUTPUT) kiểu chuỗi
            cs.registerOutParameter(1, java.sql.Types.NVARCHAR);

            // Thực thi stored procedure
            cs.execute();

            // Lấy giá trị trả về từ tham số OUTPUT
            return cs.getString(1);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã phiếu dịch vụ tiếp theo: " + e.getMessage());
            throw new Exception("Lấy mã phiếu dịch vụ tiếp theo thất bại: " + e.getMessage());
        }
    }
}
