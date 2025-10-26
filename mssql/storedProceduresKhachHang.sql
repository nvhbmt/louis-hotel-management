-- ================================================
-- Store Procedure: Quản lý Khách Hàng
-- Có thêm cột hangKhach, trangThai
-- ================================================

USE QuanLyKhachSan;
GO

-- 1. Thêm khách hàng mới
CREATE PROCEDURE sp_ThemKhachHang @maKH NVARCHAR(20),
                                  @hoTen NVARCHAR(100),
                                  @soDT NVARCHAR(15),
                                  @email NVARCHAR(100),
                                  @diaChi NVARCHAR(255),
                                  @ngaySinh DATE,
                                  @ghiChu NVARCHAR(255),
                                  @CCCD NVARCHAR(20),
                                  @hangKhach NVARCHAR(50),
                                  @trangThai NVARCHAR(50)
AS
BEGIN
    INSERT INTO KhachHang(maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD, hangKhach, trangThai)
    VALUES (@maKH, @hoTen, @soDT, @email, @diaChi, @ngaySinh, @ghiChu, @CCCD, @hangKhach, @trangThai);
END;
GO

-- 2. Lấy danh sách khách hàng
CREATE PROCEDURE sp_LayDSKhachHang
AS
BEGIN
    SELECT * FROM KhachHang;
END;
GO

-- 3. Lấy khách hàng theo mã
CREATE PROCEDURE sp_LayKhachHangTheoMa @maKH NVARCHAR(20)
AS
BEGIN
    SELECT * FROM KhachHang WHERE maKH = @maKH;
END;
GO

-- 4. Tìm kiếm khách hàng theo tên
CREATE PROCEDURE sp_TimKiemKhachHangTheoTen @ten NVARCHAR(100)
AS
BEGIN
    SELECT *
    FROM KhachHang
    WHERE hoTen LIKE '%' + @ten + '%';
END;
GO

-- 5. Tìm kiếm khách hàng theo số điện thoại
CREATE PROCEDURE sp_TimKiemKhachHangTheoSDT @soDT NVARCHAR(15)
AS
BEGIN
    SELECT *
    FROM KhachHang
    WHERE soDT LIKE '%' + @soDT + '%';
END;
GO

-- 6. Tìm kiếm khách hàng theo CCCD
CREATE PROCEDURE sp_TimKiemKhachHangTheoCCCD @CCCD NVARCHAR(20)
AS
BEGIN
    SELECT *
    FROM KhachHang
    WHERE CCCD = @CCCD;
END;
GO

-- 7. Cập nhật khách hàng
CREATE PROCEDURE sp_CapNhatKhachHang @maKH NVARCHAR(20),
                                     @hoTen NVARCHAR(100),
                                     @soDT NVARCHAR(15),
                                     @email NVARCHAR(100),
                                     @diaChi NVARCHAR(255),
                                     @ngaySinh DATE,
                                     @ghiChu NVARCHAR(255),
                                     @CCCD NVARCHAR(20),
                                     @hangKhach NVARCHAR(50),
                                     @trangThai NVARCHAR(50)
AS
BEGIN
    UPDATE KhachHang
    SET hoTen     = @hoTen,
        soDT      = @soDT,
        email     = @email,
        diaChi    = @diaChi,
        ngaySinh  = @ngaySinh,
        ghiChu    = @ghiChu,
        CCCD      = @CCCD,
        hangKhach = @hangKhach,
        trangThai = @trangThai
    WHERE maKH = @maKH;
END;
GO

-- 8. Xóa khách hàng
CREATE PROCEDURE sp_XoaKhachHang @maKH NVARCHAR(20)
AS
BEGIN
    DELETE FROM KhachHang WHERE maKH = @maKH;
END;
GO

