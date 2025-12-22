-- ================================================
-- Store Procedure: Quản lý Mã Giảm Giá
-- ================================================

USE QuanLyKhachSan;
GO

-- 1. Lấy toàn bộ danh sách mã giảm giá
CREATE PROCEDURE sp_LayDSKhuyenMai
AS
BEGIN
    SELECT maKM,
           code,
           giamGia,
           kieuGiamGia,
           ngayBatDau,
           ngayKetThuc,
           tongTienToiThieu,
           moTa,
           trangThai,
           maNV
    FROM KhuyenMai;
END
GO

-- 2. Lấy danh sách mã giảm giá đang hoạt động
CREATE PROCEDURE sp_LayKhuyenMaiDangHoatDong
AS
BEGIN
    SELECT *
    FROM KhuyenMai
    WHERE trangThai = N'Đang diễn ra'
      AND GETDATE() BETWEEN ngayBatDau AND ngayKetThuc;
END
GO

-- 3. Thêm mã giảm giá mới
CREATE PROCEDURE sp_ThemKhuyenMai @maKM NVARCHAR(10), -- Mã khuyến mãi
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
    INSERT INTO KhuyenMai(maKM, code, giamGia, kieuGiamGia, ngayBatDau, ngayKetThuc,
                          tongTienToiThieu, moTa, trangThai, maNV)
    VALUES (@maKM, @code, @giamGia, @kieuGiamGia, @ngayBatDau, @ngayKetThuc,
            @tongTienToiThieu, @moTa, @trangThai, @maNV);
END
GO

-- 4. Cập nhật mã giảm giá
CREATE PROCEDURE sp_CapNhatKhuyenMai @maKM NVARCHAR(10), -- Mã khuyến mãi
                                     @code NVARCHAR(50), -- Code giảm giá
                                     @giamGia DECIMAL(18, 2), -- Giá trị giảm
                                     @kieuGiamGia NVARCHAR(10), -- Kiểu giảm
                                     @ngayKetThuc DATE, -- Ngày kết thúc mới
                                     @moTa NVARCHAR(255), -- Mô tả mới
                                     @trangThai NVARCHAR(50) -- Trạng thái mới
AS
BEGIN
    UPDATE KhuyenMai
    SET code        = @code,
        giamGia     = @giamGia,
        kieuGiamGia = @kieuGiamGia,
        ngayKetThuc = @ngayKetThuc,
        moTa        = @moTa,
        trangThai   = @trangThai
    WHERE maKM = @maKM;
END
GO

-- 5. Xóa mã giảm giá
CREATE PROCEDURE sp_XoaKhuyenMai @maKM NVARCHAR(10) -- Mã khuyến mãi cần xóa
AS
BEGIN
    DELETE
    FROM KhuyenMai
    WHERE maKM = @maKM;
END
GO

-- Store Procedure: sp_LayKhuyenMaiTheoMa
-- Mục đích: Lấy thông tin chi tiết của một mã khuyến mãi dựa trên Mã KM (maKM)
CREATE PROCEDURE sp_LayKhuyenMaiTheoMa
@maKM NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        maKM,
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
        KhuyenMai
    WHERE
        maKM = @maKM;
END;
GO

-- Store Procedure: sp_layMaKMTiepTheo
-- Mục đích: Lấy mã khuyến mãi tiếp theo
CREATE OR ALTER PROCEDURE sp_layMaKMTiepTheo
AS
BEGIN
    SET NOCOUNT ON;

    -- Lấy mã lớn nhất hiện tại và tăng lên 1
    DECLARE @maxMaKM NVARCHAR(10);
    DECLARE @nextNumber INT;

    SELECT @maxMaKM = MAX(maKM)
    FROM KhuyenMai
    WHERE maKM LIKE 'KM%';

    IF @maxMaKM IS NULL
    BEGIN
        SET @nextNumber = 1;
    END
    ELSE
    BEGIN
        -- Lấy số từ mã (bỏ 'KM' prefix)
        SET @nextNumber = CAST(SUBSTRING(@maxMaKM, 3, LEN(@maxMaKM) - 2) AS INT) + 1;
    END

    -- Trả về mã mới
    SELECT 'KM' + RIGHT('00000' + CAST(@nextNumber AS NVARCHAR(5)), 5) AS maKM;
END;
GO
