package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.NhanVien;
import com.example.louishotelmanagement.model.TaiKhoan;

import java.sql.*;
import java.util.ArrayList;

public class TaiKhoanDAO {
    // 1. Lấy danh sách tất cả tài khoản
    public ArrayList<TaiKhoan> layDSTaiKhoan() throws SQLException {
        ArrayList<TaiKhoan> ds = new ArrayList<>();
        String sql = "{call sp_LayDSTaiKhoan()}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            ResultSet rs = cs.executeQuery();
            NhanVienDAO nhanVienDAO = new NhanVienDAO();

            while (rs.next()) {
                // Load đầy đủ thông tin nhân viên
                NhanVien nhanVien = nhanVienDAO.timNhanVienTheoMa(rs.getString("maNV"));
                TaiKhoan tk = new TaiKhoan(
                        rs.getString("maTK"),
                        nhanVien != null ? nhanVien : new NhanVien(rs.getString("maNV")),
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhauHash"),
                        rs.getString("quyen"),
                        rs.getBoolean("trangThai")
                );
                ds.add(tk);
            }
        }
        return ds;
    }

    public String layMaTKTiepTheo() throws SQLException {
        String sql = "{call sp_LayMaTKTiepTheo(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.registerOutParameter(1, Types.NVARCHAR);
            cs.execute();
            
            String maTKTiepTheo = cs.getString(1);
            return maTKTiepTheo;
        }
    }

    // 2. Thêm tài khoản
    public boolean themTaiKhoan(String maTK, NhanVien nv, String tenDangNhap, String matkhau, String quyen, boolean trangThai) throws SQLException {
        String sql = "{call sp_ThemTaiKhoan(?,?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, maTK);
            cs.setString(2, nv.getMaNV());
            cs.setString(3, tenDangNhap);
            cs.setString(4, matkhau);
            cs.setString(5, quyen);
            cs.setBoolean(6, trangThai);
            return cs.executeUpdate() > 0;
        }
    }

    // 2.1. Thêm tài khoản với String trangThai (để tương thích)
    public boolean themTaiKhoan(String maTK, NhanVien nv, String tenDangNhap, String matkhau, String quyen, String trangThai) throws SQLException {
        boolean trangThaiBool = "Hoạt động".equals(trangThai);
        return themTaiKhoan(maTK, nv, tenDangNhap, matkhau, quyen, trangThaiBool);
    }

    // 3. Cập nhật tài khoản
    public boolean capNhatTaiKhoan(String maTK, NhanVien nv, String tenDangNhap, String matkhau, String quyen, boolean trangThai) throws SQLException {
        String sql = "{call sp_CapNhatTaiKhoan(?,?,?,?,?,?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, maTK);
            cs.setString(2, nv.getMaNV());
            cs.setString(3, tenDangNhap);
            cs.setString(4, matkhau);
            cs.setString(5, quyen);
            cs.setBoolean(6, trangThai);
            return cs.executeUpdate() > 0;
        }
    }

    // 3.1. Cập nhật tài khoản với String trangThai (để tương thích)
    public boolean capNhatTaiKhoan(String maTK, NhanVien nv, String tenDangNhap, String matkhau, String quyen, String trangThai) throws SQLException {
        boolean trangThaiBool = "Hoạt động".equals(trangThai);
        return capNhatTaiKhoan(maTK, nv, tenDangNhap, matkhau, quyen, trangThaiBool);
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
                // Load đầy đủ thông tin nhân viên
                NhanVienDAO nhanVienDAO = new NhanVienDAO();
                NhanVien nhanVien = nhanVienDAO.timNhanVienTheoMa(rs.getString("maNV"));

                return new TaiKhoan(
                        rs.getString("maTK"),
                        nhanVien != null ? nhanVien : new NhanVien(rs.getString("maNV")),
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhauHash"),
                        rs.getString("quyen"),
                        rs.getBoolean("trangThai")
                );
            }
        }
        return null;
    }

    // 5.1. Tìm tài khoản theo tên đăng nhập
    public TaiKhoan timTaiKhoanTheoTenDangNhap(String tenDangNhap) throws SQLException {
        String sql = "{call sp_TimTaiKhoanTheoTenDangNhap(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, tenDangNhap);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                // Load đầy đủ thông tin nhân viên
                NhanVienDAO nhanVienDAO = new NhanVienDAO();
                NhanVien nhanVien = nhanVienDAO.timNhanVienTheoMa(rs.getString("maNV"));

                return new TaiKhoan(
                        rs.getString("maTK"),
                        nhanVien != null ? nhanVien : new NhanVien(rs.getString("maNV")),
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhauHash"),
                        rs.getString("quyen"),
                        rs.getBoolean("trangThai")
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
                        rs.getBoolean("trangThai")
                );
            }
        }
        return null;
    }
}
