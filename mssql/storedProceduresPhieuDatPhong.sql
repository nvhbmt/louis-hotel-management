USE QuanLyKhachSan;
GO

-- =============================================
-- 1. Thêm phiếu đặt phòng
-- =============================================
CREATE PROCEDURE sp_ThemPhieuDatPhong @maPhieu NVARCHAR(10),
                                      @ngayDat DATE = NULL,
                                      @ngayDen DATE = NULL,
                                      @ngayDi DATE = NULL,
                                      @trangThai NVARCHAR(50),
                                      @ghiChu NVARCHAR(255) = NULL,
                                      @maKH NVARCHAR(10),
                                      @maNV NVARCHAR(10),
                                      @tienCoc DECIMAL(18, 2) = 0.00 -- THÊM THAM SỐ TIỀN CỌC
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO PhieuDatPhong(maPhieu, ngayDat, ngayDen, ngayDi, trangThai, ghiChu, maKH, maNV, tienCoc)
    VALUES (@maPhieu, @ngayDat, @ngayDen, @ngayDi, @trangThai, @ghiChu, @maKH, @maNV, @tienCoc);
END;
GO

-- =============================================
-- sp_SinhMaPhieuDatPhongTiepTheo (Giữ nguyên logic)
-- =============================================
CREATE PROCEDURE sp_SinhMaPhieuDatPhongTiepTheo
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @maPhieuCuoi NVARCHAR(10);
    DECLARE @soMoi INT;
    DECLARE @maMoi NVARCHAR(10);

    SELECT @maPhieuCuoi = MAX(maPhieu) FROM PhieuDatPhong;

    IF @maPhieuCuoi IS NULL
        BEGIN
            SET @maMoi = 'PD001';
        END
    ELSE
        BEGIN
            SET @soMoi = CAST(SUBSTRING(@maPhieuCuoi, 4, LEN(@maPhieuCuoi) - 3) AS INT) + 1;
            SET @maMoi = 'PD' + RIGHT('000' + CAST(@soMoi AS NVARCHAR(3)), 3);
        END

    SELECT @maMoi AS maPhieuMoi;
END;
GO

-- =============================================
-- 2. Lấy danh sách tất cả phiếu đặt phòng
-- =============================================
CREATE PROCEDURE sp_LayDSPhieuDatPhong
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maPhieu,
           ngayDat,
           ngayDen,
           ngayDi,
           trangThai,
           ghiChu,
           maKH,
           maNV,
           tienCoc -- THÊM tienCoc
    FROM PhieuDatPhong
    ORDER BY ngayDat DESC;
END;
GO

-- =============================================
-- 3. Lấy phiếu đặt phòng theo mã
-- =============================================
CREATE PROCEDURE sp_LayPhieuDatPhongTheoMa @maPhieu NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maPhieu,
           ngayDat,
           ngayDen,
           ngayDi,
           trangThai,
           ghiChu,
           maKH,
           maNV,
           tienCoc -- THÊM tienCoc
    FROM PhieuDatPhong
    WHERE maPhieu = @maPhieu;
END;
GO

-- =============================================
-- 4. Lấy danh sách phiếu đặt phòng theo khách hàng
-- =============================================
CREATE PROCEDURE sp_LayDSPhieuDatPhongTheoKhachHang @maKH NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maPhieu,
           ngayDat,
           ngayDen,
           ngayDi,
           trangThai,
           ghiChu,
           maKH,
           maNV,
           tienCoc -- THÊM tienCoc
    FROM PhieuDatPhong
    WHERE maKH = @maKH
    ORDER BY ngayDat DESC;
END;
GO

-- =============================================
-- 5. Lấy danh sách phiếu đặt phòng theo nhân viên
-- =============================================
CREATE PROCEDURE sp_LayDSPhieuDatPhongTheoNhanVien @maNV NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maPhieu,
           ngayDat,
           ngayDen,
           ngayDi,
           trangThai,
           ghiChu,
           maKH,
           maNV,
           tienCoc -- THÊM tienCoc
    FROM PhieuDatPhong
    WHERE maNV = @maNV
    ORDER BY ngayDat DESC;
END;
GO

-- =============================================
-- 6. Lấy danh sách phiếu đặt phòng theo trạng thái
-- =============================================
CREATE PROCEDURE sp_LayDSPhieuDatPhongTheoTrangThai @trangThai NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maPhieu,
           ngayDat,
           ngayDen,
           ngayDi,
           trangThai,
           ghiChu,
           maKH,
           maNV,
           tienCoc -- THÊM tienCoc
    FROM PhieuDatPhong
    WHERE trangThai = @trangThai
    ORDER BY ngayDat DESC;
END;
GO

-- =============================================
-- 7. Lấy danh sách phiếu đặt phòng trong khoảng thời gian
-- =============================================
CREATE PROCEDURE sp_LayDSPhieuDatPhongTrongKhoangThoiGian @ngayBatDau DATE,
                                                          @ngayKetThuc DATE
AS
BEGIN
    SET NOCOUNT ON;
    SELECT maPhieu,
           ngayDat,
           ngayDen,
           ngayDi,
           trangThai,
           ghiChu,
           maKH,
           maNV,
           tienCoc -- THÊM tienCoc
    FROM PhieuDatPhong
    WHERE ngayDat BETWEEN @ngayBatDau AND @ngayKetThuc
    ORDER BY ngayDat DESC;
END;
GO

-- =============================================
-- 8. Cập nhật toàn bộ thông tin phiếu đặt phòng
-- =============================================
CREATE PROCEDURE sp_CapNhatPhieuDatPhong @maPhieu NVARCHAR(10),
                                         @ngayDat DATE = NULL,
                                         @ngayDen DATE = NULL,
                                         @ngayDi DATE = NULL,
                                         @trangThai NVARCHAR(50),
                                         @ghiChu NVARCHAR(255) = NULL,
                                         @maKH NVARCHAR(10),
                                         @maNV NVARCHAR(10),
                                         @tienCoc DECIMAL(18, 2) = NULL -- THÊM THAM SỐ TIỀN CỌC
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE PhieuDatPhong
    SET ngayDat   = @ngayDat,
        ngayDen   = @ngayDen,
        ngayDi    = @ngayDi,
        trangThai = @trangThai,
        ghiChu    = @ghiChu,
        maKH      = @maKH,
        maNV      = @maNV,
        tienCoc   = @tienCoc -- CẬP NHẬT TIỀN CỌC
    WHERE maPhieu = @maPhieu AND daXoaLuc IS NULL;
END;
GO

-- =============================================
-- 9. Cập nhật trạng thái phiếu đặt phòng (Giữ nguyên)
-- =============================================
CREATE PROCEDURE sp_CapNhatTrangThaiPhieuDatPhong @maPhieu NVARCHAR(10),
                                                  @trangThai NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE PhieuDatPhong
    SET trangThai = @trangThai
    WHERE maPhieu = @maPhieu;
END;
GO
