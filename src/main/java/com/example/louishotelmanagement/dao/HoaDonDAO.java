package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.HoaDon;
import com.example.louishotelmanagement.model.HoaDonChiTietItem;
import com.example.louishotelmanagement.model.PhuongThucThanhToan;
import com.example.louishotelmanagement.model.TrangThaiHoaDon;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    // 🔹 Sinh mã hóa đơn tiếp theo (SỬA: Dùng OutParameter, như trong HoaDonDAO2)
    public String taoMaHoaDonTiepTheo() throws SQLException {
        // Giả định SP sp_TaoMaHoaDonTiepTheo sử dụng output parameter
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


    // 🔹 Thêm hóa đơn mới (SỬA: Gọi SP sp_ThemHoaDon)
    public boolean themHoaDon(HoaDon hd) throws SQLException {
        // Giả định SP sp_ThemHoaDon có 8 tham số
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

    // 🔹 Xóa hóa đơn (SỬA: Gọi SP sp_XoaHoaDon)
    public boolean xoaHoaDon(String maHD) throws SQLException {
        String sql = "{CALL sp_XoaHoaDon(?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, maHD);
            return cs.executeUpdate() > 0;
        }
    }

    // 🔹 Cập nhật hóa đơn (SỬA: Gọi SP sp_SuaHoaDon - Cập nhật toàn diện)
    public boolean capNhatHoaDon(HoaDon hd) throws SQLException {
        // Giả định SP sp_SuaHoaDon có 8 tham số, cập nhật toàn bộ hóa đơn
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

    // 🔹 Cập nhật trạng thái hóa đơn (SỬA: Gọi SP sp_CapNhatTrangThaiHoaDon)
    public boolean capNhatTrangThaiHoaDon(String maHD, String trangThai) throws SQLException {
        String sql = "{CALL sp_CapNhatTrangThaiHoaDon(?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, maHD);
            cs.setString(2, trangThai);
            return cs.executeUpdate() > 0;
        }
    }

    // 🔹 Lấy danh sách tất cả hóa đơn (SỬA: Gọi SP sp_LayDanhSachHoaDon)
    public List<HoaDon> layDanhSachHoaDon() throws SQLException {
        List<HoaDon> ds = new ArrayList<>();
        // Giả định SP này trả về dữ liệu tương tự như HoaDonDAO2
        String sql = "{CALL sp_LayDanhSachHoaDon()}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
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

                // Nếu SP có JOIN dữ liệu, có thể thêm:
                // hd.setSoPhong(rs.getString("soPhong"));
                // hd.setNgayCheckOut(rs.getDate("ngayCheckOut").toLocalDate());

                ds.add(hd);
            }
        }
        return ds;
    }

    // 🔹 Tìm hóa đơn theo mã (Giữ nguyên: Đã dùng SP)
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
                    hd.setNgayLap(ngayLapDate != null ? ngayLapDate.toLocalDate() : null);

                    hd.setPhuongThuc(PhuongThucThanhToan.fromString(rs.getString("phuongThuc")));
                    hd.setTongTien(rs.getBigDecimal("tongTien"));
                    hd.setMaKH(rs.getString("maKH"));
                    hd.setMaNV(rs.getString("maNV"));
                    hd.setMaGG(rs.getString("maGG"));
                    hd.setTrangThai(TrangThaiHoaDon.fromString(rs.getString("trangThai")));

                    return hd;
                }
            }
        }
        return null;
    }

    // 🔹 Cập nhật tổng tiền hóa đơn bằng store procedure (Giữ nguyên: Đã dùng SP)
    public boolean capNhatTongTienHoaDon(String maHD) throws SQLException {
        String sql = "{CALL sp_CapNhatTongTienHoaDon(?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, maHD);
            return cs.executeUpdate() > 0;
        }
    }
    public List<HoaDonChiTietItem> layChiTietHoaDon(String maHD) throws SQLException {
        List<HoaDonChiTietItem> dsChiTiet = new ArrayList<>();
        // Khai báo SQL gọi Stored Procedure
        String sql = "{CALL sp_LayChiTietHoaDonTheoMaHD(?)}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            // 1. Thiết lập tham số đầu vào (maHD)
            cs.setString(1, maHD);

            // 2. Thực thi SP và xử lý ResultSet
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    // Lấy dữ liệu từ ResultSet (tên cột phải khớp với SP)
                    int stt = rs.getInt("STT");
                    String tenChiTiet = rs.getString("TenChiTiet");
                    int soLuong = rs.getInt("SoLuong");
                    // Sử dụng getBigDecimal cho các giá trị tiền tệ để đảm bảo độ chính xác
                    BigDecimal donGia = rs.getBigDecimal("DonGia");
                    BigDecimal thanhTien = rs.getBigDecimal("ThanhTien");

                    // Khởi tạo đối tượng và thêm vào danh sách
                    HoaDonChiTietItem item = new HoaDonChiTietItem(stt, tenChiTiet, soLuong, donGia, thanhTien);
                    dsChiTiet.add(item);
                }
            }
        } catch (SQLException e) {
            // Log lỗi hoặc ném lại ngoại lệ
            throw e;
        }
        return dsChiTiet;
    }

}