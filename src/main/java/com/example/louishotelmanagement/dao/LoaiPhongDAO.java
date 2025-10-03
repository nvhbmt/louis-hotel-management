package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.LoaiPhong;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class LoaiPhongDAO {
    
    // Thêm loại phòng
    public boolean themLoaiPhong(LoaiPhong loaiPhong) throws SQLException {
        String sql = "{call sp_ThemLoaiPhong(?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, loaiPhong.getMaLoaiPhong());
            cs.setString(2, loaiPhong.getTenLoai());
            cs.setString(3, loaiPhong.getMoTa());
            cs.setBigDecimal(4, loaiPhong.getDonGia());

            return cs.executeUpdate() > 0;
        }
    }

    // Lấy danh sách tất cả loại phòng
    public ArrayList<LoaiPhong> layDSLoaiPhong() throws SQLException {
        ArrayList<LoaiPhong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSLoaiPhong()}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                LoaiPhong loaiPhong = new LoaiPhong(
                        rs.getString("maLoaiPhong"),
                        rs.getString("tenLoai"),
                        rs.getString("moTa"),
                        rs.getBigDecimal("donGia")
                );
                ds.add(loaiPhong);
            }
        }
        return ds;
    }

    // Lấy loại phòng theo mã
    public LoaiPhong layLoaiPhongTheoMa(String maLoaiPhong) throws SQLException {
        String sql = "{call sp_LayLoaiPhongTheoMa(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maLoaiPhong);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return new LoaiPhong(
                        rs.getString("maLoaiPhong"),
                        rs.getString("tenLoai"),
                        rs.getString("moTa"),
                        rs.getBigDecimal("donGia")
                );
            }
        }
        return null;
    }

    // Cập nhật loại phòng
    public boolean capNhatLoaiPhong(LoaiPhong loaiPhong) throws SQLException {
        String sql = "{call sp_CapNhatLoaiPhong(?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, loaiPhong.getMaLoaiPhong());
            cs.setString(2, loaiPhong.getTenLoai());
            cs.setString(3, loaiPhong.getMoTa());
            cs.setBigDecimal(4, loaiPhong.getDonGia());

            return cs.executeUpdate() > 0;
        }
    }

    // Xóa loại phòng
    public boolean xoaLoaiPhong(String maLoaiPhong) throws SQLException {
        String sql = "{call sp_XoaLoaiPhong(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maLoaiPhong);
            return cs.executeUpdate() > 0;
        }
    }

    // Kiểm tra loại phòng có được sử dụng không
    public boolean kiemTraLoaiPhongDuocSuDung(String maLoaiPhong) throws SQLException {
        String sql = "{call sp_KiemTraLoaiPhongDuocSuDung(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maLoaiPhong);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        }
        return false;
    }
}
