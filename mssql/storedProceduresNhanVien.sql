-- ================================================
-- Store Procedure: Quản lý Nhân viên
-- ================================================

USE QuanLyKhachSan;
GO

-- 1. Lấy toàn bộ danh sách nhân viên
CREATE PROCEDURE sp_LayDSNhanVien
AS
BEGIN
    SELECT maNV, hoTen, soDT, diaChi, chucVu, ngaySinh
    FROM NhanVien
    WHERE daXoaLuc IS NULL;
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
    WHERE maNV = @maNV AND daXoaLuc IS NULL;
END
GO

-- 4. Xóa nhân viên (soft delete)
CREATE PROCEDURE sp_XoaNhanVien @maNV NVARCHAR(10) -- Mã nhân viên cần xóa
AS
BEGIN
    UPDATE NhanVien
    SET daXoaLuc = GETDATE()
    WHERE maNV = @maNV AND daXoaLuc IS NULL;
END
GO

-- 5. Tìm nhân viên theo mã
CREATE PROCEDURE sp_TimNhanVienTheoMa @maNV NVARCHAR(10) -- Mã nhân viên cần tìm
AS
BEGIN
    SELECT *
    FROM NhanVien
    WHERE maNV = @maNV AND daXoaLuc IS NULL;
END
GO

-- 6. Lấy mã nhân viên tiếp theo
CREATE PROCEDURE sp_layMaNVTiepTheo
AS
BEGIN
    SET NOCOUNT ON;

    -- Lấy mã lớn nhất hiện tại và tăng lên 1
    DECLARE @maxMaNV NVARCHAR(10);
    DECLARE @nextNumber INT;

    SELECT @maxMaNV = MAX(maNV)
    FROM NhanVien
    WHERE maNV LIKE 'NV[0-9][0-9][0-9]';

    IF @maxMaNV IS NULL
    BEGIN
        SET @nextNumber = 1;
    END
    ELSE
    BEGIN
        -- Lấy số từ mã (bỏ 'NV' prefix)
        SET @nextNumber = CAST(SUBSTRING(@maxMaNV, 3, LEN(@maxMaNV) - 2) AS INT) + 1;
    END

    -- Trả về mã mới (NV001, NV002, etc.)
    SELECT 'NV' + RIGHT('000' + CAST(@nextNumber AS NVARCHAR(3)), 3) AS maNV;
END
GO