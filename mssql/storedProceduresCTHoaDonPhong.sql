-- =========================================================
-- KHỞI TẠO LẠI CÁC STORES PROCEDURES MỚI
-- (Sử dụng dbo.CTHoaDonPhong)
-- =========================================================

-- 1. sp_ThemCTHoaDonPhong (Thêm mới)
CREATE PROCEDURE sp_ThemCTHoaDonPhong
    @maHD NVARCHAR(10),
    @maPhieu NVARCHAR(10),
    @maPhong NVARCHAR(10),
    @ngayDen DATE,
    @ngayDi DATE,
    @giaPhong DECIMAL(18, 2)
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO dbo.CTHoaDonPhong (maHD, maPhieu, maPhong, ngayDen, ngayDi, giaPhong)
    VALUES (@maHD, @maPhieu, @maPhong, @ngayDen, @ngayDi, @giaPhong);
END
GO

-- 2. SP_SelectCTHoaDonPhongByMaPhieu (Lấy theo Mã Phiếu)
CREATE PROCEDURE SP_SelectCTHoaDonPhongByMaPhieu
@maPhieu NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maHD, maPhieu, maPhong, ngayDen, ngayDi, giaPhong
    FROM dbo.CTHoaDonPhong
    WHERE maPhieu = @maPhieu;
END
GO

-- 3. sp_CapNhatNgayDenThucTe (Cập nhật Ngày Đến thực tế)
CREATE PROCEDURE sp_CapNhatNgayDenThucTe
    @maHD NVARCHAR(10),
    @maPhong NVARCHAR(10),
    @ngayDen DATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE dbo.CTHoaDonPhong
    SET ngayDen = @ngayDen
    WHERE maHD = @maHD AND maPhong = @maPhong;
END
GO

-- 4. sp_CapNhatNgayDiThucTe (Cập nhật Ngày Đi thực tế)
CREATE PROCEDURE sp_CapNhatNgayDiThucTe
    @maHD NVARCHAR(10),
    @maPhong NVARCHAR(10),
    @ngayDi DATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE dbo.CTHoaDonPhong
    SET ngayDi = @ngayDi
    WHERE maHD = @maHD AND maPhong = @maPhong;
END
GO

-- 5. sp_CapNhatMaPhongVaGia (Cập nhật Mã Phòng và Giá)
CREATE PROCEDURE sp_CapNhatMaPhongVaGia
    @maPhieu NVARCHAR(10),
    @maPhongCu NVARCHAR(10),
    @maPhongMoi NVARCHAR(10),
    @giaPhongMoi DECIMAL(18, 2)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE dbo.CTHoaDonPhong
    SET maPhong = @maPhongMoi,
        giaPhong = @giaPhongMoi
    WHERE maPhieu = @maPhieu AND maPhong = @maPhongCu;
END
GO

-- 6. sp_GetCTHoaDonPhongTheoMaPhong (Lấy theo Mã Phòng)
CREATE PROCEDURE sp_GetCTHoaDonPhongTheoMaPhong
@maPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maHD, maPhieu, maPhong, ngayDen, ngayDi, giaPhong
    FROM dbo.CTHoaDonPhong
    WHERE maPhong = @maPhong;
END
GO

-- 7. sp_GetCTHoaDonPhongTheoMaHD (Lấy theo Mã Hóa Đơn)
CREATE PROCEDURE sp_GetCTHoaDonPhongTheoMaHD
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maHD, maPhieu, maPhong, ngayDen, ngayDi, giaPhong
    FROM dbo.CTHoaDonPhong
    WHERE maHD = @maHD;
END
GO

-- 8. sp_TinhTongTienPhongTheoHD (Tính Tổng Tiền Phòng)
-- Lưu ý: Logic tính tổng tiền đơn giản: (Số ngày * Giá phòng)
CREATE PROCEDURE sp_TinhTongTienPhongTheoHD
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT SUM(
                   DATEDIFF(day, ISNULL(ngayDen, GETDATE()), ISNULL(ngayDi, GETDATE())) * giaPhong
           ) AS TongTien
    FROM dbo.CTHoaDonPhong
    WHERE maHD = @maHD;
END
GO

-- 9. sp_XoaCTHoaDonPhong (Xóa)
CREATE PROCEDURE sp_XoaCTHoaDonPhong
    @maHD NVARCHAR(10),
    @maPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM dbo.CTHoaDonPhong
    WHERE maHD = @maHD AND maPhong = @maPhong;
END
GO