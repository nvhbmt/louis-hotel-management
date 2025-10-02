package com.example.louishotelmanagement.model;

public class TaiKhoan {
    private String maTK;
    private NhanVien nhanVien;
    private String tenDangNhap;
    private String matKhauHash;
    private String quyen;
    private String trangThai;

    public TaiKhoan(String maTK, NhanVien nhanVien, String tenDangNhap, String matKhauHash, String quyen, String trangThai) {
        this.maTK = maTK;
        this.nhanVien = nhanVien;
        this.tenDangNhap = tenDangNhap;
        this.matKhauHash = matKhauHash;
        this.quyen = quyen;
        this.trangThai = trangThai;
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

    public void setMatKhauHash(String matKhauHash) {
        this.matKhauHash = matKhauHash;
    }

    public String getQuyen() {
        return quyen;
    }

    public void setQuyen(String quyen) {
        this.quyen = quyen;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
