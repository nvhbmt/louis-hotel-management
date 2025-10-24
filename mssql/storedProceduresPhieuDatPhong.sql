-- =============================================
-- 1. Thêm phiếu đặt phòng
-- =============================================
CREATE OR ALTER PROCEDURE sp_ThemPhieuDatPhong
    @maPhieu NVARCHAR(10),
    @ngayDat DATE = NULL,
    @ngayDen DATE = NULL,
    @ngayDi DATE = NULL,
    @trangThai NVARCHAR(50),
    @ghiChu NVARCHAR(255) = NULL,
    @maKH NVARCHAR(10),
    @maNV NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO PhieuDatPhong(maPhieu, ngayDat, ngayDen, ngayDi, trangThai, ghiChu, maKH, maNV)
    VALUES (@maPhieu, @ngayDat, @ngayDen, @ngayDi, @trangThai, @ghiChu, @maKH, @maNV);
END;
GO

-- =============================================
-- 2. Lấy danh sách tất cả phiếu đặt phòng
-- =============================================
CREATE OR ALTER PROCEDURE sp_LayDSPhieuDatPhong
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maPhieu, ngayDat, ngayDen, ngayDi, trangThai, ghiChu, maKH, maNV
    FROM PhieuDatPhong
    ORDER BY ngayDat DESC;
END;
GO

-- =============================================
-- 3. Lấy phiếu đặt phòng theo mã
-- =============================================
CREATE OR ALTER PROCEDURE sp_LayPhieuDatPhongTheoMa
@maPhieu NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maPhieu, ngayDat, ngayDen, ngayDi, trangThai, ghiChu, maKH, maNV
    FROM PhieuDatPhong
    WHERE maPhieu = @maPhieu;
END;
GO

-- =============================================
-- 4. Lấy danh sách phiếu đặt phòng theo khách hàng
-- =============================================
CREATE OR ALTER PROCEDURE sp_LayDSPhieuDatPhongTheoKhachHang
@maKH NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maPhieu, ngayDat, ngayDen, ngayDi, trangThai, ghiChu, maKH, maNV
    FROM PhieuDatPhong
    WHERE maKH = @maKH
    ORDER BY ngayDat DESC;
END;
GO

-- =============================================
-- 5. Lấy danh sách phiếu đặt phòng theo nhân viên
-- =============================================
CREATE OR ALTER PROCEDURE sp_LayDSPhieuDatPhongTheoNhanVien
@maNV NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maPhieu, ngayDat, ngayDen, ngayDi, trangThai, ghiChu, maKH, maNV
    FROM PhieuDatPhong
    WHERE maNV = @maNV
    ORDER BY ngayDat DESC;
END;
GO

-- =============================================
-- 6. Lấy danh sách phiếu đặt phòng theo trạng thái
-- =============================================
CREATE OR ALTER PROCEDURE sp_LayDSPhieuDatPhongTheoTrangThai
@trangThai NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maPhieu, ngayDat, ngayDen, ngayDi, trangThai, ghiChu, maKH, maNV
    FROM PhieuDatPhong
    WHERE trangThai = @trangThai
    ORDER BY ngayDat DESC;
END;
GO

-- =============================================
-- 7. Lấy danh sách phiếu đặt phòng trong khoảng thời gian
-- =============================================
CREATE OR ALTER PROCEDURE sp_LayDSPhieuDatPhongTrongKhoangThoiGian
    @ngayBatDau DATE,
    @ngayKetThuc DATE
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maPhieu, ngayDat, ngayDen, ngayDi, trangThai, ghiChu, maKH, maNV
    FROM PhieuDatPhong
    WHERE ngayDat BETWEEN @ngayBatDau AND @ngayKetThuc
    ORDER BY ngayDat DESC;
END;
GO

-- =============================================
-- 8. Cập nhật toàn bộ thông tin phiếu đặt phòng
-- =============================================
CREATE OR ALTER PROCEDURE sp_CapNhatPhieuDatPhong
    @maPhieu NVARCHAR(10),
    @ngayDat DATE = NULL,
    @ngayDen DATE = NULL,
    @ngayDi DATE = NULL,
    @trangThai NVARCHAR(50),
    @ghiChu NVARCHAR(255) = NULL,
    @maKH NVARCHAR(10),
    @maNV NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE PhieuDatPhong
    SET ngayDat = @ngayDat,
        ngayDen = @ngayDen,
        ngayDi = @ngayDi,
        trangThai = @trangThai,
        ghiChu = @ghiChu,
        maKH = @maKH,
        maNV = @maNV
    WHERE maPhieu = @maPhieu;
END;
GO

-- =============================================
-- 9. Cập nhật trạng thái phiếu đặt phòng
-- =============================================
CREATE OR ALTER PROCEDURE sp_CapNhatTrangThaiPhieuDatPhong
    @maPhieu NVARCHAR(10),
    @trangThai NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE PhieuDatPhong
    SET trangThai = @trangThai
    WHERE maPhieu = @maPhieu;
END;
GO

-- =============================================
-- 10. Xóa phiếu đặt phòng
-- =============================================
CREATE OR ALTER PROCEDURE sp_XoaPhieuDatPhong
@maPhieu NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM PhieuDatPhong
    WHERE maPhieu = @maPhieu;
END;
GO
