USE QuanLyKhachSan;
GO

/* =========================================================
   1. THÊM CHI TIẾT HÓA ĐƠN PHÒNG (CHECK CONFLICT – daHuy = 0)
   ========================================================= */
CREATE PROCEDURE sp_ThemCTHoaDonPhong
    @maHD NVARCHAR(10),
    @maPhieu NVARCHAR(10),
    @maPhong NVARCHAR(10),
    @ngayDen DATE,
    @ngayDi DATE,
    @giaPhong DECIMAL(18,2)
AS
BEGIN
    SET NOCOUNT ON;

    IF @ngayDi <= @ngayDen
        BEGIN
            RAISERROR(N'Ngày đi phải sau ngày đến', 16, 1);
            RETURN;
        END

    -- Check phòng trùng lịch (CHỈ phòng chưa hủy)
    IF EXISTS (
        SELECT 1
        FROM CTHoaDonPhong c
                 JOIN PhieuDatPhong p ON c.maPhieu = p.maPhieu
        WHERE c.maPhong = @maPhong
          AND c.daHuy = 0
          AND p.trangThai NOT IN (N'Đã hủy', N'Hoàn thành')
          AND c.ngayDen <= @ngayDi
          AND c.ngayDi >= @ngayDen
          AND c.maPhieu <> @maPhieu
    )
        BEGIN
            RAISERROR(N'Phòng đã được đặt trong khoảng thời gian này', 16, 1);
            RETURN;
        END

    INSERT INTO CTHoaDonPhong
    (maHD, maPhieu, maPhong, ngayDen, ngayDi, giaPhong, daHuy)
    VALUES
        (@maHD, @maPhieu, @maPhong, @ngayDen, @ngayDi, @giaPhong, 0);
END
GO




/* =========================================================
   3. LẤY CHI TIẾT THEO MÃ HÓA ĐƠN (KHÔNG LỌC daHuy)
   ========================================================= */
CREATE PROCEDURE sp_GetCTHoaDonPhongTheoMaHD
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT maHD, maPhieu, maPhong,
           ngayDen, ngayDi, giaPhong,
           thanhTien, daHuy, ngayHuy
    FROM CTHoaDonPhong
    WHERE maHD = @maHD
    and daHuy = 0;
END
GO


/******************************************************
   4. ĐỔI PHÒNG + CẬP NHẬT GIÁ (CHỈ PHÒNG CHƯA HỦY)
******************************************************/
CREATE PROCEDURE sp_CapNhatMaPhongVaGia
    @maPhieu NVARCHAR(10),
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
      AND maPhong = @maPhongCu
      AND daHuy = 0;
END
GO


/******************************************************
   5. CẬP NHẬT NGÀY ĐẾN THỰC TẾ (CHỈ PHÒNG CHƯA HỦY)
******************************************************/
CREATE PROCEDURE sp_CapNhatNgayDenThucTe
    @maHD NVARCHAR(10),
    @maPhong NVARCHAR(10),
    @ngayDen DATE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE CTHoaDonPhong
    SET ngayDen = @ngayDen
    WHERE maHD = @maHD
      AND maPhong = @maPhong
      AND daHuy = 0;
END
GO


/******************************************************
   6. CẬP NHẬT NGÀY ĐI THỰC TẾ (CHỈ PHÒNG CHƯA HỦY)
******************************************************/
CREATE PROCEDURE sp_CapNhatNgayDiThucTe
    @maHD NVARCHAR(10),
    @maPhong NVARCHAR(10),
    @ngayDi DATE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE CTHoaDonPhong
    SET ngayDi = @ngayDi
    WHERE maHD = @maHD
      AND maPhong = @maPhong
      AND daHuy = 0;
END
GO


/******************************************************
   7. TÍNH TỔNG TIỀN PHÒNG THEO HÓA ĐƠN (daHuy = 0)
******************************************************/
CREATE PROCEDURE sp_TinhTongTienPhongTheoHD
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        SUM(
                CASE
                    WHEN DATEDIFF(DAY, ngayDen, ngayDi) = 0 THEN giaPhong
                    ELSE DATEDIFF(DAY, ngayDen, ngayDi) * giaPhong
                    END
        ) AS TongTienPhong
    FROM CTHoaDonPhong
    WHERE maHD = @maHD
      AND daHuy = 0;
END
GO


/******************************************************
   8. XÓA MỀM PHÒNG KHỎI PHIẾU ĐẶT
******************************************************/
CREATE PROCEDURE sp_HuyPhongKhoiPhieu
    @maHD NVARCHAR(10),
    @maPhong NVARCHAR(10),
    @ketQua INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE CTHoaDonPhong
    SET daHuy = 1,
        ngayHuy = GETDATE()
    WHERE maHD = @maHD
      AND maPhong = @maPhong
      AND daHuy = 0;

    SET @ketQua = @@ROWCOUNT;
END
GO

CREATE PROCEDURE sp_GetCTHoaDonPhongTheoMaPhong
@maPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        maHD,
        maPhieu,
        maPhong,
        ngayDen,
        ngayDi,
        giaPhong,
        thanhTien,
        daHuy,
        ngayHuy
    FROM CTHoaDonPhong
    WHERE maPhong = @maPhong
      AND daHuy = 0; -- chỉ lấy các chi tiết chưa bị hủy
END
GO

