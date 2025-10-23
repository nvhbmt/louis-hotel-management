-- =============================================
-- Script Name: storedProceduresKhachHang.sql
-- Description: Các stored procedure quản lý khách hàng
-- Author: Generated from KhachHangDAO.java
-- =============================================

USE QuanLyKhachSan;
GO

-- =============================================
-- Procedure: sp_ThemKhachHang
-- Description: Thêm khách hàng mới
-- =============================================
CREATE OR ALTER PROCEDURE sp_ThemKhachHang
    @maKH NVARCHAR(10),
    @hoTen NVARCHAR(100),
    @soDT NVARCHAR(15),
    @email NVARCHAR(100),
    @diaChi NVARCHAR(255),
    @ngaySinh DATE,
    @ghiChu NVARCHAR(255),
    @CCCD NVARCHAR(20)
AS
BEGIN
    SET NOCOUNT ON;
    
    BEGIN TRY
        INSERT INTO KhachHang (maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD)
        VALUES (@maKH, @hoTen, @soDT, @email, @diaChi, @ngaySinh, @ghiChu, @CCCD);
        
        RETURN 1; -- Success
    END TRY
    BEGIN CATCH
        RETURN 0; -- Error
    END CATCH
END
GO

-- =============================================
-- Procedure: sp_LayDSKhachHang
-- Description: Lấy danh sách tất cả khách hàng
-- =============================================
CREATE OR ALTER PROCEDURE sp_LayDSKhachHang
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD
    FROM KhachHang
    ORDER BY maKH;
END
GO

-- =============================================
-- Procedure: sp_LayKhachHangTheoMa
-- Description: Lấy khách hàng theo mã
-- =============================================
CREATE OR ALTER PROCEDURE sp_LayKhachHangTheoMa
    @maKH NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD
    FROM KhachHang
    WHERE maKH = @maKH;
END
GO

-- =============================================
-- Procedure: sp_TimKiemKhachHangTheoTen
-- Description: Tìm kiếm khách hàng theo tên
-- =============================================
CREATE OR ALTER PROCEDURE sp_TimKiemKhachHangTheoTen
    @ten NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD
    FROM KhachHang
    WHERE hoTen LIKE @ten
    ORDER BY hoTen;
END
GO

-- =============================================
-- Procedure: sp_TimKiemKhachHangTheoSDT
-- Description: Tìm kiếm khách hàng theo số điện thoại
-- =============================================
CREATE OR ALTER PROCEDURE sp_TimKiemKhachHangTheoSDT
    @soDT NVARCHAR(15)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD
    FROM KhachHang
    WHERE soDT LIKE @soDT
    ORDER BY hoTen;
END
GO

-- =============================================
-- Procedure: sp_TimKiemKhachHangTheoCCCD
-- Description: Tìm kiếm khách hàng theo CCCD
-- =============================================
CREATE OR ALTER PROCEDURE sp_TimKiemKhachHangTheoCCCD
    @CCCD NVARCHAR(20)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD
    FROM KhachHang
    WHERE CCCD = @CCCD;
END
GO

-- =============================================
-- Procedure: sp_CapNhatKhachHang
-- Description: Cập nhật thông tin khách hàng
-- =============================================
CREATE OR ALTER PROCEDURE sp_CapNhatKhachHang
    @maKH NVARCHAR(10),
    @hoTen NVARCHAR(100),
    @soDT NVARCHAR(15),
    @email NVARCHAR(100),
    @diaChi NVARCHAR(255),
    @ngaySinh DATE,
    @ghiChu NVARCHAR(255),
    @CCCD NVARCHAR(20)
AS
BEGIN
    SET NOCOUNT ON;
    
    BEGIN TRY
        UPDATE KhachHang 
        SET hoTen = @hoTen,
            soDT = @soDT,
            email = @email,
            diaChi = @diaChi,
            ngaySinh = @ngaySinh,
            ghiChu = @ghiChu,
            CCCD = @CCCD
        WHERE maKH = @maKH;
        
        RETURN 1; -- Success
    END TRY
    BEGIN CATCH
        RETURN 0; -- Error
    END CATCH
END
GO

