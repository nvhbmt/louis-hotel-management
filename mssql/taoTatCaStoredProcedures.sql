-- =============================================
-- Script tổng hợp tạo tất cả Stored Procedures
-- Hệ thống Quản Lý Khách Sạn Louis
-- =============================================

USE QuanLyKhachSan;
GO

PRINT 'Bắt đầu tạo các Stored Procedures...';
PRINT '';

-- =============================================
-- 1. Stored Procedures cho LoaiPhong
-- =============================================
PRINT '1. Tạo Stored Procedures cho LoaiPhong...';

-- Thêm loại phòng
CREATE OR ALTER PROCEDURE sp_ThemLoaiPhong
    @maLoaiPhong NVARCHAR(10),
    @tenLoai NVARCHAR(100),
    @moTa NVARCHAR(255),
    @donGia DECIMAL(18,2)
AS
BEGIN
    SET NOCOUNT ON;
    
    IF EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong)
    BEGIN
        RAISERROR('Mã loại phòng đã tồn tại!', 16, 1);
        RETURN;
    END
    
    IF @maLoaiPhong IS NULL OR LTRIM(RTRIM(@maLoaiPhong)) = ''
    BEGIN
        RAISERROR('Mã loại phòng không được để trống!', 16, 1);
        RETURN;
    END
    
    IF @tenLoai IS NULL OR LTRIM(RTRIM(@tenLoai)) = ''
    BEGIN
        RAISERROR('Tên loại phòng không được để trống!', 16, 1);
        RETURN;
    END
    
    IF @donGia IS NULL OR @donGia <= 0
    BEGIN
        RAISERROR('Đơn giá phải lớn hơn 0!', 16, 1);
        RETURN;
    END
    
    INSERT INTO LoaiPhong (maLoaiPhong, tenLoai, moTa, donGia)
    VALUES (@maLoaiPhong, @tenLoai, @moTa, @donGia);
    
    PRINT 'Đã thêm loại phòng thành công!';
END
GO

-- Lấy danh sách tất cả loại phòng
CREATE OR ALTER PROCEDURE sp_LayDSLoaiPhong
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT maLoaiPhong, tenLoai, moTa, donGia
    FROM LoaiPhong
    ORDER BY tenLoai;
END
GO

-- Lấy loại phòng theo mã
CREATE OR ALTER PROCEDURE sp_LayLoaiPhongTheoMa
    @maLoaiPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT maLoaiPhong, tenLoai, moTa, donGia
    FROM LoaiPhong
    WHERE maLoaiPhong = @maLoaiPhong;
END
GO

-- Cập nhật loại phòng
CREATE OR ALTER PROCEDURE sp_CapNhatLoaiPhong
    @maLoaiPhong NVARCHAR(10),
    @tenLoai NVARCHAR(100),
    @moTa NVARCHAR(255),
    @donGia DECIMAL(18,2)
AS
BEGIN
    SET NOCOUNT ON;
    
    IF NOT EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong)
    BEGIN
        RAISERROR('Loại phòng không tồn tại!', 16, 1);
        RETURN;
    END
    
    IF @tenLoai IS NULL OR LTRIM(RTRIM(@tenLoai)) = ''
    BEGIN
        RAISERROR('Tên loại phòng không được để trống!', 16, 1);
        RETURN;
    END
    
    IF @donGia IS NULL OR @donGia <= 0
    BEGIN
        RAISERROR('Đơn giá phải lớn hơn 0!', 16, 1);
        RETURN;
    END
    
    UPDATE LoaiPhong
    SET tenLoai = @tenLoai,
        moTa = @moTa,
        donGia = @donGia
    WHERE maLoaiPhong = @maLoaiPhong;
    
    PRINT 'Đã cập nhật loại phòng thành công!';
END
GO

-- Xóa loại phòng
CREATE OR ALTER PROCEDURE sp_XoaLoaiPhong
    @maLoaiPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    
    IF NOT EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong)
    BEGIN
        RAISERROR('Loại phòng không tồn tại!', 16, 1);
        RETURN;
    END
    
    IF EXISTS (SELECT 1 FROM Phong WHERE maLoaiPhong = @maLoaiPhong)
    BEGIN
        RAISERROR('Không thể xóa loại phòng này vì đang được sử dụng!', 16, 1);
        RETURN;
    END
    
    DELETE FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong;
    
    PRINT 'Đã xóa loại phòng thành công!';
