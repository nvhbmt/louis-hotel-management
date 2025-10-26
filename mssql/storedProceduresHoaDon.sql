-- =============================================
-- ========== CÁC STORED PROCEDURE HÓA ĐƠN ======
-- =============================================

USE QuanLyKhachSan;
GO

-- 1. Thêm hóa đơn
CREATE PROCEDURE sp_ThemHoaDon @maHD NVARCHAR(10),
                               @ngayLap DATE,
                               @phuongThuc NVARCHAR(50),
                               @tongTien DECIMAL(18, 2),
                               @maKH NVARCHAR(10),
                               @maNV NVARCHAR(10),
                               @maGG NVARCHAR(10) = NULL,
                               @trangThai NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO HoaDon
        (maHD, ngayLap, phuongThuc, tongTien, maKH, maNV, maGG, trangThai)
    VALUES (@maHD, @ngayLap, @phuongThuc, @tongTien, @maKH, @maNV, @maGG, @trangThai);
END;
GO

-- 2. Sửa hóa đơn
CREATE PROCEDURE sp_SuaHoaDon @maHD NVARCHAR(10),
                              @ngayLap DATE,
                              @phuongThuc NVARCHAR(50),
                              @tongTien DECIMAL(18, 2),
                              @maKH NVARCHAR(10),
                              @maNV NVARCHAR(10),
                              @maGG NVARCHAR(10),
                              @trangThai NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE HoaDon
    SET ngayLap    = @ngayLap,
        phuongThuc = @phuongThuc,
        tongTien   = @tongTien,
        maKH       = @maKH,
        maNV       = @maNV,
        maGG       = @maGG,
        trangThai  = @trangThai
    WHERE maHD = @maHD;
END;
GO

-- 3. Xóa hóa đơn
CREATE PROCEDURE sp_XoaHoaDon @maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM HoaDon WHERE maHD = @maHD;
END;
GO

-- 4. Lấy danh sách hóa đơn
CREATE PROCEDURE sp_LayDanhSachHoaDon
AS
BEGIN
    SET NOCOUNT ON;
    SELECT *
    FROM HoaDon;
END;
GO

-- 5. Tìm hóa đơn theo mã
CREATE PROCEDURE sp_TimHoaDonTheoMa @maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT *
    FROM HoaDon
    WHERE maHD = @maHD;
END;
GO

-- 6. Sinh mã hóa đơn tự động (HD001, HD002, ...)
CREATE PROCEDURE sp_TaoMaHoaDonTiepTheo
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @maCu NVARCHAR(10);
    DECLARE @so INT;
    DECLARE @maMoi NVARCHAR(10);

    -- 🔹 Lấy mã hóa đơn lớn nhất hiện tại
    SELECT @maCu = MAX(maHD)
    FROM HoaDon;

    -- 🔹 Nếu chưa có hóa đơn nào thì gán là HD001
    IF @maCu IS NULL
        SET @maMoi = 'HD001';
    ELSE
        BEGIN
            -- Lấy phần số từ mã cũ (ví dụ HD005 → 5)
            SET @so = CAST(SUBSTRING(@maCu, 3, LEN(@maCu) - 2) AS INT) + 1;

            -- Sinh mã mới với 3 chữ số, thêm 0 phía trước nếu cần
            SET @maMoi = 'HD' + RIGHT('000' + CAST(@so AS NVARCHAR(3)), 3);
        END

    -- 🔹 Trả kết quả ra
    SELECT @maMoi AS maHDMoi;
END;
GO;

-- 7. Cập nhật trạng thái hóa đơn
CREATE PROCEDURE sp_CapNhatTrangThaiHoaDon @maHD NVARCHAR(10),
                                           @trangThaiMoi NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE HoaDon
    SET trangThai = @trangThaiMoi
    WHERE maHD = @maHD;
END;
GO
