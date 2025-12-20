CREATE DATABASE QuanLyKhachSan;
GO

USE QuanLyKhachSan;
GO

-- Bảng loại phòng
CREATE TABLE LoaiPhong
(
    maLoaiPhong nvarchar(10) PRIMARY KEY,
    tenLoai     NVARCHAR(100)  NOT NULL,
    moTa        NVARCHAR(255),
    donGia      DECIMAL(18, 2) NOT NULL
);
GO
-- Bảng phòng
CREATE TABLE Phong
(
    maPhong     nvarchar(10) PRIMARY KEY,
    tang        INT,
    trangThai   NVARCHAR(50) CHECK (trangThai IN (N'Trống', N'Đang sử dụng', N'Đã đặt', N'Bảo trì')) DEFAULT N'Trống',
    moTa        NVARCHAR(255),
    maLoaiPhong nvarchar(10),
    FOREIGN KEY (maLoaiPhong) REFERENCES LoaiPhong (maLoaiPhong)
);
GO
-- Bảng khách hàng
CREATE TABLE KhachHang
(
    maKH      NVARCHAR(10) PRIMARY KEY,
    hoTen     NVARCHAR(100) NOT NULL,
    soDT      NVARCHAR(15)  NOT NULL,
    email     NVARCHAR(100) NULL,
    diaChi    NVARCHAR(255) NULL,
    ngaySinh  DATE          NULL,
    ghiChu    NVARCHAR(255) NULL,
    CCCD      NVARCHAR(20)  NULL,
    hangKhach NVARCHAR(50) DEFAULT N'Khách thường',
    --(Khách VIP, Khách quen)
    trangThai NVARCHAR(50) DEFAULT N'Đang lưu trú'
    --(Enum TrangThaiKhachHang)
);
GO
-- Bảng nhân viên
CREATE TABLE NhanVien
(
    maNV     nvarchar(10) PRIMARY KEY,
    hoTen    NVARCHAR(100),
    soDT     NVARCHAR(15),
    diaChi   NVARCHAR(255),
    chucVu   NVARCHAR(50),
    ngaySinh DATE
);
GO
-- Bảng tài khoản
CREATE TABLE TaiKhoan
(
    maTK        nvarchar(10) PRIMARY KEY,
    maNV        nvarchar(10) UNIQUE,
    tenDangNhap NVARCHAR(50) UNIQUE NOT NULL,
    matKhauHash NVARCHAR(255),
    quyen       NVARCHAR(50),
    trangThai   BIT DEFAULT 1,
    -- 1 = Hoạt động, 0 = Khóa
    FOREIGN KEY (maNV) REFERENCES NhanVien (maNV)
);

-- Phiếu đặt phòng (Updated CREATE TABLE with tienCoc)
CREATE TABLE PhieuDatPhong
(
    maPhieu   nvarchar(10) PRIMARY KEY,
    ngayDat   DATE,
    ngayDen   DATE,
    ngayDi    DATE,
    trangThai NVARCHAR(50),
    ghiChu    NVARCHAR(255),
    maKH      nvarchar(10),
    maNV      nvarchar(10),
    tienCoc   DECIMAL(18, 2) NULL, -- Trường tiền cọc đã được thêm vào
    FOREIGN KEY (maKH) REFERENCES KhachHang (maKH),
    FOREIGN KEY (maNV) REFERENCES NhanVien (maNV)
);
GO
-- Bảng dịch vụ
CREATE TABLE DichVu
(
    maDV         nvarchar(10) PRIMARY KEY,
    tenDV        NVARCHAR(100),
    soLuong      INT,
    donGia       DECIMAL(18, 2),
    moTa         NVARCHAR(255),
    conKinhDoanh BIT
);
GO
-- Bảng mã giảm giá
CREATE TABLE KhuyenMai
(
    maGG             nvarchar(10) PRIMARY KEY,
    code             NVARCHAR(50) UNIQUE NOT NULL,
    giamGia          DECIMAL(18, 2)      NOT NULL,
    kieuGiamGia      NVARCHAR(10) CHECK (kieuGiamGia IN ('PERCENT', 'AMOUNT')) DEFAULT 'PERCENT',
    ngayBatDau       DATE,
    ngayKetThuc      DATE,
    tongTienToiThieu DECIMAL(18, 2),
    moTa             NVARCHAR(255),
    trangThai        NVARCHAR(50),
    maNV             nvarchar(10),
    FOREIGN KEY (maNV) REFERENCES NhanVien (maNV)
);
GO
-- Bảng phiếu dịch vụ
CREATE TABLE PhieuDichVu
(
    maPhieuDV nvarchar(10) PRIMARY KEY,
    maHD      nvarchar(10),
    ngayLap   DATE,
    maNV      nvarchar(10),
    ghiChu    NVARCHAR(255) NULL,
    FOREIGN KEY (maNV) REFERENCES NhanVien (maNV)
);
GO
-- Sửa đổi Bảng HoaDon để lưu trữ chi tiết tính toán
CREATE TABLE HoaDon
(
    maHD             NVARCHAR(10) PRIMARY KEY,

    ngayLap          DATE,
    phuongThuc       NVARCHAR(50),

    trangThai        NVARCHAR(50)
        CHECK (trangThai IN (N'Chưa thanh toán', N'Đã thanh toán')),

    -- ========= TIỀN PHÒNG + TỔNG =========
    tongTien         DECIMAL(18, 2),                -- Tổng tiền cuối cùng phải thanh toán

    -- ========= PHÍ / PHẠT =========
    PhatNhanPhongTre DECIMAL(18, 2) NULL DEFAULT 0, -- Nhận phòng trễ
    PhatTraPhongSom  DECIMAL(18, 2) NULL DEFAULT 0, -- Trả phòng sớm
    PhatTraPhongTre  DECIMAL(18, 2) NULL DEFAULT 0, -- Trả phòng trễ

    -- ========= GIẢM GIÁ =========
    GiamGiaMaGG      DECIMAL(18, 2) NULL DEFAULT 0, -- Giảm theo mã GG
    GiamGiaHangKH    DECIMAL(18, 2) NULL DEFAULT 0, -- Giảm theo hạng KH

    -- ========= THUẾ / NGÀY THỰC TẾ =========
    TongVAT          DECIMAL(18, 2) NULL,           -- Tổng VAT
    NgayCheckOut     DATE           NULL,           -- Ngày trả phòng thực tế

    -- ========= KHÓA NGOẠI =========
    maKH             NVARCHAR(10),
    maNV             NVARCHAR(10),
    maGG             NVARCHAR(10)   NULL,

    FOREIGN KEY (maKH) REFERENCES KhachHang (maKH),
    FOREIGN KEY (maNV) REFERENCES NhanVien (maNV),
    FOREIGN KEY (maGG) REFERENCES KhuyenMai (maGG)
);
GO