END
GO

-- Kiểm tra loại phòng có được sử dụng không
CREATE OR ALTER PROCEDURE sp_KiemTraLoaiPhongDuocSuDung
    @maLoaiPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT COUNT(*) as count
    FROM Phong
    WHERE maLoaiPhong = @maLoaiPhong;
END
GO

PRINT '   ✓ Hoàn thành Stored Procedures cho LoaiPhong';
PRINT '';

-- =============================================
-- 2. Stored Procedures cho Phong
-- =============================================
PRINT '2. Tạo Stored Procedures cho Phong...';

-- Thêm phòng
CREATE OR ALTER PROCEDURE sp_ThemPhong
    @maPhong NVARCHAR(10),
    @tang INT,
    @trangThai NVARCHAR(50),
    @moTa NVARCHAR(255),
    @maLoaiPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    
    IF EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong)
    BEGIN
        RAISERROR('Mã phòng đã tồn tại!', 16, 1);
        RETURN;
    END
    
    IF @maPhong IS NULL OR LTRIM(RTRIM(@maPhong)) = ''
    BEGIN
        RAISERROR('Mã phòng không được để trống!', 16, 1);
        RETURN;
    END
    
    IF @tang IS NULL OR @tang < 1 OR @tang > 20
    BEGIN
        RAISERROR('Tầng phải từ 1 đến 20!', 16, 1);
        RETURN;
    END
    
    IF @trangThai IS NULL OR LTRIM(RTRIM(@trangThai)) = ''
    BEGIN
        RAISERROR('Trạng thái không được để trống!', 16, 1);
        RETURN;
    END
    
    IF @maLoaiPhong IS NULL OR LTRIM(RTRIM(@maLoaiPhong)) = ''
    BEGIN
        RAISERROR('Mã loại phòng không được để trống!', 16, 1);
        RETURN;
    END
    
    IF NOT EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong)
    BEGIN
        RAISERROR('Loại phòng không tồn tại!', 16, 1);
        RETURN;
    END
    
    INSERT INTO Phong (maPhong, tang, trangThai, moTa, maLoaiPhong)
    VALUES (@maPhong, @tang, @trangThai, @moTa, @maLoaiPhong);
    
    PRINT 'Đã thêm phòng thành công!';
END
GO

-- Lấy danh sách tất cả phòng
CREATE OR ALTER PROCEDURE sp_LayDSPhong
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT p.maPhong, p.tang, p.trangThai, p.moTa, p.maLoaiPhong,
           lp.tenLoai, lp.donGia
    FROM Phong p
    LEFT JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
    ORDER BY p.tang, p.maPhong;
END
GO

-- Lấy danh sách phòng theo tầng
CREATE OR ALTER PROCEDURE sp_LayDSPhongTheoTang
    @tang INT
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT p.maPhong, p.tang, p.trangThai, p.moTa, p.maLoaiPhong,
           lp.tenLoai, lp.donGia
    FROM Phong p
    LEFT JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
    WHERE p.tang = @tang
    ORDER BY p.maPhong;
END
GO

-- Lấy danh sách phòng theo trạng thái
CREATE OR ALTER PROCEDURE sp_LayDSPhongTheoTrangThai
    @trangThai NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT p.maPhong, p.tang, p.trangThai, p.moTa, p.maLoaiPhong,
           lp.tenLoai, lp.donGia
    FROM Phong p
    LEFT JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
    WHERE p.trangThai = @trangThai
    ORDER BY p.tang, p.maPhong;
END
GO

-- Lấy danh sách phòng còn trống
CREATE OR ALTER PROCEDURE sp_LayDSPhongTrong
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT p.maPhong, p.tang, p.trangThai, p.moTa, p.maLoaiPhong,
           lp.tenLoai, lp.donGia
    FROM Phong p
    LEFT JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
    WHERE p.trangThai = N'Trống'
    ORDER BY p.tang, p.maPhong;
