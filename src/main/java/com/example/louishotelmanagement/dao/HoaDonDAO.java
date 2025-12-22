package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    // 1. LẤY DANH SÁCH HÓA ĐƠN
    public List<HoaDon> layDanhSachHoaDon() throws SQLException {
        List<HoaDon> ds = new ArrayList<>();
        String sql = "{CALL sp_LayDanhSachHoaDon()}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                // mapResultSetToHoaDonFull đã bao gồm mapResultSetToHoaDonSimple
                HoaDon hd = mapResultSetToHoaDonFull(rs);

                // Các cột này lấy từ phép JOIN trong sp_LayDanhSachHoaDon
                KhachHang kh = new KhachHang();
                kh.setHoTen(rs.getString("hoTen"));
                kh.setSoDT(rs.getString("soDT"));
                kh.setDiaChi(rs.getString("diaChi"));
                hd.setKhachHang(kh);

                hd.setSoPhong(rs.getString("soPhong"));
                ds.add(hd);
            }
        }
        return ds;
    }

    // 2. HELPER MAPPING - FIX CHÍNH XÁC TÊN CỘT TỪ SQL
    private HoaDon mapResultSetToHoaDonSimple(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHD(rs.getString("maHD"));

        Date ngayLap = rs.getDate("ngayLap");
        if (ngayLap != null) hd.setNgayLap(ngayLap.toLocalDate());

        hd.setPhuongThuc(PhuongThucThanhToan.fromString(rs.getString("phuongThuc")));
        hd.setTongTien(rs.getBigDecimal("tongTien"));
        hd.setMaKH(rs.getString("maKH"));
        hd.setMaNV(rs.getString("maNV"));
        hd.setMaKM(rs.getString("maKM"));
        hd.setTrangThai(TrangThaiHoaDon.fromString(rs.getString("trangThai")));

        // CHỈNH SỬA TẠI ĐÂY: Sử dụng "ngayDi" để khớp với Procedure sp_LayDanhSachHoaDon (hd.*)
        Date checkOut = rs.getDate("ngayDi");
        if (checkOut != null) hd.setngayDi(checkOut.toLocalDate());

        return hd;
    }

    private HoaDon mapResultSetToHoaDonFull(ResultSet rs) throws SQLException {
        HoaDon hd = mapResultSetToHoaDonSimple(rs);

        // Đọc các cột phạt (Khớp với các tham số trong Stored Procedures)
        BigDecimal p1 = rs.getBigDecimal("PhatNhanPhongTre");
        BigDecimal p2 = rs.getBigDecimal("PhatTraPhongSom");
        BigDecimal p3 = rs.getBigDecimal("PhatTraPhongTre");

        hd.setPhatNhanPhongTre(p1 != null ? p1 : BigDecimal.ZERO);
        hd.setPhatTraPhongSom(p2 != null ? p2 : BigDecimal.ZERO);
        hd.setPhatTraPhongTre(p3 != null ? p3 : BigDecimal.ZERO);

        hd.setTienPhat(hd.getPhatNhanPhongTre().add(hd.getPhatTraPhongSom()).add(hd.getPhatTraPhongTre()));

        // Đọc các cột giảm giá
        BigDecimal g1 = rs.getBigDecimal("GiamGiaMaGG");
        BigDecimal g2 = rs.getBigDecimal("GiamGiaHangKH");

        hd.setGiamGiaTheoMa(g1 != null ? g1 : BigDecimal.ZERO);
        hd.setGiamGiaTheoHangKH(g2 != null ? g2 : BigDecimal.ZERO);

        hd.setTongGiamGia(hd.getGiamGiaTheoMa().add(hd.getGiamGiaTheoHangKH()));

        hd.setTongVAT(rs.getBigDecimal("TongVAT"));
        return hd;
    }

    // 3. TÌM HÓA ĐƠN THEO MÃ
    public HoaDon timHoaDonTheoMa(String maHD) throws SQLException {
        String sql = "{CALL sp_TimHoaDonTheoMa(?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    HoaDon hd = mapResultSetToHoaDonFull(rs);

                    // Bổ sung Mapping khách hàng cho sp_TimHoaDonTheoMa
                    KhachHang kh = new KhachHang();
                    kh.setHoTen(rs.getString("hoTen"));
                    kh.setSoDT(rs.getString("soDT"));
                    kh.setDiaChi(rs.getString("diaChi"));
                    hd.setKhachHang(kh);

                    hd.setSoPhong(rs.getString("soPhong"));
                    return hd;
                }
            }
        }
        return null;
    }

    // 4. TẠO MÃ TỰ ĐỘNG
    public String taoMaHoaDonTiepTheo() {
        String sql = "{CALL sp_TaoMaHoaDonTiepTheo}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            if (rs.next()) return rs.getString("maHDMoi");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 5. THÊM HÓA ĐƠN
    public boolean themHoaDon(HoaDon hd) throws SQLException {
        String sql = "{CALL sp_ThemHoaDon(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            setParamsForHoaDon(cs, hd);
            return cs.executeUpdate() > 0;
        }
    }

    // 6. CẬP NHẬT HÓA ĐƠN
    public boolean capNhatHoaDon(HoaDon hd) throws SQLException {
        String sql = "{CALL sp_SuaHoaDon(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            setParamsForHoaDon(cs, hd);
            return cs.executeUpdate() > 0;
        }
    }

    // Helper set tham số để đồng bộ giữa Add và Update
    private void setParamsForHoaDon(CallableStatement cs, HoaDon hd) throws SQLException {
        cs.setString(1, hd.getMaHD());
        cs.setDate(2, hd.getNgayLap() != null ? Date.valueOf(hd.getNgayLap()) : Date.valueOf(LocalDate.now()));
        cs.setString(3, hd.getPhuongThuc() != null ? hd.getPhuongThuc().toString() : null);
        cs.setBigDecimal(4, hd.getTongTien());
        cs.setString(5, hd.getMaKH());
        cs.setString(6, hd.getMaNV());
        cs.setString(7, hd.getMaKM());
        cs.setString(8, hd.getTrangThai() != null ? hd.getTrangThai().toString() : "Chưa thanh toán");
        cs.setDate(9, hd.getngayDi() != null ? Date.valueOf(hd.getngayDi()) : null);
        cs.setBigDecimal(10, hd.getPhatNhanPhongTre() != null ? hd.getPhatNhanPhongTre() : BigDecimal.ZERO);
        cs.setBigDecimal(11, hd.getPhatTraPhongSom() != null ? hd.getPhatTraPhongSom() : BigDecimal.ZERO);
        cs.setBigDecimal(12, hd.getPhatTraPhongTre() != null ? hd.getPhatTraPhongTre() : BigDecimal.ZERO);
        cs.setBigDecimal(13, hd.getGiamGiaTheoMa() != null ? hd.getGiamGiaTheoMa() : BigDecimal.ZERO);
        cs.setBigDecimal(14, hd.getGiamGiaTheoHangKH() != null ? hd.getGiamGiaTheoHangKH() : BigDecimal.ZERO);
        cs.setBigDecimal(15, hd.getTongVAT() != null ? hd.getTongVAT() : BigDecimal.ZERO);
    }

    // 7. LẤY CHI TIẾT HÓA ĐƠN (TABLE)
    public List<HoaDonChiTietItem> layChiTietHoaDon(String maHD) throws SQLException {
        List<HoaDonChiTietItem> dsChiTiet = new ArrayList<>();
        String sql = "{CALL sp_LayChiTietHoaDonTheoMaHD(?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, maHD);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    dsChiTiet.add(new HoaDonChiTietItem(
                            rs.getInt("STT"),
                            rs.getString("TenChiTiet"),
                            rs.getInt("SoLuong"),
                            rs.getBigDecimal("DonGia"),
                            rs.getBigDecimal("ThanhTien")
                    ));
                }
            }
        }
        return dsChiTiet;
    }
}