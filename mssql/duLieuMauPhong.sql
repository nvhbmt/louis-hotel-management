-- Thêm dữ liệu mẫu cho các loại phòng
USE QuanLyKhachSan;
GO

-- Thêm các loại phòng
INSERT INTO LoaiPhong (maLoaiPhong, tenLoai, moTa, donGia) VALUES
('LP001', N'Phòng Đơn', N'Phòng dành cho 1 người, có giường đơn, phòng tắm riêng', 500000),
('LP002', N'Phòng Đôi', N'Phòng dành cho 2 người, có giường đôi, phòng tắm riêng', 800000),
('LP003', N'Phòng Gia Đình', N'Phòng dành cho gia đình, có giường đôi và giường phụ', 1200000),
('LP004', N'Phòng VIP', N'Phòng cao cấp với đầy đủ tiện nghi', 2000000),
('LP005', N'Phòng Suite', N'Phòng sang trọng với phòng khách và phòng ngủ riêng biệt', 3000000);
GO

-- Thêm một số phòng mẫu
INSERT INTO Phong (maPhong, tang, trangThai, moTa, maLoaiPhong) VALUES
-- Tầng 1 - Phòng đơn
('P101', 1, N'Đã đặt', N'Phòng đơn tầng 1, cửa sổ hướng sân', 'LP001'),
('P102', 1, N'Đã đặt', N'Phòng đơn tầng 1, cửa sổ hướng đường', 'LP001'),
('P103', 1, N'Trống', N'Phòng đơn tầng 1, góc tòa nhà', 'LP001'),

-- Tầng 1 - Phòng đôi
('P104', 1, N'Trống', N'Phòng đôi tầng 1, cửa sổ hướng sân', 'LP002'),
('P105', 1, N'Trống', N'Phòng đôi tầng 1, cửa sổ hướng đường', 'LP002'),

-- Tầng 2 - Phòng đơn
('P201', 2, N'Trống', N'Phòng đơn tầng 2, cửa sổ hướng sân', 'LP001'),
('P202', 2, N'Trống', N'Phòng đơn tầng 2, cửa sổ hướng đường', 'LP001'),
('P203', 2, N'Trống', N'Phòng đơn tầng 2, góc tòa nhà', 'LP001'),

-- Tầng 2 - Phòng đôi
('P204', 2, N'Trống', N'Phòng đôi tầng 2, cửa sổ hướng sân', 'LP002'),
('P205', 2, N'Trống', N'Phòng đôi tầng 2, cửa sổ hướng đường', 'LP002'),
('P206', 2, N'Trống', N'Phòng đôi tầng 2, góc tòa nhà', 'LP002'),

-- Tầng 3 - Phòng gia đình
('P301', 3, N'Trống', N'Phòng gia đình tầng 3, cửa sổ hướng sân', 'LP003'),
('P302', 3, N'Trống', N'Phòng gia đình tầng 3, cửa sổ hướng đường', 'LP003'),

-- Tầng 4 - Phòng VIP
('P401', 4, N'Trống', N'Phòng VIP tầng 4, view đẹp', 'LP004'),
('P402', 4, N'Trống', N'Phòng VIP tầng 4, góc tòa nhà', 'LP004'),

-- Tầng 5 - Phòng Suite
('P501', 5, N'Trống', N'Phòng Suite tầng 5, sang trọng nhất', 'LP005'),
('P502', 5, N'Trống', N'Phòng Suite tầng 5, view thành phố', 'LP005');
GO


-- ===================================================================
-- 1. Nhân viên (NhanVien)
-- ===================================================================
PRINT N'Đang thêm dữ liệu bảng NhanVien...';
INSERT INTO NhanVien (maNV, hoTen, soDT, diaChi, chucVu, ngaySinh) VALUES
                                                                       ('NV01', N'Phạm Văn Nam', '0901111222', N'Hà Nội', N'Quản lý', '1988-01-10'),
                                                                       ('NV02', N'Nguyễn Thị Linh', '0912222333', N'TP.HCM', N'Lễ tân', '1995-07-12'),
                                                                       ('NV03', N'Trần Văn Hùng', '0923333444', N'Đà Nẵng', N'Phục vụ', '1997-03-05'),
                                                                       ('NV04', N'Lê Thị Mai', '0934444555', N'Cần Thơ', N'Lễ tân', '1996-09-21'),
                                                                       ('NV05', N'Ngô Văn Long', '0945555666', N'Hải Phòng', N'Bảo vệ', '1990-02-11'),
                                                                       ('NV06', N'Đặng Thị Hoa', '0956666777', N'Hà Nội', N'Kế toán', '1992-12-30'),
                                                                       ('NV07', N'Vũ Minh Quân', '0967777888', N'TP.HCM', N'Phục vụ', '1999-05-16'),
                                                                       ('NV08', N'Hoàng Thị Yến', '0978888999', N'Quảng Ninh', N'Tạp vụ', '2000-08-25');
GO

