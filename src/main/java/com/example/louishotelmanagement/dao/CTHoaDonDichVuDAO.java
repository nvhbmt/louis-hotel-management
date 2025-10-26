package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.CTHoaDonDichVu;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CTHoaDonDichVuDAO {

    // 🟢 Thêm chi tiết dịch vụ
    public boolean themCTHoaDonDichVu(CTHoaDonDichVu ct) throws SQLException {
        String sql = "{CALL sp_ThemCTHoaDonDichVu(?, ?, ?, ?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, ct.getMaHD());
            cs.setString(2, ct.getMaPhieuDV());
            cs.setString(3, ct.getMaDV());
            cs.setInt(4, ct.getSoLuong());
            cs.setBigDecimal(5, ct.getDonGia());

            return cs.executeUpdate() > 0;
        }
    }

    // 🟡 Cập nhật số lượng và đơn giá
    public boolean capNhatCTHoaDonDichVu(String maHD, String maDV, int soLuong, BigDecimal donGia) throws SQLException {
        String sql = "{CALL sp_CapNhatCTHoaDonDichVu(?, ?, ?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);
            cs.setString(2, maDV);
            cs.setInt(3, soLuong);
            cs.setBigDecimal(4, donGia);

            return cs.executeUpdate() > 0;
        }
    }

    // 🔴 Xóa chi tiết dịch vụ
    public boolean xoaCTHoaDonDichVu(String maHD, String maDV) throws SQLException {
        String sql = "{CALL sp_XoaCTHoaDonDichVu(?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);
            cs.setString(2, maDV);

            return cs.executeUpdate() > 0;
        }
    }

    public CTHoaDonDichVu timCTDVTheoMaHDMaDV(String maHD, String maDV) throws Exception {
        CTHoaDonDichVu chiTiet = null;

        // Giả định Stored Procedure có tên là sp_TimCTHDDVTheoMaHDMaDV
        String sql = "{CALL sp_TimCTHDDVTheoMaHDMaDV(?, ?)}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, maHD);
            stmt.setString(2, maDV);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Giả định constructor hoặc setter của CTHoaDonDichVu có thể xử lý các trường này
                    chiTiet = new CTHoaDonDichVu(
                            rs.getString("maHD"),
                            rs.getString("maPhieuDV"),
                            rs.getString("maDV"),
                            rs.getInt("soLuong"),
                            rs.getBigDecimal("donGia")
                            // Thêm các trường khác nếu cần
                    );
                }
            }
        }
        return chiTiet; // Trả về đối tượng CTHDDV hoặc null nếu không tìm thấy
    }

    // Trong CTHoaDonDichVuDAO.java
    public boolean capNhatSoLuongCTHDDV(String maHD, String maDV, int soLuongMoi) throws Exception {
        String sql = "{CALL sp_CapNhatSoLuongCTHDDV(?, ?, ?)}";
        int rowsAffected = 0;

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement stmt = con.prepareCall(sql)) {

            // Loại bỏ khoảng trắng thừa (Đúng)
            stmt.setString(1, maHD.trim());
            stmt.setString(2, maDV.trim());
            stmt.setInt(3, soLuongMoi);

            // Sử dụng execute() để chạy lệnh và bắt đầu chuỗi kết quả
            boolean hadResults = stmt.execute();

            // Lặp qua tất cả các kết quả (Result Set và Update Count)
            while (hadResults || stmt.getUpdateCount() != -1) {
                if (hadResults) {
                    // Đọc Result Set chứa SELECT @@ROWCOUNT
                    try (ResultSet rs = stmt.getResultSet()) {
                        if (rs.next()) {
                            // Lấy giá trị đầu tiên (chính là @@ROWCOUNT)
                            rowsAffected = rs.getInt(1);
                            break; // Đã lấy được kết quả cần, thoát vòng lặp
                        }
                    }
                }

                // Di chuyển đến kết quả tiếp theo
                hadResults = stmt.getMoreResults();
            }

            // rowsAffected = 1 nếu thành công (do SELECT @@ROWCOUNT)
            return rowsAffected > 0;
        }
    }

    // 🔵 Lấy danh sách chi tiết dịch vụ theo mã hóa đơn
    public List<CTHoaDonDichVu> layCTHoaDonDichVuTheoMaHD(String maHD) throws SQLException {
        List<CTHoaDonDichVu> list = new ArrayList<>();
        String sql = "{CALL sp_LayCTHoaDonDichVuTheoMaHD(?)}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                CTHoaDonDichVu ct = new CTHoaDonDichVu();
                ct.setMaHD(rs.getString("maHD"));
                ct.setMaPhieuDV(rs.getString("maPhieuDV"));
                ct.setMaDV(rs.getString("maDV"));
                ct.setSoLuong(rs.getInt("soLuong"));
                ct.setDonGia(rs.getBigDecimal("donGia"));
                ct.setThanhTien(rs.getBigDecimal("thanhTien"));
                list.add(ct);
            }
        }
        return list;
    }

    // ⚙️ Tính tổng tiền dịch vụ của 1 hóa đơn
    public BigDecimal tinhTongTienDichVu(String maHD) throws SQLException {
        String sql = "{CALL sp_TinhTongTienDichVu(?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                BigDecimal tongTien = rs.getBigDecimal("TongTienDichVu");
                return tongTien != null ? tongTien : BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }
}
