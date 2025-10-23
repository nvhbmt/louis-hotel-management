-- Thêm dữ liệu mẫu cho các loại phòng
USE QuanLyKhachSan;
GO

-------------------------------------------------------------------
-- 1. LoaiPhong (Loại Phòng) & Phong (Phòng)
-------------------------------------------------------------------

-- Thêm các loại phòng
INSERT INTO LoaiPhong (maLoaiPhong, tenLoai, moTa, donGia) VALUES
                                                               ('LP001', N'Phòng Đơn Tiêu Chuẩn', N'Phòng dành cho 1 người, có giường đơn, phòng tắm riêng', 500000),
                                                               ('LP002', N'Phòng Đôi Hạng Sang', N'Phòng dành cho 2 người, có giường đôi lớn, đầy đủ tiện nghi', 850000),
                                                               ('LP003', N'Phòng Gia Đình Superior', N'Phòng dành cho gia đình, có 1 giường đôi và 1 giường đơn phụ', 1300000),
                                                               ('LP004', N'Phòng VIP Executive', N'Phòng cao cấp, có khu vực làm việc riêng biệt, view thành phố', 2200000),
                                                               ('LP005', N'Phòng Suite Tổng Thống', N'Phòng sang trọng nhất với phòng khách và phòng ngủ riêng biệt, bếp nhỏ', 3500000);
GO

-- Thêm một số phòng mẫu
INSERT INTO Phong (maPhong, tang, trangThai, moTa, maLoaiPhong) VALUES
-- Tầng 1 - Phòng đơn
('P101', 1, N'Đang dọn dẹp', N'Phòng đơn tầng 1, cửa sổ hướng sân vườn', 'LP001'),
('P102', 1, N'Đã đặt', N'Phòng đơn tầng 1, cửa sổ hướng đường lớn', 'LP001'),
('P103', 1, N'Trống', N'Phòng đơn tầng 1, yên tĩnh', 'LP001'),

-- Tầng 1 - Phòng đôi
('P104', 1, N'Trống', N'Phòng đôi tầng 1, cửa sổ hướng sân', 'LP002'),
('P105', 1, N'Đang sử dụng', N'Phòng đôi tầng 1, cửa sổ hướng đường', 'LP002'), -- Đang sử dụng cho HD03

-- Tầng 2 - Phòng đơn
('P201', 2, N'Trống', N'Phòng đơn tầng 2, view hồ bơi', 'LP001'),
('P202', 2, N'Trống', N'Phòng đơn tầng 2, view thành phố', 'LP001'),
('P203', 2, N'Trống', N'Phòng đơn tầng 2, góc tòa nhà', 'LP001'),

-- Tầng 2 - Phòng đôi
('P204', 2, N'Trống', N'Phòng đôi tầng 2, view sân', 'LP002'),
('P205', 2, N'Trống', N'Phòng đôi tầng 2, view đường', 'LP002'),
('P206', 2, N'Trống', N'Phòng đôi tầng 2, góc tòa nhà', 'LP002'),

-- Tầng 3 - Phòng gia đình
('P301', 3, N'Đang sử dụng', N'Phòng gia đình tầng 3, cửa sổ hướng sân', 'LP003'), -- Đang sử dụng cho HD01
('P302', 3, N'Trống', N'Phòng gia đình tầng 3, cửa sổ hướng đường', 'LP003'),

-- Tầng 4 - Phòng VIP
('P401', 4, N'Đang sử dụng', N'Phòng VIP tầng 4, view đẹp nhất', 'LP004'), -- Đang sử dụng cho HD02
('P402', 4, N'Trống', N'Phòng VIP tầng 4, góc tòa nhà', 'LP004'),

-- Tầng 5 - Phòng Suite
('P501', 5, N'Trống', N'Phòng Suite tầng 5, sang trọng nhất', 'LP005'),
('P502', 5, N'Trống', N'Phòng Suite tầng 5, view thành phố', 'LP005');
GO

