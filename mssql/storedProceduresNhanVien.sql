-- ================================================
-- Store Procedure: Quản lý Nhân viên
-- ================================================

-- 1. Lấy toàn bộ danh sách nhân viên
CREATE PROCEDURE sp_LayDSNhanVien
AS
BEGIN
    SELECT maNV, hoTen, soDT, diaChi, chucVu, ngaySinh
    FROM NhanVien;
END
GO

-- 2. Thêm nhân viên mới
CREATE PROCEDURE sp_ThemNhanVien @maNV NVARCHAR(10), -- Mã nhân viên
                                 @hoTen NVARCHAR(100), -- Họ tên nhân viên
                                 @soDT NVARCHAR(15), -- Số điện thoại
                                 @diaChi NVARCHAR(200), -- Địa chỉ
                                 @chucVu NVARCHAR(50), -- Chức vụ
                                 @ngaySinh DATE -- Ngày sinh
AS
BEGIN
    INSERT INTO NhanVien(maNV, hoTen, soDT, diaChi, chucVu, ngaySinh)
    VALUES (@maNV, @hoTen, @soDT, @diaChi, @chucVu, @ngaySinh);
END
GO

-- 3. Cập nhật thông tin nhân viên
CREATE PROCEDURE sp_CapNhatNhanVien @hoTen NVARCHAR(100), -- Họ tên nhân viên
                                    @soDT NVARCHAR(15), -- Số điện thoại
                                    @diaChi NVARCHAR(200), -- Địa chỉ
                                    @chucVu NVARCHAR(50), -- Chức vụ
                                    @ngaySinh DATE, -- Ngày sinh
                                    @maNV NVARCHAR(10) -- Mã nhân viên cần cập nhật
AS
BEGIN
    UPDATE NhanVien
    SET hoTen   = @hoTen,
        soDT    = @soDT,
        diaChi  = @diaChi,
        chucVu  = @chucVu,
        ngaySinh= @ngaySinh
    WHERE maNV = @maNV;
END
GO

-- 4. Xóa nhân viên
CREATE PROCEDURE sp_XoaNhanVien @maNV NVARCHAR(10) -- Mã nhân viên cần xóa
AS
BEGIN
    DELETE
    FROM NhanVien
    WHERE maNV = @maNV;
END
GO

-- 5. Tìm nhân viên theo mã
CREATE PROCEDURE sp_TimNhanVienTheoMa @maNV NVARCHAR(10) -- Mã nhân viên cần tìm
AS
BEGIN
    SELECT *
    FROM NhanVien
    WHERE maNV = @maNV;
END
GO