-- =============================================
-- Procedure: sp_XoaKhachHang
-- Description: Xóa khách hàng
-- =============================================
CREATE OR ALTER PROCEDURE sp_XoaKhachHang
    @maKH NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    
    BEGIN TRY
        DELETE FROM KhachHang 
        WHERE maKH = @maKH;
        
        RETURN 1; -- Success
    END TRY
    BEGIN CATCH
        RETURN 0; -- Error
    END CATCH
END
GO

-- =============================================
-- Procedure: sp_KiemTraKhachHangDuocSuDung
-- Description: Kiểm tra khách hàng có được sử dụng không
-- =============================================
CREATE OR ALTER PROCEDURE sp_KiemTraKhachHangDuocSuDung
    @maKH NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT COUNT(*) as count
    FROM PhieuDatPhong 
    WHERE maKH = @maKH;
END
GO

-- =============================================
-- Các stored procedure tiện ích bổ sung
-- =============================================

-- =============================================
-- Procedure: sp_LayMaKhachHangTiepTheo
-- Description: Lấy mã khách hàng tiếp theo
-- =============================================
CREATE OR ALTER PROCEDURE sp_LayMaKhachHangTiepTheo
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @nextID NVARCHAR(10);
    
    SELECT @nextID = 'KH' + RIGHT('000000' + CAST(ISNULL(MAX(CAST(SUBSTRING(maKH, 3, 6) AS INT)), 0) + 1 AS NVARCHAR(6)), 6)
    FROM KhachHang
    WHERE maKH LIKE 'KH%';
    
    SELECT @nextID as nextMaKH;
END
GO

-- =============================================
-- Procedure: sp_KiemTraKhachHangTonTai
-- Description: Kiểm tra khách hàng tồn tại
-- =============================================
CREATE OR ALTER PROCEDURE sp_KiemTraKhachHangTonTai
    @maKH NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT COUNT(*) as count
    FROM KhachHang 
    WHERE maKH = @maKH;
END
GO

-- =============================================
-- Procedure: sp_KiemTraCCCDTonTai
-- Description: Kiểm tra CCCD đã tồn tại
-- =============================================
CREATE OR ALTER PROCEDURE sp_KiemTraCCCDTonTai
    @CCCD NVARCHAR(20),
    @maKH NVARCHAR(10) = NULL
AS
BEGIN
    SET NOCOUNT ON;
    
    IF @maKH IS NULL
    BEGIN
        SELECT COUNT(*) as count
        FROM KhachHang 
        WHERE CCCD = @CCCD;
    END
    ELSE
    BEGIN
        SELECT COUNT(*) as count
        FROM KhachHang 
        WHERE CCCD = @CCCD AND maKH != @maKH;
    END
END
GO

-- =============================================
-- Procedure: sp_KiemTraEmailTonTai
-- Description: Kiểm tra email đã tồn tại
-- =============================================
CREATE OR ALTER PROCEDURE sp_KiemTraEmailTonTai
    @email NVARCHAR(100),
    @maKH NVARCHAR(10) = NULL
AS
BEGIN
    SET NOCOUNT ON;
    
    IF @maKH IS NULL
    BEGIN
        SELECT COUNT(*) as count
        FROM KhachHang 
        WHERE email = @email;
    END
    ELSE
    BEGIN
        SELECT COUNT(*) as count
        FROM KhachHang 
        WHERE email = @email AND maKH != @maKH;
    END
END
GO

-- =============================================
-- Procedure: sp_KiemTraSDTTonTai
-- Description: Kiểm tra số điện thoại đã tồn tại
-- =============================================
CREATE OR ALTER PROCEDURE sp_KiemTraSDTTonTai
    @soDT NVARCHAR(15),
    @maKH NVARCHAR(10) = NULL
AS
BEGIN
    SET NOCOUNT ON;
    
    IF @maKH IS NULL
    BEGIN
        SELECT COUNT(*) as count
        FROM KhachHang 
        WHERE soDT = @soDT;
    END
    ELSE
    BEGIN
        SELECT COUNT(*) as count
        FROM KhachHang 
        WHERE soDT = @soDT AND maKH != @maKH;
    END
END
GO

PRINT 'Tất cả stored procedure KhachHang đã được tạo thành công!'