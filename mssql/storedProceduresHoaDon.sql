-- =============================================
-- ========== CÁC STORED PROCEDURE HÓA ĐƠN ======
-- =============================================

-- 1. Thêm hóa đơn
CREATE OR ALTER PROCEDURE sp_ThemHoaDon
    @maHD NVARCHAR(10),
    @ngayLap DATE,
    @phuongThuc NVARCHAR(50),
    @tongTien DECIMAL(18,2),
    @maKH NVARCHAR(10),
    @maNV NVARCHAR(10),
    @maGG NVARCHAR(10) = NULL,
    @trangThai NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO dbo.HoaDon (maHD, ngayLap, phuongThuc, tongTien, maKH, maNV, maGG, trangThai)
    VALUES (@maHD, @ngayLap, @phuongThuc, @tongTien, @maKH, @maNV, @maGG, @trangThai);
END;
GO

-- 2. Sửa hóa đơn
CREATE OR ALTER PROCEDURE sp_SuaHoaDon
    @maHD NVARCHAR(10),
    @ngayLap DATE,
    @phuongThuc NVARCHAR(50),
    @tongTien DECIMAL(18,2),
    @maKH NVARCHAR(10),
    @maNV NVARCHAR(10),
    @maGG NVARCHAR(10),
    @trangThai NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE dbo.HoaDon
    SET ngayLap = @ngayLap,
        phuongThuc = @phuongThuc,
        tongTien = @tongTien,
        maKH = @maKH,
        maNV = @maNV,
        maGG = @maGG,
        trangThai = @trangThai
    WHERE maHD = @maHD;
END;
GO

-- 3. Xóa hóa đơn
CREATE OR ALTER PROCEDURE sp_XoaHoaDon
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM dbo.HoaDon WHERE maHD = @maHD;
END;
GO

-- 4. Lấy danh sách hóa đơn
CREATE OR ALTER PROCEDURE sp_LayDanhSachHoaDon
AS
BEGIN
    SET NOCOUNT ON;
    SELECT * FROM dbo.HoaDon;
END;
GO

-- 5. Tìm hóa đơn theo mã
CREATE OR ALTER PROCEDURE sp_TimHoaDonTheoMa
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT * FROM dbo.HoaDon WHERE maHD = @maHD;
END;
GO

-- 6. Sinh mã hóa đơn tự động (HD001, HD002, ...)
CREATE OR ALTER PROCEDURE sp_TaoMaHoaDonMoi
AS
BEGIN
    DECLARE @maMoi NVARCHAR(10);
    DECLARE @so INT;

    SELECT @so = MAX(CAST(SUBSTRING(maHD, 3, LEN(maHD)) AS INT)) FROM HoaDon;

    IF @so IS NULL
        SET @so = 1;
    ELSE
        SET @so = @so + 1;

    SET @maMoi = 'HD' + RIGHT('000' + CAST(@so AS NVARCHAR(3)), 3);
    SELECT @maMoi AS maHDMoi;
END;
GO

-- 7. Cập nhật trạng thái hóa đơn
CREATE OR ALTER PROCEDURE sp_CapNhatTrangThaiHoaDon
    @maHD NVARCHAR(10),
    @trangThaiMoi NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE HoaDon
    SET trangThai = @trangThaiMoi
    WHERE maHD = @maHD;
END;
GO
