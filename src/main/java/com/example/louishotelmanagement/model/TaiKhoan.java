package com.example.louishotelmanagement.model;

public class TaiKhoan {
    private String maTK;
    private NhanVien nhanVien;
    private String tenDangNhap;
    private String matKhauHash;
    private String quyen;
    private boolean trangThai; // true = Hoạt động, false = Khóa

    public TaiKhoan(String maTK, NhanVien nhanVien, String tenDangNhap, String matKhauHash, String quyen, boolean trangThai) {
        this.maTK = maTK;
        this.nhanVien = nhanVien;
        this.tenDangNhap = tenDangNhap;
        this.matKhauHash = matKhauHash;
        this.quyen = quyen;
        this.trangThai = trangThai;
    }
    
    // Constructor với String trangThai để tương thích với database
    public TaiKhoan(String maTK, NhanVien nhanVien, String tenDangNhap, String matKhauHash, String quyen, String trangThai) {
        this.maTK = maTK;
        this.nhanVien = nhanVien;
        this.tenDangNhap = tenDangNhap;
        this.matKhauHash = matKhauHash;
        this.quyen = quyen;
        this.trangThai = "Hoạt động".equals(trangThai);
    }

    public String getMaTK() {
        return maTK;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhauHash() {
        return matKhauHash;
    }

    public void setMatKhauHash(String matKhauHash) {
        this.matKhauHash = matKhauHash;
    }

    public String getQuyen() {
        return quyen;
    }

    public void setQuyen(String quyen) {
        this.quyen = quyen;
    }

    public boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
    
    public void setTrangThai(String trangThai) {
        this.trangThai = "Hoạt động".equals(trangThai);
    }
    
    public String getTrangThaiDisplay() {
        return trangThai ? "Hoạt động" : "Khóa";
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "maTK='" + maTK + '\'' +
                ", maNhanVien=" + nhanVien.getMaNV() +
                ", tenDangNhap='" + tenDangNhap + '\'' +
                ", matKhauHash='" + matKhauHash + '\'' +
                ", quyen='" + quyen + '\'' +
                ", trangThai='" + getTrangThaiDisplay() + '\'' +
                '}';
    }
}
