package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.CTPhieuDatPhong;
import com.example.louishotelmanagement.model.Phong;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CTPhieuDatPhongDAO {
    
    // Thêm chi tiết phiếu đặt phòng
    public boolean themCTPhieuDatPhong(CTPhieuDatPhong ctPhieuDatPhong) throws SQLException {
        String sql = "{call sp_ThemCTPhieuDatPhong(?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, ctPhieuDatPhong.getMaPhieu());
            cs.setString(2, ctPhieuDatPhong.getMaPhong());
            cs.setObject(3, ctPhieuDatPhong.getNgayDen() != null ? Date.valueOf(ctPhieuDatPhong.getNgayDen()) : null);
            cs.setObject(4, ctPhieuDatPhong.getNgayDi() != null ? Date.valueOf(ctPhieuDatPhong.getNgayDi()) : null);
            cs.setBigDecimal(5, ctPhieuDatPhong.getGiaPhong());

            return cs.executeUpdate() > 0;
        }
    }

    // Lấy danh sách tất cả chi tiết phiếu đặt phòng
    public ArrayList<CTPhieuDatPhong> layDSCTPhieuDatPhong() throws SQLException {
        ArrayList<CTPhieuDatPhong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSCTPhieuDatPhong()}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                CTPhieuDatPhong ctPhieuDatPhong = new CTPhieuDatPhong(
                        rs.getString("maPhieu"),
                        rs.getString("maPhong"),
                        rs.getDate("ngayDen") != null ? rs.getDate("ngayDen").toLocalDate() : null,
                        rs.getDate("ngayDi") != null ? rs.getDate("ngayDi").toLocalDate() : null,
                        rs.getBigDecimal("giaPhong")
                );
                ds.add(ctPhieuDatPhong);
            }
        }
        return ds;
    }

    // Lấy chi tiết phiếu đặt phòng theo mã phiếu và mã phòng
    public CTPhieuDatPhong layCTPhieuDatPhongTheoMa(String maPhieu, String maPhong) throws SQLException {
        String sql = "{call sp_LayCTPhieuDatPhongTheoMa(?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhieu);
            cs.setString(2, maPhong);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return new CTPhieuDatPhong(
                        rs.getString("maPhieu"),
                        rs.getString("maPhong"),
                        rs.getDate("ngayDen") != null ? rs.getDate("ngayDen").toLocalDate() : null,
                        rs.getDate("ngayDi") != null ? rs.getDate("ngayDi").toLocalDate() : null,
                        rs.getBigDecimal("giaPhong")
                );
            }
        }
        return null;
    }

    // Lấy danh sách chi tiết phiếu đặt phòng theo mã phiếu
    public ArrayList<CTPhieuDatPhong> layDSCTPhieuDatPhongTheoPhieu(String maPhieu) throws SQLException {
        ArrayList<CTPhieuDatPhong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSCTPhieuDatPhongTheoPhieu(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhieu);
            cs.execute();
            ResultSet rs = cs.getResultSet();

            while (rs.next()) {
                CTPhieuDatPhong ctPhieuDatPhong = new CTPhieuDatPhong(
                        rs.getString("maPhieu"),
                        rs.getString("maPhong"),
                        rs.getDate("ngayDen") != null ? rs.getDate("ngayDen").toLocalDate() : null,
                        rs.getDate("ngayDi") != null ? rs.getDate("ngayDi").toLocalDate() : null,
                        rs.getBigDecimal("giaPhong")
                );
                ds.add(ctPhieuDatPhong);
            }
        }
        return ds;
    }

    // Lấy danh sách chi tiết phiếu đặt phòng theo mã phòng
    public ArrayList<CTPhieuDatPhong> layDSCTPhieuDatPhongTheoPhong(String maPhong) throws SQLException {
        ArrayList<CTPhieuDatPhong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSCTPhieuDatPhongTheoPhong(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhong);
            cs.execute();
            ResultSet rs = cs.getResultSet();

            while (rs.next()) {
                CTPhieuDatPhong ctPhieuDatPhong = new CTPhieuDatPhong(
                        rs.getString("maPhieu"),
                        rs.getString("maPhong"),
                        rs.getDate("ngayDen") != null ? rs.getDate("ngayDen").toLocalDate() : null,
                        rs.getDate("ngayDi") != null ? rs.getDate("ngayDi").toLocalDate() : null,
                        rs.getBigDecimal("giaPhong")
                );
                ds.add(ctPhieuDatPhong);
            }
        }
        return ds;
    }

    // Lấy danh sách chi tiết phiếu đặt phòng trong khoảng thời gian
    public ArrayList<CTPhieuDatPhong> layDSCTPhieuDatPhongTrongKhoangThoiGian(LocalDate ngayBatDau, LocalDate ngayKetThuc) throws SQLException {
        ArrayList<CTPhieuDatPhong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSCTPhieuDatPhongTrongKhoangThoiGian(?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setDate(1, ngayBatDau != null ? Date.valueOf(ngayBatDau) : null);
            cs.setDate(2, ngayKetThuc != null ? Date.valueOf(ngayKetThuc) : null);
            cs.execute();
            ResultSet rs = cs.getResultSet();

            while (rs.next()) {
                CTPhieuDatPhong ctPhieuDatPhong = new CTPhieuDatPhong(
                        rs.getString("maPhieu"),
                        rs.getString("maPhong"),
                        rs.getDate("ngayDen") != null ? rs.getDate("ngayDen").toLocalDate() : null,
                        rs.getDate("ngayDi") != null ? rs.getDate("ngayDi").toLocalDate() : null,
                        rs.getBigDecimal("giaPhong")
                );
                ds.add(ctPhieuDatPhong);
            }
        }
        return ds;
    }

    // Cập nhật chi tiết phiếu đặt phòng
    public boolean capNhatCTPhieuDatPhong(CTPhieuDatPhong ctPhieuDatPhong) throws SQLException {
        String sql = "{call sp_CapNhatCTPhieuDatPhong(?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, ctPhieuDatPhong.getMaPhieu());
            cs.setString(2, ctPhieuDatPhong.getMaPhong());
            cs.setObject(3, ctPhieuDatPhong.getNgayDen() != null ? Date.valueOf(ctPhieuDatPhong.getNgayDen()) : null);
            cs.setObject(4, ctPhieuDatPhong.getNgayDi() != null ? Date.valueOf(ctPhieuDatPhong.getNgayDi()) : null);
            cs.setBigDecimal(5, ctPhieuDatPhong.getGiaPhong());

            return cs.executeUpdate() > 0;
        }
    }

    // Xóa chi tiết phiếu đặt phòng
    public boolean xoaCTPhieuDatPhong(String maPhieu, String maPhong) throws SQLException {
        String sql = "{call sp_XoaCTPhieuDatPhong(?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhieu);
            cs.setString(2, maPhong);
            return cs.executeUpdate() > 0;
        }
    }

    // Xóa tất cả chi tiết phiếu đặt phòng theo mã phiếu
    public boolean xoaTatCaCTPhieuDatPhongTheoPhieu(String maPhieu) throws SQLException {
        String sql = "{call sp_XoaTatCaCTPhieuDatPhongTheoPhieu(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhieu);
            return cs.executeUpdate() > 0;
        }
    }

    // Kiểm tra phòng có được đặt trong khoảng thời gian không
    public boolean kiemTraPhongDaDuocDat(String maPhong, LocalDate ngayDen, LocalDate ngayDi) throws SQLException {
        String sql = "{call sp_KiemTraPhongDaDuocDat(?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhong);
            cs.setDate(2, ngayDen != null ? Date.valueOf(ngayDen) : null);
            cs.setDate(3, ngayDi != null ? Date.valueOf(ngayDi) : null);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        }
        return false;
    }

    // Tính tổng tiền của phiếu đặt phòng
    public BigDecimal tinhTongTienPhieuDatPhong(String maPhieu) throws SQLException {
        String sql = "{call sp_TinhTongTienPhieuDatPhong(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhieu);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("tongTien");
            }
        }
        return BigDecimal.ZERO;
    }
}
