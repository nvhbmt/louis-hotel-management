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

-- ================================================
-- 7. Thống kê trạng thái phòng
-- ================================================
CREATE PROCEDURE sp_ThongKeTrangThaiPhong
AS
BEGIN
    SELECT
        trangThai,
        COUNT(*) as soLuong
    FROM Phong
    GROUP BY trangThai;
END;
GO

-- ================================================
-- 8. Thống kê doanh thu theo dịch vụ
-- ================================================
CREATE PROCEDURE sp_ThongKeDoanhThuDichVu
    @tuNgay DATE,
    @denNgay DATE
AS
BEGIN
    SELECT
        dv.tenDV as tenDichVu,
        ISNULL(SUM(cthddv.soLuong), 0) as tongSoLuong,
        ISNULL(SUM(cthddv.thanhTien), 0) as tongTien
    FROM HoaDon hd
        LEFT JOIN CTHoaDonDichVu cthddv ON hd.maHD = cthddv.maHD
        LEFT JOIN DichVu dv ON cthddv.maDV = dv.maDV
    WHERE hd.ngayLap BETWEEN @tuNgay AND @denNgay
    GROUP BY dv.tenDV
    ORDER BY SUM(cthddv.thanhTien) DESC;
END;
GO

-- ================================================
-- 9. Thống kê khách hàng theo tháng
-- ================================================
CREATE PROCEDURE sp_ThongKeKhachHangTheoThang
    @nam INT
AS
BEGIN
    SELECT
        N'Tháng ' + CAST(MONTH(pdp.ngayDat) AS NVARCHAR(2)) as thang,
        COUNT(DISTINCT pdp.maKH) as soKhachHangMoi,
        COUNT(*) as tongSoDatPhong
    FROM PhieuDatPhong pdp
    WHERE YEAR(pdp.ngayDat) = @nam
    GROUP BY MONTH(pdp.ngayDat)
    ORDER BY MONTH(pdp.ngayDat);
END;
GO

-- ================================================
-- 10. Thống kê tổng quan
-- ================================================
CREATE PROCEDURE sp_ThongKeTongQuan
AS
BEGIN
    SELECT
        (SELECT COUNT(*)
        FROM Phong) as tongSoPhong,
        (SELECT COUNT(*)
        FROM Phong
        WHERE trangThai = N'Trống') as phongTrong,
        (SELECT COUNT(*)
        FROM Phong
        WHERE trangThai = N'Đang sử dụng') as phongDangSuDung,
        (SELECT COUNT(*)
        FROM Phong
        WHERE trangThai = N'Đã đặt') as phongDaDat,
        (SELECT COUNT(*)
        FROM Phong
        WHERE trangThai = N'Bảo trì') as phongBaoTri,
        (SELECT COUNT(*)
        FROM KhachHang) as tongKhachHang,
        (SELECT COUNT(*)
        FROM LoaiPhong) as tongLoaiPhong,
        (SELECT COUNT(*)
        FROM PhieuDatPhong
        WHERE YEAR(ngayDat) = YEAR(GETDATE())) as datPhongTrongNam,
        (SELECT ISNULL(SUM(cthdp.thanhTien), 0) + ISNULL(SUM(cthddv.thanhTien), 0)
        FROM HoaDon hd
            LEFT JOIN CTHoaDonPhong cthdp ON hd.maHD = cthdp.maHD
            LEFT JOIN CTHoaDonDichVu cthddv ON hd.maHD = cthddv.maHD
        WHERE YEAR(hd.ngayLap) = YEAR(GETDATE())) as doanhThuTrongNam;
END;
GO

-- ================================================
-- 11. Thống kê phòng được đặt nhiều nhất
-- ================================================
CREATE PROCEDURE sp_ThongKePhongDatNhieuNhat
    @tuNgay DATE,
    @denNgay DATE
AS
BEGIN
    SELECT TOP 10
        p.maPhong,
        lp.tenLoai,
        COUNT(ctpdp.maPhong) as soLanDat,
        ISNULL(SUM(cthdp.thanhTien), 0) as tongDoanhThu
    FROM Phong p
        LEFT JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
        LEFT JOIN CTHoaDonPhong ctpdp ON p.maPhong = ctpdp.maPhong
        LEFT JOIN PhieuDatPhong pdp ON ctpdp.maPhieu = pdp.maPhieu
        LEFT JOIN HoaDon hd ON pdp.maPhieu = hd.maHD
        LEFT JOIN CTHoaDonPhong cthdp ON hd.maHD = cthdp.maHD AND cthdp.maPhong = p.maPhong
    WHERE pdp.ngayDat BETWEEN @tuNgay AND @denNgay
    GROUP BY p.maPhong, lp.tenLoai
    ORDER BY COUNT(ctpdp.maPhong) DESC;
