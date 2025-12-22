/* =========================================================
   STORED PROCEDURES – CTHoaDonPhong
   ========================================================= */

/* 1. Thêm chi tiết hóa đơn phòng */
CREATE PROCEDURE sp_ThemCTHoaDonPhong
    @maHD NVARCHAR(10),
    @maPhieu NVARCHAR(10),
    @maPhong NVARCHAR(10),
    @ngayDen DATE,
    @ngayDi DATE,
    @giaPhong DECIMAL(18,2)
AS
BEGIN
    INSERT INTO CTHoaDonPhong (
        maHD, maPhieu, maPhong, ngayDen, ngayDi, giaPhong, daHuy
    )
    VALUES (
               @maHD, @maPhieu, @maPhong, @ngayDen, @ngayDi, @giaPhong, 0
           );
END
GO

/* 2. Lấy DS CTHDP theo mã phiếu (chưa hủy) */
CREATE PROCEDURE SP_SelectCTHoaDonPhongByMaPhieu
@maPhieu NVARCHAR(10)
AS
BEGIN
    SELECT *
    FROM CTHoaDonPhong
    WHERE maPhieu = @maPhieu
      AND daHuy = 0;
END
GO

/* 3. Cập nhật ngày đến thực tế */
CREATE PROCEDURE sp_CapNhatNgayDenThucTe
    @maHD NVARCHAR(10),
    @maPhong NVARCHAR(10),
    @ngayDen DATE
AS
BEGIN
    UPDATE CTHoaDonPhong
    SET ngayDen = @ngayDen
    WHERE maHD = @maHD
      AND maPhong = @maPhong
      AND daHuy = 0;
END
GO

/* 4. Cập nhật ngày đi thực tế */
CREATE PROCEDURE sp_CapNhatNgayDiThucTe
    @maHD NVARCHAR(10),
    @maPhong NVARCHAR(10),
    @ngayDi DATE
AS
BEGIN
    UPDATE CTHoaDonPhong
    SET ngayDi = @ngayDi
    WHERE maHD = @maHD
      AND maPhong = @maPhong
      AND daHuy = 0;
END
GO

/* 5. Đổi phòng + cập nhật giá */
CREATE PROCEDURE sp_CapNhatMaPhongVaGia
    @maPhieu NVARCHAR(10),
    @maPhongCu NVARCHAR(10),
    @maPhongMoi NVARCHAR(10),
    @giaPhongMoi DECIMAL(18,2)
AS
BEGIN
    UPDATE CTHoaDonPhong
    SET maPhong = @maPhongMoi,
        giaPhong = @giaPhongMoi
    WHERE maPhieu = @maPhieu
      AND maPhong = @maPhongCu
      AND daHuy = 0;
END
GO

/* 6. Lấy DS CTHDP theo mã phòng */
CREATE PROCEDURE sp_GetCTHoaDonPhongTheoMaPhong
@maPhong NVARCHAR(10)
AS
BEGIN
    SELECT *
    FROM CTHoaDonPhong
    WHERE maPhong = @maPhong
      AND daHuy = 0;
END
GO

/* 7. Lấy DS CTHDP theo mã hóa đơn */
CREATE PROCEDURE sp_GetCTHoaDonPhongTheoMaHD
@maHD NVARCHAR(10)
AS
BEGIN
    SELECT *
    FROM CTHoaDonPhong
    WHERE maHD = @maHD
      AND daHuy = 0;
END
GO

/* 8. Tính tổng tiền phòng theo hóa đơn */
CREATE PROCEDURE sp_TinhTongTienPhongTheoHD
@maHD NVARCHAR(10)
AS
BEGIN
    SELECT
        SUM(DATEDIFF(DAY, ngayDen, ngayDi) * giaPhong) AS TongTienPhong
    FROM CTHoaDonPhong
    WHERE maHD = @maHD
      AND daHuy = 0;
END
GO

/* 9. Hủy phòng khỏi phiếu (xóa mềm) */
CREATE PROCEDURE sp_HuyPhongKhoiPhieu
    @maHD NVARCHAR(10),
    @maPhong NVARCHAR(10),
    @ketQua INT OUTPUT
AS
BEGIN
    UPDATE CTHoaDonPhong
    SET daHuy = 1,
        ngayHuy = GETDATE()
    WHERE maHD = @maHD
      AND maPhong = @maPhong
      AND daHuy = 0;

    SET @ketQua = @@ROWCOUNT;
END
GO