END
GO

-- Lấy phòng theo mã
CREATE OR ALTER PROCEDURE sp_LayPhongTheoMa
    @maPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT p.maPhong, p.tang, p.trangThai, p.moTa, p.maLoaiPhong,
           lp.tenLoai, lp.donGia
    FROM Phong p
    LEFT JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
    WHERE p.maPhong = @maPhong;
END
GO

-- Cập nhật phòng
CREATE OR ALTER PROCEDURE sp_CapNhatPhong
    @maPhong NVARCHAR(10),
    @tang INT,
    @trangThai NVARCHAR(50),
    @moTa NVARCHAR(255),
    @maLoaiPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    
    IF NOT EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong)
    BEGIN
        RAISERROR('Phòng không tồn tại!', 16, 1);
        RETURN;
    END
    
    IF @tang IS NULL OR @tang < 1 OR @tang > 20
    BEGIN
        RAISERROR('Tầng phải từ 1 đến 20!', 16, 1);
        RETURN;
    END
    
    IF @trangThai IS NULL OR LTRIM(RTRIM(@trangThai)) = ''
    BEGIN
        RAISERROR('Trạng thái không được để trống!', 16, 1);
        RETURN;
    END
    
    IF @maLoaiPhong IS NULL OR LTRIM(RTRIM(@maLoaiPhong)) = ''
    BEGIN
        RAISERROR('Mã loại phòng không được để trống!', 16, 1);
        RETURN;
    END
    
    IF NOT EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong)
    BEGIN
        RAISERROR('Loại phòng không tồn tại!', 16, 1);
        RETURN;
    END
    
    UPDATE Phong
    SET tang = @tang,
        trangThai = @trangThai,
        moTa = @moTa,
        maLoaiPhong = @maLoaiPhong
    WHERE maPhong = @maPhong;
    
    PRINT 'Đã cập nhật phòng thành công!';
END
GO

-- Cập nhật trạng thái phòng
CREATE OR ALTER PROCEDURE sp_CapNhatTrangThaiPhong
    @maPhong NVARCHAR(10),
    @trangThai NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    
    IF NOT EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong)
    BEGIN
        RAISERROR('Phòng không tồn tại!', 16, 1);
        RETURN;
    END
    
    IF @trangThai IS NULL OR LTRIM(RTRIM(@trangThai)) = ''
    BEGIN
        RAISERROR('Trạng thái không được để trống!', 16, 1);
        RETURN;
    END
    
    UPDATE Phong
    SET trangThai = @trangThai
    WHERE maPhong = @maPhong;
    
    PRINT 'Đã cập nhật trạng thái phòng thành công!';
END
GO

-- Xóa phòng
CREATE OR ALTER PROCEDURE sp_XoaPhong
    @maPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    
    IF NOT EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong)
    BEGIN
        RAISERROR('Phòng không tồn tại!', 16, 1);
        RETURN;
    END
    
    IF EXISTS (SELECT 1 FROM CTPhieuDatPhong WHERE maPhong = @maPhong)
    BEGIN
        RAISERROR('Không thể xóa phòng này vì đang được sử dụng!', 16, 1);
        RETURN;
    END
    
    DELETE FROM Phong WHERE maPhong = @maPhong;
    
    PRINT 'Đã xóa phòng thành công!';
END
GO

-- Kiểm tra phòng có được sử dụng không
CREATE OR ALTER PROCEDURE sp_KiemTraPhongDuocSuDung
    @maPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT COUNT(*) as count
    FROM CTPhieuDatPhong
    WHERE maPhong = @maPhong;
END
GO

PRINT '   ✓ Hoàn thành Stored Procedures cho Phong';
PRINT '';

-- =============================================
-- 3. Stored Procedures bổ sung cho thống kê
-- =============================================
PRINT '3. Tạo Stored Procedures cho thống kê...';

-- Thống kê phòng theo trạng thái
CREATE OR ALTER PROCEDURE sp_ThongKePhongTheoTrangThai
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT p.trangThai, COUNT(*) as soLuong
    FROM Phong p
    GROUP BY p.trangThai
    ORDER BY soLuong DESC;
