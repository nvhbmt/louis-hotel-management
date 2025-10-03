package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;

import java.sql.*;
import java.util.ArrayList;

public class PhongDAO {
    
    // Thêm phòng
    public boolean themPhong(Phong phong) throws SQLException {
        String sql = "{call sp_ThemPhong(?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, phong.getMaPhong());
            cs.setObject(2, phong.getTang());
            cs.setString(3, phong.getTrangThai());
            cs.setString(4, phong.getMoTa());
            cs.setString(5, phong.getMaLoaiPhong());

            return cs.executeUpdate() > 0;
        }
    }

    // Lấy danh sách tất cả phòng
    public ArrayList<Phong> layDSPhong() throws SQLException {
        ArrayList<Phong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhong()}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Phong phong = new Phong(
                        rs.getString("maPhong"),
                        rs.getObject("tang", Integer.class),
                        rs.getString("trangThai"),
                        rs.getString("moTa"),
                        rs.getString("maLoaiPhong")
                );
                ds.add(phong);
            }
        }
        return ds;
    }

    // Lấy danh sách phòng theo tầng
    public ArrayList<Phong> layDSPhongTheoTang(Integer tang) throws SQLException {
        ArrayList<Phong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhongTheoTang(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setObject(1, tang);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Phong phong = new Phong(
                        rs.getString("maPhong"),
                        rs.getObject("tang", Integer.class),
                        rs.getString("trangThai"),
                        rs.getString("moTa"),
                        rs.getString("maLoaiPhong")
                );
                ds.add(phong);
            }
        }
        return ds;
    }

    // Lấy danh sách phòng theo trạng thái
    public ArrayList<Phong> layDSPhongTheoTrangThai(String trangThai) throws SQLException {
        ArrayList<Phong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhongTheoTrangThai(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, trangThai);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Phong phong = new Phong(
                        rs.getString("maPhong"),
                        rs.getObject("tang", Integer.class),
                        rs.getString("trangThai"),
                        rs.getString("moTa"),
                        rs.getString("maLoaiPhong")
                );
                ds.add(phong);
            }
        }
        return ds;
    }

    // Lấy danh sách phòng còn trống
    public ArrayList<Phong> layDSPhongTrong() throws SQLException {
        ArrayList<Phong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhongTrong()}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Phong phong = new Phong(
                        rs.getString("maPhong"),
                        rs.getObject("tang", Integer.class),
                        rs.getString("trangThai"),
                        rs.getString("moTa"),
                        rs.getString("maLoaiPhong")
                );
                ds.add(phong);
            }
        }
        return ds;
    }

    // Lấy phòng theo mã
    public Phong layPhongTheoMa(String maPhong) throws SQLException {
        String sql = "{call sp_LayPhongTheoMa(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhong);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return new Phong(
                        rs.getString("maPhong"),
                        rs.getObject("tang", Integer.class),
                        rs.getString("trangThai"),
                        rs.getString("moTa"),
                        rs.getString("maLoaiPhong")
                );
            }
        }
        return null;
    }

    // Cập nhật phòng
    public boolean capNhatPhong(Phong phong) throws SQLException {
        String sql = "{call sp_CapNhatPhong(?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, phong.getMaPhong());
            cs.setObject(2, phong.getTang());
            cs.setString(3, phong.getTrangThai());
            cs.setString(4, phong.getMoTa());
            cs.setString(5, phong.getMaLoaiPhong());

            return cs.executeUpdate() > 0;
        }
    }

    // Cập nhật trạng thái phòng
    public boolean capNhatTrangThaiPhong(String maPhong, String trangThai) throws SQLException {
        String sql = "{call sp_CapNhatTrangThaiPhong(?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhong);
            cs.setString(2, trangThai);

            return cs.executeUpdate() > 0;
        }
    }

    // Xóa phòng
    public boolean xoaPhong(String maPhong) throws SQLException {
        String sql = "{call sp_XoaPhong(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhong);
            return cs.executeUpdate() > 0;
        }
    }

    // Kiểm tra phòng có được sử dụng không
    public boolean kiemTraPhongDuocSuDung(String maPhong) throws SQLException {
        String sql = "{call sp_KiemTraPhongDuocSuDung(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhong);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        }
        return false;
    }
}