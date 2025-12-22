-- =============================================
-- Stored Procedures cho hệ thống quản lý phòng
-- =============================================

USE QuanLyKhachSan;
GO

-- =============================================
-- Stored Procedures cho bảng LoaiPhong
-- =============================================

-- Thêm loại phòng
CREATE PROCEDURE sp_ThemLoaiPhong @maLoaiPhong NVARCHAR(10),
                                  @tenLoai NVARCHAR(100),
                                  @moTa NVARCHAR(255),
                                  @donGia DECIMAL(18, 2)
AS
BEGIN

    -- Kiểm tra mã loại phòng đã tồn tại chưa
    IF EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong AND daXoaLuc IS NULL)
        BEGIN
            RAISERROR ('Mã loại phòng đã tồn tại!', 16, 1);
            RETURN;
        END

    -- Kiểm tra dữ liệu đầu vào
    IF @maLoaiPhong IS NULL OR LTRIM(RTRIM(@maLoaiPhong)) = ''
        BEGIN
            RAISERROR ('Mã loại phòng không được để trống!', 16, 1);
            RETURN;
        END

    IF @tenLoai IS NULL OR LTRIM(RTRIM(@tenLoai)) = ''
        BEGIN
            RAISERROR ('Tên loại phòng không được để trống!', 16, 1);
            RETURN;
        END

    IF @donGia IS NULL OR @donGia <= 0
        BEGIN
            RAISERROR ('Đơn giá phải lớn hơn 0!', 16, 1);
            RETURN;
        END

    -- Thêm loại phòng
    INSERT INTO LoaiPhong (maLoaiPhong, tenLoai, moTa, donGia)
    VALUES (@maLoaiPhong, @tenLoai, @moTa, @donGia);

END
GO

-- Lấy danh sách tất cả loại phòng
CREATE PROCEDURE sp_LayDSLoaiPhong
AS
BEGIN

    SELECT maLoaiPhong, tenLoai, moTa, donGia
    FROM LoaiPhong
    WHERE daXoaLuc IS NULL
    ORDER BY tenLoai;
END
GO

-- Lấy loại phòng theo mã
CREATE PROCEDURE sp_LayLoaiPhongTheoMa @maLoaiPhong NVARCHAR(10)
AS
BEGIN

    SELECT maLoaiPhong, tenLoai, moTa, donGia
    FROM LoaiPhong
    WHERE maLoaiPhong = @maLoaiPhong
      AND daXoaLuc IS NULL;
END
GO

-- Cập nhật loại phòng
CREATE PROCEDURE sp_CapNhatLoaiPhong @maLoaiPhong NVARCHAR(10),
                                     @tenLoai NVARCHAR(100),
                                     @moTa NVARCHAR(255),
                                     @donGia DECIMAL(18, 2)
