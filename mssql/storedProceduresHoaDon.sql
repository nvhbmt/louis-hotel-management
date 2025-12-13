USE QuanLyKhachSan;
GO

-- =============================================
-- ========== CÁC STORED PROCEDURE HÓA ĐƠN ======
-- =============================================

-- 1. Thêm hóa đơn (Thêm các cột chi tiết)
-- Khớp với 12 tham số trong DAO
CREATE PROCEDURE sp_ThemHoaDon
    @maHD NVARCHAR(10),
    @ngayLap DATE,
    @phuongThuc NVARCHAR(50),
    @tongTien DECIMAL(18,2),
    @maKH NVARCHAR(10),
    @maNV NVARCHAR(10),
    @maGG NVARCHAR(10) = NULL,
    @trangThai NVARCHAR(50),
    -- CỘT MỚI: Đảm bảo chấp nhận NULL
    @ngayCheckOut DATE = NULL,
    @tienPhat DECIMAL(18,2) = 0.0,
    @tongGiamGia DECIMAL(18,2) = 0.0,
    @tongVAT DECIMAL(18,2) = 0.0
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO HoaDon (maHD, ngayLap, phuongThuc, trangThai, tongTien, maKH, maNV, maGG, NgayCheckOut, TienPhat, TongGiamGia, TongVAT)
    VALUES (@maHD, @ngayLap, @phuongThuc, @trangThai, @tongTien, @maKH, @maNV, @maGG, @ngayCheckOut, @tienPhat, @tongGiamGia, @tongVAT);
END;
GO

-- 2. Cập nhật hóa đơn (sp_SuaHoaDon)
-- Khớp với 12 tham số trong DAO, cập nhật tất cả các cột
CREATE PROCEDURE sp_SuaHoaDon
    @maHD NVARCHAR(10),
    @ngayLap DATE,
    @phuongThuc NVARCHAR(50),
    @tongTien DECIMAL(18,2),
    @maKH NVARCHAR(10),
    @maNV NVARCHAR(10),
    @maGG NVARCHAR(10),
    @trangThai NVARCHAR(50),
    -- CỘT MỚI
    @ngayCheckOut DATE,
    @tienPhat DECIMAL(18,2),
    @tongGiamGia DECIMAL(18,2),
    @tongVAT DECIMAL(18,2)
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE HoaDon
    SET ngayLap = @ngayLap,
        phuongThuc = @phuongThuc,
        tongTien = @tongTien,
        maKH = @maKH,
        maNV = @maNV,
        maGG = @maGG,
        trangThai = @trangThai,
        -- Cập nhật các trường mới
        NgayCheckOut = @ngayCheckOut,
        TienPhat = @tienPhat,
        TongGiamGia = @tongGiamGia,
        TongVAT = @tongVAT
    WHERE maHD = @maHD;
END;
GO

-- 3. Xóa hóa đơn (sp_XoaHoaDon)
CREATE PROCEDURE sp_XoaHoaDon
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    DELETE FROM HoaDon WHERE maHD = @maHD;
END;
GO

-- 4. Lấy danh sách hóa đơn (sp_LayDanhSachHoaDon)
-- Đảm bảo SELECT tất cả các cột của HoaDon (bao gồm các cột mới)
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
        hd.*, -- Bao gồm tất cả các cột trong HoaDon (maHD, tongTien, NgayCheckOut, TienPhat, TongGiamGia, TongVAT...)
        kh.hoTen,
        kh.soDT,
        kh.diaChi,
        r_ctp.maPhong AS soPhong
    FROM HoaDon hd
             LEFT JOIN KhachHang kh ON hd.maKH = kh.maKH
             LEFT JOIN RankedCTP r_ctp ON hd.maHD = r_ctp.maHD AND r_ctp.rn = 1;

END;
GO

-- 5. Tìm hóa đơn theo mã (sp_TimHoaDonTheoMa)
-- Đảm bảo SELECT * để trả về tất cả các cột cho DAO
CREATE PROCEDURE sp_TimHoaDonTheoMa
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT * FROM HoaDon WHERE maHD = @maHD;
END;
GO