-------------------------------------------------------------------
-- 2. Nhân viên (NhanVien)
-------------------------------------------------------------------
PRINT N'Đang thêm dữ liệu bảng NhanVien...';
INSERT INTO NhanVien (maNV, hoTen, soDT, diaChi, chucVu, ngaySinh) VALUES
                                                                       ('NV001', N'Phạm Văn Nam', '0901111222', N'Hà Nội', N'Quản lý', '1988-01-10'),
                                                                       ('NV002', N'Nguyễn Thị Linh', '0912222333', N'TP.HCM', N'Lễ tân', '1995-07-12'),
                                                                       ('NV003', N'Trần Văn Hùng', '0923333444', N'Đà Nẵng', N'Phục vụ', '1997-03-05'),
                                                                       ('NV004', N'Lê Thị Mai', '0934444555', N'Cần Thơ', N'Lễ tân', '1996-09-21'),
                                                                       ('NV005', N'Ngô Văn Long', '0945555666', N'Hải Phòng', N'Bảo vệ', '1990-02-11');
GO

-------------------------------------------------------------------
-- 3. Tài khoản (TaiKhoan)
-------------------------------------------------------------------
PRINT N'Đang thêm dữ liệu bảng TaiKhoan...';
INSERT INTO TaiKhoan (maTK, maNV, tenDangNhap, matKhauHash, quyen) VALUES
                                                                       ('TK001', 'NV001', 'manager', '4XKzp/qajqT93GnwSpzI3k00cLhgU/fEFLw/RW1tsqjv6NREO9+L+8ckMRGLxrxY', 'Manager'), -- Giả định mật khẩu là '123456' đã được băm
                                                                       ('TK002', 'NV002', 'linhnguyen', 'j+NE/7ITxbv5hcu41ShyHze7rEOu+3rJVS/pCcDVyKs1xKlDgyPNxFmQhcC0iVZ3', 'Staff'), -- Giả định mật khẩu là 'staff123' đã được băm
                                                                       ('TK003', 'NV004', 'maile', 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Staff');
GO

-------------------------------------------------------------------
-- 4. Khách hàng (KhachHang)
-------------------------------------------------------------------
PRINT N'Đang thêm dữ liệu bảng KhachHang...';
INSERT INTO KhachHang (maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD) VALUES
                                                                                     ('KH001', N'Nguyễn Thanh Tùng', '0912345678', 'tung.nguyen@company.com', N'Hà Nội', '1995-02-15', N'Khách doanh nghiệp, cần hóa đơn VAT', '012345678901'),
                                                                                     ('KH002', N'Trần Thị Mai Hương', '0987654321', 'huong.tran@email.com', N'TP.HCM', '1998-06-22', N'Khách du lịch nước ngoài (đặt phòng VIP)', '012345678902'),
                                                                                     ('KH003', N'Lê Văn Chiến', '0934567890', 'chien.le@web.com', N'Đà Nẵng', '2000-09-10', NULL, '012345678903'),
                                                                                     ('KH004', N'Phạm Thị Yến', '0945678901', 'yen.pham@private.com', N'Hải Phòng', '1992-03-20', N'Khách quen, thích phòng tầng cao', '012345678904'),
                                                                                     ('KH005', N'Hoàng Anh Dũng', '0956789012', 'dung.hoang@contact.com', N'Quảng Ninh', '1989-11-05', N'Thanh toán bằng thẻ tín dụng', '012345678905');
GO

-------------------------------------------------------------------
-- 5. Mã giảm giá (MaGiamGia)
-------------------------------------------------------------------
PRINT N'Đang thêm dữ liệu bảng MaGiamGia...';
INSERT INTO MaGiamGia (maGG, code, giamGia, kieuGiamGia, ngayBatDau, ngayKetThuc, tongTienToiThieu, moTa, trangThai, maNV) VALUES
                                                                                                                               ('GG001', 'SALE10OCT', 10, 'PERCENT', '2025-10-01', '2025-10-31', 2000000, N'Giảm 10% cho tổng hóa đơn từ 2 triệu trong tháng 10', N'Đang diễn ra', 'NV001'),
                                                                                                                               ('GG002', 'WEEKEND50K', 50000, 'AMOUNT', '2025-10-10', '2025-10-25', 1000000, N'Giảm 50,000 cho hóa đơn cuối tuần từ 1 triệu', N'Đang diễn ra', 'NV002'),
                                                                                                                               ('GG003', 'VIP15', 15, 'PERCENT', '2025-09-01', '2025-12-31', 0, N'Ưu đãi 15% cho khách VIP đến hết năm', N'Đang diễn ra', 'NV001'),
                                                                                                                               ('GG004', 'SAVE100K', 100000, 'AMOUNT', '2025-10-01', '2025-11-01', 3000000, N'Giảm 100,000 cho hóa đơn từ 3 triệu', N'Đang diễn ra', 'NV004'),
                                                                                                                               ('GG005', 'BLACKFRI30', 30, 'PERCENT', '2025-11-29', '2025-11-29', NULL, N'Giảm 30% Black Friday', N'Chưa diễn ra', 'NV002');
GO

-------------------------------------------------------------------
-- 6. Dịch vụ (DichVu)
-------------------------------------------------------------------
PRINT N'Đang thêm dữ liệu bảng DichVu...';
INSERT INTO DichVu (maDV, tenDV, soLuong, donGia, moTa, conKinhDoanh) VALUES
                                                                          ('DV001', N'Giặt ủi nhanh', 100, 60000, N'Dịch vụ giặt ủi nhanh (trong 4 giờ)', 1),
                                                                          ('DV002', N'Đưa đón sân bay (VIP)', 50, 350000, N'Xe 7 chỗ đưa đón cao cấp', 1),
                                                                          ('DV003', N'Massage Toàn Thân', 30, 600000, N'Gói Massage Thư giãn 60 phút', 1),
                                                                          ('DV004', N'Ăn sáng buffet', 200, 180000, N'Buffet sáng cao cấp tại nhà hàng', 1),
                                                                          ('DV005', N'Thức ăn phòng (Room Service)', 100, 150000, N'Dịch vụ gọi món ăn nhẹ tại phòng', 1);
GO

-------------------------------------------------------------------
-- 7. Phiếu đặt phòng (PhieuDatPhong)
-------------------------------------------------------------------
PRINT N'Đang thêm dữ liệu bảng PhieuDatPhong...';
INSERT INTO PhieuDatPhong (maPhieu, ngayDat, ngayDen, ngayDi, trangThai, ghiChu, maKH, maNV) VALUES
                                                                                                 ('PD001', '2025-10-15', '2025-10-20', '2025-10-23', N'Đã hủy', N'Khách báo hủy vì lý do cá nhân', 'KH001', 'NV002'), -- Phiếu đã hủy
                                                                                                 ('PD002', '2025-10-20', '2025-10-22', '2025-10-25', N'Đã đặt', N'Khách yêu cầu phòng yên tĩnh', 'KH003', 'NV004'), -- Phiếu còn hiệu lực
                                                                                                 ('PD003', '2025-10-10', '2025-10-18', '2025-10-20', N'Đã nhận phòng', N'Đã check-in sớm 1 giờ', 'KH004', 'NV002'); -- Đã nhận phòng
GO

-------------------------------------------------------------------
-- 8. Chi tiết phiếu đặt phòng (CTPhieuDatPhong)
-------------------------------------------------------------------
PRINT N'Đang thêm dữ liệu bảng CTPhieuDatPhong...';
INSERT INTO CTPhieuDatPhong (maPhieu, maPhong, ngayDen, ngayDi, ngayNhan, ngayTra, giaPhong) VALUES
                                                                                                 ('PD001', 'P102', '2025-10-20', '2025-10-23', NULL, NULL, 500000), -- PD001 bị hủy
                                                                                                 ('PD002', 'P204', '2025-10-22', '2025-10-25', NULL, NULL, 850000), -- PD002 sắp đến
                                                                                                 ('PD003', 'P105', '2025-10-18', '2025-10-20', '2025-10-18', NULL, 850000); -- PD003 đã nhận
GO

-------------------------------------------------------------------
-- 9. Hóa đơn (HoaDon)
-------------------------------------------------------------------
PRINT N'Đang thêm dữ liệu bảng HoaDon...';
INSERT INTO HoaDon (maHD, ngayLap, phuongThuc, ngayDat, ngayDen, ngayDi, maKH, maNV, maGG, maPhieu, maPhieuDV) VALUES
                                                                                                                   ('HD001', '2025-10-12', N'Chuyển khoản', '2025-10-10', '2025-10-10', '2025-10-12', 'KH001', 'NV002', 'GG001', NULL, 'DVHD001'), -- Thanh toán phòng P301, dùng GG001
                                                                                                                   ('HD002', '2025-10-15', N'Tiền mặt', '2025-10-14', '2025-10-14', '2025-10-15', 'KH002', 'NV004', NULL, NULL, 'DVHD002'), -- Thanh toán phòng P401
                                                                                                                   ('HD003', '2025-10-17', N'Thẻ tín dụng', '2025-10-16', '2025-10-16', '2025-10-17', 'KH003', 'NV002', 'GG002', NULL, NULL); -- Thanh toán phòng P105 (Giả sử P105 đã check-out ngày 17)
GO

-------------------------------------------------------------------
-- 10. Phiếu dịch vụ (PhieuDichVu)
-------------------------------------------------------------------
PRINT N'Đang thêm dữ liệu bảng PhieuDichVu...';
INSERT INTO PhieuDichVu (maPhieuDV, maHD, ngayLap, maNV, ghiChu) VALUES
                                                                     ('DVHD001', 'HD001', '2025-10-11', 'NV003', N'Giặt ủi cho khách phòng P301'),
                                                                     ('DVHD002', 'HD002', '2025-10-15', 'NV003', N'Đưa đón sân bay cho khách phòng P401');
GO

-------------------------------------------------------------------
-- 11. Chi tiết phiếu dịch vụ (CTPhieuDichVu)
-------------------------------------------------------------------
PRINT N'Đang thêm dữ liệu bảng CTPhieuDichVu...';
INSERT INTO CTPhieuDichVu (maPhieuDV, maDV, soLuong, donGia) VALUES
                                                                 ('DVHD001', 'DV001', 3, 60000),    -- Giặt ủi 3 món cho HD001
                                                                 ('DVHD001', 'DV004', 1, 180000),   -- 1 suất ăn sáng cho HD001
                                                                 ('DVHD002', 'DV002', 1, 350000);   -- Đưa đón sân bay cho HD002
GO

-------------------------------------------------------------------
-- 12. Chi tiết hóa đơn (CTHoaDon)
-------------------------------------------------------------------
PRINT N'Đang thêm dữ liệu bảng CTHoaDon...';
INSERT INTO CTHoaDon (maCTHD, maHD, loai, maPhong, maDV, soLuong, donGia) VALUES
-- Các chi tiết cho Hóa đơn HD001 (P301 - 2 đêm, DV001 - 3, DV004 - 1)
('CT001', 'HD001', N'Phong',  'P301', NULL,   2, 1300000), -- Tiền phòng P301, 2 đêm * 1,300,000
('CT002', 'HD001', N'DichVu', NULL,   'DV001', 3, 60000),  -- Tiền dịch vụ giặt ủi, 3 món * 60,000
('CT003', 'HD001', N'DichVu', NULL,   'DV004', 1, 180000), -- Tiền dịch vụ ăn sáng, 1 suất * 180,000

-- Các chi tiết cho Hóa đơn HD002 (P401 - 1 đêm, DV002 - 1)
('CT004', 'HD002', N'Phong',  'P401', NULL,   1, 2200000), -- Tiền phòng P401, 1 đêm * 2,200,000
('CT005', 'HD002', N'DichVu', NULL,   'DV002', 1, 350000), -- Tiền dịch vụ đưa đón, 1 lần * 350,000

-- Các chi tiết cho Hóa đơn HD003 (P105 - 1 đêm)
('CT006', 'HD003', N'Phong',  'P105', NULL,   1, 850000); -- Tiền phòng P105, 1 đêm * 850,000
GO

PRINT N'Thêm dữ liệu mẫu hoàn tất.';