package com.example.louishotelmanagement.dao;
import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.NhanVien;
import com.example.louishotelmanagement.model.TaiKhoan;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaiKhoanDAO {
    // 1. Lấy danh sách tất cả tài khoản
    public ArrayList<TaiKhoan> layDSTaiKhoan() throws SQLException {
        ArrayList<TaiKhoan> ds = new ArrayList<>();
        String sql = "{call sp_LayDSTaiKhoan()}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan(
                        rs.getString("maTK"),
                        new NhanVien(rs.getString("maNV")), // chỉ gán mã NV
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhauHash"),
                        rs.getString("quyen"),
                        rs.getString("trangThai")
                );
                ds.add(tk);
            }
        }
        return ds;
    }

    // 2. Thêm tài khoản
    public boolean themTaiKhoan(String maTK,NhanVien nv,String tenDangNhap,String matkhau,String quyen,String trangThai) throws SQLException {
        String sql = "{call sp_ThemTaiKhoan(?,?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, maTK);
            cs.setString(2, nv.getMaNV());
            cs.setString(3, tenDangNhap);
            cs.setString(4, matkhau);
            cs.setString(5, quyen);
            cs.setString(6, trangThai);
            return cs.executeUpdate() > 0;
        }
    }

    // 3. Cập nhật tài khoản
    public boolean capNhatTaiKhoan(String maTK,NhanVien nv,String tenDangNhap,String matkhau,String quyen,String trangThai) throws SQLException {
        String sql = "{call sp_CapNhatTaiKhoan(?,?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, maTK);
            cs.setString(2, nv.getMaNV());
            cs.setString(3, tenDangNhap);
            cs.setString(4, matkhau);
            cs.setString(5, quyen);
            cs.setString(6, trangThai);
            return cs.executeUpdate() > 0;
        }
    }

    // 4. Xóa tài khoản
    public boolean xoaTaiKhoan(String maTK) throws SQLException {
        String sql = "{call sp_XoaTaiKhoan(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, maTK);
            return cs.executeUpdate() > 0;
        }
    }

    // 5. Tìm tài khoản theo mã
    public TaiKhoan timTaiKhoanTheoMa(String maTK) throws SQLException {
        String sql = "{call sp_TimTaiKhoanTheoMa(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, maTK);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                return new TaiKhoan(
                        rs.getString("maTK"),
                        new NhanVien(rs.getString("maNV")),
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhauHash"),
                        rs.getString("quyen"),
                        rs.getString("trangThai")
                );
            }
        }
        return null;
    }

    // 6. Đăng nhập (tìm theo tên đăng nhập + mật khẩu hash)
    public TaiKhoan dangNhap(String tenDangNhap, String matKhauHash) throws SQLException {
        String sql = "{call sp_DangNhapTaiKhoan(?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, tenDangNhap);
            cs.setString(2, matKhauHash);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                return new TaiKhoan(
                        rs.getString("maTK"),
                        new NhanVien(rs.getString("maNV")),
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhauHash"),
                        rs.getString("quyen"),
                        rs.getString("trangThai")
                );
            }
        }
        return null;
    }
}
