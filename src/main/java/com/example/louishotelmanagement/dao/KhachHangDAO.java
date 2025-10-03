package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.KhachHang;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class KhachHangDAO {
    
    // Thêm khách hàng
    public boolean themKhachHang(KhachHang khachHang) throws SQLException {
        String sql = "{call sp_ThemKhachHang(?,?,?,?,?,?,?,?)}";
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
                KhachHang khachHang = new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("hoTen"),
                        rs.getString("soDT"),
                        rs.getString("email"),
                        rs.getString("diaChi"),
                        rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                        rs.getString("ghiChu"),
                        rs.getString("CCCD")
                );
                ds.add(khachHang);
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
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("hoTen"),
                        rs.getString("soDT"),
                        rs.getString("email"),
                        rs.getString("diaChi"),
                        rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                        rs.getString("ghiChu"),
                        rs.getString("CCCD")
                );
            }
        }
        return null;
    }

    // Tìm kiếm khách hàng theo tên
    public ArrayList<KhachHang> timKiemKhachHangTheoTen(String ten) throws SQLException {
        ArrayList<KhachHang> ds = new ArrayList<>();
        String sql = "{call sp_TimKiemKhachHangTheoTen(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, "%" + ten + "%");
            cs.execute();
            ResultSet rs = cs.getResultSet();

            while (rs.next()) {
                KhachHang khachHang = new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("hoTen"),
                        rs.getString("soDT"),
                        rs.getString("email"),
                        rs.getString("diaChi"),
                        rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                        rs.getString("ghiChu"),
                        rs.getString("CCCD")
                );
                ds.add(khachHang);
            }
        }
        return ds;
    }

    // Tìm kiếm khách hàng theo số điện thoại
    public ArrayList<KhachHang> timKiemKhachHangTheoSDT(String soDT) throws SQLException {
        ArrayList<KhachHang> ds = new ArrayList<>();
        String sql = "{call sp_TimKiemKhachHangTheoSDT(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, "%" + soDT + "%");
            cs.execute();
            ResultSet rs = cs.getResultSet();

            while (rs.next()) {
                KhachHang khachHang = new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("hoTen"),
                        rs.getString("soDT"),
                        rs.getString("email"),
                        rs.getString("diaChi"),
                        rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                        rs.getString("ghiChu"),
                        rs.getString("CCCD")
                );
                ds.add(khachHang);
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
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("hoTen"),
                        rs.getString("soDT"),
                        rs.getString("email"),
                        rs.getString("diaChi"),
                        rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                        rs.getString("ghiChu"),
                        rs.getString("CCCD")
                );
            }
        }
        return null;
    }

    // Cập nhật khách hàng
    public boolean capNhatKhachHang(KhachHang khachHang) throws SQLException {
        String sql = "{call sp_CapNhatKhachHang(?,?,?,?,?,?,?,?)}";
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
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        }
        return false;
    }
}