package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.PhieuDatPhong;
import com.example.louishotelmanagement.model.TrangThaiPhieuDatPhong;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PhieuDatPhongDAO {

    /**
     * Hàm helper để xây dựng đối tượng PhieuDatPhong từ ResultSet.
     * @param rs ResultSet chứa dữ liệu phiếu đặt phòng.
     * @return PhieuDatPhong object.
     * @throws SQLException
     */
    private PhieuDatPhong createPhieuDatPhongFromResultSet(ResultSet rs) throws SQLException {
        // GỌI CONSTRUCTOR MỚI HOẶC CHỈNH SỬA CONSTRUCTOR CŨ TRONG MODEL
        return new PhieuDatPhong(
                rs.getString("maPhieu"),
                rs.getDate("ngayDat") != null ? rs.getDate("ngayDat").toLocalDate() : null,
                rs.getDate("ngayDen") != null ? rs.getDate("ngayDen").toLocalDate() : null,
                rs.getDate("ngayDi") != null ? rs.getDate("ngayDi").toLocalDate() : null,
                TrangThaiPhieuDatPhong.fromString(rs.getString("trangThai")),
                rs.getString("ghiChu"),
                rs.getString("maKH"),
                rs.getString("maNV"),
                rs.getBigDecimal("tienCoc") // 💡 THAY ĐỔI LỚN: Lấy tienCoc trực tiếp vào constructor/model
        );
    }

    // Thêm phiếu đặt phòng
    public boolean themPhieuDatPhong(PhieuDatPhong phieuDatPhong) throws SQLException {
        // Tham số thứ 9 là tiền cọc
        String sql = "{call sp_ThemPhieuDatPhong(?,?,?,?,?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, phieuDatPhong.getMaPhieu());
            cs.setObject(2, phieuDatPhong.getNgayDat() != null ? Date.valueOf(phieuDatPhong.getNgayDat()) : null);
            cs.setObject(3, phieuDatPhong.getNgayDen() != null ? Date.valueOf(phieuDatPhong.getNgayDen()) : null);
            cs.setObject(4, phieuDatPhong.getNgayDi() != null ? Date.valueOf(phieuDatPhong.getNgayDi()) : null);
            cs.setString(5, phieuDatPhong.getTrangThai().toString());
            cs.setString(6, phieuDatPhong.getGhiChu());
            cs.setString(7, phieuDatPhong.getMaKH());
            cs.setString(8, phieuDatPhong.getMaNV());
            cs.setBigDecimal(9, phieuDatPhong.getTienCoc());

            return cs.executeUpdate() > 0;
        }
    }

    public String sinhMaPhieuTiepTheo() throws SQLException {
        // Logic giữ nguyên
        String sql = "{call sp_SinhMaPhieuDatPhongTiepTheo()}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            if (rs.next()) {
                return rs.getString("maPhieuMoi");
            }
        }
        return "PDP001"; // mặc định nếu lỗi
    }


    // Lấy danh sách tất cả phiếu đặt phòng
    public ArrayList<PhieuDatPhong> layDSPhieuDatPhong() throws SQLException {
        ArrayList<PhieuDatPhong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhieuDatPhong()}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                // SỬ DỤNG HÀM HELPER
                ds.add(createPhieuDatPhongFromResultSet(rs));
            }
        }
        return ds;
    }

    // Lấy phiếu đặt phòng theo mã
    public PhieuDatPhong layPhieuDatPhongTheoMa(String maPhieu) throws SQLException {
        String sql = "{call sp_LayPhieuDatPhongTheoMa(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhieu);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                // SỬ DỤNG HÀM HELPER
                return createPhieuDatPhongFromResultSet(rs);
            }
        }
        return null;
    }

    // Lấy danh sách phiếu đặt phòng theo khách hàng
    public ArrayList<PhieuDatPhong> layDSPhieuDatPhongTheoKhachHang(String maKH) throws SQLException {
        ArrayList<PhieuDatPhong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhieuDatPhongTheoKhachHang(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maKH);
            cs.execute();
            ResultSet rs = cs.getResultSet();

            while (rs.next()) {
                // SỬ DỤNG HÀM HELPER
                ds.add(createPhieuDatPhongFromResultSet(rs));
            }
        }
        return ds;
    }

    // Lấy danh sách phiếu đặt phòng theo nhân viên
    public ArrayList<PhieuDatPhong> layDSPhieuDatPhongTheoNhanVien(String maNV) throws SQLException {
        ArrayList<PhieuDatPhong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhieuDatPhongTheoNhanVien(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maNV);
            cs.execute();
            ResultSet rs = cs.getResultSet();

            while (rs.next()) {
                // SỬ DỤNG HÀM HELPER
                ds.add(createPhieuDatPhongFromResultSet(rs));
            }
        }
        return ds;
    }

    // Lấy danh sách phiếu đặt phòng theo trạng thái
    public ArrayList<PhieuDatPhong> layDSPhieuDatPhongTheoTrangThai(String trangThai) throws SQLException {
        ArrayList<PhieuDatPhong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhieuDatPhongTheoTrangThai(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, trangThai);
            cs.execute();
            ResultSet rs = cs.getResultSet();

            while (rs.next()) {
                // SỬ DỤNG HÀM HELPER
                ds.add(createPhieuDatPhongFromResultSet(rs));
            }
        }
        return ds;
    }

    // Lấy danh sách phiếu đặt phòng trong khoảng thời gian
    public ArrayList<PhieuDatPhong> layDSPhieuDatPhongTrongKhoangThoiGian(LocalDate ngayBatDau, LocalDate ngayKetThuc) throws SQLException {
        ArrayList<PhieuDatPhong> ds = new ArrayList<>();
        String sql = "{call sp_LayDSPhieuDatPhongTrongKhoangThoiGian(?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setDate(1, ngayBatDau != null ? Date.valueOf(ngayBatDau) : null);
            cs.setDate(2, ngayKetThuc != null ? Date.valueOf(ngayKetThuc) : null);
            cs.execute();
            ResultSet rs = cs.getResultSet();

            while (rs.next()) {
                // SỬ DỤNG HÀM HELPER
                ds.add(createPhieuDatPhongFromResultSet(rs));
            }
        }
        return ds;
    }

    // Cập nhật phiếu đặt phòng
    public boolean capNhatPhieuDatPhong(PhieuDatPhong phieuDatPhong) throws SQLException {
        // Tham số thứ 9 là tiền cọc
        String sql = "{call sp_CapNhatPhieuDatPhong(?,?,?,?,?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, phieuDatPhong.getMaPhieu());
            cs.setObject(2, phieuDatPhong.getNgayDat() != null ? Date.valueOf(phieuDatPhong.getNgayDat()) : null);
            cs.setObject(3, phieuDatPhong.getNgayDen() != null ? Date.valueOf(phieuDatPhong.getNgayDen()) : null);
            cs.setObject(4, phieuDatPhong.getNgayDi() != null ? Date.valueOf(phieuDatPhong.getNgayDi()) : null);
            cs.setString(5, phieuDatPhong.getTrangThai().toString());
            cs.setString(6, phieuDatPhong.getGhiChu());
            cs.setString(7, phieuDatPhong.getMaKH());
            cs.setString(8, phieuDatPhong.getMaNV());
            cs.setBigDecimal(9, phieuDatPhong.getTienCoc());

            return cs.executeUpdate() > 0;
        }
    }

    // Cập nhật trạng thái phiếu đặt phòng (Giữ nguyên)
    public boolean capNhatTrangThaiPhieuDatPhong(String maPhieu, String trangThai) throws SQLException {
        String sql = "{call sp_CapNhatTrangThaiPhieuDatPhong(?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhieu);
            cs.setString(2, trangThai);

            return cs.executeUpdate() > 0;
        }
    }

    // Xóa phiếu đặt phòng (Giữ nguyên)
    public boolean xoaPhieuDatPhong(String maPhieu) throws SQLException {
        String sql = "{call sp_XoaPhieuDatPhong(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, maPhieu);
            return cs.executeUpdate() > 0;
        }
    }
}