END;
GO

-- ================================================
-- 12. Thống kê doanh thu theo nhân viên
-- ================================================
CREATE PROCEDURE sp_ThongKeDoanhThuTheoNhanVien
    @tuNgay DATE,
    @denNgay DATE
AS
BEGIN
    SELECT
        nv.hoTen as tenNhanVien,
        COUNT(hd.maHD) as soHoaDon,
        ISNULL(SUM(cthdp.thanhTien), 0) + ISNULL(SUM(cthddv.thanhTien), 0) as tongDoanhThu
    FROM NhanVien nv
        LEFT JOIN HoaDon hd ON nv.maNV = hd.maNV
        LEFT JOIN CTHoaDonPhong cthdp ON hd.maHD = cthdp.maHD
        LEFT JOIN CTHoaDonDichVu cthddv ON hd.maHD = cthddv.maHD
    WHERE hd.ngayLap BETWEEN @tuNgay AND @denNgay
    GROUP BY nv.maNV, nv.hoTen
    ORDER BY SUM(cthdp.thanhTien) + SUM(cthddv.thanhTien) DESC;
END;
GO

-- ================================================
-- 13. Thống kê sử dụng mã giảm giá
-- ================================================
CREATE PROCEDURE sp_ThongKeSuDungMaGiamGia
    @tuNgay DATE,
    @denNgay DATE
AS
BEGIN
    SELECT
        mgg.code as maGiamGia,
        mgg.giamGia,
        mgg.kieuGiamGia,
        COUNT(hd.maHD) as soLanSuDung,
        ISNULL(SUM(cthdp.thanhTien), 0) + ISNULL(SUM(cthddv.thanhTien), 0) as tongDoanhThu
    FROM MaGiamGia mgg
        LEFT JOIN HoaDon hd ON mgg.maGG = hd.maGG
        LEFT JOIN CTHoaDonPhong cthdp ON hd.maHD = cthdp.maHD
        LEFT JOIN CTHoaDonDichVu cthddv ON hd.maHD = cthddv.maHD
    WHERE hd.ngayLap BETWEEN @tuNgay AND @denNgay
    GROUP BY mgg.maGG, mgg.code, mgg.giamGia, mgg.kieuGiamGia
    ORDER BY COUNT(hd.maHD) DESC;
END;
GO

-- ================================================
-- 14. Thống kê doanh thu theo tầng
-- ================================================
CREATE PROCEDURE sp_ThongKeDoanhThuTheoTang
    @tuNgay DATE,
    @denNgay DATE
AS
BEGIN
    SELECT
        p.tang,
        COUNT(DISTINCT p.maPhong) as soPhong,
        ISNULL(SUM(cthdp.thanhTien), 0) as tongDoanhThu
    FROM Phong p
        LEFT JOIN CTHoaDonPhong cthdp ON p.maPhong = cthdp.maPhong
        LEFT JOIN HoaDon hd ON cthdp.maHD = hd.maHD
    WHERE hd.ngayLap BETWEEN @tuNgay AND @denNgay
    GROUP BY p.tang
    ORDER BY p.tang;
END;
GO

-- ================================================
-- 15. Thống kê tỷ lệ hủy đặt phòng
-- ================================================
CREATE PROCEDURE sp_ThongKeTyLeHuyDatPhong
    @tuNgay DATE,
    @denNgay DATE
AS
BEGIN
    SELECT
        COUNT(*) as tongSoDatPhong,
        SUM(CASE WHEN pdp.trangThai = N'Hủy' THEN 1 ELSE 0 END) as soPhongHuy,
        CAST(SUM(CASE WHEN pdp.trangThai = N'Hủy' THEN 1 ELSE 0 END) * 100.0 / COUNT(*) AS DECIMAL(5,2)) as tyLeHuy
    FROM CTHoaDonPhong cthdp
        INNER JOIN PhieuDatPhong pdp ON cthdp.maPhieu = pdp.maPhieu
    WHERE pdp.ngayDat BETWEEN @tuNgay AND @denNgay;
END;
GO