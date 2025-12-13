package com.example.louishotelmanagement.dao;

import com.example.louishotelmanagement.config.CauHinhDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ThongKeDAO {
    
    // Lấy doanh thu theo ngày
    public Map<String, Double> layDoanhThuTheoNgay(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        Map<String, Double> doanhThu = new HashMap<>();
        String sql = "{call sp_LayDoanhThuTheoNgay(?, ?)}";
        
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setDate(1, Date.valueOf(tuNgay));
            cs.setDate(2, Date.valueOf(denNgay));
            ResultSet rs = cs.executeQuery();
            
            while (rs.next()) {
                String ngay = rs.getString("ngay");
                Double tongTien = rs.getDouble("tongTien");
                doanhThu.put(ngay, tongTien);
            }
        }
        return doanhThu;
    }
    
    // Lấy doanh thu theo tuần
    public Map<String, Double> layDoanhThuTheoTuan(int nam) throws SQLException {
        Map<String, Double> doanhThu = new HashMap<>();
        String sql = "{call sp_LayDoanhThuTheoTuan(?)}";
        
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, nam);
            ResultSet rs = cs.executeQuery();
            
            while (rs.next()) {
                String tuan = rs.getString("tuan");
                Double tongTien = rs.getDouble("tongTien");
                doanhThu.put(tuan, tongTien);
            }
        }
        return doanhThu;
    }
    
    // Lấy doanh thu theo tháng
    public Map<String, Double> layDoanhThuTheoThang(int nam) throws SQLException {
        Map<String, Double> doanhThu = new HashMap<>();
        String sql = "{call sp_LayDoanhThuTheoThang(?)}";
        
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, nam);
            ResultSet rs = cs.executeQuery();
            
            while (rs.next()) {
                String thang = rs.getString("thang");
                Double tongTien = rs.getDouble("tongTien");
                doanhThu.put(thang, tongTien);
            }
        }
        return doanhThu;
    }
    
    // Lấy tổng doanh thu trong khoảng thời gian
    public Double layTongDoanhThu(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        String sql = "{call sp_LayTongDoanhThu(?, ?)}";
        
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setDate(1, Date.valueOf(tuNgay));
            cs.setDate(2, Date.valueOf(denNgay));
            ResultSet rs = cs.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("tongDoanhThu");
            }
        }
        return 0.0;
    }
    
    // Lấy doanh thu theo loại phòng
    public Map<String, Double> layDoanhThuTheoLoaiPhong(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        Map<String, Double> doanhThu = new HashMap<>();
        String sql = "{call sp_LayDoanhThuTheoLoaiPhong(?, ?)}";
        
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setDate(1, Date.valueOf(tuNgay));
            cs.setDate(2, Date.valueOf(denNgay));
            ResultSet rs = cs.executeQuery();
            
            while (rs.next()) {
                String loaiPhong = rs.getString("loaiPhong");
                Double tongTien = rs.getDouble("tongTien");
                doanhThu.put(loaiPhong, tongTien);
            }
        }
        return doanhThu;
    }
    
    // Lấy số lượng đặt phòng theo thời gian
    public Map<String, Integer> laySoLuongDatPhongTheoThang(int nam) throws SQLException {
        Map<String, Integer> soLuong = new HashMap<>();
        String sql = "{call sp_LaySoLuongDatPhongTheoThang(?)}";
        
        try (Connection con = CauHinhDatabase.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, nam);
            ResultSet rs = cs.executeQuery();
            
            while (rs.next()) {
                String thang = rs.getString("thang");
                Integer soLuongDat = rs.getInt("soLuong");
                soLuong.put(thang, soLuongDat);
            }
        }
        return soLuong;
    }
}
