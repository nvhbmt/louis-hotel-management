-- ================================================
-- Store Procedure: Quản lý Mã Giảm Giá
-- ================================================

USE QuanLyKhachSan;
GO

-- 1. Lấy toàn bộ danh sách mã giảm giá
CREATE PROCEDURE sp_LayDSMaGiamGia
AS
BEGIN
    SELECT maGG,
           code,
           giamGia,
           kieuGiamGia,
           ngayBatDau,
           ngayKetThuc,
           tongTienToiThieu,
           moTa,
           trangThai,
           maNV
    FROM MaGiamGia;
END
GO

-- 2. Lấy danh sách mã giảm giá đang hoạt động
CREATE PROCEDURE sp_LayMaGiamGiaDangHoatDong
AS
BEGIN
    SELECT *
    FROM MaGiamGia
    WHERE trangThai = N'Đang diễn ra'
      AND GETDATE() BETWEEN ngayBatDau AND ngayKetThuc;
END
GO

-- 3. Thêm mã giảm giá mới
CREATE PROCEDURE sp_ThemMaGiamGia @maGG NVARCHAR(10), -- Mã giảm giá
                                  @code NVARCHAR(50), -- Code giảm giá
                                  @giamGia DECIMAL(18, 2), -- Giá trị giảm
                                  @kieuGiamGia NVARCHAR(10), -- Kiểu giảm (PERCENT / AMOUNT)
                                  @ngayBatDau DATE, -- Ngày bắt đầu
                                  @ngayKetThuc DATE, -- Ngày kết thúc
                                  @tongTienToiThieu DECIMAL(18, 2), -- Tổng tiền tối thiểu để áp dụng
                                  @moTa NVARCHAR(255), -- Mô tả
                                  @trangThai NVARCHAR(50), -- Trạng thái (Đang diễn ra / Hết hạn)
                                  @maNV NVARCHAR(10) -- Nhân viên tạo mã
AS
BEGIN
    INSERT INTO MaGiamGia(maGG, code, giamGia, kieuGiamGia, ngayBatDau, ngayKetThuc,
                          tongTienToiThieu, moTa, trangThai, maNV)
    VALUES (@maGG, @code, @giamGia, @kieuGiamGia, @ngayBatDau, @ngayKetThuc,
            @tongTienToiThieu, @moTa, @trangThai, @maNV);
END
GO

-- 4. Cập nhật mã giảm giá
CREATE PROCEDURE sp_CapNhatMaGiamGia @maGG NVARCHAR(10), -- Mã giảm giá
                                     @giamGia DECIMAL(18, 2), -- Giá trị giảm
                                     @kieuGiamGia NVARCHAR(10), -- Kiểu giảm
                                     @ngayKetThuc DATE, -- Ngày kết thúc mới
                                     @moTa NVARCHAR(255), -- Mô tả mới
                                     @trangThai NVARCHAR(50) -- Trạng thái mới
AS
BEGIN
    UPDATE MaGiamGia
    SET giamGia     = @giamGia,
        kieuGiamGia = @kieuGiamGia,
        ngayKetThuc = @ngayKetThuc,
        moTa        = @moTa,
        trangThai   = @trangThai
    WHERE maGG = @maGG;
END
GO

-- 5. Xóa mã giảm giá
CREATE PROCEDURE sp_XoaMaGiamGia @maGG NVARCHAR(10) -- Mã giảm giá cần xóa
AS
BEGIN
    DELETE
    FROM MaGiamGia
    WHERE maGG = @maGG;
END
GO

-- Store Procedure: sp_LayMaGiamGiaTheoMa
-- Mục đích: Lấy thông tin chi tiết của một mã giảm giá dựa trên Mã GG (maGG)
CREATE OR ALTER PROCEDURE sp_LayMaGiamGiaTheoMa
@maGG NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        maGG,
        code,
        giamGia,
        kieuGiamGia,
        ngayBatDau,
        ngayKetThuc,
        tongTienToiThieu,
        moTa,
        trangThai,
        maNV
    FROM
        MaGiamGia
    WHERE
        maGG = @maGG;
END;
GO