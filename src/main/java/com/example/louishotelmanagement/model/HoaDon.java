package com.example.louishotelmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HoaDon {
    private String maHD;
    private LocalDate ngayLap;
    private PhuongThucThanhToan phuongThuc;
    private TrangThaiHoaDon trangThai;
    private BigDecimal tongTien;
    private String maKH;
    private String maNV;
    private String maKM;
    private KhachHang khachHang;
    private String soPhong;
    private LocalDate ngayDi; // Đã đổi tên từ ngayDi

    // Tiền phạt & Giảm giá (Khởi tạo mặc định để tránh Null)
    private BigDecimal phatNhanPhongTre = BigDecimal.ZERO;
    private BigDecimal phatTraPhongSom = BigDecimal.ZERO;
    private BigDecimal phatTraPhongTre = BigDecimal.ZERO;
    private BigDecimal giamGiaTheoMa = BigDecimal.ZERO;
    private BigDecimal giamGiaTheoHangKH = BigDecimal.ZERO;
    private BigDecimal tongVAT = BigDecimal.ZERO;

    // =======================================================
    // ====== TỔNG TIỀN (DB ĐANG DÙNG) =======================
    // =======================================================
    private BigDecimal tienPhat;        // Tổng tiền phạt
    private BigDecimal tongGiamGia;     // Tổng giảm giá// Tổng VAT

    // =======================================================
    // ====== CHI TIẾT TIỀN PHẠT (UI / TƯƠNG LAI) ============
    // =======================================================// Trả phòng trễ

    // =======================================================
    // ====== CHI TIẾT GIẢM GIÁ (UI / TƯƠNG LAI) ==============
    // =======================================================
    // Hạng khách hàng

    // =======================================================
    // ================= CONSTRUCTOR =========================
    // =======================================================

    public HoaDon() {
    }

    public HoaDon(String maHD) {
        this.maHD = maHD;
    }

    public HoaDon(String maHD, LocalDate ngayLap, PhuongThucThanhToan phuongThuc,
                  TrangThaiHoaDon trangThai, BigDecimal tongTien,
                  String maKH, String maNV, String maKM) {
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.phuongThuc = phuongThuc;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.maKH = maKH;
        this.maNV = maNV;
        this.maKM = maKM;
        this.khachHang = new KhachHang();
    }

    // =======================================================
    // ================== GETTER / SETTER ====================
    // =======================================================

    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }

    public LocalDate getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDate ngayLap) { this.ngayLap = ngayLap; }

    public PhuongThucThanhToan getPhuongThuc() { return phuongThuc; }
    public void setPhuongThuc(PhuongThucThanhToan phuongThuc) { this.phuongThuc = phuongThuc; }

    public TrangThaiHoaDon getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiHoaDon trangThai) { this.trangThai = trangThai; }

    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public LocalDate getngayDi() { return ngayDi; }
    public void setngayDi(LocalDate ngayDi) { this.ngayDi = ngayDi; }

    // ===== TỔNG =====
    public BigDecimal getTienPhat() { return tienPhat; }
    public void setTienPhat(BigDecimal tienPhat) { this.tienPhat = tienPhat; }

    public BigDecimal getTongGiamGia() { return tongGiamGia; }
    public void setTongGiamGia(BigDecimal tongGiamGia) { this.tongGiamGia = tongGiamGia; }

    public BigDecimal getTongVAT() { return tongVAT; }
    public void setTongVAT(BigDecimal tongVAT) { this.tongVAT = tongVAT; }

    // ===== PHẠT CHI TIẾT =====
    public BigDecimal getPhatNhanPhongTre() { return phatNhanPhongTre; }
    public void setPhatNhanPhongTre(BigDecimal v) { this.phatNhanPhongTre = v; }

    public BigDecimal getPhatTraPhongSom() { return phatTraPhongSom; }
    public void setPhatTraPhongSom(BigDecimal v) { this.phatTraPhongSom = v; }

    public BigDecimal getPhatTraPhongTre() { return phatTraPhongTre; }
    public void setPhatTraPhongTre(BigDecimal v) { this.phatTraPhongTre = v; }

    // ===== GIẢM GIÁ CHI TIẾT =====
    public BigDecimal getGiamGiaTheoMa() { return giamGiaTheoMa; }
    public void setGiamGiaTheoMa(BigDecimal v) { this.giamGiaTheoMa = v; }

    public BigDecimal getGiamGiaTheoHangKH() { return giamGiaTheoHangKH; }
    public void setGiamGiaTheoHangKH(BigDecimal v) { this.giamGiaTheoHangKH = v; }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    // =======================================================
    // ================== HELPER (OPTIONAL) ==================
    // =======================================================

    public BigDecimal tinhTongTienPhat() {
        return safe(phatNhanPhongTre)
                .add(safe(phatTraPhongSom))
                .add(safe(phatTraPhongTre));
    }

    public BigDecimal tinhTongGiamGia() {
        return safe(giamGiaTheoMa)
                .add(safe(giamGiaTheoHangKH));
    }

    private BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
