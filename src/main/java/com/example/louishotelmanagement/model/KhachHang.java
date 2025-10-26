package com.example.louishotelmanagement.model;

import java.time.LocalDate;

public class KhachHang {
    private String maKH;
    private String hoTen;
    private String soDT;
    private String email;
    private String diaChi;
    private LocalDate ngaySinh;
    private String ghiChu;
    private String CCCD;
    private String hangKhach;
    private TrangThaiKhachHang trangThai; // enum riêng đã có

    // Constructor mặc định
    public KhachHang() {
        this.maKH = "";
        this.hoTen = "";
        this.soDT = "";
        this.email = "";
        this.diaChi = "";
        this.ngaySinh = null;
        this.ghiChu = "";
        this.CCCD = "";
        this.hangKhach = "";
        this.trangThai = TrangThaiKhachHang.DANG_LUU_TRU; // mặc định
    }

    // Constructor đầy đủ
    public KhachHang(String maKH, String hoTen, String soDT, String email, String diaChi,
                     LocalDate ngaySinh, String ghiChu, String CCCD, String hangKhach,
                     TrangThaiKhachHang trangThai) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.soDT = soDT;
        this.email = email;
        this.diaChi = diaChi;
        this.ngaySinh = ngaySinh;
        this.ghiChu = ghiChu;
        this.CCCD = CCCD;
        this.hangKhach = hangKhach;
        this.trangThai = trangThai != null ? trangThai : TrangThaiKhachHang.DANG_LUU_TRU;
    }

    // Constructor chỉ với mã khách hàng
    public KhachHang(String maKH) {
        this();
        this.maKH = maKH;
    }

    // Getter & Setter
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSoDT() { return soDT; }
    public void setSoDT(String soDT) { this.soDT = soDT; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getCCCD() { return CCCD; }
    public void setCCCD(String CCCD) { this.CCCD = CCCD; }

    public String getHangKhach() { return hangKhach; }
    public void setHangKhach(String hangKhach) { this.hangKhach = hangKhach; }

    public TrangThaiKhachHang getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiKhachHang trangThai) {
        this.trangThai = trangThai != null ? trangThai : TrangThaiKhachHang.DANG_LUU_TRU;
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", soDT='" + soDT + '\'' +
                ", email='" + email + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", ghiChu='" + ghiChu + '\'' +
                ", CCCD='" + CCCD + '\'' +
                ", hangKhach='" + hangKhach + '\'' +
                ", trangThai=" + (trangThai != null ? trangThai : "null") +
                '}';
    }
}