-- Chi tiết hóa đơn
CREATE TABLE CTHoaDonPhong
(
    maHD      nvarchar(10),
    maPhieu   nvarchar(10),
    maPhong   nvarchar(10),
    ngayDen   DATE,
    ngayDi    DATE,
    giaPhong  DECIMAL(18, 2),
    thanhTien AS (
        CASE
            WHEN DATEDIFF(DAY, ngayDen, ngayDi) = 0
                THEN giaPhong * 1 -- Tính 1 ngày/đêm nếu đến và đi cùng ngày
            ELSE DATEDIFF(DAY, ngayDen, ngayDi) * giaPhong
            END
        ) PERSISTED,
    daHuy     BIT      NOT NULL DEFAULT 0,
    ngayHuy   DATETIME NULL,
    PRIMARY KEY (maHD, maPhong),
    FOREIGN KEY (maHD) REFERENCES HoaDon (maHD),
    FOREIGN KEY (maPhieu) REFERENCES PhieuDatPhong (maPhieu),
    FOREIGN KEY (maPhong) REFERENCES Phong (maPhong)
);
GO
CREATE TABLE CTHoaDonDichVu
(
    maHD      nvarchar(10),
    maPhieuDV nvarchar(10),
    maDV      nvarchar(10),
    soLuong   INT,
    donGia    DECIMAL(18, 2),
    thanhTien AS (soLuong * donGia) PERSISTED,
    PRIMARY KEY (maHD, maDV),
    FOREIGN KEY (maHD) REFERENCES HoaDon (maHD),
    FOREIGN KEY (maPhieuDV) REFERENCES PhieuDichVu (maPhieuDV),
    FOREIGN KEY (maDV) REFERENCES DichVu (maDV)
);
GO

-- =============================================
-- Indexes để optimize performance cho tính năng đặt phòng theo khoảng thời gian
-- =============================================

-- Index cho CTHoaDonPhong để optimize overlap check
-- Sử dụng cho stored procedure sp_LayDSPhongTrongTheoKhoangThoiGian
IF NOT EXISTS (SELECT 1
               FROM sys.indexes
               WHERE name = 'IX_CTHoaDonPhong_DateRange'
                 AND object_id = OBJECT_ID('CTHoaDonPhong'))
    BEGIN
        CREATE NONCLUSTERED INDEX IX_CTHoaDonPhong_DateRange
            ON CTHoaDonPhong (maPhong, ngayDen, ngayDi)
            INCLUDE (maPhieu);
    END
GO

-- Index cho PhieuDatPhong để filter theo trạng thái
-- Giúp loại trừ booking đã hủy/hoàn thành nhanh hơn
IF NOT EXISTS (SELECT 1
               FROM sys.indexes
               WHERE name = 'IX_PhieuDatPhong_Status'
                 AND object_id = OBJECT_ID('PhieuDatPhong'))
    BEGIN
        CREATE NONCLUSTERED INDEX IX_PhieuDatPhong_Status
            ON PhieuDatPhong (trangThai)
            INCLUDE (maPhieu, ngayDen, ngayDi);
    END
GO

-- Index cho Phong để filter theo trạng thái
-- Giúp loại trừ phòng bảo trì nhanh hơn
IF NOT EXISTS (SELECT 1
               FROM sys.indexes
               WHERE name = 'IX_Phong_TrangThai'
                 AND object_id = OBJECT_ID('Phong'))
    BEGIN
        CREATE NONCLUSTERED INDEX IX_Phong_TrangThai
            ON Phong (trangThai)
            WHERE trangThai != N'Bảo trì';
    END
GO

-- Index composite cho CTHoaDonPhong với maPhieu để join nhanh hơn
IF NOT EXISTS (SELECT 1
               FROM sys.indexes
               WHERE name = 'IX_CTHoaDonPhong_MaPhieu'
                 AND object_id = OBJECT_ID('CTHoaDonPhong'))
    BEGIN
        CREATE NONCLUSTERED INDEX IX_CTHoaDonPhong_MaPhieu
            ON CTHoaDonPhong (maPhieu)
            INCLUDE (maPhong, ngayDen, ngayDi);
    END
GO

