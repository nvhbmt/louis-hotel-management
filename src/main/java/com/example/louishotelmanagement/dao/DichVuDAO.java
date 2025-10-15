package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.DichVu;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
    public List<DichVu> layTatCaDichVu(Boolean chiLayConKinhDoanh) throws Exception {
        List<DichVu> list = new ArrayList<>();
        String sql = "{CALL sp_LayTatCaDichVu(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement stmt = con.prepareCall(sql)) {
            if (chiLayConKinhDoanh == null) {
                stmt.setNull(1, java.sql.Types.BIT);
            } else {
                stmt.setBoolean(1, chiLayConKinhDoanh);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DichVu dv = new DichVu(
                            rs.getString("maDV"),
                            rs.getString("tenDV"),
                            rs.getInt("soLuong"),
                            rs.getDouble("donGia"),
                            rs.getString("moTa"),
                            rs.getBoolean("conKinhDoanh")
                    );
                    list.add(dv);
                }
            }
        }
        return list;
    }
}
