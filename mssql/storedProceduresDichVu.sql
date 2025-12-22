-- =============================================
-- Stored Procedures cho hệ thống quản lý dịch vụ
-- =============================================

USE QuanLyKhachSan;
GO

-- =============================================
-- Stored Procedures cho bảng Dịch vụ
-- =============================================

-- Thêm dịch vụ
CREATE PROCEDURE sp_ThemDichVu @maDV NVARCHAR(10),
                               @tenDV NVARCHAR(100),
                               @soLuong INT,
                               @donGia DECIMAL(18, 2),
                               @moTa NVARCHAR(255) = NULL,
                               @conKinhDoanh BIT = 1
AS
BEGIN
    -- 1. KIỂM TRA MÃ DỊCH VỤ (NOT NULL và Tồn Tại)
    IF @maDV IS NULL OR LTRIM(RTRIM(@maDV)) = ''
        BEGIN
            RAISERROR (N'Mã dịch vụ không được để trống.', 16, 1)
        END

    IF EXISTS (SELECT 1 FROM DichVu WHERE maDV = @maDV AND daXoaLuc IS NULL)
        BEGIN
            RAISERROR (N'Mã dịch vụ đã tồn tại. Vui lòng chọn mã khác.', 16, 1)
        END

    -- 2. KIỂM TRA TÊN DỊCH VỤ (NOT NULL)
    IF @tenDV IS NULL OR LTRIM(RTRIM(@tenDV)) = ''
        BEGIN
            RAISERROR (N'Tên dịch vụ không được để trống.', 16, 1)
        END

    -- 3. KIỂM TRA SỐ LƯỢNG (NOT NULL và >= 0)
    IF @soLuong IS NULL OR @soLuong < 0
        BEGIN
            RAISERROR (N'Số lượng phải là một số nguyên không âm (>= 0).', 16, 1)
        END

    -- 4. KIỂM TRA ĐƠN GIÁ (NOT NULL và >= 0)
    IF @donGia IS NULL OR @donGia < 0.00
        BEGIN
            RAISERROR (N'Đơn giá phải là một giá trị tiền tệ không âm (>= 0).', 16, 1)
        END

    -- 5. KIỂM TRA TRẠNG THÁI KINH DOANH (NOT NULL)
    IF @conKinhDoanh IS NULL
        BEGIN
            -- Mặc dù đã có giá trị mặc định, nên kiểm tra nếu người dùng truyền NULL rõ ràng
            RAISERROR (N'Trạng thái kinh doanh không được để trống.', 16, 1)
        END

    -- THỰC HIỆN THÊM DỮ LIỆU NẾU TẤT CẢ ĐỀU HỢP LỆ
    INSERT INTO DichVu (maDV, tenDV, soLuong, donGia, moTa, conKinhDoanh)
    VALUES (@maDV, @tenDV, @soLuong, @donGia, @moTa, @conKinhDoanh)

    SELECT N'Thêm dịch vụ thành công.' AS Result
END
GO

-- Lấy xóa phòng đánh dấu ngừng kinh doanh
CREATE PROCEDURE sp_XoaDichVu @maDV NVARCHAR(10)
AS
BEGIN
    -- Kiểm tra xem mã dịch vụ có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM DichVu WHERE maDV = @maDV AND daXoaLuc IS NULL)
        BEGIN
            RAISERROR (N'Không tìm thấy mã dịch vụ để xóa/ngừng kinh doanh.', 16, 1)
        END

    UPDATE DichVu
    SET daXoaLuc = GETDATE()
    WHERE maDV = @maDV
      AND daXoaLuc IS NULL
END
GO


-- Cập nhật dịch vụ
CREATE PROCEDURE sp_CapNhatDichVu @maDV NVARCHAR(10),
                                  @tenDV NVARCHAR(100),
                                  @soLuong INT,
                                  @donGia DECIMAL(18, 2),
                                  @moTa NVARCHAR(255) = NULL,
                                  @conKinhDoanh BIT