-- ===================================================================
-- 2. Tài khoản (TaiKhoan)
-- ===================================================================
PRINT N'Đang thêm dữ liệu bảng TaiKhoan...';
-- !!! CẢNH BÁO BẢO MẬT: Mật khẩu không nên lưu dưới dạng văn bản thuần.
-- Cột matKhauHash nên chứa giá trị đã được băm (hashed) bằng thuật toán an toàn.
INSERT INTO TaiKhoan (maTK, maNV, tenDangNhap, matKhauHash, quyen, trangThai) VALUES
                                                                                  ('TK01', 'NV01', 'admin', '123456', N'Admin', N'Active'),
                                                                                  ('TK02', 'NV02', 'linh_nv', '123456', N'NhanVien', N'Active'),
                                                                                  ('TK03', 'NV03', 'hung_nv', '123456', N'NhanVien', N'Active'),
                                                                                  ('TK04', 'NV04', 'mai_nv', '123456', N'NhanVien', N'Active'),
                                                                                  ('TK05', 'NV05', 'long_nv', '123456', N'NhanVien', N'Active'),
                                                                                  ('TK06', 'NV06', 'hoa_nv', '123456', N'NhanVien', N'Active'),
                                                                                  ('TK07', 'NV07', 'quan_nv', '123456', N'NhanVien', N'Active'),
                                                                                  ('TK08', 'NV08', 'yen_nv', '123456', N'NhanVien', N'Active');
GO

-- ===================================================================
-- 3. Khách hàng (KhachHang)
-- ===================================================================
PRINT N'Đang thêm dữ liệu bảng KhachHang...';
INSERT INTO KhachHang (maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD) VALUES
                                                                                     ('KH01', N'Nguyễn Văn A', '0912345678', 'a.nguyen@gmail.com', N'Hà Nội', '1995-02-15', N'Khách VIP', '012345678901'),
                                                                                     ('KH02', N'Trần Thị B', '0987654321', 'b.tran@gmail.com', N'TP.HCM', '1998-06-22', N'Khách quen', '012345678902'),
                                                                                     ('KH03', N'Lê Minh C', '0934567890', 'c.le@gmail.com', N'Đà Nẵng', '2000-09-10', NULL, '012345678903'),
                                                                                     ('KH04', N'Phạm Thị D', '0945678901', 'd.pham@gmail.com', N'Hải Phòng', '1992-03-20', NULL, '012345678904'),
                                                                                     ('KH05', N'Hoàng Văn E', '0956789012', 'e.hoang@gmail.com', N'Quảng Ninh', '1989-11-05', N'Khách doanh nghiệp', '012345678905'),
                                                                                     ('KH06', N'Nguyễn Thị F', '0967890123', 'f.nguyen@gmail.com', N'Hà Nội', '1997-01-25', NULL, '012345678906'),
                                                                                     ('KH07', N'Đỗ Văn G', '0978901234', 'g.do@gmail.com', N'Cần Thơ', '1994-04-12', NULL, '012345678907'),
                                                                                     ('KH08', N'Vũ Thị H', '0989012345', 'h.vu@gmail.com', N'TP.HCM', '1999-07-19', NULL, '012345678908'),
                                                                                     ('KH09', N'Bùi Văn I', '0990123456', 'i.bui@gmail.com', N'Huế', '1990-10-30', NULL, '012345678909'),
                                                                                     ('KH10', N'Trịnh Thị J', '0901234567', 'j.trinh@gmail.com', N'Nghệ An', '1996-05-09', NULL, '012345678910');
GO

-- ===================================================================
-- 4. Mã giảm giá (MaGiamGia)
-- ===================================================================
PRINT N'Đang thêm dữ liệu bảng MaGiamGia...';
INSERT INTO MaGiamGia (maGG, code, giamGia, kieuGiamGia, ngayBatDau, ngayKetThuc, tongTienToiThieu, moTa, trangThai, maNV) VALUES
                                                                                                                               ('GG01', 'SALE10OCT', 10, 'PERCENT', '2025-10-01', '2025-10-15', 200000, N'Giảm 10% cho hóa đơn từ 200k trong tháng 10', N'Đang diễn ra', 'NV01'),
                                                                                                                               ('GG02', 'OPEN20', 20, 'PERCENT', '2025-09-15', '2025-09-25', NULL, N'Giảm 20% dịp khai trương', N'Hết hạn', 'NV02'),
                                                                                                                               ('GG03', 'VIP15', 15, 'PERCENT', '2025-09-01', '2025-12-31', 0, N'Ưu đãi 15% cho khách VIP đến hết năm', N'Đang diễn ra', 'NV03'),
                                                                                                                               ('GG04', 'SAVE50K', 50000, 'AMOUNT', '2025-10-01', '2025-11-01', 300000, N'Giảm 50,000 cho hóa đơn từ 300k', N'Đang diễn ra', 'NV01'),
                                                                                                                               ('GG05', 'BLACKFRIDAY30', 30, 'PERCENT', '2025-11-29', '2025-11-29', NULL, N'Giảm 30% Black Friday', N'Chưa diễn ra', 'NV02');
GO

