/* * SỬA 1: Đảm bảo các SP được TẠO RA trong CSDL của ứng dụng.
 */
USE QuanLyKhachSan;
GO

-- =============================================
-- ========== CÁC STORED PROCEDURE HÓA ĐƠN ======
-- =============================================

-- 1. Thêm hóa đơn
CREATE PROCEDURE sp_ThemHoaDon
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

    INSERT INTO HoaDon (maHD, ngayLap, phuongThuc, tongTien, maKH, maNV, maGG, trangThai)
    VALUES (@maHD, @ngayLap, @phuongThuc, @tongTien, @maKH, @maNV, @maGG, @trangThai);
END;
GO


CREATE PROCEDURE sp_SuaHoaDon
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
    UPDATE HoaDon
    SET ngayLap = @ngayLap, phuongThuc = @phuongThuc, tongTien = @tongTien,
        maKH = @maKH, maNV = @maNV, maGG = @maGG, trangThai = @trangThai
    WHERE maHD = @maHD;
END;
GO

-- 3. Xóa hóa đơn
CREATE PROCEDURE sp_XoaHoaDon
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    DELETE FROM HoaDon WHERE maHD = @maHD;
END;
GO


CREATE PROCEDURE sp_LayDanhSachHoaDon
AS
BEGIN
    SET NOCOUNT ON;

    WITH RankedCTP AS (
        SELECT
            ctp.maHD,
            ctp.maPhong,
            ctp.ngayDi,
            ROW_NUMBER() OVER(PARTITION BY ctp.maHD ORDER BY ctp.ngayDi DESC) as rn
        FROM CTHoaDonPhong ctp
    )
    SELECT
        hd.*,
        kh.hoTen,
        kh.soDT,      /* <-- CỘT BỊ THIẾU ĐÂY */
        kh.diaChi,    /* <-- CỘT BỊ THIẾU ĐÂY */
        r_ctp.maPhong AS soPhong,
        r_ctp.ngayDi AS ngayCheckOut
    FROM HoaDon hd
             LEFT JOIN KhachHang kh ON hd.maKH = kh.maKH
             LEFT JOIN RankedCTP r_ctp ON hd.maHD = r_ctp.maHD AND r_ctp.rn = 1;

END;
GO

-- 5. Tìm hóa đơn theo mã
CREATE PROCEDURE sp_TimHoaDonTheoMa
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT * FROM HoaDon WHERE maHD = @maHD;
END;
GO


CREATE PROCEDURE sp_TaoMaHoaDonTiepTheo
    AS
    BEGIN
        SET NOCOUNT ON;

        DECLARE @maCu NVARCHAR(10);
        DECLARE @so INT;
        DECLARE @maMoi NVARCHAR(10);

        -- 🔹 Lấy mã hóa đơn lớn nhất hiện tại
        SELECT @maCu = MAX(maHD)
        FROM HoaDon;

        -- 🔹 Nếu chưa có hóa đơn nào thì gán là HD001
        IF @maCu IS NULL
            SET @maMoi = 'HD001';
        ELSE
            BEGIN
                -- Lấy phần số từ mã cũ (ví dụ HD005 → 5)
                SET @so = CAST(SUBSTRING(@maCu, 3, LEN(@maCu) - 2) AS INT) + 1;

                -- Sinh mã mới với 3 chữ số, thêm 0 phía trước nếu cần
                SET @maMoi = 'HD' + RIGHT('000' + CAST(@so AS NVARCHAR(3)), 3);
            END

        -- 🔹 Trả kết quả ra
        SELECT @maMoi AS maHDMoi;
    END;
GO;

-- 7. Cập nhật trạng thái hóa đơn
CREATE PROCEDURE sp_CapNhatTrangThaiHoaDon
    @maHD NVARCHAR(10),
    @trangThaiMoi NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    /* SỬA 2: Thêm "master.dbo." */
    UPDATE HoaDon
    SET trangThai = @trangThaiMoi
    WHERE maHD = @maHD;
END;
GO

CREATE PROCEDURE sp_LayChiTietHoaDonTheoMaHD
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

            CTHoaDonPhong AS ctp
                JOIN
            Phong AS p ON ctp.maPhong = p.maPhong
                JOIN
            LoaiPhong AS lp ON p.maLoaiPhong = lp.maLoaiPhong
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

            CTHoaDonDichVu AS ctdv
                JOIN
            DichVu AS dv ON ctdv.maDV = dv.maDV
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
CREATE PROCEDURE sp_LayMaGiamGiaTheoMa
@maGG nvarchar(10)
AS
BEGIN
    SET NOCOUNT ON;
    SELECT * FROM MaGiamGia WHERE maGG = @maGG;
END;
GO