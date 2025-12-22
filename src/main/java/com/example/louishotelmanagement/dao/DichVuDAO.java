package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.DichVu;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DichVuDAO {
    //THEM DICH VU
    public boolean themDichVu(DichVu dichVu) throws Exception {
        String sql ="{CALL sp_ThemDichVu(?,?,?,?,?,?)}";
        try(Connection con = CauHinhDatabase.getConnection();
            CallableStatement stmt = con.prepareCall(sql)){
            stmt.setString(1,dichVu.getMaDV());
            stmt.setString(2,dichVu.getTenDV());
            stmt.setInt(3,dichVu.getSoLuong());
            stmt.setDouble(4,dichVu.getDonGia());
            stmt.setString(5,dichVu.getMoTa());
            stmt.setBoolean(6,dichVu.isConKinhDoanh());
            return  stmt.executeUpdate()>0;

        }
    }
    public DichVu timDichVuTheoMa(String maDV) throws Exception {
        DichVu dichVu = null;
        // Giả sử bạn có stored procedure tên là sp_TimDichVuTheoMa
        String sql = "{CALL sp_TimDichVuTheoMa(?)}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, maDV);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dichVu = new DichVu(
                            rs.getString("maDV"),
                            rs.getString("tenDV"),
                            rs.getInt("soLuong"),
                            rs.getDouble("donGia"),
                            rs.getString("moTa"),
                            rs.getBoolean("conKinhDoanh")
                    );
                }
            }
        }
        return dichVu; // Trả về DichVu hoặc null nếu không tìm thấy
    }
    // CẬP NHẬT DỊCH VỤ
    public boolean capNhatDichVu(DichVu dichVu) throws Exception {
        String sql = "{CALL sp_CapNhatDichVu(?,?,?,?,?,?)}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, dichVu.getMaDV());
            stmt.setString(2, dichVu.getTenDV());
            stmt.setInt(3, dichVu.getSoLuong());
            stmt.setDouble(4, dichVu.getDonGia());
            stmt.setString(5, dichVu.getMoTa());
            stmt.setBoolean(6, dichVu.isConKinhDoanh());
            return stmt.executeUpdate() > 0;
        }
    }
    // XÓA DỊCH VỤ (NGỪNG KINH DOANH)
    public boolean xoaDichVu(String maDV) throws Exception {
        String sql = "{CALL sp_XoaDichVu(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, maDV);
         return stmt.executeUpdate() > 0;
        }
    }
    // LẤY DANH SÁCH DỊCH VỤ
    public ArrayList<DichVu> layTatCaDichVu(boolean b) throws SQLException {
        ArrayList<DichVu> ds = new ArrayList<>();
        String sql = "{CALL sp_LayTatCaDichVu(NULL)}"; // gọi SP không lọc theo trạng thái

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                DichVu dv = new DichVu(
                        rs.getString("maDV"),
                        rs.getString("tenDV"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia"),
                        rs.getString("moTa"),
                        rs.getBoolean("conKinhDoanh")
                );
                ds.add(dv);
            }
        }

        return ds;
    }



    // Lấy mã dịch vụ tiếp theo
    public String layMaDichVuTiepTheo() throws Exception {
        String sql = "{call sp_LayMaDichVuTiepTheo(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.registerOutParameter(1, java.sql.Types.VARCHAR);
            cs.execute();

            return cs.getString(1);
        }
    }
    public boolean capNhatSoLuongTonKho(String maDV, int soLuongMoi) throws Exception {
        String sql = "{CALL sp_CapNhatSoLuongTonKho(?, ?)}";

        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, maDV);
            stmt.setInt(2, soLuongMoi);

            // THAY THẾ: return stmt.executeUpdate() > 0;
            // Bằng:

            // Sử dụng execute() để xử lý cả Result Set và Update Count
            boolean hadResults = stmt.execute();

            // Lặp qua tất cả các kết quả (Result Set và Update Count)
            while (true) {
                if (hadResults) {
                    // Xử lý Result Set nếu cần (ví dụ: stmt.getResultSet().close();)
                } else {
                    if (stmt.getUpdateCount() == -1) {
                        break; // Không còn kết quả nào nữa
                    }
                    // Có thể kiểm tra: return stmt.getUpdateCount() > 0;
                }
                hadResults = stmt.getMoreResults();
            }
            return true; // Giả định thành công nếu không có Exception

            // Hoặc đơn giản hơn, nếu chắc chắn SP chỉ là UPDATE:
            // return stmt.executeUpdate() > 0; // Và sửa SP
        }
    }
}
