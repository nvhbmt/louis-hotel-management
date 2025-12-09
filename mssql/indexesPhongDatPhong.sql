-- =============================================
-- Indexes để optimize performance cho tính năng đặt phòng theo khoảng thời gian
-- =============================================

USE QuanLyKhachSan;
GO

-- Index cho CTHoaDonPhong để optimize overlap check
-- Sử dụng cho stored procedure sp_LayDSPhongTrongTheoKhoangThoiGian
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_CTHoaDonPhong_DateRange' AND object_id = OBJECT_ID('CTHoaDonPhong'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_CTHoaDonPhong_DateRange 
    ON CTHoaDonPhong(maPhong, ngayDen, ngayDi)
    INCLUDE (maPhieu);
END
GO

-- Index cho PhieuDatPhong để filter theo trạng thái
-- Giúp loại trừ booking đã hủy/hoàn thành nhanh hơn
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_PhieuDatPhong_Status' AND object_id = OBJECT_ID('PhieuDatPhong'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_PhieuDatPhong_Status 
    ON PhieuDatPhong(trangThai)
    INCLUDE (maPhieu, ngayDen, ngayDi);
END
GO

-- Index cho Phong để filter theo trạng thái
-- Giúp loại trừ phòng bảo trì nhanh hơn
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Phong_TrangThai' AND object_id = OBJECT_ID('Phong'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Phong_TrangThai 
    ON Phong(trangThai)
    WHERE trangThai != N'Bảo trì';
END
GO

-- Index composite cho CTHoaDonPhong với maPhieu để join nhanh hơn
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_CTHoaDonPhong_MaPhieu' AND object_id = OBJECT_ID('CTHoaDonPhong'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_CTHoaDonPhong_MaPhieu 
    ON CTHoaDonPhong(maPhieu)
    INCLUDE (maPhong, ngayDen, ngayDi);
END
GO

