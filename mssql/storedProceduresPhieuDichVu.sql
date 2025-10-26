-- =============================================
-- Stored Procedures cho hệ thống quản lý dịch vụ
-- =============================================

USE QuanLyKhachSan;
GO

-- =============================================
-- Stored Procedures cho bảng Dịch vụ
-- =============================================
--THÊM PHIẾU
CREATE PROCEDURE sp_ThemPhieuDV
    @maPhieuDV NVARCHAR(10),
    @maHD NVARCHAR(10),
    @maNV NVARCHAR(10),
    @ghiChu NVARCHAR(255) = NULL -- Ghi chú có thể NULL
AS
BEGIN
    -- 1. Kiểm tra NULL và Rỗng cho các trường bắt buộc
    IF @maPhieuDV IS NULL OR LTRIM(RTRIM(@maPhieuDV)) = ''
        BEGIN
            RAISERROR(N'Mã Phiếu Dịch Vụ không được để trống.', 16, 1)
            RETURN
        END
    IF @maHD IS NULL OR LTRIM(RTRIM(@maHD)) = ''
        BEGIN
            RAISERROR(N'Mã Hóa Đơn không được để trống.', 16, 1)
            RETURN
        END
    IF @maNV IS NULL OR LTRIM(RTRIM(@maNV)) = ''
        BEGIN
            RAISERROR(N'Mã Nhân Viên không được để trống.', 16, 1)
            RETURN
        END

    -- 2. Kiểm tra Phiếu DV đã tồn tại chưa
    IF EXISTS (SELECT 1 FROM PhieuDichVu WHERE maPhieuDV = @maPhieuDV)
        BEGIN
            RAISERROR(N'Mã Phiếu Dịch Vụ đã tồn tại. Vui lòng chọn mã khác.', 16, 1)
            RETURN
        END

    -- 3. Kiểm tra Khóa Ngoại: maHD (Giả định bảng HoaDon tồn tại)
    IF NOT EXISTS (SELECT 1 FROM HoaDon WHERE maHD = @maHD)
        BEGIN
            RAISERROR(N'Mã Hóa Đơn không tồn tại. Vui lòng kiểm tra lại.', 16, 1)
            RETURN
        END

    -- 4. Kiểm tra Khóa Ngoại: maNV (Giả định bảng NhanVien tồn tại)
    IF NOT EXISTS (SELECT 1 FROM NhanVien WHERE maNV = @maNV)
        BEGIN
            RAISERROR(N'Mã Nhân Viên không tồn tại. Vui lòng kiểm tra lại.', 16, 1)
            RETURN
        END

    -- THỰC HIỆN INSERT
    INSERT INTO PhieuDichVu (maPhieuDV, maHD, ngayLap, maNV, ghiChu)
    VALUES (@maPhieuDV, @maHD, GETDATE(), @maNV, @ghiChu) -- ngayLap lấy ngày hệ thống
END
GO
--CẬP NHẬT
CREATE PROCEDURE sp_CapNhatPhieuDV
    @maPhieuDV NVARCHAR(10),
    @ghiChu NVARCHAR(255) = NULL
AS
BEGIN
    -- 1. Kiểm tra Phiếu DV có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM PhieuDichVu WHERE maPhieuDV = @maPhieuDV)
        BEGIN
            RAISERROR(N'Không tìm thấy Mã Phiếu Dịch Vụ để cập nhật.', 16, 1)
            RETURN
        END

    -- THỰC HIỆN UPDATE
    UPDATE PhieuDichVu
    SET
        ghiChu = @ghiChu
    WHERE
        maPhieuDV = @maPhieuDV
END
GO
--XÓA
CREATE PROCEDURE sp_XoaPhieuDV
@maPhieuDV NVARCHAR(10)
AS
BEGIN
    -- Kiểm tra Phiếu DV có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM PhieuDichVu WHERE maPhieuDV = @maPhieuDV)
        BEGIN
            RAISERROR(N'Không tìm thấy Mã Phiếu Dịch Vụ để xóa.', 16, 1)
            RETURN
        END

    -- !!! QUAN TRỌNG: Cần xóa các mục trong bảng Chi Tiết Phiếu Dịch Vụ (CTPhieuDichVu) trước
    -- Nếu không có ràng buộc CASCADE ON DELETE, lệnh sau sẽ báo lỗi.
    -- Bật khối DELETE này lên nếu bạn muốn xóa chi tiết liên quan:
    -- DELETE FROM CTPhieuDichVu WHERE maPhieuDV = @maPhieuDV;

    DELETE FROM PhieuDichVu
    WHERE maPhieuDV = @maPhieuDV
END
GO
--Lấy DS
CREATE PROCEDURE sp_LayTatCaPhieuDV
@maHD NVARCHAR(10) = NULL -- Tham số tùy chọn để lọc theo Hóa Đơn
AS
BEGIN
    SELECT
        maPhieuDV,
        maHD,
        ngayLap,
        maNV,
        ghiChu
    FROM
        PhieuDichVu
    WHERE
        (@maHD IS NULL OR maHD = @maHD)
    ORDER BY
        ngayLap DESC, maPhieuDV
END
GO

-- TÊN PROCEDURE: sp_LayMaPhieuDVTiepTheo
-- MỤC ĐÍCH: Tự động tạo mã phiếu dịch vụ tiếp theo
-- Giả định bảng PhieuDichVu có cột maPhieuDV NVARCHAR(10)

CREATE PROCEDURE sp_LayMaPhieuDVTiepTheo
@maDVTiepTheo NVARCHAR(10) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    -- Khai báo biến tạm thời để lưu mã lớn nhất
    DECLARE @maLonNhat NVARCHAR(10);
    DECLARE @soTiepTheo INT;

    -- 1. Tìm mã lớn nhất hiện có với tiền tố "PDV"
    SELECT @maLonNhat = MAX(maPhieuDV)
    FROM PhieuDichVu
    WHERE maPhieuDV LIKE 'PDV%';

    -- 2. Xử lý logic tạo mã
    IF @maLonNhat IS NULL
        BEGIN
            -- Chưa có PDV nào, bắt đầu từ 1
            SET @soTiepTheo = 1;
        END
    ELSE
        BEGIN
            -- Lấy phần số của mã lớn nhất (ví dụ: từ 'PDV015' lấy ra 15)
            -- Tăng lên 1
            -- Chú ý: SUBSTRING bắt đầu từ vị trí 4 ('P'='1', 'D'='2', 'V'='3', '0'='4')
            SET @soTiepTheo = CAST(SUBSTRING(@maLonNhat, 4, LEN(@maLonNhat) - 3) AS INT) + 1;
        END

    -- 3. Định dạng lại thành 'PDV' + số (với 3 chữ số đệm 0)
    SET @maDVTiepTheo = 'PDV' + RIGHT('00' + CAST(@soTiepTheo AS NVARCHAR(3)), 3);

    -- Giá trị được trả về thông qua tham số OUTPUT
END
GO
