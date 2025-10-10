-- ================================================
-- Store Procedure: Quản lý Tài Khoản
-- ================================================

-- 1. Lấy danh sách tài khoản
CREATE OR ALTER PROCEDURE sp_LayDSTaiKhoan
AS
BEGIN
    SELECT maTK, maNV, tenDangNhap, quyen, trangThai
    FROM TaiKhoan;
END
GO

-- 2. Thêm tài khoản mới
CREATE OR ALTER PROCEDURE sp_ThemTaiKhoan @maTK NVARCHAR(10), -- Mã tài khoản
                                          @maNV NVARCHAR(10), -- Mã nhân viên (nếu có)
                                          @tenDangNhap NVARCHAR(50), -- Tên đăng nhập
                                          @matKhauHash NVARCHAR(255), -- Mật khẩu đã hash
                                          @quyen NVARCHAR(50), -- Quyền (Admin, Lễ tân, Quản lý, ...)
                                          @trangThai NVARCHAR(50) -- Trạng thái (Hoạt động / Khóa)
AS
BEGIN
    INSERT INTO TaiKhoan(maTK, maNV, tenDangNhap, matKhauHash, quyen, trangThai)
    VALUES (@maTK, @maNV, @tenDangNhap, @matKhauHash, @quyen, @trangThai);
END
GO

-- 3. Cập nhật thông tin tài khoản (không đổi mật khẩu)
CREATE OR ALTER PROCEDURE sp_CapNhatTaiKhoan @maTK NVARCHAR(10),
                                             @maNV NVARCHAR(10),
                                             @quyen NVARCHAR(50),
                                             @trangThai NVARCHAR(50)
AS
BEGIN
    UPDATE TaiKhoan
    SET maNV      = @maNV,
        quyen     = @quyen,
        trangThai = @trangThai
    WHERE maTK = @maTK;
END
GO

-- 4. Đổi mật khẩu
CREATE OR ALTER PROCEDURE sp_DoiMatKhau @maTK NVARCHAR(10),
                                        @matKhauHash NVARCHAR(255)
AS
BEGIN
    UPDATE TaiKhoan
    SET matKhauHash = @matKhauHash
    WHERE maTK = @maTK;
END
GO

-- 5. Xóa tài khoản
CREATE OR ALTER PROCEDURE sp_XoaTaiKhoan @maTK NVARCHAR(10)
AS
BEGIN
    DELETE FROM TaiKhoan WHERE maTK = @maTK;
END
GO

-- 6. Kiểm tra đăng nhập (Login)
CREATE OR ALTER PROCEDURE sp_DangNhap @tenDangNhap NVARCHAR(50),
                                      @matKhauHash NVARCHAR(255)
AS
BEGIN
    SELECT maTK, maNV, tenDangNhap, quyen, trangThai
    FROM TaiKhoan
    WHERE tenDangNhap = @tenDangNhap
      AND matKhauHash = @matKhauHash;
END
GO