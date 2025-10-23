package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.model.TrangThaiKhachHang;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class KhachHangDAO {

    // Thêm khách hàng
    public boolean themKhachHang(KhachHang khachHang) throws SQLException {
        String sql = "{call sp_ThemKhachHang(?,?,?,?,?,?,?,?,?)}"; // thêm tham số trạng thái
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, khachHang.getMaKH());
            cs.setString(2, khachHang.getHoTen());
            cs.setString(3, khachHang.getSoDT());
            cs.setString(4, khachHang.getEmail());
            cs.setString(5, khachHang.getDiaChi());
            cs.setObject(6, khachHang.getNgaySinh() != null ? Date.valueOf(khachHang.getNgaySinh()) : null);
            cs.setString(7, khachHang.getGhiChu());
            cs.setString(8, khachHang.getCCCD());
            cs.setString(9, khachHang.getTrangThai().name()); // lưu enum dưới dạng String

            return cs.executeUpdate() > 0;
        }
    }

    // Lấy danh sách tất cả khách hàng
    public ArrayList<KhachHang> layDSKhachHang() throws SQLException {
        ArrayList<KhachHang> ds = new ArrayList<>();
        String sql = "{call sp_LayDSKhachHang()}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                ds.add(mapResultSetToKhachHang(rs));
            }
        }
        return ds;
    }

    // Lấy khách hàng theo mã
    public KhachHang layKhachHangTheoMa(String maKH) throws SQLException {
        String sql = "{call sp_LayKhachHangTheoMa(?)}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maKH);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKhachHang(rs);
                }
            }
        }
        return null;
    }

    // Tìm kiếm khách hàng theo tên
    public ArrayList<KhachHang> timKiemKhachHangTheoTen(String ten) throws SQLException {
        return timKiemKhachHang("{call sp_TimKiemKhachHangTheoTen(?)}", ten);
    }

    // Tìm kiếm khách hàng theo số điện thoại
    public ArrayList<KhachHang> timKiemKhachHangTheoSDT(String soDT) throws SQLException {
        return timKiemKhachHang("{call sp_TimKiemKhachHangTheoSDT(?)}", soDT);
    }

    private ArrayList<KhachHang> timKiemKhachHang(String sp, String thamSo) throws SQLException {
        ArrayList<KhachHang> ds = new ArrayList<>();
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sp)) {

            cs.setString(1, "%" + thamSo + "%");
            cs.execute();
            try (ResultSet rs = cs.getResultSet()) {
                while (rs.next()) {
                    ds.add(mapResultSetToKhachHang(rs));
                }
            }
        }
        return ds;
    }

    // Tìm kiếm khách hàng theo CCCD
    public KhachHang timKiemKhachHangTheoCCCD(String CCCD) throws SQLException {
        String sql = "{call sp_TimKiemKhachHangTheoCCCD(?)}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, CCCD);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKhachHang(rs);
                }
            }
        }
        return null;
    }

    // Cập nhật khách hàng
    public boolean capNhatKhachHang(KhachHang khachHang) throws SQLException {
        String sql = "{call sp_CapNhatKhachHang(?,?,?,?,?,?,?,?,?)}"; // thêm trạng thái
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, khachHang.getMaKH());
            cs.setString(2, khachHang.getHoTen());
            cs.setString(3, khachHang.getSoDT());
            cs.setString(4, khachHang.getEmail());
            cs.setString(5, khachHang.getDiaChi());
            cs.setObject(6, khachHang.getNgaySinh() != null ? Date.valueOf(khachHang.getNgaySinh()) : null);
            cs.setString(7, khachHang.getGhiChu());
            cs.setString(8, khachHang.getCCCD());
            cs.setString(9, khachHang.getTrangThai().name());

            return cs.executeUpdate() > 0;
        }
    }

    // Xóa khách hàng
    public boolean xoaKhachHang(String maKH) throws SQLException {
        String sql = "{call sp_XoaKhachHang(?)}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maKH);
            return cs.executeUpdate() > 0;
        }
    }

    // Kiểm tra khách hàng có được sử dụng không
    public boolean kiemTraKhachHangDuocSuDung(String maKH) throws SQLException {
        String sql = "{call sp_KiemTraKhachHangDuocSuDung(?)}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maKH);
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() && rs.getInt("count") > 0;
            }
        }
    }

    // Mapping ResultSet -> KhachHang
    private KhachHang mapResultSetToKhachHang(ResultSet rs) throws SQLException {
        return new KhachHang(
                rs.getString("maKH"),
                rs.getString("hoTen"),
                rs.getString("soDT"),
                rs.getString("email"),
                rs.getString("diaChi"),
                rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                rs.getString("ghiChu"),
                rs.getString("CCCD"),
                rs.getString("hangKhach"),
                rs.getString("trangThai") != null
                        ? TrangThaiKhachHang.valueOf(rs.getString("trangThai"))
                        : TrangThaiKhachHang.DANG_LUU_TRU
        );
    }
}
