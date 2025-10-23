-- =============================================
-- ========== STORED PROCEDURE: CTHoaDonDichVu ==========
-- =============================================

-- 1️⃣ Thêm chi tiết dịch vụ
CREATE OR ALTER PROCEDURE sp_ThemCTHoaDonDichVu
    @maHD NVARCHAR(10),
    @maPhieuDV NVARCHAR(10),
    @maDV NVARCHAR(10),
    @soLuong INT,
    @donGia DECIMAL(18,2)
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO CTHoaDonDichVu(maHD, maPhieuDV, maDV, soLuong, donGia)
    VALUES (@maHD, @maPhieuDV, @maDV, @soLuong, @donGia);
END;
GO

-- 2️⃣ Cập nhật số lượng hoặc đơn giá
CREATE OR ALTER PROCEDURE sp_CapNhatCTHoaDonDichVu
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
CREATE OR ALTER PROCEDURE sp_XoaCTHoaDonDichVu
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
CREATE OR ALTER PROCEDURE sp_LayCTHoaDonDichVuTheoMaHD
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
CREATE OR ALTER PROCEDURE sp_TinhTongTienDichVu
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT SUM(thanhTien) AS TongTienDichVu
    FROM CTHoaDonDichVu
    WHERE maHD = @maHD;
END;
GO