AS
BEGIN

    -- Kiểm tra loại phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong AND daXoaLuc IS NULL)
        BEGIN
            RAISERROR ('Loại phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Kiểm tra dữ liệu đầu vào
    IF @tenLoai IS NULL OR LTRIM(RTRIM(@tenLoai)) = ''
        BEGIN
            RAISERROR ('Tên loại phòng không được để trống!', 16, 1);
            RETURN;
        END

    IF @donGia IS NULL OR @donGia <= 0
        BEGIN
            RAISERROR ('Đơn giá phải lớn hơn 0!', 16, 1);
            RETURN;
        END

    -- Cập nhật loại phòng
    UPDATE LoaiPhong
    SET tenLoai = @tenLoai,
        moTa    = @moTa,
        donGia  = @donGia
    WHERE maLoaiPhong = @maLoaiPhong;
END
GO

-- Xóa loại phòng
CREATE PROCEDURE sp_XoaLoaiPhong @maLoaiPhong NVARCHAR(10)
AS
BEGIN

    -- Kiểm tra loại phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong AND daXoaLuc IS NULL)
        BEGIN
            RAISERROR ('Loại phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Kiểm tra loại phòng có đang được sử dụng không
    IF EXISTS (SELECT 1 FROM Phong WHERE maLoaiPhong = @maLoaiPhong AND daXoaLuc IS NULL)
        BEGIN
            RAISERROR ('Không thể xóa loại phòng này vì đang được sử dụng!', 16, 1);
            RETURN;
        END

    -- Xóa loại phòng
    UPDATE LoaiPhong SET daXoaLuc = GETDATE() WHERE maLoaiPhong = @maLoaiPhong AND daXoaLuc IS NULL;
END
GO

-- Kiểm tra loại phòng có được sử dụng không
CREATE PROCEDURE sp_KiemTraLoaiPhongDuocSuDung @maLoaiPhong NVARCHAR(10)
AS
BEGIN

    SELECT COUNT(*) as count
    FROM Phong
    WHERE maLoaiPhong = @maLoaiPhong;
END
GO

-- =============================================
-- Stored Procedures cho bảng Phong
-- =============================================

-- Thêm phòng
CREATE PROCEDURE sp_ThemPhong @maPhong NVARCHAR(10),
                              @tang INT,
                              @trangThai NVARCHAR(50),
                              @moTa NVARCHAR(255),
                              @maLoaiPhong NVARCHAR(10)
AS
BEGIN

    -- Kiểm tra mã phòng đã tồn tại chưa
    IF EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong)
        BEGIN
            RAISERROR ('Mã phòng đã tồn tại!', 16, 1);
            RETURN;
        END

    -- Kiểm tra dữ liệu đầu vào
    IF @maPhong IS NULL OR LTRIM(RTRIM(@maPhong)) = ''
        BEGIN
            RAISERROR ('Mã phòng không được để trống!', 16, 1);
            RETURN;
        END

    IF @tang IS NULL OR @tang < 1 OR @tang > 20
        BEGIN
            RAISERROR ('Tầng phải từ 1 đến 20!', 16, 1);
            RETURN;
        END

    IF @trangThai IS NULL OR LTRIM(RTRIM(@trangThai)) = ''
        BEGIN
            RAISERROR ('Trạng thái không được để trống!', 16, 1);
            RETURN;
        END

    IF @maLoaiPhong IS NULL OR LTRIM(RTRIM(@maLoaiPhong)) = ''
        BEGIN
            RAISERROR ('Mã loại phòng không được để trống!', 16, 1);
            RETURN;
        END

    -- Kiểm tra loại phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong AND daXoaLuc IS NULL)
        BEGIN
            RAISERROR ('Loại phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Thêm phòng
    INSERT INTO Phong (maPhong, tang, trangThai, moTa, maLoaiPhong)
    VALUES (@maPhong, @tang, @trangThai, @moTa, @maLoaiPhong);
END
GO

-- Lấy danh sách tất cả phòng
CREATE PROCEDURE sp_LayDSPhong
AS
BEGIN

    SELECT p.maPhong,
           p.tang,
           p.trangThai,
           p.moTa,
           p.maLoaiPhong,
           lp.tenLoai,
           lp.donGia
    FROM Phong p
             LEFT JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
    WHERE p.daXoaLuc IS NULL
    ORDER BY p.tang, p.maPhong;
END
GO

-- Lấy danh sách phòng theo tầng
CREATE PROCEDURE sp_LayDSPhongTheoTang @tang INT
AS
BEGIN

    SELECT p.maPhong,
           p.tang,
           p.trangThai,
           p.moTa,
           p.maLoaiPhong,
           lp.tenLoai,
           lp.donGia
    FROM Phong p
             LEFT JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
    WHERE p.tang = @tang
    ORDER BY p.maPhong;
END
GO

-- Lấy danh sách phòng theo trạng thái
CREATE PROCEDURE sp_LayDSPhongTheoTrangThai @trangThai NVARCHAR(50)
AS
BEGIN

    SELECT p.maPhong,
           p.tang,
           p.trangThai,
           p.moTa,
           p.maLoaiPhong,
           lp.tenLoai,
           lp.donGia
    FROM Phong p
             LEFT JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
    WHERE p.trangThai = @trangThai
    ORDER BY p.tang, p.maPhong;
END
GO

-- Lấy danh sách phòng còn trống
CREATE PROCEDURE sp_LayDSPhongTrong
AS
BEGIN

    SELECT p.maPhong,
           p.tang,
           p.trangThai,
           p.moTa,
           p.maLoaiPhong,
           lp.tenLoai,
           lp.donGia
    FROM Phong p
             LEFT JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
    WHERE p.trangThai = N'Trống'
    ORDER BY p.tang, p.maPhong;
END
GO

-- Lấy phòng theo mã
CREATE PROCEDURE sp_LayPhongTheoMa @maPhong NVARCHAR(10)
AS
BEGIN

    SELECT p.maPhong,
           p.tang,
           p.trangThai,
           p.moTa,
           p.maLoaiPhong,
           lp.tenLoai,
           lp.donGia
    FROM Phong p
             LEFT JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
    WHERE p.maPhong = @maPhong;
END
GO

-- Cập nhật phòng
CREATE PROCEDURE sp_CapNhatPhong @maPhong NVARCHAR(10),
                                 @tang INT,
                                 @trangThai NVARCHAR(50),
                                 @moTa NVARCHAR(255),
                                 @maLoaiPhong NVARCHAR(10)
AS
BEGIN
    -- Kiểm tra phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong)
        BEGIN
            RAISERROR ('Phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Kiểm tra dữ liệu đầu vào
    IF @tang IS NULL OR @tang < 1 OR @tang > 20
        BEGIN
            RAISERROR ('Tầng phải từ 1 đến 20!', 16, 1);
            RETURN;
        END

    IF @trangThai IS NULL OR LTRIM(RTRIM(@trangThai)) = ''
        BEGIN
            RAISERROR ('Trạng thái không được để trống!', 16, 1);
            RETURN;
        END

    IF @maLoaiPhong IS NULL OR LTRIM(RTRIM(@maLoaiPhong)) = ''
        BEGIN
            RAISERROR ('Mã loại phòng không được để trống!', 16, 1);
            RETURN;
        END

    -- Kiểm tra loại phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong AND daXoaLuc IS NULL)
        BEGIN
            RAISERROR ('Loại phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Cập nhật phòng
    UPDATE Phong
    SET tang        = @tang,
        trangThai   = @trangThai,
        moTa        = @moTa,
        maLoaiPhong = @maLoaiPhong
    WHERE maPhong = @maPhong;
END
GO

-- Cập nhật trạng thái phòng
CREATE PROCEDURE sp_CapNhatTrangThaiPhong @maPhong NVARCHAR(10),
                                          @trangThai NVARCHAR(50)
AS
BEGIN

    -- Kiểm tra phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong)
        BEGIN
            RAISERROR ('Phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Kiểm tra trạng thái
    IF @trangThai IS NULL OR LTRIM(RTRIM(@trangThai)) = ''
        BEGIN
            RAISERROR ('Trạng thái không được để trống!', 16, 1);
            RETURN;
        END

    -- Cập nhật trạng thái phòng
    UPDATE Phong
    SET trangThai = @trangThai
    WHERE maPhong = @maPhong;
END
GO

-- Xóa phòng
CREATE PROCEDURE sp_XoaPhong @maPhong NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    -- Kiểm tra phòng có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM Phong WHERE maPhong = @maPhong)
        BEGIN
            RAISERROR ('Phòng không tồn tại!', 16, 1);
            RETURN;
        END

    -- Xóa phòng
    UPDATE Phong SET daXoaLuc = GETDATE() WHERE maPhong = @maPhong AND daXoaLuc IS NULL;