END
GO

-- Thống kê phòng theo loại
CREATE OR ALTER PROCEDURE sp_ThongKePhongTheoLoai
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT lp.tenLoai, COUNT(p.maPhong) as soLuong, 
           AVG(lp.donGia) as giaTrungBinh
    FROM LoaiPhong lp
    LEFT JOIN Phong p ON lp.maLoaiPhong = p.maLoaiPhong
    GROUP BY lp.maLoaiPhong, lp.tenLoai
    ORDER BY soLuong DESC;
END
GO

-- Thống kê phòng theo tầng
CREATE OR ALTER PROCEDURE sp_ThongKePhongTheoTang
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT p.tang, COUNT(*) as soLuong,
           SUM(CASE WHEN p.trangThai = N'Trống' THEN 1 ELSE 0 END) as phongTrong,
           SUM(CASE WHEN p.trangThai = N'Đang sử dụng' THEN 1 ELSE 0 END) as phongDangSuDung
    FROM Phong p
    GROUP BY p.tang
    ORDER BY p.tang;
END
GO

-- Tìm kiếm phòng
CREATE OR ALTER PROCEDURE sp_TimKiemPhong
    @tuKhoa NVARCHAR(100),
    @tang INT = NULL,
    @trangThai NVARCHAR(50) = NULL,
    @maLoaiPhong NVARCHAR(10) = NULL
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT p.maPhong, p.tang, p.trangThai, p.moTa, p.maLoaiPhong,
           lp.tenLoai, lp.donGia
    FROM Phong p
    LEFT JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
    WHERE (@tuKhoa IS NULL OR @tuKhoa = '' OR 
           p.maPhong LIKE '%' + @tuKhoa + '%' OR 
           p.moTa LIKE '%' + @tuKhoa + '%')
      AND (@tang IS NULL OR p.tang = @tang)
      AND (@trangThai IS NULL OR p.trangThai = @trangThai)
      AND (@maLoaiPhong IS NULL OR p.maLoaiPhong = @maLoaiPhong)
    ORDER BY p.tang, p.maPhong;
END
GO

PRINT '   ✓ Hoàn thành Stored Procedures cho thống kê';
PRINT '';

-- =============================================
-- 4. Kiểm tra và báo cáo
-- =============================================
PRINT '4. Kiểm tra các Stored Procedures đã tạo...';

-- Liệt kê tất cả stored procedures
SELECT 
    ROUTINE_NAME as 'Tên Stored Procedure',
    ROUTINE_TYPE as 'Loại',
    CREATED as 'Ngày tạo'
FROM INFORMATION_SCHEMA.ROUTINES 
WHERE ROUTINE_TYPE = 'PROCEDURE' 
  AND ROUTINE_NAME LIKE 'sp_%'
ORDER BY ROUTINE_NAME;

PRINT '';
PRINT '===============================================';
PRINT 'HOÀN THÀNH TẠO TẤT CẢ STORED PROCEDURES!';
PRINT '===============================================';
PRINT '';
PRINT 'Danh sách Stored Procedures đã tạo:';
PRINT '• LoaiPhong: 6 procedures';
PRINT '• Phong: 9 procedures';  
PRINT '• Thống kê: 4 procedures';
PRINT '• Tổng cộng: 19 procedures';
PRINT '';
PRINT 'Các Stored Procedures chính:';
PRINT '  - sp_ThemLoaiPhong, sp_LayDSLoaiPhong, sp_CapNhatLoaiPhong, sp_XoaLoaiPhong';
PRINT '  - sp_ThemPhong, sp_LayDSPhong, sp_CapNhatPhong, sp_XoaPhong';
PRINT '  - sp_ThongKePhongTheoTrangThai, sp_ThongKePhongTheoLoai, sp_ThongKePhongTheoTang';
PRINT '  - sp_TimKiemPhong, sp_KiemTraPhongDuocSuDung';
PRINT '';
PRINT 'Hệ thống đã sẵn sàng để sử dụng!';
