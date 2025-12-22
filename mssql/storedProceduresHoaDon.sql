USE QuanLyKhachSan;
GO

/* =====================================================
   1. THÊM HÓA ĐƠN
   ===================================================== */
CREATE PROCEDURE sp_ThemHoaDon
    @maHD NVARCHAR(10), @ngayLap DATE, @phuongThuc NVARCHAR(50), @tongTien DECIMAL(18,2),
    @maKH NVARCHAR(10), @maNV NVARCHAR(10), @maKM NVARCHAR(10) = NULL, @trangThai NVARCHAR(50),
    @ngayDi DATE = NULL,
    @phatNhanTre DECIMAL(18,2) = 0, @phatTraSom DECIMAL(18,2) = 0, @phatTraTre DECIMAL(18,2) = 0,
    @giamGiaMaGG DECIMAL(18,2) = 0, @giamGiaHangKH DECIMAL(18,2) = 0, @tongVAT DECIMAL(18,2) = 0
AS
BEGIN
    INSERT INTO HoaDon (
        maHD, ngayLap, phuongThuc, trangThai, tongTien, maKH, maNV, maKM, ngayDi,
        PhatNhanPhongTre, PhatTraPhongSom, PhatTraPhongTre, GiamGiaMaGG, GiamGiaHangKH, TongVAT
    )
    VALUES (
               @maHD, @ngayLap, @phuongThuc, @trangThai, @tongTien, @maKH, @maNV, @maKM, @ngayDi,
               @phatNhanTre, @phatTraSom, @phatTraTre, @giamGiaMaGG, @giamGiaHangKH, @tongVAT
           );
END;
GO

/* =====================================================
   2. SỬA HÓA ĐƠN
   ===================================================== */
CREATE PROCEDURE sp_SuaHoaDon
    @maHD NVARCHAR(10), @ngayLap DATE, @phuongThuc NVARCHAR(50), @tongTien DECIMAL(18,2),
    @maKH NVARCHAR(10), @maNV NVARCHAR(10), @maKM NVARCHAR(10), @trangThai NVARCHAR(50),
    @ngayDi DATE, @phatNhanTre DECIMAL(18,2), @phatTraSom DECIMAL(18,2), @phatTraTre DECIMAL(18,2),
    @giamGiaMaGG DECIMAL(18,2), @giamGiaHangKH DECIMAL(18,2), @tongVAT DECIMAL(18,2)
AS
BEGIN
    UPDATE HoaDon SET
                      ngayLap = @ngayLap, phuongThuc = @phuongThuc, tongTien = @tongTien, maKH = @maKH, maNV = @maNV,
                      maKM = @maKM, trangThai = @trangThai, ngayDi = @ngayDi, PhatNhanPhongTre = @phatNhanTre,
                      PhatTraPhongSom = @phatTraSom, PhatTraPhongTre = @phatTraTre, GiamGiaMaGG = @giamGiaMaGG,
                      GiamGiaHangKH = @giamGiaHangKH, TongVAT = @tongVAT
    WHERE maHD = @maHD;
END;
GO

/* =====================================================
   3. LẤY DANH SÁCH HÓA ĐƠN (Có kèm thông tin Khách hàng & Phòng)
   ===================================================== */
CREATE PROCEDURE sp_LayDanhSachHoaDon
AS
BEGIN
    SET NOCOUNT ON;
    WITH PhongGanNhat AS (
        SELECT
            ctp.maHD,
            ctp.maPhong,
            ROW_NUMBER() OVER(PARTITION BY ctp.maHD ORDER BY ctp.ngayDi DESC) AS rn
        FROM CTHoaDonPhong ctp
        WHERE ctp.daHuy = 0
    )
    SELECT
        hd.*,
        kh.hoTen, kh.soDT, kh.diaChi,
        pgn.maPhong AS soPhong
    FROM HoaDon hd
             LEFT JOIN KhachHang kh ON hd.maKH = kh.maKH
             LEFT JOIN PhongGanNhat pgn ON hd.maHD = pgn.maHD AND pgn.rn = 1;
END;
GO

/* =====================================================
   4. TÌM HÓA ĐƠN THEO MÃ (Đã sửa để lấy đầy đủ cột giống List)
   ===================================================== */
CREATE PROCEDURE sp_TimHoaDonTheoMa
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT
        hd.*,
        kh.hoTen, kh.soDT, kh.diaChi,
        (SELECT TOP 1 maPhong FROM CTHoaDonPhong WHERE maHD = hd.maHD AND daHuy = 0) AS soPhong
    FROM HoaDon hd
             LEFT JOIN KhachHang kh ON hd.maKH = kh.maKH
    WHERE hd.maHD = @maHD;
END;
GO

/* =====================================================
   5. TẠO MÃ HÓA ĐƠN TIẾP THEO
   ===================================================== */
CREATE PROCEDURE sp_TaoMaHoaDonTiepTheo
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @maCu NVARCHAR(10), @so INT, @maMoi NVARCHAR(10);
    SELECT @maCu = MAX(maHD) FROM HoaDon;
    IF @maCu IS NULL
        SET @maMoi = 'HD001';
    ELSE
        BEGIN
            SET @so = CAST(SUBSTRING(@maCu, 3, LEN(@maCu) - 2) AS INT) + 1;
            SET @maMoi = 'HD' + RIGHT('000' + CAST(@so AS NVARCHAR(3)), 3);
        END
    SELECT @maMoi AS maHDMoi;
END;
GO

/* =====================================================
   6. LẤY CHI TIẾT HÓA ĐƠN (Phòng + Dịch vụ)
   ===================================================== */
CREATE PROCEDURE sp_LayChiTietHoaDonTheoMaHD
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;
    WITH ChiTiet AS (
        SELECT
            lp.tenLoai + N' (' + p.maPhong + N')' AS TenChiTiet,
            CASE
                WHEN DATEDIFF(DAY, ctp.ngayDen, ctp.ngayDi) = 0 THEN 1
                ELSE DATEDIFF(DAY, ctp.ngayDen, ctp.ngayDi)
                END AS SoLuong,
            ctp.giaPhong AS DonGia,
            ctp.thanhTien AS ThanhTien,
            1 AS UuTien
        FROM CTHoaDonPhong ctp
                 JOIN Phong p ON ctp.maPhong = p.maPhong
                 JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
        WHERE ctp.maHD = @maHD AND ctp.daHuy = 0
        UNION ALL
        SELECT
            dv.tenDV, ctdv.soLuong, ctdv.donGia, ctdv.thanhTien, 2
        FROM CTHoaDonDichVu ctdv
                 JOIN DichVu dv ON ctdv.maDV = dv.maDV
        WHERE ctdv.maHD = @maHD
    )
    SELECT
        ROW_NUMBER() OVER(ORDER BY UuTien, TenChiTiet) AS STT,
        TenChiTiet, SoLuong, DonGia, ThanhTien
    FROM ChiTiet;
END;
GO