END
GO


-- =============================================
-- Stored Procedures bổ sung cho thống kê và báo cáo
-- =============================================


-- Lấy mã loại phòng tiếp theo
CREATE PROCEDURE sp_LayMaLoaiPhongTiepTheo @maLoaiPhongTiepTheo NVARCHAR(10) OUTPUT
AS
BEGIN
    DECLARE @maxSo INT = 0;

    -- Lấy số lớn nhất từ các mã loại phòng hiện có
    SELECT @maxSo = ISNULL(MAX(CAST(SUBSTRING(maLoaiPhong, 3, LEN(maLoaiPhong) - 2) AS INT)), 0)
    FROM LoaiPhong
    WHERE maLoaiPhong LIKE 'LP%'
      AND LEN(maLoaiPhong) = 5
      AND ISNUMERIC(SUBSTRING(maLoaiPhong, 3, 3)) = 1;

    -- Tạo mã loại phòng tiếp theo
    SET @maLoaiPhongTiepTheo = 'LP' + RIGHT('000' + CAST(@maxSo + 1 AS VARCHAR(3)), 3);
END
GO

-- =============================================
-- 11. Lấy danh sách phòng trống trong khoảng thời gian
-- =============================================
CREATE PROCEDURE sp_LayDSPhongTrongTheoKhoangThoiGian @ngayDen DATE,
                                                      @ngayDi DATE
AS
BEGIN
    SELECT
        p.MaPhong,
        p.Tang,
        lp.MaLoaiPhong,
        lp.TenLoai,
        lp.DonGia,
        COALESCE(x.TrangThai, N'TRỐNG') AS TrangThai
    FROM Phong p
    JOIN LoaiPhong lp
        ON p.MaLoaiPhong = lp.MaLoaiPhong
    OUTER APPLY (
        SELECT TOP 1 pdp.TrangThai
        FROM CTHoaDonPhong ct
        JOIN PhieuDatPhong pdp
            ON ct.MaPhieu = pdp.MaPhieu
        WHERE ct.MaPhong = p.MaPhong
        AND pdp.TrangThai IN (N'Đang sử dụng', N'Đã Đặt')
         AND @ngayDen <= ct.NgayDi
        AND @ngayDi >= ct.NgayDen
        ORDER BY
            CASE pdp.TrangThai
                WHEN N'Đang sử dụng' THEN 1
                WHEN N'Đã Đặt' THEN 2
            END
    ) x
    WHERE p.TrangThai != N'Bảo trì';
END
GO

-- =============================================
-- Đếm số lượng đặt phòng chồng lấn cho một phòng trong khoảng thời gian
-- =============================================
CREATE PROCEDURE sp_KiemTraPhongTrongTheoKhoangThoiGian @maPhong NVARCHAR(50),
                                                        @ngayDen DATETIME,
                                                        @ngayDi DATETIME
AS
BEGIN
    SET NOCOUNT ON;

    SELECT COUNT(*) as SoLuong
    FROM CTHoaDonPhong ct
             JOIN PhieuDatPhong pdp ON ct.MaPhieu = pdp.MaPhieu
    WHERE ct.MaPhong = @maPhong
      AND (pdp.TrangThai = 'Đã đặt' OR pdp.TrangThai = N'Đang sử dụng')
      AND (
        (@ngayDen <= ct.NgayDi) OR (@ngayDi >= ct.NgayDen)
        )
      AND ct.daXoaLuc IS NULL
      AND pdp.daXoaLuc IS NULL;
END
GO