-- 6. Tạo mã hóa đơn tiếp theo (sp_TaoMaHoaDonTiepTheo)
-- DAO2 đang gọi với OUT parameter, nhưng code cũ dùng SELECT. Tôi sẽ sửa lại dùng SELECT như code gốc của bạn
CREATE PROCEDURE sp_TaoMaHoaDonTiepTheo
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @maCu NVARCHAR(10);
    DECLARE @so INT;
    DECLARE @maMoi NVARCHAR(10);

    SELECT @maCu = MAX(maHD)
    FROM HoaDon;

    IF @maCu IS NULL
        SET @maMoi = 'HD001';
    ELSE
        BEGIN
            SET @so = CAST(SUBSTRING(@maCu, 3, LEN(@maCu) - 2) AS INT) + 1;
            SET @maMoi = 'HD' + RIGHT('000' + CAST(@so AS NVARCHAR(3)), 3);
        END

    -- Trả kết quả ra dưới dạng cột
    SELECT @maMoi AS maHDMoi;
END;
GO;

-- 7. Cập nhật trạng thái hóa đơn (sp_CapNhatTrangThaiHoaDon)
CREATE PROCEDURE sp_CapNhatTrangThaiHoaDon
    @maHD NVARCHAR(10),
    @trangThaiMoi NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE HoaDon
    SET trangThai = @trangThaiMoi
    WHERE maHD = @maHD;
END;
GO

-- 8. Lấy chi tiết hóa đơn (sp_LayChiTietHoaDonTheoMaHD)
-- Cần OR ALTER vì SP này đã tồn tại
CREATE PROCEDURE sp_LayChiTietHoaDonTheoMaHD
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    WITH ChiTiet AS (
        SELECT
            (lp.tenLoai + N' (' + p.maPhong + N')') AS TenChiTiet,

            -- Cột 1: Tính Số Lượng (Số Ngày)
            (CASE
                 WHEN DATEDIFF(day, ctp.ngayDen, ISNULL(ctp.ngayDi, GETDATE())) = 0 THEN 1
                 ELSE DATEDIFF(day, ctp.ngayDen, ISNULL(ctp.ngayDi, GETDATE()))
                END) AS SoLuong,

            ctp.giaPhong AS DonGia,

            -- Cột 3: TÍNH LẠI THÀNH TIỀN (SỬA LỖI: Nhân DonGia với Số Lượng đã tính)
            (ctp.giaPhong * (CASE
                                 WHEN DATEDIFF(day, ctp.ngayDen, ISNULL(ctp.ngayDi, GETDATE())) = 0 THEN 1
                                 ELSE DATEDIFF(day, ctp.ngayDen, ISNULL(ctp.ngayDi, GETDATE()))
                END)
                ) AS ThanhTien,

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

        -- Phần Dịch Vụ (Giữ nguyên, vì ThanhTien dịch vụ thường được lưu vật lý)
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

-- 9. Cập nhật Tổng Tiền Hóa Đơn (sp_CapNhatTongTienHoaDon)
-- Giữ nguyên logic tính tổng gốc từ chi tiết (không bao gồm VAT/GG)
CREATE PROCEDURE sp_CapNhatTongTienHoaDon
@maHD NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @TongThanhToanGoc DECIMAL(18, 2);

    SET @TongThanhToanGoc = (
                                SELECT ISNULL(SUM(ThanhTien), 0)
                                FROM CTHoaDonPhong
                                WHERE maHD = @maHD
                            ) + (
                                SELECT ISNULL(SUM(ThanhTien), 0)
                                FROM CTHoaDonDichVu
                                WHERE maHD = @maHD
                            );

    UPDATE HoaDon
    SET tongTien = @TongThanhToanGoc
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

            -- Tính Số Lượng (Số Ngày) dựa trên GETDATE() nếu chưa check-out
            (CASE
                 WHEN DATEDIFF(day, ctp.ngayDen, ISNULL(ctp.ngayDi, GETDATE())) = 0 THEN 1
                 ELSE DATEDIFF(day, ctp.ngayDen, ISNULL(ctp.ngayDi, GETDATE()))
                END) AS SoLuong,

            ctp.giaPhong AS DonGia,

            ctp.thanhTien AS ThanhTien, -- Lấy giá trị đã lưu
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

        -- Phần Dịch Vụ
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