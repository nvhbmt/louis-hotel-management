-- ================================================
-- Store Procedure: Quản lý Tài Khoản
-- ================================================

-- 1. Lấy danh sách tài khoản
CREATE PROCEDURE sp_LayDSTaiKhoan
AS
BEGIN
    SELECT maTK, maNV, tenDangNhap, matKhauHash, quyen, trangThai
    FROM TaiKhoan;
END
GO

-- 2. Thêm tài khoản mới
CREATE PROCEDURE sp_ThemTaiKhoan @maTK NVARCHAR(10), -- Mã tài khoản
                                 @maNV NVARCHAR(10), -- Mã nhân viên (nếu có)
                                 @tenDangNhap NVARCHAR(50), -- Tên đăng nhập
                                 @matKhauHash NVARCHAR(255), -- Mật khẩu đã hash
                                 @quyen NVARCHAR(50), -- Quyền (Manager, Staff)
                                 @trangThai BIT -- Trạng thái (1 = Hoạt động, 0 = Khóa)
AS
BEGIN
    INSERT INTO TaiKhoan(maTK, maNV, tenDangNhap, matKhauHash, quyen, trangThai)
    VALUES (@maTK, @maNV, @tenDangNhap, @matKhauHash, @quyen, @trangThai);
END
GO

-- 3. Cập nhật thông tin tài khoản
CREATE PROCEDURE sp_CapNhatTaiKhoan @maTK NVARCHAR(10),
                                    @maNV NVARCHAR(10),
                                    @tenDangNhap NVARCHAR(50),
                                    @matKhauHash NVARCHAR(255),
                                    @quyen NVARCHAR(50),
                                    @trangThai BIT
AS
BEGIN
    UPDATE TaiKhoan
    SET maNV         = @maNV,
        tenDangNhap  = @tenDangNhap,
        matKhauHash  = @matKhauHash,
        quyen        = @quyen,
        trangThai    = @trangThai
    WHERE maTK = @maTK;
END
GO

-- 4. Đổi mật khẩu
CREATE PROCEDURE sp_DoiMatKhau @maTK NVARCHAR(10),
                               @matKhauHash NVARCHAR(255)
AS
BEGIN
    UPDATE TaiKhoan
    SET matKhauHash = @matKhauHash
    WHERE maTK = @maTK;
END
GO

-- 5. Xóa tài khoản
CREATE PROCEDURE sp_XoaTaiKhoan @maTK NVARCHAR(10)
AS
BEGIN
    DELETE FROM TaiKhoan WHERE maTK = @maTK;
END
GO

-- 5.1. Tìm tài khoản theo tên đăng nhập
CREATE PROCEDURE sp_TimTaiKhoanTheoTenDangNhap @tenDangNhap NVARCHAR(50)
AS
BEGIN
    SELECT maTK, maNV, tenDangNhap, matKhauHash, quyen, trangThai
    FROM TaiKhoan
    WHERE tenDangNhap = @tenDangNhap;
END
GO

-- 6. Kiểm tra đăng nhập (Login)
CREATE PROCEDURE sp_DangNhapTaiKhoan @tenDangNhap NVARCHAR(50),
                                     @matKhauHash NVARCHAR(255)
AS
BEGIN
    SELECT maTK, maNV, tenDangNhap, matKhauHash, quyen, trangThai
    FROM TaiKhoan
    WHERE tenDangNhap = @tenDangNhap
      AND matKhauHash = @matKhauHash
      AND trangThai = 'Hoạt động';
END
GO