package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.HoaDon;
import com.example.louishotelmanagement.model.PhuongThucThanhToan;
import com.example.louishotelmanagement.model.TrangThaiHoaDon;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    // 🔹 Sinh mã hóa đơn tiếp theo bằng stored procedure
    public String taoMaHoaDonTiepTheo() throws SQLException {
        String sql = "{CALL sp_TaoMaHoaDonTiepTheo()}"; // ❌ Bỏ dấu (?)
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            if (rs.next()) {
                return rs.getString("maHDMoi"); // ✅ Tên cột đúng với SP của bạn
            }
        }
        return null;
    }


    // 🔹 Thêm hóa đơn mới
    public boolean themHoaDon(HoaDon hd) throws SQLException {
        String sql = "INSERT INTO HoaDon(maHD, ngayLap, phuongThuc, tongTien, maKH, maNV, maGG, trangThai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = CauHinhDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHD());
            ps.setDate(2, Date.valueOf(hd.getNgayLap()));
            if(hd.getPhuongThuc()!=null){
                ps.setString(3, hd.getPhuongThuc().toString());
            }else{
                ps.setNull(3,Types.NULL);
            }
            ps.setBigDecimal(4, hd.getTongTien() != null ? hd.getTongTien() : BigDecimal.ZERO);
            ps.setString(5, hd.getMaKH());
            ps.setString(6, hd.getMaNV());
            if (hd.getMaGG() != null && !hd.getMaGG().isEmpty())
                ps.setString(7, hd.getMaGG());
            else
                ps.setNull(7, Types.NVARCHAR);

            ps.setString(8, hd.getTrangThai().toString() != null ? hd.getTrangThai().toString() : "Chưa thanh toán");

            return ps.executeUpdate() > 0;
        }
    }

    // 🔹 Xóa hóa đơn
    public boolean xoaHoaDon(String maHD) throws SQLException {
        String sql = "DELETE FROM HoaDon WHERE maHD = ?";
        try (Connection conn = CauHinhDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        }
    }

    // 🔹 Cập nhật hóa đơn (phuongThuc, maGG, trangThai)
    public boolean capNhatHoaDon(HoaDon hd) throws SQLException {
        String sql = "UPDATE HoaDon SET phuongThuc = ?, maGG = ?, trangThai = ? WHERE maHD = ?";
        try (Connection conn = CauHinhDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hd.getPhuongThuc().toString());
            if (hd.getMaGG() != null && !hd.getMaGG().isEmpty())
                ps.setString(2, hd.getMaGG());
            else
                ps.setNull(2, Types.NVARCHAR);

            ps.setString(3, hd.getTrangThai().toString());
            ps.setString(4, hd.getMaHD());

            return ps.executeUpdate() > 0;
        }
    }

    // 🔹 Cập nhật trạng thái hóa đơn (VD: Chưa thanh toán → Đã thanh toán)
    public boolean capNhatTrangThaiHoaDon(String maHD, String trangThai) throws SQLException {
        String sql = "UPDATE HoaDon SET trangThai = ? WHERE maHD = ?";
        try (Connection conn = CauHinhDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setString(2, maHD);
            return ps.executeUpdate() > 0;
        }
    }

    // 🔹 Lấy danh sách tất cả hóa đơn
    public List<HoaDon> layDanhSachHoaDon() throws SQLException {
        List<HoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";
        try (Connection conn = CauHinhDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("maHD"));
                hd.setNgayLap(rs.getDate("ngayLap").toLocalDate());
                hd.setPhuongThuc(PhuongThucThanhToan.fromString(rs.getString("phuongThuc")));
                hd.setTongTien(rs.getBigDecimal("tongTien"));
                hd.setMaKH(rs.getString("maKH"));
                hd.setMaNV(rs.getString("maNV"));
                hd.setMaGG(rs.getString("maGG"));
                hd.setTrangThai(TrangThaiHoaDon.fromString(rs.getString("trangThai")));
                ds.add(hd);
            }
        }
        return ds;
    }

    // 🔹 Tìm hóa đơn theo mã
    public HoaDon timHoaDonTheoMa(String maHD) throws SQLException {
        // 💡 THAY ĐỔI: Gọi Stored Procedure
        String sql = "{CALL sp_TimHoaDonTheoMa(?)}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMaHD(rs.getString("maHD"));

                    // Lấy ngày lập và kiểm tra null
                    Date ngayLapDate = rs.getDate("ngayLap");
                    hd.setNgayLap(ngayLapDate != null ? ngayLapDate.toLocalDate() : null);

                    // Lấy các trường còn lại
                    hd.setPhuongThuc(PhuongThucThanhToan.fromString(rs.getString("phuongThuc")));
                    hd.setTongTien(rs.getBigDecimal("tongTien"));
                    hd.setMaKH(rs.getString("maKH"));
                    hd.setMaNV(rs.getString("maNV"));
                    hd.setMaGG(rs.getString("maGG"));
                    hd.setTrangThai(TrangThaiHoaDon.fromString(rs.getString("trangThai")));

                    return hd;
                }
            }
        }
        return null;
    }

    // 🔹 Cập nhật tổng tiền hóa đơn bằng store procedure
    public boolean capNhatTongTienHoaDon(String maHD) throws SQLException {
        String sql = "{CALL sp_CapNhatTongTienHoaDon(?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, maHD);
            return cs.executeUpdate() > 0;
        }
    }
}
