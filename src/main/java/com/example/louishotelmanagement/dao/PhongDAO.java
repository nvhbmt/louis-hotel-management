package com.example.louishotelmanagement.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    // =================================================================================
    // 🔥 2 HÀM MỚI BỔ SUNG (DÙNG RAW SQL TRỰC TIẾP, KHÔNG CẦN TẠO PROCEDURE) 🔥
    // =================================================================================

    public boolean kiemTraPhongTrongTheoKhoangThoiGian(String maPhong, LocalDate ngayDen, LocalDate ngayDi) throws SQLException {
        if (ngayDen == null || ngayDi == null || maPhong == null) return false;

        String sql = """
            SELECT COUNT(*) as SoLuong
            FROM CTHoaDonPhong ct
            JOIN PhieuDatPhong pdp ON ct.MaPhieu = pdp.MaPhieu
            WHERE ct.MaPhong = ?
            AND (pdp.TrangThai = 'DA_DAT' OR pdp.TrangThai = 'DANG_SU_DUNG')
            AND (
                (? <= ct.NgayDi) AND (? >= ct.NgayDen)
            )
        """;

        try (Connection con = CauHinhDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhong);
            ps.setDate(2, Date.valueOf(ngayDen));
            ps.setDate(3, Date.valueOf(ngayDi));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("SoLuong") == 0;
            }
        }
        return true;
    }

    public ArrayList<Phong> layDSPhongTrongTheoKhoangThoiGian(LocalDate ngayDen, LocalDate ngayDi) throws SQLException {
        if (ngayDen == null || ngayDi == null) return new ArrayList<>();

        ArrayList<Phong> ds = new ArrayList<>();

        String sql = """
            SELECT p.*, lp.TenLoai, lp.DonGia
            FROM Phong p
            JOIN LoaiPhong lp ON p.MaLoaiPhong = lp.MaLoaiPhong
            WHERE p.TrangThai != 'BAO_TRI'
            AND p.MaPhong NOT IN (
                SELECT ct.MaPhong 
                FROM CTHoaDonPhong ct
                JOIN PhieuDatPhong pdp ON ct.MaPhieu = pdp.MaPhieu
                WHERE (pdp.TrangThai = 'DA_DAT' OR pdp.TrangThai = 'DANG_SU_DUNG')
                AND (
                    (? <= ct.NgayDi) AND (? >= ct.NgayDen)
                )
            )
        """;

        try (Connection con = CauHinhDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(ngayDen));
            ps.setDate(2, Date.valueOf(ngayDi));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LoaiPhong loaiPhong = new LoaiPhong();
                loaiPhong.setMaLoaiPhong(rs.getString("MaLoaiPhong"));
                loaiPhong.setTenLoai(rs.getString("TenLoai"));
                loaiPhong.setDonGia(rs.getDouble("DonGia"));

                Phong phong = new Phong(
                        rs.getString("MaPhong"),
                        rs.getInt("Tang"),
                        TrangThaiPhong.TRONG,
                        rs.getString("MoTa"),
                        loaiPhong
                );
                ds.add(phong);
            }
        }
        return ds;
    }
}