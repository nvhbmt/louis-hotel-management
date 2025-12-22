-- ================================================
-- Store Procedure: Thống kê
-- ================================================

USE QuanLyKhachSan;
GO

-- ================================================
-- 1. Lấy doanh thu theo ngày
-- ================================================
CREATE PROCEDURE sp_LayDoanhThuTheoNgay
    @tuNgay DATE,
    @denNgay DATE
AS
BEGIN
    SELECT
        FORMAT(hd.ngayLap, 'dd/MM/yyyy') as ngay,
        ISNULL(SUM(cthdp.thanhTien), 0) + ISNULL(SUM(cthddv.thanhTien), 0) as tongTien
    FROM HoaDon hd
        LEFT JOIN CTHoaDonPhong cthdp ON hd.maHD = cthdp.maHD
        LEFT JOIN CTHoaDonDichVu cthddv ON hd.maHD = cthddv.maHD
    WHERE hd.ngayLap BETWEEN @tuNgay AND @denNgay
    GROUP BY hd.ngayLap
    ORDER BY hd.ngayLap;
END;
GO

-- ================================================
-- 2. Lấy doanh thu theo tuần
-- ================================================
CREATE PROCEDURE sp_LayDoanhThuTheoTuan
    @nam INT
AS
BEGIN
    SELECT
        N'Tuần ' + CAST(DATEPART(WEEK, hd.ngayLap) AS NVARCHAR(2)) as tuan,
        ISNULL(SUM(cthdp.thanhTien), 0) + ISNULL(SUM(cthddv.thanhTien), 0) as tongTien
    FROM HoaDon hd
        LEFT JOIN CTHoaDonPhong cthdp ON hd.maHD = cthdp.maHD
        LEFT JOIN CTHoaDonDichVu cthddv ON hd.maHD = cthddv.maHD
    WHERE YEAR(hd.ngayLap) = @nam
    GROUP BY DATEPART(WEEK, hd.ngayLap)
    ORDER BY DATEPART(WEEK, hd.ngayLap);
END;
GO

-- ================================================
-- 3. Lấy doanh thu theo tháng
-- ================================================
CREATE PROCEDURE sp_LayDoanhThuTheoThang
    @nam INT
AS
BEGIN
    SELECT
        N'Tháng ' + CAST(MONTH(hd.ngayLap) AS NVARCHAR(2)) as thang,
        ISNULL(SUM(cthdp.thanhTien), 0) + ISNULL(SUM(cthddv.thanhTien), 0) as tongTien
    FROM HoaDon hd
        LEFT JOIN CTHoaDonPhong cthdp ON hd.maHD = cthdp.maHD
        LEFT JOIN CTHoaDonDichVu cthddv ON hd.maHD = cthddv.maHD
    WHERE YEAR(hd.ngayLap) = @nam
    GROUP BY MONTH(hd.ngayLap)
    ORDER BY MONTH(hd.ngayLap);
END;
GO

-- ================================================
-- 4. Lấy tổng doanh thu trong khoảng thời gian
-- ================================================
CREATE PROCEDURE sp_LayTongDoanhThu
    @tuNgay DATE,
    @denNgay DATE
AS
BEGIN
    SELECT
        ISNULL(SUM(cthdp.thanhTien), 0) + ISNULL(SUM(cthddv.thanhTien), 0) as tongDoanhThu
    FROM HoaDon hd
        LEFT JOIN CTHoaDonPhong cthdp ON hd.maHD = cthdp.maHD
        LEFT JOIN CTHoaDonDichVu cthddv ON hd.maHD = cthddv.maHD
    WHERE hd.ngayLap BETWEEN @tuNgay AND @denNgay;
END;
GO

-- ================================================
-- 5. Lấy doanh thu theo loại phòng
-- ================================================
CREATE PROCEDURE sp_LayDoanhThuTheoLoaiPhong
    @tuNgay DATE,
    @denNgay DATE
AS
BEGIN
    SELECT
        lp.tenLoai as loaiPhong,
        ISNULL(SUM(cthdp.thanhTien), 0) as tongTien
    FROM HoaDon hd
        LEFT JOIN CTHoaDonPhong cthdp ON hd.maHD = cthdp.maHD
        LEFT JOIN Phong p ON cthdp.maPhong = p.maPhong
        LEFT JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
    WHERE hd.ngayLap BETWEEN @tuNgay AND @denNgay
    GROUP BY lp.tenLoai
    ORDER BY SUM(cthdp.thanhTien) DESC;
END;
GO

-- ================================================
-- 6. Lấy số lượng đặt phòng theo tháng
-- ================================================
CREATE PROCEDURE sp_LaySoLuongDatPhongTheoThang
    @nam INT
AS
BEGIN
    SELECT
        N'Tháng ' + CAST(MONTH(pdp.ngayDat) AS NVARCHAR(2)) as thang,
        COUNT(*) as soLuong
    FROM PhieuDatPhong pdp
    WHERE YEAR(pdp.ngayDat) = @nam
    GROUP BY MONTH(pdp.ngayDat)
    ORDER BY MONTH(pdp.ngayDat);
END;
GO




