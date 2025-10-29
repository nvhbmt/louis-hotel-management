package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO2 {

    public List<HoaDon> layDanhSachHoaDon() throws SQLException {
        List<HoaDon> ds = new ArrayList<>();
        String sql = "{CALL sp_LayDanhSachHoaDon()}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                try {
                    HoaDon hd = new HoaDon();

                    String maHD = rs.getString("maHD");
                    hd.setMaHD(maHD);

                    Date ngayLapDate = rs.getDate("ngayLap");
                    if (ngayLapDate != null) {
                        hd.setNgayLap(ngayLapDate.toLocalDate());
                    }

                    String phuongThucStr = rs.getString("phuongThuc");
                    if (phuongThucStr != null && !phuongThucStr.isEmpty()) {
                        hd.setPhuongThuc(PhuongThucThanhToan.fromString(phuongThucStr));
                    }

                    BigDecimal tongTien = rs.getBigDecimal("tongTien");
                    hd.setTongTien(tongTien);

                    hd.setMaKH(rs.getString("maKH"));
                    hd.setMaNV(rs.getString("maNV"));
                    hd.setMaGG(rs.getString("maGG"));

                    String trangThaiStr = rs.getString("trangThai");
                    if (trangThaiStr != null && !trangThaiStr.isEmpty()) {
                        hd.setTrangThai(TrangThaiHoaDon.fromString(trangThaiStr));
                    }

                    KhachHang kh = new KhachHang();
                    kh.setHoTen(rs.getString("hoTen"));
                    kh.setSoDT(rs.getString("soDT"));
                    kh.setDiaChi(rs.getString("diaChi"));
                    hd.setKhachHang(kh);

                    String soPhong = rs.getString("soPhong");
                    hd.setSoPhong(soPhong);

                    Date ngayDi = rs.getDate("ngayCheckOut");
                    if (ngayDi != null) {
                        hd.setNgayCheckOut(ngayDi.toLocalDate());
                    }

                    ds.add(hd);

                } catch (Exception e_inner) {

                }
            }
        } catch (SQLException e_outer) {
            throw e_outer;
        } catch (Exception e_other) {

        }
        return ds;
    }

    public List<HoaDonChiTietItem> layChiTietHoaDon(String maHD) throws SQLException {
        List<HoaDonChiTietItem> dsChiTiet = new ArrayList<>();
        String sql = "{CALL sp_LayChiTietHoaDonTheoMaHD(?)}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    int stt = rs.getInt("STT");
                    String tenChiTiet = rs.getString("TenChiTiet");
                    int soLuong = rs.getInt("SoLuong");
                    BigDecimal donGia = rs.getBigDecimal("DonGia");
                    BigDecimal thanhTien = rs.getBigDecimal("ThanhTien");

                    dsChiTiet.add(new HoaDonChiTietItem(stt, tenChiTiet, soLuong, donGia, thanhTien));
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return dsChiTiet;
    }


    public String taoMaHoaDonTiepTheo() throws SQLException {
        String sql = "{CALL sp_TaoMaHoaDonTiepTheo(?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.registerOutParameter(1, Types.NVARCHAR);
            cs.execute();
            String nextId = cs.getString(1);
            if (nextId == null || nextId.isEmpty()) {
                throw new SQLException("Stored procedure sp_TaoMaHoaDonTiepTheo không trả về giá trị.");
            }
            return nextId;
        }
    }

    public boolean themHoaDon(HoaDon hd) throws SQLException {
        String sql = "{CALL sp_ThemHoaDon(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, hd.getMaHD());

            if (hd.getNgayLap() != null) {
                cs.setDate(2, Date.valueOf(hd.getNgayLap()));
            } else {
                cs.setDate(2, Date.valueOf(LocalDate.now()));
            }

            if (hd.getPhuongThuc() != null) {
                cs.setString(3, hd.getPhuongThuc().toString());
            } else {
                cs.setNull(3, Types.NVARCHAR);
            }

            cs.setBigDecimal(4, hd.getTongTien() != null ? hd.getTongTien() : BigDecimal.ZERO);
            cs.setString(5, hd.getMaKH());
            cs.setString(6, hd.getMaNV());

            if (hd.getMaGG() != null && !hd.getMaGG().isEmpty()) {
                cs.setString(7, hd.getMaGG());
            } else {
                cs.setNull(7, Types.NVARCHAR);
            }

            if (hd.getTrangThai() != null) {
                cs.setString(8, hd.getTrangThai().toString());
            } else {
                cs.setString(8, "Chưa thanh toán");
            }

            return cs.executeUpdate() > 0;
        }
    }

    public boolean xoaHoaDon(String maHD) throws SQLException {
        String sql = "{CALL sp_XoaHoaDon(?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, maHD);
            return cs.executeUpdate() > 0;
        }
    }

    public boolean capNhatHoaDon(HoaDon hd) throws SQLException {
        String sql = "{CALL sp_SuaHoaDon(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, hd.getMaHD());

            if (hd.getNgayLap() != null) {
                cs.setDate(2, Date.valueOf(hd.getNgayLap()));
            } else {
                cs.setNull(2, Types.DATE);
            }

            if (hd.getPhuongThuc() != null) {
                cs.setString(3, hd.getPhuongThuc().toString());
            } else {
                cs.setNull(3, Types.NVARCHAR);
            }

            cs.setBigDecimal(4, hd.getTongTien());
            cs.setString(5, hd.getMaKH());
            cs.setString(6, hd.getMaNV());

            if (hd.getMaGG() != null && !hd.getMaGG().isEmpty()) {
                cs.setString(7, hd.getMaGG());
            } else {
                cs.setNull(7, Types.NVARCHAR);
            }

            if (hd.getTrangThai() != null) {
                cs.setString(8, hd.getTrangThai().toString());
            } else {
                cs.setNull(8, Types.NVARCHAR);
            }

            return cs.executeUpdate() > 0;
        }
    }

    public boolean capNhatTrangThaiHoaDon(String maHD, String trangThai) throws SQLException {
        String sql = "{CALL sp_CapNhatTrangThaiHoaDon(?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, maHD);
            cs.setString(2, trangThai);
            return cs.executeUpdate() > 0;
        }
    }

    public HoaDon timHoaDonTheoMa(String maHD) throws SQLException {
        String sql = "{CALL sp_TimHoaDonTheoMa(?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMaHD(rs.getString("maHD"));

                    Date ngayLapDate = rs.getDate("ngayLap");
                    if (ngayLapDate != null) {
                        hd.setNgayLap(ngayLapDate.toLocalDate());
                    }

                    String phuongThucStr = rs.getString("phuongThuc");
                    if (phuongThucStr != null) {
                        hd.setPhuongThuc(PhuongThucThanhToan.fromString(phuongThucStr));
                    }

                    hd.setTongTien(rs.getBigDecimal("tongTien"));
                    hd.setMaKH(rs.getString("maKH"));
                    hd.setMaNV(rs.getString("maNV"));
                    hd.setMaGG(rs.getString("maGG"));

                    String trangThaiStr = rs.getString("trangThai");
                    if (trangThaiStr != null) {
                        hd.setTrangThai(TrangThaiHoaDon.fromString(trangThaiStr));
                    }

                    return hd;
                }
            }
        }
        return null;
    }
}