AS
BEGIN
    -- 1. KIỂM TRA MÃ DỊCH VỤ CÓ TỒN TẠI KHÔNG (Ràng buộc quan trọng nhất cho UPDATE)
    IF @maDV IS NULL OR LTRIM(RTRIM(@maDV)) = ''
        BEGIN
            RAISERROR (N'Mã dịch vụ không được để trống khi cập nhật.', 16, 1)
        END

    IF NOT EXISTS (SELECT 1 FROM DichVu WHERE maDV = @maDV AND daXoaLuc IS NULL)
        BEGIN
            RAISERROR (N'Không tìm thấy mã dịch vụ này để cập nhật.', 16, 1)
        END

    -- 2. KIỂM TRA TÊN DỊCH VỤ (NOT NULL)
    IF @tenDV IS NULL OR LTRIM(RTRIM(@tenDV)) = ''
        BEGIN
            RAISERROR (N'Tên dịch vụ không được để trống.', 16, 1)
        END

    -- 3. KIỂM TRA SỐ LƯỢNG (NOT NULL và >= 0)
    IF @soLuong IS NULL OR @soLuong < 0
        BEGIN
            RAISERROR (N'Số lượng phải là một số nguyên không âm (>= 0).', 16, 1)
        END

    -- 4. KIỂM TRA ĐƠN GIÁ (NOT NULL và >= 0)
    IF @donGia IS NULL OR @donGia < 0.00
        BEGIN
            RAISERROR (N'Đơn giá phải là một giá trị tiền tệ không âm (>= 0).', 16, 1)
        END

    -- 5. KIỂM TRA TRẠNG THÁI KINH DOANH (NOT NULL)
    IF @conKinhDoanh IS NULL
        BEGIN
            RAISERROR (N'Trạng thái kinh doanh không được để trống.', 16, 1)
        END

    -- THỰC HIỆN CẬP NHẬT DỮ LIỆU NẾU TẤT CẢ ĐỀU HỢP LỆ
    UPDATE DichVu
    SET tenDV        = @tenDV,
        soLuong      = @soLuong,
        donGia       = @donGia,
        moTa         = @moTa,
        conKinhDoanh = @conKinhDoanh
    WHERE maDV = @maDV

    SELECT N'Cập nhật dịch vụ thành công.' AS Result
END
GO
CREATE PROCEDURE sp_LayTatCaDichVu @chiLayConKinhDoanh BIT = NULL -- NULL: Lấy tất cả; 1: Đang kinh doanh; 0: Đã ngừng kinh doanh
AS
BEGIN
    SELECT maDV,
           tenDV,
           soLuong,
           donGia,
           moTa,
           conKinhDoanh
    FROM DichVu
    WHERE (@chiLayConKinhDoanh IS NULL OR conKinhDoanh = @chiLayConKinhDoanh)
      AND daXoaLuc IS NULL
    ORDER BY maDV
END
GO

-- Lấy mã dịch vụ tiếp theo
CREATE PROCEDURE sp_LayMaDichVuTiepTheo @maDichVuTiepTheo NVARCHAR(10) OUTPUT
AS
BEGIN
    DECLARE @maxSo INT = 0;

    -- Lấy số lớn nhất từ các mã dịch vụ hiện có
    SELECT @maxSo = ISNULL(MAX(CAST(SUBSTRING(maDV, 3, LEN(maDV) - 2) AS INT)), 0)
    FROM DichVu
    WHERE maDV LIKE 'DV%'
      AND LEN(maDV) = 5
      AND ISNUMERIC(SUBSTRING(maDV, 3, 3)) = 1;

    -- Tạo mã dịch vụ tiếp theo
    SET @maDichVuTiepTheo = 'DV' + RIGHT('000' + CAST(@maxSo + 1 AS VARCHAR(3)), 3);
END
GO

-- TÊN PROCEDURE: sp_CapNhatSoLuongTonKho
-- MỤC ĐÍCH: Cập nhật số lượng tồn kho (soLuong) của một dịch vụ.

CREATE PROCEDURE sp_CapNhatSoLuongTonKho @maDV NVARCHAR(10), -- Mã Dịch Vụ cần cập nhật
                                         @soLuongMoi INT -- Số lượng tồn kho mới
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE DichVu
    SET soLuong = @soLuongMoi
    WHERE maDV = @maDV;

    -- Trả về số dòng bị ảnh hưởng (0 hoặc 1)
    SELECT @@ROWCOUNT;
END
GO

CREATE PROCEDURE sp_TimDichVuTheoMa @MaDV NVARCHAR(10)
AS
BEGIN
    SELECT maDV,
           tenDV,
           soLuong,
           donGia,
           moTa,
           conKinhDoanh
    FROM DichVu
    WHERE maDV = @MaDV;
END