    package com.example.louishotelmanagement.dao;

    import com.example.louishotelmanagement.config.CauHinhDatabase;
    import com.example.louishotelmanagement.model.CTHoaDonPhong;

    import java.math.BigDecimal;
    import java.sql.*;
    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.List;

    public class CTHoaDonPhongDAO {

        public boolean themCTHoaDonPhong(CTHoaDonPhong ct) throws SQLException {
            String sql = "{CALL sp_ThemCTHoaDonPhong(?, ?, ?, ?, ?, ?)}";
            try (Connection conn = CauHinhDatabase.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {
                cs.setString(1, ct.getMaHD());
                cs.setString(2, ct.getMaPhieu());
                cs.setString(3, ct.getMaPhong());
                cs.setDate(4, Date.valueOf(ct.getNgayDen()));
                cs.setDate(5, Date.valueOf(ct.getNgayDi()));
                cs.setBigDecimal(6, ct.getGiaPhong());
                return cs.executeUpdate() > 0;
            }
        }
        public ArrayList<CTHoaDonPhong> getCTHoaDonPhongTheoMaPhieu(String maPhieu) throws SQLException {
            ArrayList<CTHoaDonPhong> list = new ArrayList<>();
            // Sử dụng Stored Procedure tương tự như các hàm khác
            String sql = "{CALL SP_SelectCTHoaDonPhongByMaPhieu(?)}";

            try (Connection conn = CauHinhDatabase.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {

                cs.setString(1, maPhieu);

                // Thực thi Stored Procedure và nhận ResultSet
                ResultSet rs = cs.executeQuery();

                while (rs.next()) {
                    // Ánh xạ dữ liệu từ ResultSet vào đối tượng CTHoaDonPhong.
                    // Lưu ý: Dùng constructor của CTHoaDonPhong cần 6 tham số.
                    CTHoaDonPhong ct = new CTHoaDonPhong(
                            rs.getString("maHD"),
                            rs.getString("maPhieu"), // maPhieu này trùng với maPhieu đầu vào
                            rs.getString("maPhong"),
                            rs.getDate("ngayDen") != null ? rs.getDate("ngayDen").toLocalDate() : null,
                            rs.getDate("ngayDi") != null ? rs.getDate("ngayDi").toLocalDate() : null,
                            rs.getBigDecimal("giaPhong")
                    );
                    list.add(ct);
                }
            }
            return list;
        }

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

        public boolean capNhatMaPhongVaGia(String maPhieu, String maPhongCu,
                                           String maPhongMoi, BigDecimal giaPhongMoi) throws SQLException {

            // Stored Procedure sẽ nhận 4 tham số: MaPhieu, MaPhongCu, MaPhongMoi, GiaPhongMoi
            String sql = "{CALL sp_CapNhatMaPhongVaGia(?, ?, ?, ?)}";

            try (Connection conn = CauHinhDatabase.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {

                // 1. Tham số dùng để định danh bản ghi CTHoaDonPhong (WHERE clause)
                cs.setString(1, maPhieu);
                cs.setString(2, maPhongCu);

                // 2. Tham số dùng để cập nhật giá trị (SET clause)
                cs.setString(3, maPhongMoi);
                cs.setBigDecimal(4, giaPhongMoi);

                return cs.executeUpdate() > 0;
            }
        }

        public ArrayList<CTHoaDonPhong> getDSCTHoaDonPhongTheoMaPhong(String maPhong) throws SQLException {
            ArrayList<CTHoaDonPhong> list = new ArrayList<>();
            // Định dạng gọi Stored Procedure: {CALL Tên_SP(?)}
            String sql = "{CALL sp_GetCTHoaDonPhongTheoMaPhong(?)}";

            // Sử dụng CauHinhDatabase.getConnection() và CallableStatement
            try (Connection conn = CauHinhDatabase.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {

                cs.setString(1, maPhong);

                // Thực thi Stored Procedure và nhận ResultSet
                ResultSet rs = cs.executeQuery();

                while (rs.next()) {
                    // Ánh xạ dữ liệu vào đối tượng CTHoaDonPhong bằng constructor 6 tham số
                    CTHoaDonPhong ct = new CTHoaDonPhong(
                            rs.getString("maHD"),
                            rs.getString("maPhieu"),
                            rs.getString("maPhong"),
                            rs.getDate("ngayDen") != null ? rs.getDate("ngayDen").toLocalDate() : null,
                            rs.getDate("ngayDi") != null ? rs.getDate("ngayDi").toLocalDate() : null,
                            rs.getBigDecimal("giaPhong")
                    );
                    list.add(ct);
                }
            }
            return list;
        }

        public List<CTHoaDonPhong> getCTHoaDonPhongTheoMaHD(String maHD) throws SQLException {
            List<CTHoaDonPhong> list = new ArrayList<>();
            String sql = "{CALL sp_GetCTHoaDonPhongTheoMaHD(?)}";
            try (Connection conn = CauHinhDatabase.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {
                cs.setString(1, maHD);
                ResultSet rs = cs.executeQuery();
                while (rs.next()) {
                    CTHoaDonPhong ct = new CTHoaDonPhong(
                            rs.getString("maHD"),
                            rs.getString("maPhieu"),
                            rs.getString("maPhong"),
                            rs.getDate("ngayDen") != null ? rs.getDate("ngayDen").toLocalDate() : null,
                            rs.getDate("ngayDi") != null ? rs.getDate("ngayDi").toLocalDate() : null,
                            rs.getBigDecimal("giaPhong")
                    );
                    list.add(ct);
                }
            }
            return list;
        }

        public double tinhTongTienPhongTheoHD(String maHD) throws SQLException {
            String sql = "{CALL sp_TinhTongTienPhongTheoHD(?)}";
            try (Connection conn = CauHinhDatabase.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {
                cs.setString(1, maHD);
                ResultSet rs = cs.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("TongTien");
                }
            }
            return 0;
        }

        public boolean xoaCTHoaDonPhong(String maHD, String maPhong) throws SQLException {
            String sql = "{CALL sp_XoaCTHoaDonPhong(?, ?)}";
            try (Connection conn = CauHinhDatabase.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {
                cs.setString(1, maHD);
                cs.setString(2, maPhong);
                return cs.executeUpdate() > 0;
            }
        }
    }
