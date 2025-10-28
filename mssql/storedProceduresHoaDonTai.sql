/* * SỬA 1: Đảm bảo các SP được TẠO RA trong CSDL của ứng dụng.
 */
USE QuanLyKhachSan;
GO

-- =============================================
-- ========== CÁC STORED PROCEDURE HÓA ĐƠN ======
-- =============================================

-- 1. Thêm hóa đơn
CREATE OR ALTER PROCEDURE sp_ThemHoaDon
    @maHD NVARCHAR(10),
    @ngayLap DATE,
    @phuongThuc NVARCHAR(50),
    @tongTien DECIMAL(18,2),
    @maKH NVARCHAR(10),
    @maNV NVARCHAR(10),
    @maGG NVARCHAR(10) = NULL,
    @trangThai NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO master.dbo.HoaDon (maHD, ngayLap, phuongThuc, tongTien, maKH, maNV, maGG, trangThai)
    VALUES (@maHD, @ngayLap, @phuongThuc, @tongTien, @maKH, @maNV, @maGG, @trangThai);
END;
GO


CREATE OR ALTER PROCEDURE sp_SuaHoaDon
    @maHD NVARCHAR(10),
    @ngayLap DATE,
    @phuongThuc NVARCHAR(50),
    @tongTien DECIMAL(18,2),
    @maKH NVARCHAR(10),
    @maNV NVARCHAR(10),
    @maGG NVARCHAR(10),
    @trangThai NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    /* SỬA 2: Thêm "master.dbo." */
    UPDATE master.dbo.HoaDon
    SET ngayLap = @ngayLap, phuongThuc = @phuongThuc, tongTien = @tongTien,
        maKH = @maKH, maNV = @maNV, maGG = @maGG, trangThai = @trangThai
    WHERE maHD = @maHD;
END;
GO

-- 3. Xóa hóa đơn
CREATE OR ALTER PROCEDURE sp_XoaHoaDon
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    DELETE FROM master.dbo.HoaDon WHERE maHD = @maHD;
END;
GO


CREATE OR ALTER PROCEDURE sp_LayDanhSachHoaDon
AS
BEGIN
    SET NOCOUNT ON;

    WITH RankedCTP AS (
        SELECT
            ctp.maHD,
            ctp.maPhong,
            ctp.ngayDi,
            ROW_NUMBER() OVER(PARTITION BY ctp.maHD ORDER BY ctp.ngayDi DESC) as rn
        FROM dbo.CTHoaDonPhong ctp
    )
    SELECT
        hd.*,
        kh.hoTen,
        kh.soDT,      /* <-- CỘT BỊ THIẾU ĐÂY */
        kh.diaChi,    /* <-- CỘT BỊ THIẾU ĐÂY */
        r_ctp.maPhong AS soPhong,
        r_ctp.ngayDi AS ngayCheckOut
    FROM dbo.HoaDon hd
             LEFT JOIN master.dbo.KhachHang kh ON hd.maKH = kh.maKH
             LEFT JOIN RankedCTP r_ctp ON hd.maHD = r_ctp.maHD AND r_ctp.rn = 1;

END;
GO

-- 5. Tìm hóa đơn theo mã
CREATE OR ALTER PROCEDURE sp_TimHoaDonTheoMa
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT * FROM dbo.HoaDon WHERE maHD = @maHD;
END;
GO


CREATE OR ALTER PROCEDURE sp_TaoMaHoaDonTiepTheo
@maMoi NVARCHAR(10) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @so INT;

    SELECT @so = ISNULL(MAX(CAST(SUBSTRING(maHD, 3, LEN(maHD) - 2) AS INT)), 0)
    /* SỬA 2: Thêm "master.dbo." */
    FROM dbo.HoaDon
    WHERE maHD LIKE 'HD%' AND ISNUMERIC(SUBSTRING(maHD, 3, LEN(maHD) - 2)) = 1;

    SET @maMoi = 'HD' + RIGHT('000' + CAST(@so + 1 AS NVARCHAR(3)), 3);
END;
GO

-- 7. Cập nhật trạng thái hóa đơn
CREATE OR ALTER PROCEDURE sp_CapNhatTrangThaiHoaDon
    @maHD NVARCHAR(10),
    @trangThaiMoi NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    /* SỬA 2: Thêm "master.dbo." */
    UPDATE dbo.HoaDon
    SET trangThai = @trangThaiMoi
    WHERE maHD = @maHD;
END;
GO

CREATE OR ALTER PROCEDURE sp_LayChiTietHoaDonTheoMaHD
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    WITH ChiTiet AS (


        SELECT

            (lp.tenLoai + N' (' + p.maPhong + N')') AS TenChiTiet,

            (CASE
                 WHEN DATEDIFF(day, ctp.ngayDen, ctp.ngayDi) = 0 THEN 1
                 ELSE DATEDIFF(day, ctp.ngayDen, ctp.ngayDi)
                END) AS SoLuong,


            ctp.giaPhong AS DonGia,


            ctp.thanhTien AS ThanhTien,

            1 AS UuTienSapXep
        FROM

            dbo.CTHoaDonPhong AS ctp
                JOIN
            dbo.Phong AS p ON ctp.maPhong = p.maPhong
                JOIN
            dbo.LoaiPhong AS lp ON p.maLoaiPhong = lp.maLoaiPhong
        WHERE
            ctp.maHD = @maHD

        UNION ALL


        SELECT
            dv.tenDV AS TenChiTiet,
            ctdv.soLuong AS SoLuong,
            ctdv.donGia AS DonGia,

            ctdv.thanhTien AS ThanhTien,
            2 AS UuTienSapXep
        FROM

            dbo.CTHoaDonDichVu AS ctdv
                JOIN
            dbo.DichVu AS dv ON ctdv.maDV = dv.maDV
        WHERE
            ctdv.maHD = @maHD
    )


    SELECT
        ROW_NUMBER() OVER (ORDER BY UuTienSapXep, TenChiTiet) AS STT,
        TenChiTiet,
        SoLuong,
        DonGia,
        ThanhTien
    FROM
        ChiTiet
    ORDER BY
        UuTienSapXep, TenChiTiet;

END;
GO
CREATE OR ALTER PROCEDURE sp_LayMaGiamGiaTheoMa
@maGG nvarchar(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT * FROM dbo.MaGiamGia WHERE maGG = @maGG;
END;
GO