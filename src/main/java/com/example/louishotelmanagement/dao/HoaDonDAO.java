package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    // Hàm phụ trợ map dữ liệu từ ResultSet vào Object HoaDon để dùng chung
    private HoaDon mapResultSetToHoaDon(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHD(rs.getString("maHD"));

        Date ngayLapDate = rs.getDate("ngayLap");
        hd.setNgayLap(ngayLapDate != null ? ngayLapDate.toLocalDate() : null);

        hd.setPhuongThuc(PhuongThucThanhToan.fromString(rs.getString("phuongThuc")));
        hd.setTongTien(rs.getBigDecimal("tongTien"));
        hd.setMaKH(rs.getString("maKH"));
        hd.setMaNV(rs.getString("maNV"));
        hd.setMaGG(rs.getString("maGG"));
        hd.setTrangThai(TrangThaiHoaDon.fromString(rs.getString("trangThai")));

        // Đọc các trường ngày tháng thực tế
        Date ngayCheckOutDate = rs.getDate("NgayCheckOut");
        hd.setNgayCheckOut(ngayCheckOutDate != null ? ngayCheckOutDate.toLocalDate() : null);

        // Đọc chi tiết tiền phạt và tính tổng để set vào TienPhat (Tránh lỗi Invalid Column TienPhat)
        BigDecimal p1 = rs.getBigDecimal("PhatNhanPhongTre");
        BigDecimal p2 = rs.getBigDecimal("PhatTraPhongSom");
        BigDecimal p3 = rs.getBigDecimal("PhatTraPhongTre");
        BigDecimal tongPhat = (p1 != null ? p1 : BigDecimal.ZERO)
                .add(p2 != null ? p2 : BigDecimal.ZERO)
                .add(p3 != null ? p3 : BigDecimal.ZERO);
        hd.setTienPhat(tongPhat);

        // Gán chi tiết vào model (nếu class HoaDon có các field này)
        hd.setPhatNhanPhongTre(p1);
        hd.setPhatTraPhongSom(p2);
        hd.setPhatTraPhongTre(p3);

        // Đọc chi tiết giảm giá và tính tổng
        BigDecimal g1 = rs.getBigDecimal("GiamGiaMaGG");
        BigDecimal g2 = rs.getBigDecimal("GiamGiaHangKH");
        BigDecimal tongGiamGia = (g1 != null ? g1 : BigDecimal.ZERO)
                .add(g2 != null ? g2 : BigDecimal.ZERO);
        hd.setTongGiamGia(tongGiamGia);

        hd.setGiamGiaTheoMa(g1);
        hd.setGiamGiaTheoHangKH(g2);

        hd.setTongVAT(rs.getBigDecimal("TongVAT"));

        return hd;
    }

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

    // Trong HoaDonDAO.java

    public boolean themHoaDon(HoaDon hd) throws SQLException {
        // Đảm bảo đủ 15 dấu ? tương ứng với SP vừa ALTER
        String sql = "{CALL sp_ThemHoaDon(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, hd.getMaHD());
            cs.setDate(2, hd.getNgayLap() != null ? Date.valueOf(hd.getNgayLap()) : Date.valueOf(LocalDate.now()));
            cs.setString(3, hd.getPhuongThuc() != null ? hd.getPhuongThuc().toString() : null);
            cs.setBigDecimal(4, hd.getTongTien());
            cs.setString(5, hd.getMaKH());
            cs.setString(6, hd.getMaNV());
            cs.setString(7, hd.getMaGG());
            cs.setString(8, hd.getTrangThai() != null ? hd.getTrangThai().toString() : "Chưa thanh toán");
            cs.setDate(9, hd.getNgayCheckOut() != null ? Date.valueOf(hd.getNgayCheckOut()) : null);

            // Các giá trị mặc định truyền từ Java
            cs.setBigDecimal(10, hd.getPhatNhanPhongTre() != null ? hd.getPhatNhanPhongTre() : BigDecimal.ZERO);
            cs.setBigDecimal(11, hd.getPhatTraPhongSom() != null ? hd.getPhatTraPhongSom() : BigDecimal.ZERO);
            cs.setBigDecimal(12, hd.getPhatTraPhongTre() != null ? hd.getPhatTraPhongTre() : BigDecimal.ZERO);
            cs.setBigDecimal(13, hd.getGiamGiaTheoMa() != null ? hd.getGiamGiaTheoMa() : BigDecimal.ZERO);
            cs.setBigDecimal(14, hd.getGiamGiaTheoHangKH() != null ? hd.getGiamGiaTheoHangKH() : BigDecimal.ZERO);
            cs.setBigDecimal(15, hd.getTongVAT() != null ? hd.getTongVAT() : BigDecimal.ZERO);

            return cs.executeUpdate() > 0;
        }
    }

    public HoaDon timHoaDonTheoMa(String maHD) throws SQLException {
        String sql = "{CALL sp_TimHoaDonTheoMa(?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, maHD);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) return mapResultSetToHoaDon(rs);
            }
        }
        return null;
    }

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