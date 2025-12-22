package com.example.louishotelmanagement.dao;
import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.KieuGiamGia;
import com.example.louishotelmanagement.model.KhuyenMai;

import java.sql.*;
import java.util.ArrayList;

public class KhuyenMaiDAO {
    // Thêm mã giảm giá
    public boolean themKhuyenMai(KhuyenMai khuyenMai) throws SQLException {
        String sql = "{call sp_ThemKhuyenMai(?,?,?,?,?,?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, khuyenMai.getMaKM());
            cs.setString(2, khuyenMai.getCode());
            cs.setDouble(3, khuyenMai.getGiamGia());
            cs.setString(4, khuyenMai.getKieuGiamGia().toString());
            cs.setDate(5, Date.valueOf(khuyenMai.getNgayBatDau()));
            cs.setDate(6, Date.valueOf(khuyenMai.getNgayKetThuc()));
            cs.setDouble(7, khuyenMai.getTongTienToiThieu());
            cs.setString(8, khuyenMai.getMoTa());
            cs.setString(9, khuyenMai.getTrangThai());
            cs.setString(10, khuyenMai.getMaNhanVien());

            return cs.executeUpdate() > 0;
        }
    }

    // Lấy danh sách khuyến mãi
    public ArrayList<KhuyenMai> layDSKhuyenMai() throws SQLException {
        ArrayList<KhuyenMai> ds = new ArrayList<>();
        String sql = "{call sp_LayDSKhuyenMai()}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                KhuyenMai khuyenMai = new KhuyenMai(
                        rs.getString("maKM"),
                        rs.getString("code"),
                        rs.getDouble("giamGia"),
                        KieuGiamGia.fromString(rs.getString("kieuGiamGia")),
                        rs.getDate("ngayBatDau").toLocalDate(),
                        rs.getDate("ngayKetThuc").toLocalDate(),
                        rs.getDouble("tongTienToiThieu"),
                        rs.getString("moTa"),
                        rs.getString("trangThai"),
                        rs.getString("maNV")
                );
                ds.add(khuyenMai);
            }
        }
        return ds;
    }
    // Lấy danh sách khuyến mãi còn hoạt động
    public ArrayList<KhuyenMai> layKhuyenMaiDangHoatDong() throws SQLException {
        ArrayList<KhuyenMai> ds = new ArrayList<>();
        String sql = "{call sp_LayKhuyenMaiDangHoatDong()}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                KhuyenMai khuyenMai = new KhuyenMai(
                        rs.getString("maKM"),
                        rs.getString("code"),
                        rs.getDouble("giamGia"),
                        KieuGiamGia.fromString(rs.getString("kieuGiamGia")),
                        rs.getDate("ngayBatDau").toLocalDate(),
                        rs.getDate("ngayKetThuc").toLocalDate(),
                        rs.getDouble("tongTienToiThieu"),
                        rs.getString("moTa"),
                        rs.getString("trangThai"),
                        rs.getString("maNV")
                );
                ds.add(khuyenMai);
            }
        }
        return ds;
    }
    // Cập nhật khuyến mãi
    public boolean capNhatKhuyenMai(KhuyenMai khuyenMai) throws SQLException {

        System.out.println("khuyến mãi: " + khuyenMai);
        String sql = "{call sp_CapNhatKhuyenMai(?,?,?,?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, khuyenMai.getMaKM());
            cs.setString(2, khuyenMai.getCode());
            cs.setDouble(3, khuyenMai.getGiamGia());
            cs.setString(4, khuyenMai.getKieuGiamGia().toString());
            cs.setDate(5, Date.valueOf(khuyenMai.getNgayBatDau()));
            cs.setDate(6, Date.valueOf(khuyenMai.getNgayKetThuc()));
            cs.setString(7, khuyenMai.getMoTa());
            cs.setString(8, khuyenMai.getTrangThai());

            return cs.executeUpdate() > 0;
        }
    }

    // Xóa mã giảm giá
    public boolean xoaKhuyenMai(String maKM) throws SQLException {
        String sql = "{call sp_XoaKhuyenMai(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maKM);
            return cs.executeUpdate() > 0;
        }
    }
    //laykm
    public KhuyenMai layKhuyenMaiTheoMa(String maKM) throws SQLException {
        String sql = "{call sp_LayKhuyenMaiTheoMa(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
                CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maKM);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return new KhuyenMai(rs.getString("maKM"), rs.getString("code"),
                            rs.getDouble("giamGia"),
                            KieuGiamGia.fromString(rs.getString("kieuGiamGia")),
                            rs.getDate("ngayBatDau").toLocalDate(),
                            rs.getDate("ngayKetThuc").toLocalDate(),
                            rs.getDouble("tongTienToiThieu"), rs.getString("moTa"),
                            rs.getString("trangThai"), rs.getString("maNV"));
                }
            }
        }
        return null;
    }
    
    public String layMaKMTiepTheo() throws SQLException {
        String sql = "{call sp_layMaKMTiepTheo()}";
        try (Connection con = CauHinhDatabase.getConnection();
                CallableStatement cs = con.prepareCall(sql)) {
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("maKM");
                }
            }
        }
        return null;
    }
}