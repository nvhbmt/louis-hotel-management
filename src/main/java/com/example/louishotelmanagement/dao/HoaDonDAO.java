package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.HoaDon;
import com.example.louishotelmanagement.model.HoaDonChiTietItem;
import com.example.louishotelmanagement.model.PhuongThucThanhToan;
import com.example.louishotelmanagement.model.TrangThaiHoaDon;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    // 🔹 Sinh mã hóa đơn tiếp theo (Giữ nguyên)
    public String taoMaHoaDonTiepTheo() {
        String sql = "{CALL sp_TaoMaHoaDonTiepTheo}";
        String maHDMoi = null;
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                maHDMoi = rs.getString("maHDMoi");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo mã hóa đơn tiếp theo: " + e.getMessage());
            e.printStackTrace();
        }
        return maHDMoi;
    }


    // 🔹 Thêm hóa đơn mới (CẬP NHẬT: Thêm 4 trường mới, NgayCheckOut, TienPhat, TongGiamGia, TongVAT)
    public boolean themHoaDon(HoaDon hd) throws SQLException {
        // Cần 12 tham số
        String sql = "{CALL sp_ThemHoaDon(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, hd.getMaHD());

            if (hd.getNgayLap() != null) {
                cs.setDate(2, Date.valueOf(hd.getNgayLap()));
            } else {
                cs.setDate(2, Date.valueOf(LocalDate.now()));
            }

            if (hd.getPhuongThuc() != null) {
                cs.setString(3, hd.getPhuongThuc().toString());
            } else {
                cs.setNull(3, Types.NVARCHAR);
            }

            cs.setBigDecimal(4, hd.getTongTien() != null ? hd.getTongTien() : BigDecimal.ZERO);
            cs.setString(5, hd.getMaKH());
            cs.setString(6, hd.getMaNV());

            if (hd.getMaGG() != null && !hd.getMaGG().isEmpty()) {
                cs.setString(7, hd.getMaGG());
            } else {
                cs.setNull(7, Types.NVARCHAR);
            }

            if (hd.getTrangThai() != null) {
                cs.setString(8, hd.getTrangThai().toString());
            } else {
                cs.setString(8, "Chưa thanh toán");
            }

            // 9. NgayCheckOut
            if (hd.getNgayCheckOut() != null) {
                cs.setDate(9, Date.valueOf(hd.getNgayCheckOut()));
            } else {
                cs.setNull(9, Types.DATE);
            }

            // 10. TienPhat
            cs.setBigDecimal(10, hd.getTienPhat() != null ? hd.getTienPhat() : BigDecimal.ZERO);

            // 11. TongGiamGia
            cs.setBigDecimal(11, hd.getTongGiamGia() != null ? hd.getTongGiamGia() : BigDecimal.ZERO);

            // 12. TongVAT
            cs.setBigDecimal(12, hd.getTongVAT() != null ? hd.getTongVAT() : BigDecimal.ZERO);


            return cs.executeUpdate() > 0;
        }
    }

    // 🔹 Xóa hóa đơn (Giữ nguyên)
    public boolean xoaHoaDon(String maHD) throws SQLException {
        String sql = "{CALL sp_XoaHoaDon(?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, maHD);
            return cs.executeUpdate() > 0;
        }
    }

    // 🔹 Cập nhật hóa đơn (CẬP NHẬT: Thêm 4 trường mới)
    public boolean capNhatHoaDon(HoaDon hd) throws SQLException {
        // Cần 12 tham số
        String sql = "{CALL sp_SuaHoaDon(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, hd.getMaHD());

            if (hd.getNgayLap() != null) {
                cs.setDate(2, Date.valueOf(hd.getNgayLap()));
            } else {
                cs.setNull(2, Types.DATE);
            }

            if (hd.getPhuongThuc() != null) {
                cs.setString(3, hd.getPhuongThuc().toString());
            } else {
                cs.setNull(3, Types.NVARCHAR);
            }

            cs.setBigDecimal(4, hd.getTongTien());
            cs.setString(5, hd.getMaKH());
            cs.setString(6, hd.getMaNV());

            if (hd.getMaGG() != null && !hd.getMaGG().isEmpty()) {
                cs.setString(7, hd.getMaGG());
            } else {
                cs.setNull(7, Types.NVARCHAR);
            }

            if (hd.getTrangThai() != null) {
                cs.setString(8, hd.getTrangThai().toString());
            } else {
                cs.setNull(8, Types.NVARCHAR);
            }

            // 9. NgayCheckOut
            if (hd.getNgayCheckOut() != null) {
                cs.setDate(9, Date.valueOf(hd.getNgayCheckOut()));
            } else {
                cs.setNull(9, Types.DATE);
            }

            // 10. TienPhat
            cs.setBigDecimal(10, hd.getTienPhat());

            // 11. TongGiamGia
            cs.setBigDecimal(11, hd.getTongGiamGia());

            // 12. TongVAT
            cs.setBigDecimal(12, hd.getTongVAT());


            return cs.executeUpdate() > 0;
        }
    }

    // 🔹 Cập nhật trạng thái hóa đơn (Giữ nguyên)
    public boolean capNhatTrangThaiHoaDon(String maHD, String trangThai) throws SQLException {
        String sql = "{CALL sp_CapNhatTrangThaiHoaDon(?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, maHD);
            cs.setString(2, trangThai);
            return cs.executeUpdate() > 0;
        }
    }

    // 🔹 Lấy danh sách tất cả hóa đơn (CẬP NHẬT: Đọc 4 trường mới)
    public List<HoaDon> layDanhSachHoaDon() throws SQLException {
        List<HoaDon> ds = new ArrayList<>();
        String sql = "{CALL sp_LayDanhSachHoaDon()}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("maHD"));

                Date ngayLapDate = rs.getDate("ngayLap");
                hd.setNgayLap(ngayLapDate != null ? ngayLapDate.toLocalDate() : null);

                hd.setPhuongThuc(PhuongThucThanhToan.fromString(rs.getString("phuongThuc")));
                hd.setTongTien(rs.getBigDecimal("tongTien"));
                hd.setMaKH(rs.getString("maKH"));
                hd.setMaNV(rs.getString("maNV"));
                hd.setMaGG(rs.getString("maGG"));
                hd.setTrangThai(TrangThaiHoaDon.fromString(rs.getString("trangThai")));

                // Đọc các trường mới
                Date ngayCheckOutDate = rs.getDate("ngayCheckOut");
                hd.setNgayCheckOut(ngayCheckOutDate != null ? ngayCheckOutDate.toLocalDate() : null);
                hd.setTienPhat(rs.getBigDecimal("TienPhat"));
                hd.setTongGiamGia(rs.getBigDecimal("TongGiamGia"));
                hd.setTongVAT(rs.getBigDecimal("TongVAT"));


                // Nếu SP có JOIN dữ liệu, có thể thêm:
                // hd.setSoPhong(rs.getString("soPhong"));

                ds.add(hd);
            }
        }
        return ds;
    }

    // 🔹 Tìm hóa đơn theo mã (CẬP NHẬT: Đọc 4 trường mới)
    public HoaDon timHoaDonTheoMa(String maHD) throws SQLException {
        String sql = "{CALL sp_TimHoaDonTheoMa(?)}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMaHD(rs.getString("maHD"));

                    Date ngayLapDate = rs.getDate("ngayLap");
                    hd.setNgayLap(ngayLapDate != null ? ngayLapDate.toLocalDate() : null);

                    hd.setPhuongThuc(PhuongThucThanhToan.fromString(rs.getString("phuongThuc")));
                    hd.setTongTien(rs.getBigDecimal("tongTien"));
                    hd.setMaKH(rs.getString("maKH"));
                    hd.setMaNV(rs.getString("maNV"));
                    hd.setMaGG(rs.getString("maGG"));
                    hd.setTrangThai(TrangThaiHoaDon.fromString(rs.getString("trangThai")));

                    // Đọc các trường mới
                    Date ngayCheckOutDate = rs.getDate("ngayCheckOut");
                    hd.setNgayCheckOut(ngayCheckOutDate != null ? ngayCheckOutDate.toLocalDate() : null);
                    hd.setTienPhat(rs.getBigDecimal("TienPhat"));
                    hd.setTongGiamGia(rs.getBigDecimal("TongGiamGia"));
                    hd.setTongVAT(rs.getBigDecimal("TongVAT"));

                    return hd;
                }
            }
        }
        return null;
    }

    // 🔹 Cập nhật tổng tiền hóa đơn bằng store procedure (Giữ nguyên)
    public boolean capNhatTongTienHoaDon(String maHD) throws SQLException {
        String sql = "{CALL sp_CapNhatTongTienHoaDon(?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, maHD);
            return cs.executeUpdate() > 0;
        }
    }

    // 🔹 Lấy chi tiết hóa đơn (Giữ nguyên)
    public List<HoaDonChiTietItem> layChiTietHoaDon(String maHD) throws SQLException {
        List<HoaDonChiTietItem> dsChiTiet = new ArrayList<>();
        String sql = "{CALL sp_LayChiTietHoaDonTheoMaHD(?)}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    int stt = rs.getInt("STT");
                    String tenChiTiet = rs.getString("TenChiTiet");
                    int soLuong = rs.getInt("SoLuong");
                    BigDecimal donGia = rs.getBigDecimal("DonGia");
                    BigDecimal thanhTien = rs.getBigDecimal("ThanhTien");

                    HoaDonChiTietItem item = new HoaDonChiTietItem(stt, tenChiTiet, soLuong, donGia, thanhTien);
                    dsChiTiet.add(item);
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return dsChiTiet;
    }

}