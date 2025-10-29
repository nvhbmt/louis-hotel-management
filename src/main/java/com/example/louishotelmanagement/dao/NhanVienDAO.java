package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;
import com.example.louishotelmanagement.model.NhanVien;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class NhanVienDAO {
    public boolean ThemNhanVien(NhanVien nhanVien)throws SQLException {
        String sql = "{call sp_ThemNhanVien(?,?,?,?,?,?)}";
        String maNV = nhanVien.getMaNV();
        String hoTen = nhanVien.getHoTen();
        String soDT = nhanVien.getSoDT();
        String diaChi = nhanVien.getDiaChi();
        String chucVu = nhanVien.getChucVu();
        LocalDate ngaySinh = nhanVien.getNgaySinh();
        try(Connection con = CauHinhDatabase.getConnection();
            CallableStatement cs = con.prepareCall(sql);){
            cs.setString(1,maNV);
            cs.setString(2,hoTen);
            cs.setString(3,soDT);
            cs.setString(4,diaChi);
            cs.setString(5,chucVu);
            // ngay sinh trong sql là date nha
            cs.setDate(6, Date.valueOf(ngaySinh));
            return cs.executeUpdate()>0;
        }
    }
    public ArrayList<NhanVien> layDSNhanVien()throws SQLException{
        ArrayList<NhanVien> dsnhanVien = new ArrayList<>();
        String sql = "{call sp_LayDSNhanVien()}";
        try(Connection con = CauHinhDatabase.getConnection();
        CallableStatement cs = con.prepareCall(sql);) {
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("hoTen"),
                        rs.getString("soDT"),
                        rs.getString("diaChi"),
                        rs.getString("chucVu"),
                        rs.getDate("ngaySinh").toLocalDate()
                );
                dsnhanVien.add(nv);
            }
            return dsnhanVien;
        }
    }
    public boolean SuaNhanVien(NhanVien nhanVien)throws SQLException{
        String sql = "{call sp_CapNhatNhanVien(?,?,?,?,?,?)}";
        String maNV = nhanVien.getMaNV();
        String hoTen = nhanVien.getHoTen();
        String soDT = nhanVien.getSoDT();
        String diaChi = nhanVien.getDiaChi();
        String chucVu = nhanVien.getChucVu();
        LocalDate ngaySinh = nhanVien.getNgaySinh();
        try(Connection con = CauHinhDatabase.getConnection();
            CallableStatement cs = con.prepareCall(sql);){
            cs.setString(1,hoTen);
            cs.setString(2,soDT);
            cs.setString(3,diaChi);
            cs.setString(4,chucVu);
            cs.setDate(5, Date.valueOf(ngaySinh));
            cs.setString(6,maNV);
            return cs.executeUpdate()>0;
        }
    }
    public boolean XoaNhanVien(String maNV)throws SQLException {
        String sql = "{call sp_XoaNhanVien(?)}";
        try(Connection con = CauHinhDatabase.getConnection();
        CallableStatement cs = con.prepareCall(sql);){
            cs.setString(1,maNV);
            return cs.executeUpdate()>0;
        }
    }
    public NhanVien timNhanVienTheoMa(String maNV) throws SQLException {
        String sql = "{call sp_TimNhanVienTheoMa(?)}";
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, maNV);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(
                            rs.getString("maNV"),
                            rs.getString("hoTen"),
                            rs.getString("soDT"),
                            rs.getString("diaChi"),
                            rs.getString("chucVu"),
                            rs.getDate("ngaySinh").toLocalDate()
                    );
                }
            }
        }
        return null;
    }
    public String layMaNVTiepTheo() throws SQLException {
        String sql = "SELECT TOP 1 maNV FROM NhanVien WHERE maNV LIKE 'NV[0-9][0-9][0-9]' ORDER BY maNV DESC";
        String maNVCuoi = "NV000";

        try (Connection con = CauHinhDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                maNVCuoi = rs.getString("maNV");
            }
        }


        int soHienTai = 0;
        try {

            soHienTai = Integer.parseInt(maNVCuoi.substring(2));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.err.println("Lỗi khi phân tích mã NV cuối cùng: " + maNVCuoi + " - " + e.getMessage());

            soHienTai = 0;
        }


        int soMoi = soHienTai + 1;


        return String.format("NV%03d", soMoi);
    }
    public static void main(String[] args) throws SQLException {
        NhanVienDAO nvdao = new NhanVienDAO();

        String maNV = "NV09";
        System.out.println(nvdao.XoaNhanVien(maNV));
    }
}


