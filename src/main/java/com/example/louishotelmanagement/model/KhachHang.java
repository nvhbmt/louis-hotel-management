package com.example.louishotelmanagement.model;

import java.time.LocalDate;

//-- Thêm cột
//ALTER TABLE [dbo].[KhachHang]
//ADD
//hangKhach NVARCHAR(50) NULL,     -- Ví dụ: "Khách VIP", "Khách quen", "Khách doanh nghiệp"
//trangThai NVARCHAR(50) NULL;     -- Enum: DANG_LUU_TRU, CHECK_OUT, DA_DAT
//
//
//-- Ghi đè dữ liệu khách hàng
//UPDATE [dbo].[KhachHang]
//SET hoTen = N'Nguyễn Văn Huy',
//soDT = '0901111222',
//email = 'huy.nguyen@example.com',
//diaChi = N'Quận 1, TP. Hồ Chí Minh',
//ngaySinh = '1995-05-15',
//ghiChu = N'Khách thân thiết, thường đặt phòng cuối tuần',
//CCCD = '012345678901',
//hangKhach = N'Khách VIP',
//trangThai = 'DANG_LUU_TRU'
//WHERE maKH = 'KH001';
//
//UPDATE [dbo].[KhachHang]
//SET hoTen = N'Lê Thị Thu Hà',
//soDT = '0903333444',
//email = 'le.ha@example.com',
//diaChi = N'Quận 3, TP. Hồ Chí Minh',
//ngaySinh = '1998-09-22',
//ghiChu = N'Khách mới, đặt lần đầu',
//CCCD = '012345678902',
//hangKhach = N'Khách doanh nghiệp',
//trangThai = 'DA_DAT'
//WHERE maKH = 'KH002';
//
//UPDATE [dbo].[KhachHang]
//SET hoTen = N'Phạm Minh Khoa',
//soDT = '0915555666',
//email = 'khoa.pham@example.com',
//diaChi = N'Đà Nẵng',
//ngaySinh = '1990-12-10',
//ghiChu = N'Đặt phòng cho gia đình',
//CCCD = '012345678903',
//hangKhach = N'Khách quen',
//trangThai = 'CHECK_OUT'
//WHERE maKH = 'KH003';
//
//UPDATE [dbo].[KhachHang]
//SET hoTen = N'Trần Bảo Ngọc',
//soDT = '0937777888',
//email = 'ngoc.tran@example.com',
//diaChi = N'Cần Thơ',
//ngaySinh = '1997-04-30',
//ghiChu = N'Khách trung thành, thanh toán đúng hạn',
//CCCD = '012345678904',
//hangKhach = N'Khách VIP',
//trangThai = 'DANG_LUU_TRU'
//WHERE maKH = 'KH004';
//
//UPDATE [dbo].[KhachHang]
//SET hoTen = N'Đỗ Quang Anh',
//soDT = '0949999000',
//email = 'anh.do@example.com',
//diaChi = N'Hải Phòng',
//ngaySinh = '1992-03-08',
//ghiChu = N'Khách quen, có ưu đãi giảm giá 10%',
//CCCD = '012345678905',
//hangKhach = N'Khách quen',
//trangThai = 'DA_DAT'
//WHERE maKH = 'KH005';

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
