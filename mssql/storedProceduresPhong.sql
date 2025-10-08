-- =============================================
-- Stored Procedures cho hệ thống quản lý phòng
-- =============================================

USE QuanLyKhachSan;
GO

-- =============================================
-- Stored Procedures cho bảng LoaiPhong
-- =============================================

-- Thêm loại phòng
CREATE OR ALTER PROCEDURE sp_ThemLoaiPhong
    @maLoaiPhong NVARCHAR(10),
    @tenLoai NVARCHAR(100),
    @moTa NVARCHAR(255),
    @donGia DECIMAL(18,2)
AS
BEGIN
    SET NOCOUNT ON;

    -- Kiểm tra mã loại phòng đã tồn tại chưa
    IF EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong)
        BEGIN
            RAISERROR('Mã loại phòng đã tồn tại!', 16, 1);
            RETURN;
        END

    -- Kiểm tra dữ liệu đầu vào
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

    -- Thêm loại phòng
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

    -- Kiểm tra loại phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong)
        BEGIN
            RAISERROR('Loại phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Kiểm tra dữ liệu đầu vào
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

    -- Cập nhật loại phòng
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

    -- Kiểm tra loại phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong)
        BEGIN
            RAISERROR('Loại phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Kiểm tra loại phòng có đang được sử dụng không
    IF EXISTS (SELECT 1 FROM Phong WHERE maLoaiPhong = @maLoaiPhong)
        BEGIN
            RAISERROR('Không thể xóa loại phòng này vì đang được sử dụng!', 16, 1);
            RETURN;
        END

    -- Xóa loại phòng
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

-- =============================================
-- Stored Procedures cho bảng Phong
-- =============================================

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

    -- Kiểm tra mã phòng đã tồn tại chưa
    IF EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong)
        BEGIN
            RAISERROR('Mã phòng đã tồn tại!', 16, 1);
            RETURN;
        END

    -- Kiểm tra dữ liệu đầu vào
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

    -- Kiểm tra loại phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong)
        BEGIN
            RAISERROR('Loại phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Thêm phòng
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

    -- Kiểm tra phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong)
        BEGIN
            RAISERROR('Phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Kiểm tra dữ liệu đầu vào
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

    -- Kiểm tra loại phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong)
        BEGIN
            RAISERROR('Loại phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Cập nhật phòng
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

    -- Kiểm tra phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong)
        BEGIN
            RAISERROR('Phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Kiểm tra trạng thái
    IF @trangThai IS NULL OR LTRIM(RTRIM(@trangThai)) = ''
        BEGIN
            RAISERROR('Trạng thái không được để trống!', 16, 1);
            RETURN;
        END

    -- Cập nhật trạng thái phòng
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

    -- Kiểm tra phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong)
        BEGIN
            RAISERROR('Phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Kiểm tra phòng có đang được sử dụng không
    IF EXISTS (SELECT 1 FROM CTPhieuDatPhong WHERE maPhong = @maPhong)
        BEGIN
            RAISERROR('Không thể xóa phòng này vì đang được sử dụng!', 16, 1);
            RETURN;
        END

    -- Xóa phòng
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

-- =============================================
-- Stored Procedures bổ sung cho thống kê và báo cáo
-- =============================================

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

PRINT 'Đã tạo thành công tất cả stored procedures cho hệ thống quản lý phòng!';
GO

-- ================================================
-- Store Procedure: Quản lý Nhân viên
-- ================================================

-- 1. Lấy toàn bộ danh sách nhân viên
CREATE OR ALTER PROCEDURE sp_LayDSNhanVien
AS
BEGIN
    SELECT maNV, hoTen, soDT, diaChi, chucVu, ngaySinh
    FROM NhanVien;
END
GO

-- 2. Thêm nhân viên mới
CREATE OR ALTER PROCEDURE sp_ThemNhanVien
    @maNV NVARCHAR(10),        -- Mã nhân viên
    @hoTen NVARCHAR(100),      -- Họ tên nhân viên
    @soDT NVARCHAR(15),        -- Số điện thoại
    @diaChi NVARCHAR(200),     -- Địa chỉ
    @chucVu NVARCHAR(50),      -- Chức vụ
    @ngaySinh DATE             -- Ngày sinh
AS
BEGIN
    INSERT INTO NhanVien(maNV, hoTen, soDT, diaChi, chucVu, ngaySinh)
    VALUES (@maNV, @hoTen, @soDT, @diaChi, @chucVu, @ngaySinh);
END
GO

-- 3. Cập nhật thông tin nhân viên
CREATE OR ALTER PROCEDURE sp_CapNhatNhanVien
    @hoTen NVARCHAR(100),      -- Họ tên nhân viên
    @soDT NVARCHAR(15),        -- Số điện thoại
    @diaChi NVARCHAR(200),     -- Địa chỉ
    @chucVu NVARCHAR(50),      -- Chức vụ
    @ngaySinh DATE,            -- Ngày sinh
    @maNV NVARCHAR(10)         -- Mã nhân viên cần cập nhật
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
CREATE OR ALTER PROCEDURE sp_XoaNhanVien
@maNV NVARCHAR(10)         -- Mã nhân viên cần xóa
AS
BEGIN
    DELETE FROM NhanVien
    WHERE maNV = @maNV;
END
GO

-- 5. Tìm nhân viên theo mã
CREATE OR ALTER PROCEDURE sp_TimNhanVienTheoMa
@maNV NVARCHAR(10)         -- Mã nhân viên cần tìm
AS
BEGIN
    SELECT *
    FROM NhanVien
    WHERE maNV = @maNV;
END
GO

-- ================================================
-- Store Procedure: Quản lý Mã Giảm Giá
-- ================================================

-- 1. Lấy toàn bộ danh sách mã giảm giá
CREATE OR ALTER PROCEDURE sp_LayDSMaGiamGia
AS
BEGIN
    SELECT maGG, code, giamGia, kieuGiamGia, ngayBatDau, ngayKetThuc,
           tongTienToiThieu, moTa, trangThai, maNV
    FROM MaGiamGia;
END
GO

-- 2. Lấy danh sách mã giảm giá đang hoạt động
CREATE OR ALTER PROCEDURE sp_LayMaGiamGiaDangHoatDong
AS
BEGIN
    SELECT *
    FROM MaGiamGia
    WHERE trangThai = N'Đang diễn ra'
      AND GETDATE() BETWEEN ngayBatDau AND ngayKetThuc;
END
GO

-- 3. Thêm mã giảm giá mới
CREATE OR ALTER PROCEDURE sp_ThemMaGiamGia
    @maGG NVARCHAR(10),                -- Mã giảm giá
    @code NVARCHAR(50),                -- Code giảm giá
    @giamGia DECIMAL(18,2),            -- Giá trị giảm
    @kieuGiamGia NVARCHAR(10),         -- Kiểu giảm (PERCENT / AMOUNT)
    @ngayBatDau DATE,                  -- Ngày bắt đầu
    @ngayKetThuc DATE,                 -- Ngày kết thúc
    @tongTienToiThieu DECIMAL(18,2),   -- Tổng tiền tối thiểu để áp dụng
    @moTa NVARCHAR(255),               -- Mô tả
    @trangThai NVARCHAR(50),           -- Trạng thái (Đang diễn ra / Hết hạn)
    @maNV NVARCHAR(10)                 -- Nhân viên tạo mã
AS
BEGIN
    INSERT INTO MaGiamGia(maGG, code, giamGia, kieuGiamGia, ngayBatDau, ngayKetThuc,
                          tongTienToiThieu, moTa, trangThai, maNV)
    VALUES (@maGG, @code, @giamGia, @kieuGiamGia, @ngayBatDau, @ngayKetThuc,
            @tongTienToiThieu, @moTa, @trangThai, @maNV);
END
GO

-- 4. Cập nhật mã giảm giá
CREATE OR ALTER PROCEDURE sp_CapNhatMaGiamGia
    @maGG NVARCHAR(10),                -- Mã giảm giá
    @giamGia DECIMAL(18,2),            -- Giá trị giảm
    @kieuGiamGia NVARCHAR(10),         -- Kiểu giảm
    @ngayKetThuc DATE,                 -- Ngày kết thúc mới
    @moTa NVARCHAR(255),               -- Mô tả mới
    @trangThai NVARCHAR(50)            -- Trạng thái mới
AS
BEGIN
    UPDATE MaGiamGia
    SET giamGia   = @giamGia,
        kieuGiamGia = @kieuGiamGia,
        ngayKetThuc = @ngayKetThuc,
        moTa        = @moTa,
        trangThai   = @trangThai
    WHERE maGG = @maGG;
END
GO

-- 5. Xóa mã giảm giá
CREATE OR ALTER PROCEDURE sp_XoaMaGiamGia
@maGG NVARCHAR(10)                 -- Mã giảm giá cần xóa
AS
BEGIN
    DELETE FROM MaGiamGia
    WHERE maGG = @maGG;
END
GO

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
CREATE OR ALTER PROCEDURE sp_ThemTaiKhoan
    @maTK NVARCHAR(10),             -- Mã tài khoản
    @maNV NVARCHAR(10),             -- Mã nhân viên (nếu có)
    @tenDangNhap NVARCHAR(50),      -- Tên đăng nhập
    @matKhauHash NVARCHAR(255),     -- Mật khẩu đã hash
    @quyen NVARCHAR(50),            -- Quyền (Admin, Lễ tân, Quản lý, ...)
    @trangThai NVARCHAR(50)         -- Trạng thái (Hoạt động / Khóa)
AS
BEGIN
    INSERT INTO TaiKhoan(maTK, maNV, tenDangNhap, matKhauHash, quyen, trangThai)
    VALUES (@maTK, @maNV, @tenDangNhap, @matKhauHash, @quyen, @trangThai);
END
GO

-- 3. Cập nhật thông tin tài khoản (không đổi mật khẩu)
CREATE OR ALTER PROCEDURE sp_CapNhatTaiKhoan
    @maTK NVARCHAR(10),
    @maNV NVARCHAR(10),
    @quyen NVARCHAR(50),
    @trangThai NVARCHAR(50)
AS
BEGIN
    UPDATE TaiKhoan
    SET maNV = @maNV,
        quyen = @quyen,
        trangThai = @trangThai
    WHERE maTK = @maTK;
END
GO

-- 4. Đổi mật khẩu
CREATE OR ALTER PROCEDURE sp_DoiMatKhau
    @maTK NVARCHAR(10),
    @matKhauHash NVARCHAR(255)
AS
BEGIN
    UPDATE TaiKhoan
    SET matKhauHash = @matKhauHash
    WHERE maTK = @maTK;
END
GO

-- 5. Xóa tài khoản
CREATE OR ALTER PROCEDURE sp_XoaTaiKhoan
@maTK NVARCHAR(10)
AS
BEGIN
    DELETE FROM TaiKhoan WHERE maTK = @maTK;
END
GO

-- 6. Kiểm tra đăng nhập (Login)
CREATE OR ALTER PROCEDURE sp_DangNhap
    @tenDangNhap NVARCHAR(50),
    @matKhauHash NVARCHAR(255)
AS
BEGIN
    SELECT maTK, maNV, tenDangNhap, quyen, trangThai
    FROM TaiKhoan
    WHERE tenDangNhap = @tenDangNhap AND matKhauHash = @matKhauHash;
END
GO
