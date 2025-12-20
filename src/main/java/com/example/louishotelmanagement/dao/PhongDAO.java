package com.example.louishotelmanagement.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;

public class PhongDAO {

    // Thêm phòng
    public boolean themPhong(Phong phong) throws SQLException {
        String sql = "{call sp_ThemPhong(?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, phong.getMaPhong());
            cs.setObject(2, phong.getTang());
            cs.setString(3, phong.getTrangThai().toString());
            cs.setString(4, phong.getMoTa());
            cs.setString(5, phong.getLoaiPhong().getMaLoaiPhong());

            return cs.executeUpdate() > 0;
        }
    }

    // Lấy danh sách tất cả phòng
    public ArrayList<Phong> layDSPhong() throws SQLException {
        LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
        ArrayList<Phong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhong()}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                LoaiPhong loaiPhong = loaiPhongDAO.layLoaiPhongTheoMa(rs.getString("maLoaiPhong"));
                Phong phong = new Phong(
                        rs.getString("maPhong"),
                        rs.getObject("tang", Integer.class),
                        TrangThaiPhong.fromString(rs.getString("trangThai")),
                        rs.getString("moTa"),
                        loaiPhong
                );
                ds.add(phong);
            }
        }
        return ds;
    }

    // Lấy danh sách phòng theo tầng
    public ArrayList<Phong> layDSPhongTheoTang(Integer tang) throws SQLException {
        LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
        ArrayList<Phong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhongTheoTang(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setObject(1, tang);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                LoaiPhong loaiPhong = loaiPhongDAO.layLoaiPhongTheoMa(rs.getString("maLoaiPhong"));
                Phong phong = new Phong(
                        rs.getString("maPhong"),
                        rs.getObject("tang", Integer.class),
                        TrangThaiPhong.fromString(rs.getString("trangThai")),
                        rs.getString("moTa"),
                        loaiPhong
                );
                ds.add(phong);
            }
        }
        return ds;
    }

    // Lấy danh sách phòng theo trạng thái
    public ArrayList<Phong> layDSPhongTheoTrangThai(TrangThaiPhong trangThai) throws SQLException {
        LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
        ArrayList<Phong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhongTheoTrangThai(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, trangThai.toString());
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                LoaiPhong loaiPhong = loaiPhongDAO.layLoaiPhongTheoMa(rs.getString("maLoaiPhong"));
                Phong phong = new Phong(
                        rs.getString("maPhong"),
                        rs.getObject("tang", Integer.class),
                        TrangThaiPhong.fromString(rs.getString("trangThai")),
                        rs.getString("moTa"),
                        loaiPhong
                );
                ds.add(phong);
            }
        }
        return ds;
    }

    // Lấy danh sách phòng còn trống
    public ArrayList<Phong> layDSPhongTrong() throws SQLException {
        LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
        ArrayList<Phong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhongTrong()}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                LoaiPhong loaiPhong = loaiPhongDAO.layLoaiPhongTheoMa(rs.getString("maLoaiPhong"));
                Phong phong = new Phong(
                        rs.getString("maPhong"),
                        rs.getObject("tang", Integer.class),
                        TrangThaiPhong.fromString(rs.getString("trangThai")),
                        rs.getString("moTa"),
                        loaiPhong
                );
                ds.add(phong);
            }
        }
        return ds;
    }

    // Lấy phòng theo mã
    public Phong layPhongTheoMa(String maPhong) throws SQLException {
        LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
        String sql = "{call sp_LayPhongTheoMa(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhong);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                LoaiPhong loaiPhong = loaiPhongDAO.layLoaiPhongTheoMa(rs.getString("maLoaiPhong"));
                return new Phong(
                        rs.getString("maPhong"),
                        rs.getObject("tang", Integer.class),
                        TrangThaiPhong.fromString(rs.getString("trangThai")),
                        rs.getString("moTa"),
                        loaiPhong
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
            cs.setInt(2, phong.getTang());
            cs.setNString(3, phong.getTrangThai().toString());
            cs.setNString(4, phong.getMoTa());
            cs.setString(5, phong.getLoaiPhong().getMaLoaiPhong());

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

    /**
     * Kiểm tra xem một phòng có trống trong khoảng thời gian hay không.
     * 
     * @param maPhong Mã phòng cần kiểm tra
     * @param ngayDen Ngày đến (check-in)
     * @param ngayDi Ngày đi (check-out)
     * @return true nếu phòng trống trong khoảng thời gian, false nếu không
     * @throws SQLException Nếu có lỗi khi truy vấn database
     */
    public boolean kiemTraPhongTrongTheoKhoangThoiGian(String maPhong, LocalDate ngayDen, LocalDate ngayDi) throws SQLException {
        if (ngayDen == null || ngayDi == null || maPhong == null) {
            return false;
        }
        
        String sql = "{call sp_KiemTraPhongTrongTheoKhoangThoiGian(?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setString(1, maPhong);
            cs.setDate(2, Date.valueOf(ngayDen));
            cs.setDate(3, Date.valueOf(ngayDi));
            
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("isTrong");
            }
        }
        return false;
    }
    
    /**
     * Lấy danh sách phòng trống trong khoảng thời gian đã chỉ định.
     * 
     * @param ngayDen Ngày đến (check-in), không được null
     * @param ngayDi Ngày đi (check-out), không được null và phải sau ngayDen
     * @return Danh sách phòng trống trong khoảng thời gian
     * @throws SQLException Nếu có lỗi khi truy vấn database
     * @throws IllegalArgumentException Nếu ngayDen hoặc ngayDi là null
     */
    public ArrayList<Phong> layDSPhongTrongTheoKhoangThoiGian(LocalDate ngayDen, LocalDate ngayDi) throws SQLException {
        // Validate input
        if (ngayDen == null || ngayDi == null) {
            throw new IllegalArgumentException("Ngày đến và ngày đi không được null");
        }
        
        if (ngayDi.isBefore(ngayDen) || ngayDi.isEqual(ngayDen)) {
            throw new IllegalArgumentException("Ngày đi phải sau ngày đến");
        }
        
        LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
        ArrayList<Phong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhongTrongTheoKhoangThoiGian(?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setDate(1, Date.valueOf(ngayDen));
            cs.setDate(2, Date.valueOf(ngayDi));
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                LoaiPhong loaiPhong = loaiPhongDAO.layLoaiPhongTheoMa(rs.getString("maLoaiPhong"));
                Phong phong = new Phong(
                        rs.getString("maPhong"),
                        rs.getObject("tang", Integer.class),
                        TrangThaiPhong.fromString(rs.getString("trangThai")),
                        rs.getString("moTa"),
                        loaiPhong
                );
                ds.add(phong);
            }
        }
        return ds;
    }
}