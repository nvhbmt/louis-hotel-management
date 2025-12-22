package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.CTHoaDonPhong;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CTHoaDonPhongDAO {

    // ==================================================
    // 1. Thêm chi tiết hóa đơn phòng
    // ==================================================
    public boolean themCTHoaDonPhong(CTHoaDonPhong ct) throws SQLException {
        String sql = "{CALL sp_ThemCTHoaDonPhong(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, ct.getMaHD());
            cs.setString(2, ct.getMaPhieu());
            cs.setString(3, ct.getMaPhong());

            if (ct.getNgayDen() != null)
                cs.setDate(4, Date.valueOf(ct.getNgayDen()));
            else
                cs.setNull(4, Types.DATE);

            if (ct.getNgayDi() != null)
                cs.setDate(5, Date.valueOf(ct.getNgayDi()));
            else
                cs.setNull(5, Types.DATE);

            cs.setBigDecimal(6, ct.getGiaPhong());

            return cs.executeUpdate() > 0;
        }
    }

    // ==================================================
    // 2. Lấy DS CTHDP theo mã phiếu (chỉ phòng chưa hủy)
    // ==================================================
    public ArrayList<CTHoaDonPhong> getCTHoaDonPhongTheoMaPhieu(String maPhieu) throws SQLException {
        ArrayList<CTHoaDonPhong> list = new ArrayList<>();
        String sql = "{CALL sp_layCTHoaDonPhongByMaPhieu(?)}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maPhieu);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                CTHoaDonPhong ct = mapCTHoaDonPhong(rs);
                list.add(ct);
            }
        }
        return list;
    }

    // ==================================================
    // 3. Cập nhật ngày đến thực tế
    // ==================================================
    public boolean capNhatNgayDenThucTe(String maHD, String maPhong, LocalDate ngayDen) throws SQLException {
        String sql = "{CALL sp_CapNhatNgayDenThucTe(?, ?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);
            cs.setString(2, maPhong);
            cs.setDate(3, Date.valueOf(ngayDen));

            return cs.executeUpdate() > 0;
        }
    }

    // ==================================================
    // 4. Cập nhật ngày đi thực tế
    // ==================================================
    public boolean capNhatNgayDiThucTe(String maHD, String maPhong, LocalDate ngayDi) throws SQLException {
        String sql = "{CALL sp_CapNhatNgayDiThucTe(?, ?, ?)}";
        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);
            cs.setString(2, maPhong);
            cs.setDate(3, Date.valueOf(ngayDi));

            return cs.executeUpdate() > 0;
        }
    }

    // ==================================================
    // 5. Cập nhật đổi phòng + giá
    // ==================================================
    public boolean capNhatMaPhongVaGia(String maPhieu, String maPhongCu,
                                       String maPhongMoi, BigDecimal giaPhongMoi) throws SQLException {

        String sql = "{CALL sp_CapNhatMaPhongVaGia(?, ?, ?, ?)}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maPhieu);
            cs.setString(2, maPhongCu);
            cs.setString(3, maPhongMoi);
            cs.setBigDecimal(4, giaPhongMoi);

            return cs.executeUpdate() > 0;
        }
    }

    // ==================================================
    // 6. Lấy DS CTHDP theo mã phòng
    // ==================================================
    public ArrayList<CTHoaDonPhong> getDSCTHoaDonPhongTheoMaPhong(String maPhong) throws SQLException {
        ArrayList<CTHoaDonPhong> list = new ArrayList<>();
        String sql = "{CALL sp_GetCTHoaDonPhongTheoMaPhong(?)}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maPhong);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                list.add(mapCTHoaDonPhong(rs));
            }
        }
        return list;
    }

    // ==================================================
    // 7. Lấy DS CTHDP theo mã hóa đơn
    // ==================================================
    public List<CTHoaDonPhong> getCTHoaDonPhongTheoMaHD(String maHD) throws SQLException {
        List<CTHoaDonPhong> list = new ArrayList<>();
        String sql = "{CALL sp_GetCTHoaDonPhongTheoMaHD(?)}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                list.add(mapCTHoaDonPhong(rs));
            }
        }
        return list;
    }

    // ==================================================
    // 8. Tính tổng tiền phòng theo hóa đơn
    // ==================================================
    public double tinhTongTienPhongTheoHD(String maHD) throws SQLException {
        String sql = "{CALL sp_TinhTongTienPhongTheoHD(?)}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return rs.getDouble("TongTienPhong");
            }
        }
        return 0;
    }

    // ==================================================
    // 10. Hủy phòng khỏi phiếu (xóa mềm)
    // ==================================================
    public boolean huyCTHoaDonPhong(String maHD, String maPhong) throws SQLException {
        String sql = "{CALL sp_HuyPhongKhoiPhieu(?, ?, ?)}";

        try (Connection conn = CauHinhDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHD);
            cs.setString(2, maPhong);
            cs.registerOutParameter(3, Types.INTEGER);

            cs.execute();

            return cs.getInt(3) > 0;
        }
    }


    // ==================================================
    // 🔥 Hàm mapping tập trung – tránh lỗi & trùng code
    // ==================================================
    private CTHoaDonPhong mapCTHoaDonPhong(ResultSet rs) throws SQLException {
        CTHoaDonPhong ct = new CTHoaDonPhong(
                rs.getString("maHD"),
                rs.getString("maPhieu"),
                rs.getString("maPhong"),
                rs.getDate("ngayDen") != null ? rs.getDate("ngayDen").toLocalDate() : null,
                rs.getDate("ngayDi") != null ? rs.getDate("ngayDi").toLocalDate() : null,
                rs.getBigDecimal("giaPhong")
        );

        // mapping xóa mềm (nếu có)
        try {
            ct.setDaHuy(rs.getBoolean("daHuy"));
            Date ngayHuy = rs.getDate("ngayHuy");
            if (ngayHuy != null) {
                ct.setNgayHuy(ngayHuy.toLocalDate());
            }
        } catch (SQLException ignored) {
            // SP không trả daHuy thì bỏ qua
        }

        return ct;
    }
}