-- ===================================================================
-- 7. Phiếu đặt phòng (PhieuDatPhong)
-- ===================================================================
PRINT N'Đang thêm dữ liệu bảng PhieuDatPhong...';
INSERT INTO PhieuDatPhong (maPhieu, ngayDat, ngayDen, ngayDi, trangThai, ghiChu, maKH, maNV) VALUES
                                                                                                 ('PD01', '2025-10-01', '2025-10-05', '2025-10-07', N'Đã đặt', N'Khách đặt online', 'KH01', 'NV01'),
                                                                                                 ('PD02', '2025-10-02', '2025-10-06', '2025-10-08', N'Đã đặt', N'Đặt trực tiếp tại quầy', 'KH02', 'NV02');
GO

-- ===================================================================

-- 8. Chi tiết phiếu đặt phòng (CTPhieuDatPhong)
-- ===================================================================
PRINT N'Đang thêm dữ liệu bảng CTPhieuDatPhong...';
INSERT INTO CTPhieuDatPhong (maPhieu, maPhong, ngayDen, ngayDi, ngayNhan, ngayTra, giaPhong) VALUES
                                                                                                 ('PD01', 'P101', '2025-10-05', '2025-10-07', NULL, NULL, 300000),
                                                                                                 ('PD02', 'P102', '2025-10-06', '2025-10-08', NULL, NULL, 500000);
GO

-- ===================================================================
-- 9. Dịch vụ (DichVu)
-- ===================================================================
PRINT N'Đang thêm dữ liệu bảng DichVu...';
INSERT INTO DichVu (maDV, tenDV, soLuong, donGia, moTa, conKinhDoanh) VALUES
                                                                          ('DV01', N'Giặt ủi', 100, 50000, N'Dịch vụ giặt quần áo', 1),
                                                                          ('DV02', N'Đưa đón sân bay', 50, 200000, N'Xe 4 chỗ đưa đón', 1),
                                                                          ('DV03', N'Massage & Spa', 30, 300000, N'Thư giãn chăm sóc sức khỏe', 1),
                                                                          ('DV04', N'Ăn sáng buffet', 200, 150000, N'Buffet sáng tại nhà hàng', 1),
                                                                          ('DV05', N'Thức ăn phòng', 100, 120000, N'Dịch vụ gọi món tại phòng', 1);
GO

-- ===================================================================
-- 10. Hóa đơn (HoaDon)
-- ===================================================================
PRINT N'Đang thêm dữ liệu bảng HoaDon...';
INSERT INTO HoaDon (maHD, ngayLap, phuongThuc, ngayDat, ngayDen, ngayDi, maKH, maNV, maGG, maPhieu, maPhieuDV) VALUES
                                                                                                                   ('HD01', '2025-10-07', N'Tiền mặt', '2025-10-01', '2025-10-05', '2025-10-07', 'KH01', 'NV01', 'GG01', 'PD01', NULL),
                                                                                                                   ('HD02', '2025-09-24', N'Chuyển khoản', '2025-09-20', '2025-09-22', '2025-09-24', 'KH02', 'NV02', NULL, 'PD02', NULL);
GO

-- ===================================================================
-- 11. Phiếu dịch vụ (PhieuDichVu)
-- ===================================================================
PRINT N'Đang thêm dữ liệu bảng PhieuDichVu...';
INSERT INTO PhieuDichVu (maPhieuDV, maHD, ngayLap, maNV, ghiChu) VALUES
                                                                     ('DVHD01', 'HD01', '2025-10-05', 'NV01', N'Khách dùng giặt ủi'),
                                                                     ('DVHD02', 'HD02', '2025-10-06', 'NV02', N'Khách dùng buffet sáng');
GO

-- ===================================================================
-- 12. Chi tiết phiếu dịch vụ (CTPhieuDichVu)
-- ===================================================================
PRINT N'Đang thêm dữ liệu bảng CTPhieuDichVu...';
INSERT INTO CTPhieuDichVu (maPhieuDV, maDV, soLuong, donGia) VALUES
                                                                 ('DVHD01', 'DV01', 2, 50000),
                                                                 ('DVHD02', 'DV04', 2, 150000);
GO

-- ===================================================================
-- 13. Chi tiết hóa đơn (CTHoaDon)
-- ===================================================================
PRINT N'Đang thêm dữ liệu bảng CTHoaDon...';
INSERT INTO CTHoaDon (maCTHD, maHD, loai, maPhong, maDV, soLuong, donGia) VALUES
-- Các chi tiết cho Hóa đơn HD01
('CT001', 'HD01', 'Phong',  'P101', NULL,   2, 300000), -- Tiền phòng P101, 2 đêm
('CT002', 'HD01', 'DichVu', NULL,   'DV01', 2, 50000),  -- Tiền dịch vụ giặt ủi, 2 món

-- Các chi tiết cho Hóa đơn HD02
('CT003', 'HD02', 'Phong',  'P102', NULL,   2, 500000), -- Tiền phòng P102, 2 đêm
('CT004', 'HD02', 'DichVu', NULL,   'DV04', 2, 150000); -- Tiền dịch vụ buffet, 2 người
GO
