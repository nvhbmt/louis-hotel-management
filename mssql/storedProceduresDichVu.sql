-- =============================================
-- Stored Procedures cho hệ thống quản lý dịch vụ
-- =============================================

USE QuanLyKhachSan;
GO

-- =============================================
-- Stored Procedures cho bảng Dịch vụ
-- =============================================

-- Thêm dịch vụ
CREATE OR ALTER PROCEDURE sp_ThemDichVu
    @maDV NVARCHAR(10),
    @tenDV NVARCHAR(100),
    @soLuong INT,
    @donGia DECIMAL(18,2),
    @moTa NVARCHAR(255) = NULL,
    @conKinhDoanh BIT = 1
AS
BEGIN
    -- 1. KIỂM TRA MÃ DỊCH VỤ (NOT NULL và Tồn Tại)
    IF @maDV IS NULL OR LTRIM(RTRIM(@maDV)) = ''
        BEGIN
            RAISERROR(N'Mã dịch vụ không được để trống.', 16, 1)
            RETURN
        END

    IF EXISTS (SELECT 1 FROM DichVu WHERE maDV = @maDV)
        BEGIN
            RAISERROR(N'Mã dịch vụ đã tồn tại. Vui lòng chọn mã khác.', 16, 1)
            RETURN
        END

    -- 2. KIỂM TRA TÊN DỊCH VỤ (NOT NULL)
    IF @tenDV IS NULL OR LTRIM(RTRIM(@tenDV)) = ''
        BEGIN
            RAISERROR(N'Tên dịch vụ không được để trống.', 16, 1)
            RETURN
        END

    -- 3. KIỂM TRA SỐ LƯỢNG (NOT NULL và >= 0)
    IF @soLuong IS NULL OR @soLuong < 0
        BEGIN
            RAISERROR(N'Số lượng phải là một số nguyên không âm (>= 0).', 16, 1)
            RETURN
        END

    -- 4. KIỂM TRA ĐƠN GIÁ (NOT NULL và >= 0)
    IF @donGia IS NULL OR @donGia < 0.00
        BEGIN
            RAISERROR(N'Đơn giá phải là một giá trị tiền tệ không âm (>= 0).', 16, 1)
            RETURN
        END

    -- 5. KIỂM TRA TRẠNG THÁI KINH DOANH (NOT NULL)
    IF @conKinhDoanh IS NULL
        BEGIN
            -- Mặc dù đã có giá trị mặc định, nên kiểm tra nếu người dùng truyền NULL rõ ràng
            RAISERROR(N'Trạng thái kinh doanh không được để trống.', 16, 1)
            RETURN
        END

    -- THỰC HIỆN THÊM DỮ LIỆU NẾU TẤT CẢ ĐỀU HỢP LỆ
    INSERT INTO DichVu (maDV, tenDV, soLuong, donGia, moTa, conKinhDoanh)
    VALUES (@maDV, @tenDV, @soLuong, @donGia, @moTa, @conKinhDoanh)

    SELECT N'Thêm dịch vụ thành công.' AS Result
END
GO

-- Lấy danh sách tất cả dịch vụ
CREATE OR ALTER PROCEDURE sp_LayTatCaDichVu
@chiLayConKinhDoanh BIT = NULL -- NULL để lấy tất cả, 1 để lấy các dịch vụ đang KD, 0 để lấy dịch vụ đã ngừng KD
AS
BEGIN
    SELECT
        maDV,
        tenDV,
        soLuong,
        donGia,
        moTa,
        conKinhDoanh
    FROM
        DichVu
    WHERE
        (@chiLayConKinhDoanh IS NULL OR conKinhDoanh = @chiLayConKinhDoanh)
    ORDER BY
        maDV
END
GO

-- Đánh dấu ngừng kinh doanh cho một dịch vụ
CREATE OR ALTER PROCEDURE sp_XoaDichVu -- Gợi ý: Có thể đổi tên thành sp_NgungKinhDoanhDichVu cho rõ nghĩa hơn
@maDV NVARCHAR(10)
AS
BEGIN
    -- Kiểm tra xem mã dịch vụ có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM DichVu WHERE maDV = @maDV)
        BEGIN
            RAISERROR(N'Không tìm thấy mã dịch vụ để ngừng kinh doanh.', 16, 1)
            RETURN
        END

    UPDATE DichVu
    SET
        conKinhDoanh = 0 -- Đánh dấu là ngừng kinh doanh thay vì xóa cứng (Hard Delete)
    WHERE
        maDV = @maDV

    SELECT N'Đã cập nhật dịch vụ sang trạng thái ngừng kinh doanh.' AS Result
END
GO


-- Cập nhật dịch vụ
CREATE OR ALTER PROCEDURE sp_CapNhatDichVu
    @maDV NVARCHAR(10),
    @tenDV NVARCHAR(100),
    @soLuong INT,
    @donGia DECIMAL(18,2),
    @moTa NVARCHAR(255) = NULL,
    @conKinhDoanh BIT
AS
BEGIN
    -- 1. KIỂM TRA MÃ DỊCH VỤ CÓ TỒN TẠI KHÔNG (Ràng buộc quan trọng nhất cho UPDATE)
    IF @maDV IS NULL OR LTRIM(RTRIM(@maDV)) = ''
        BEGIN
            RAISERROR(N'Mã dịch vụ không được để trống khi cập nhật.', 16, 1)
            RETURN
        END

    IF NOT EXISTS (SELECT 1 FROM DichVu WHERE maDV = @maDV)
        BEGIN
            RAISERROR(N'Không tìm thấy mã dịch vụ này để cập nhật.', 16, 1)
            RETURN
        END

    -- 2. KIỂM TRA TÊN DỊCH VỤ (NOT NULL)
    IF @tenDV IS NULL OR LTRIM(RTRIM(@tenDV)) = ''
        BEGIN
            RAISERROR(N'Tên dịch vụ không được để trống.', 16, 1)
            RETURN
        END

    -- 3. KIỂM TRA SỐ LƯỢNG (NOT NULL và >= 0)
    IF @soLuong IS NULL OR @soLuong < 0
        BEGIN
            RAISERROR(N'Số lượng phải là một số nguyên không âm (>= 0).', 16, 1)
            RETURN
        END

    -- 4. KIỂM TRA ĐƠN GIÁ (NOT NULL và >= 0)
    IF @donGia IS NULL OR @donGia < 0.00
        BEGIN
            RAISERROR(N'Đơn giá phải là một giá trị tiền tệ không âm (>= 0).', 16, 1)
            RETURN
        END

    -- 5. KIỂM TRA TRẠNG THÁI KINH DOANH (NOT NULL)
    IF @conKinhDoanh IS NULL
        BEGIN
            RAISERROR(N'Trạng thái kinh doanh không được để trống.', 16, 1)
            RETURN
        END

    -- THỰC HIỆN CẬP NHẬT DỮ LIỆU NẾU TẤT CẢ ĐỀU HỢP LỆ
    UPDATE DichVu
    SET
        tenDV = @tenDV,
        soLuong = @soLuong,
        donGia = @donGia,
        moTa = @moTa,
        conKinhDoanh = @conKinhDoanh
    WHERE
        maDV = @maDV

    SELECT N'Cập nhật dịch vụ thành công.' AS Result
END
GO