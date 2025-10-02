package com.example.louishotelmanagement.dao;
import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.MaGiamGia;

import java.sql.*;
import java.util.ArrayList;

public class MaGiamGiaDAO {
    // Thêm mã giảm giá
    public boolean themMaGiamGia(MaGiamGia mg) throws SQLException {
        String sql = "{call sp_ThemMaGiamGia(?,?,?,?,?,?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, mg.getMaGG());
            cs.setString(2, mg.getCode());
            cs.setDouble(3, mg.getGiamGia());
            cs.setString(4, mg.getKieuGiamGia());
            cs.setDate(5, Date.valueOf(mg.getNgayBatDau()));
            cs.setDate(6, Date.valueOf(mg.getNgayKetThuc()));
            cs.setDouble(7, mg.getTongTienToiThieu());
            cs.setString(8, mg.getMoTa());
            cs.setString(9, mg.getTrangThai());
            cs.setString(10, mg.getMaNhanVien());

            return cs.executeUpdate() > 0;
        }
    }

    // Lấy danh sách mã giảm giá
    public ArrayList<MaGiamGia> layDSMaGiamGia() throws SQLException {
        ArrayList<MaGiamGia> ds = new ArrayList<>();
        String sql = "{call sp_LayDSMaGiamGia()}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                MaGiamGia mg = new MaGiamGia(
                        rs.getString("maGG"),
                        rs.getString("code"),
                        rs.getDouble("giamGia"),
                        rs.getString("kieuGiamGia"),
                        rs.getDate("ngayBatDau").toLocalDate(),
                        rs.getDate("ngayKetThuc").toLocalDate(),
                        rs.getDouble("tongTienToiThieu"),
                        rs.getString("moTa"),
                        rs.getString("trangThai"),
                        rs.getString("maNV")
                );
                ds.add(mg);
            }
        }
        return ds;
    }
    // Lấy danh sách mã giảm giá còn hoạt động
    public ArrayList<MaGiamGia> layMaGiamGiaDangHoatDong() throws SQLException {
        ArrayList<MaGiamGia> ds = new ArrayList<>();
        String sql = "{call sp_LayMaGiamGiaDangHoatDong()}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                MaGiamGia mg = new MaGiamGia(
                        rs.getString("maGG"),
                        rs.getString("code"),
                        rs.getDouble("giamGia"),
                        rs.getString("kieuGiamGia"),
                        rs.getDate("ngayBatDau").toLocalDate(),
                        rs.getDate("ngayKetThuc").toLocalDate(),
                        rs.getDouble("tongTienToiThieu"),
                        rs.getString("moTa"),
                        rs.getString("trangThai"),
                        rs.getString("maNV")
                );
                ds.add(mg);
            }
        }
        return ds;
    }
    // Cập nhật mã giảm giá
    public boolean capNhatMaGiamGia(MaGiamGia mg) throws SQLException {
        String sql = "{call sp_CapNhatMaGiamGia(?,?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, mg.getMaGG());
            cs.setDouble(2, mg.getGiamGia());
            cs.setString(3, mg.getKieuGiamGia());
            cs.setDate(4, Date.valueOf(mg.getNgayKetThuc()));
            cs.setString(5, mg.getMoTa());
            cs.setString(6, mg.getTrangThai());

            return cs.executeUpdate() > 0;
        }
    }

    // Xóa mã giảm giá
    public boolean xoaMaGiamGia(String maGG) throws SQLException {
        String sql = "{call sp_XoaMaGiamGia(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maGG);
            return cs.executeUpdate() > 0;
        }
    }
}
