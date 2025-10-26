-- =============================================
-- ========== STORED PROCEDURE: CTHoaDonDichVu ==========
-- =============================================

-- 1️⃣ Thêm chi tiết dịch vụ
CREATE PROCEDURE sp_ThemCTHoaDonDichVu
    @maHD NVARCHAR(10),
    @maPhieuDV NVARCHAR(10),
    @maDV NVARCHAR(10),
    @soLuong INT,
    @donGia DECIMAL(18,2)
AS
BEGIN
    INSERT INTO CTHoaDonDichVu(maHD, maPhieuDV, maDV, soLuong, donGia)
    VALUES (@maHD, @maPhieuDV, @maDV, @soLuong, @donGia);
END;
GO

-- 2️⃣ Cập nhật số lượng hoặc đơn giá
CREATE PROCEDURE sp_CapNhatCTHoaDonDichVu
    @maHD NVARCHAR(10),
    @maDV NVARCHAR(10),
    @soLuong INT,
    @donGia DECIMAL(18,2)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE CTHoaDonDichVu
    SET soLuong = @soLuong,
        donGia = @donGia
    WHERE maHD = @maHD AND maDV = @maDV;
END;
GO

-- 3️⃣ Xóa chi tiết dịch vụ
CREATE PROCEDURE sp_XoaCTHoaDonDichVu
    @maHD NVARCHAR(10),
    @maDV NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM CTHoaDonDichVu
    WHERE maHD = @maHD AND maDV = @maDV;
END;
GO

-- 4️⃣ Lấy danh sách chi tiết dịch vụ theo hóa đơn
CREATE PROCEDURE sp_LayCTHoaDonDichVuTheoMaHD
    @maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT c.maHD, c.maPhieuDV, c.maDV, c.soLuong, c.donGia, c.thanhTien
    FROM CTHoaDonDichVu c
    WHERE c.maHD = @maHD;
END;
GO

-- 5️⃣ Tính tổng tiền dịch vụ của hóa đơn
CREATE PROCEDURE sp_TinhTongTienDichVu
    @maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT SUM(thanhTien) AS TongTienDichVu
    FROM CTHoaDonDichVu
    WHERE maHD = @maHD;
END;
GO
-- Tìm CTHoaDonDichVu theo mã HD và mã DV
CREATE PROCEDURE sp_TimCTHDDVTheoMaHDMaDV
    -- Sử dụng NVARCHAR(10) chính xác như định nghĩa cột
    @MaHD NVARCHAR(10),
    @MaDV NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        maHD,
        maPhieuDV,
        maDV,
        soLuong,
        donGia,
        thanhTien
    FROM
        CTHoaDonDichVu
    WHERE
      -- Sử dụng LTRIM/RTRIM để loại bỏ khoảng trắng thừa nếu có,
      -- tăng cường khả năng tìm kiếm chính xác
        maHD = LTRIM(RTRIM(@MaHD)) AND maDV = LTRIM(RTRIM(@MaDV));
END
GO
-- Cập nhật số lượng dịch vụ trong CTHoaDonDichVu
CREATE PROCEDURE sp_CapNhatSoLuongCTHDDV
    @MaHD NVARCHAR(10),
    @MaDV NVARCHAR(10),
    @SoLuongMoi INT
AS
BEGIN
    -- KHÔNG DÙNG SET NOCOUNT ON;

    UPDATE CTHoaDonDichVu
    SET
        soLuong = @SoLuongMoi
    WHERE
        maHD = LTRIM(RTRIM(@MaHD)) AND maDV = LTRIM(RTRIM(@MaDV));

    -- BẮT BUỘC TRẢ VỀ SỐ DÒNG BỊ ẢNH HƯỞNG NHƯ MỘT RESULT SET
    SELECT @@ROWCOUNT;
END
GO
