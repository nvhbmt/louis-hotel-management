-- =========================================================
-- KHỞI TẠO LẠI CÁC STORES PROCEDURES MỚI
-- (Sử dụng CTHoaDonPhong)
-- =========================================================

USE QuanLyKhachSan;
GO

-- 1. sp_ThemCTHoaDonPhong (Thêm mới) - Có kiểm tra conflict để tránh race condition
CREATE PROCEDURE sp_ThemCTHoaDonPhong @maHD NVARCHAR(10),
                                      @maPhieu NVARCHAR(10),
                                      @maPhong NVARCHAR(10),
                                      @ngayDen DATE,
                                      @ngayDi DATE,
                                      @giaPhong DECIMAL(18, 2)
AS
BEGIN
    SET NOCOUNT ON;
    
    -- Validate input
    IF @maHD IS NULL OR @maPhieu IS NULL OR @maPhong IS NULL
    BEGIN
        RAISERROR('Mã hóa đơn, mã phiếu và mã phòng không được để trống', 16, 1);
        RETURN;
    END
    
    IF @ngayDen IS NULL OR @ngayDi IS NULL
    BEGIN
        RAISERROR('Ngày đến và ngày đi không được để trống', 16, 1);
        RETURN;
    END
    
    IF @ngayDi <= @ngayDen
    BEGIN
        RAISERROR('Ngày đi phải sau ngày đến', 16, 1);
        RETURN;
    END
    
    -- Kiểm tra phòng đã được đặt trong khoảng thời gian này chưa (Race condition protection)
    IF EXISTS (
        SELECT 1 
        FROM CTHoaDonPhong cthdp
        INNER JOIN PhieuDatPhong pdp ON cthdp.maPhieu = pdp.maPhieu
        WHERE cthdp.maPhong = @maPhong
          AND pdp.trangThai NOT IN (N'Đã hủy', N'Hoàn thành')  -- Chỉ xét booking còn hiệu lực
          AND cthdp.ngayDen <= @ngayDi  -- Overlap check
          AND cthdp.ngayDi >= @ngayDen
          AND cthdp.maPhieu != @maPhieu  -- Loại trừ chính booking hiện tại (nếu đang update)
    )
    BEGIN
        DECLARE @errorMsg NVARCHAR(500);
        SET @errorMsg = N'Phòng ' + @maPhong + N' đã được đặt trong khoảng thời gian từ ' + 
                        CONVERT(NVARCHAR(10), @ngayDen, 103) + N' đến ' + 
                        CONVERT(NVARCHAR(10), @ngayDi, 103);
        RAISERROR(@errorMsg, 16, 1);
        RETURN;
    END
    
    -- Kiểm tra phòng có tồn tại và không phải bảo trì
    IF NOT EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong)
    BEGIN
        RAISERROR('Phòng %s không tồn tại', 16, 1, @maPhong);
        RETURN;
    END
    
    IF EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong AND trangThai = N'Bảo trì')
    BEGIN
        RAISERROR('Phòng %s đang trong trạng thái bảo trì, không thể đặt', 16, 1, @maPhong);
        RETURN;
    END
    
    -- Insert nếu không có conflict
    INSERT INTO CTHoaDonPhong
        (maHD, maPhieu, maPhong, ngayDen, ngayDi, giaPhong)
    VALUES (@maHD, @maPhieu, @maPhong, @ngayDen, @ngayDi, @giaPhong);
END
GO

-- 2. SP_SelectCTHoaDonPhongByMaPhieu (Lấy theo Mã Phiếu)
CREATE PROCEDURE SP_SelectCTHoaDonPhongByMaPhieu @maPhieu NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maHD, maPhieu, maPhong, ngayDen, ngayDi, giaPhong
    FROM CTHoaDonPhong
    WHERE maPhieu = @maPhieu;
END
GO

-- 3. sp_CapNhatNgayDenThucTe (Cập nhật Ngày Đến thực tế)
CREATE PROCEDURE sp_CapNhatNgayDenThucTe @maHD NVARCHAR(10),
                                         @maPhong NVARCHAR(10),
                                         @ngayDen DATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE CTHoaDonPhong
    SET ngayDen = @ngayDen
    WHERE maHD = @maHD
      AND maPhong = @maPhong;
END
GO

-- 4. sp_CapNhatNgayDiThucTe (Cập nhật Ngày Đi thực tế)
CREATE PROCEDURE sp_CapNhatNgayDiThucTe @maHD NVARCHAR(10),
                                        @maPhong NVARCHAR(10),
                                        @ngayDi DATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE CTHoaDonPhong
    SET ngayDi = @ngayDi
    WHERE maHD = @maHD
      AND maPhong = @maPhong;
END
GO

-- 5. sp_CapNhatMaPhongVaGia (Cập nhật Mã Phòng và Giá)
CREATE PROCEDURE sp_CapNhatMaPhongVaGia @maPhieu NVARCHAR(10),
                                        @maPhongCu NVARCHAR(10),
                                        @maPhongMoi NVARCHAR(10),
                                        @giaPhongMoi DECIMAL(18, 2)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE CTHoaDonPhong
    SET maPhong  = @maPhongMoi,
        giaPhong = @giaPhongMoi
    WHERE maPhieu = @maPhieu
      AND maPhong = @maPhongCu;
END
GO

-- 6. sp_GetCTHoaDonPhongTheoMaPhong (Lấy theo Mã Phòng)
CREATE PROCEDURE sp_GetCTHoaDonPhongTheoMaPhong @maPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maHD, maPhieu, maPhong, ngayDen, ngayDi, giaPhong
    FROM CTHoaDonPhong
    WHERE maPhong = @maPhong;
END
GO

-- 7. sp_GetCTHoaDonPhongTheoMaHD (Lấy theo Mã Hóa Đơn)
CREATE PROCEDURE sp_GetCTHoaDonPhongTheoMaHD @maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maHD, maPhieu, maPhong, ngayDen, ngayDi, giaPhong
    FROM CTHoaDonPhong
    WHERE maHD = @maHD;
END
GO

-- 8. sp_TinhTongTienPhongTheoHD (Tính Tổng Tiền Phòng)
-- Lưu ý: Logic tính tổng tiền đơn giản: (Số ngày * Giá phòng)
CREATE PROCEDURE sp_TinhTongTienPhongTheoHD @maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT SUM(
                   DATEDIFF(day, ISNULL(ngayDen, GETDATE()), ISNULL(ngayDi, GETDATE())) * giaPhong
           ) AS TongTien
    FROM CTHoaDonPhong
    WHERE maHD = @maHD;
END
GO

-- 9. sp_XoaCTHoaDonPhong (Xóa)
CREATE PROCEDURE sp_XoaCTHoaDonPhong @maHD NVARCHAR(10),
                                     @maPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    DELETE
    FROM CTHoaDonPhong
    WHERE maHD = @maHD
      AND maPhong = @maPhong;
END
GO