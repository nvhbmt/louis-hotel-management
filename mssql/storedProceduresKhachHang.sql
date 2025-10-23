-- ===============================================
-- KHỐI MÃ T-SQL ĐÃ CHỈNH SỬA (Thêm dbo. cho các bảng)
-- ===============================================

-- 1. STORES PROCEDURE THÊM KHÁCH HÀNG
IF OBJECT_ID('sp_ThemKhachHang', 'P') IS NOT NULL
    DROP PROCEDURE sp_ThemKhachHang;
GO

CREATE PROCEDURE sp_ThemKhachHang
    @maKH VARCHAR(10),
    @hoTen NVARCHAR(100),
    @soDT VARCHAR(15),
    @email VARCHAR(100),
    @diaChi NVARCHAR(255),
    @ngaySinh DATE,
    @ghiChu NVARCHAR(MAX),
    @CCCD VARCHAR(20)
AS
BEGIN
    INSERT INTO dbo.KhachHang (maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD)
    VALUES (@maKH, @hoTen, @soDT, @email, @diaChi, @ngaySinh, @ghiChu, @CCCD);

    SELECT @@ROWCOUNT;
END;
GO

--------------------------------------------------

-- 2. STORES PROCEDURE CẬP NHẬT KHÁCH HÀNG
IF OBJECT_ID('sp_CapNhatKhachHang', 'P') IS NOT NULL
    DROP PROCEDURE sp_CapNhatKhachHang;
GO

CREATE PROCEDURE sp_CapNhatKhachHang
    @maKH VARCHAR(10),
    @hoTen NVARCHAR(100),
    @soDT VARCHAR(15),
    @email VARCHAR(100),
    @diaChi NVARCHAR(255),
    @ngaySinh DATE,
    @ghiChu NVARCHAR(MAX),
    @CCCD VARCHAR(20)
AS
BEGIN
    UPDATE dbo.KhachHang
    SET
        hoTen = @hoTen,
        soDT = @soDT,
        email = @email,
        diaChi = @diaChi,
        ngaySinh = @ngaySinh,
        ghiChu = @ghiChu,
        CCCD = @CCCD
    WHERE maKH = @maKH;

    SELECT @@ROWCOUNT;
END;
GO

--------------------------------------------------

-- 3. STORES PROCEDURE XÓA KHÁCH HÀNG
IF OBJECT_ID('sp_XoaKhachHang', 'P') IS NOT NULL
    DROP PROCEDURE sp_XoaKhachHang;
GO

CREATE PROCEDURE sp_XoaKhachHang
@maKH VARCHAR(10)
AS
BEGIN
    DELETE FROM dbo.KhachHang
    WHERE maKH = @maKH;

    SELECT @@ROWCOUNT;
END;
GO

--------------------------------------------------

-- 4. STORES PROCEDURE LẤY DANH SÁCH KHÁCH HÀNG
IF OBJECT_ID('sp_LayDSKhachHang', 'P') IS NOT NULL
    DROP PROCEDURE sp_LayDSKhachHang;
GO

CREATE PROCEDURE sp_LayDSKhachHang
AS
BEGIN
    SELECT maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD
    FROM dbo.KhachHang
    ORDER BY maKH;
END;
GO

--------------------------------------------------

-- 5. STORES PROCEDURE LẤY KHÁCH HÀNG THEO MÃ
IF OBJECT_ID('sp_LayKhachHangTheoMa', 'P') IS NOT NULL
    DROP PROCEDURE sp_LayKhachHangTheoMa;
GO

CREATE PROCEDURE sp_LayKhachHangTheoMa
@maKH VARCHAR(10)
AS
BEGIN
    SELECT maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD
    FROM dbo.KhachHang
    WHERE maKH = @maKH;
END;
GO

--------------------------------------------------

-- 6. STORES PROCEDURE TÌM KIẾM THEO TÊN
IF OBJECT_ID('sp_TimKiemKhachHangTheoTen', 'P') IS NOT NULL
    DROP PROCEDURE sp_TimKiemKhachHangTheoTen;
GO

CREATE PROCEDURE sp_TimKiemKhachHangTheoTen
@ten NVARCHAR(100)
AS
BEGIN
    SELECT maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD
    FROM dbo.KhachHang
    WHERE hoTen LIKE @ten
    ORDER BY hoTen;
END;
GO

--------------------------------------------------

-- 7. STORES PROCEDURE TÌM KIẾM THEO SĐT
IF OBJECT_ID('sp_TimKiemKhachHangTheoSDT', 'P') IS NOT NULL
    DROP PROCEDURE sp_TimKiemKhachHangTheoSDT;
GO

CREATE PROCEDURE sp_TimKiemKhachHangTheoSDT
@soDT VARCHAR(15)
AS
BEGIN
    SELECT maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD
    FROM dbo.KhachHang
    WHERE soDT LIKE @soDT
    ORDER BY soDT;
END;
GO

--------------------------------------------------

-- 8. STORES PROCEDURE TÌM KIẾM THEO CCCD
IF OBJECT_ID('sp_TimKiemKhachHangTheoCCCD', 'P') IS NOT NULL
    DROP PROCEDURE sp_TimKiemKhachHangTheoCCCD;
GO

CREATE PROCEDURE sp_TimKiemKhachHangTheoCCCD
@CCCD VARCHAR(20)
AS
BEGIN
    SELECT maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD
    FROM dbo.KhachHang
    WHERE CCCD = @CCCD;
END;
GO

--------------------------------------------------

-- 9. STORES PROCEDURE KIỂM TRA KHÁCH HÀNG ĐƯỢC SỬ DỤNG
IF OBJECT_ID('sp_KiemTraKhachHangDuocSuDung', 'P') IS NOT NULL
    DROP PROCEDURE sp_KiemTraKhachHangDuocSuDung;
GO

CREATE PROCEDURE sp_KiemTraKhachHangDuocSuDung
@maKH VARCHAR(10)
AS
BEGIN
    -- Kiểm tra trong bảng PhieuDatPhong
    SELECT count = COUNT(*)
    FROM dbo.PhieuDatPhong pdp
    WHERE pdp.maKH = @maKH
      AND pdp.trangThai NOT IN ('HOAN_THANH', 'DA_HUY');
END;
GO