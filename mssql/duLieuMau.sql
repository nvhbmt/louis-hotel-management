-- Thiết lập cơ sở dữ liệu


-------------------------------------------------------------------
-- 1. LoaiPhong (Loại Phòng)
-------------------------------------------------------------------
PRINT N'-- 1. Đang thêm dữ liệu bảng LoaiPhong...';
INSERT INTO LoaiPhong (maLoaiPhong, tenLoai, moTa, donGia)
VALUES ('LP001', N'Phòng Đơn Tiêu Chuẩn', N'Phòng dành cho 1 người, có giường đơn, phòng tắm riêng', 500000),
       ('LP002', N'Phòng Đôi Hạng Sang', N'Phòng dành cho 2 người, có giường đôi lớn, đầy đủ tiện nghi', 850000),
       ('LP003', N'Phòng Gia Đình Superior', N'Phòng dành cho gia đình, có 1 giường đôi và 1 giường đơn phụ', 1300000),
       ('LP004', N'Phòng VIP Executive', N'Phòng cao cấp, có khu vực làm việc riêng biệt, view thành phố', 2200000),
       ('LP005', N'Phòng Suite Tổng Thống', N'Phòng sang trọng nhất với phòng khách và phòng ngủ riêng biệt, bếp nhỏ',
        3500000);
GO

-------------------------------------------------------------------
-- 2. Phong (Phòng)
-------------------------------------------------------------------
PRINT N'-- 2. Đang thêm dữ liệu bảng Phong...';
INSERT INTO Phong (maPhong, tang, trangThai, moTa, maLoaiPhong)
VALUES
-- Tầng 1
('P101', 1, N'Trống', N'Phòng đơn tầng 1, cửa sổ hướng sân vườn', 'LP001'),
('P102', 1, N'Đang sử dụng', N'Phòng đơn tầng 1, cửa sổ hướng đường lớn', 'LP001'),
('P103', 1, N'Trống', N'Phòng đơn tầng 1, yên tĩnh', 'LP001'),
('P104', 1, N'Trống', N'Phòng đôi tầng 1, cửa sổ hướng sân', 'LP002'),
('P105', 1, N'Trống', N'Phòng đôi tầng 1, cửa sổ hướng đường', 'LP002'),

-- Tầng 2
('P201', 2, N'Trống', N'Phòng đơn tầng 2, view hồ bơi', 'LP001'),
('P202', 2, N'Trống', N'Phòng đơn tầng 2, view thành phố', 'LP001'),
('P203', 2, N'Trống', N'Phòng đơn tầng 2, góc tòa nhà', 'LP001'),
('P204', 2, N'Đang sử dụng', N'Phòng đôi tầng 2, view sân', 'LP002'),
('P205', 2, N'Đang sử dụng', N'Phòng đôi tầng 2, view đường', 'LP002'),
('P206', 2, N'Trống', N'Phòng đôi tầng 2, góc tòa nhà', 'LP002'),

-- Tầng 3
('P301', 3, N'Trống', N'Phòng gia đình tầng 3, cửa sổ hướng sân', 'LP003'),
('P302', 3, N'Trống', N'Phòng gia đình tầng 3, cửa sổ hướng đường', 'LP003'),

-- Tầng 4
('P401', 4, N'Trống', N'Phòng VIP tầng 4, view đẹp nhất', 'LP004'),
('P402', 4, N'Trống', N'Phòng VIP tầng 4, góc tòa nhà', 'LP004'),

-- Tầng 5
('P501', 5, N'Trống', N'Phòng Suite tầng 5, sang trọng nhất', 'LP005'),
('P502', 5, N'Trống', N'Phòng Suite tầng 5, view thành phố', 'LP005');
GO

---------------------------------------------
-------------------------------------------------------------------
PRINT N'-- 3. Đang thêm dữ liệu bảng NhanVien...';
INSERT INTO NhanVien (maNV, hoTen, soDT, diaChi, chucVu, ngaySinh)
VALUES ('NV001', N'Phạm Văn Nam', '0901111222', N'Hà Nội', N'Quản lý', '1988-01-10'),
       ('NV002', N'Nguyễn Thị Linh', '0912222333', N'TP.HCM', N'Lễ tân', '1995-07-12'),
       ('NV003', N'Trần Văn Hùng', '0923333444', N'Đà Nẵng', N'Lễ tân', '1997-03-05'),
       ('NV004', N'Lê Thị Mai', '0934444555', N'Cần Thơ', N'Lễ tân', '1996-09-21'),
       ('NV005', N'Ngô Văn Long', '0945555666', N'Hải Phòng', N'Lễ tân', '1990-02-11');
GO

-------------------------------------------------------------------
-- 4. TaiKhoan (Tài khoản)
-------------------------------------------------------------------
PRINT N'-- 4. Đang thêm dữ liệu bảng TaiKhoan...';
PRINT N'-- 5. Đang thêm dữ liệu bảng KhachHang...';
GO

-------------------------------------------------------------------
-- 5. KhachHang (Khách hàng)
-------------------------------------------------------------------
INSERT INTO TaiKhoan (maTK, maNV, tenDangNhap, matKhauHash, quyen)
VALUES ('TK001', 'NV001', 'manager', '4XKzp/qajqT93GnwSpzI3k00cLhgU/fEFLw/RW1tsqjv6NREO9+L+8ckMRGLxrxY',
        'Manager'), -- Mật khẩu giả định: 'manager123'
       ('TK002', 'NV002', 'staff', 'j+NE/7ITxbv5hcu41ShyHze7rEOu+3rJVS/pCcDVyKs1xKlDgyPNxFmQhcC0iVZ3',
        'Staff'),   -- Mật khẩu giả định: 'staff123';
       -- Nhân viên Trần Văn Hùng
       ('TK003', 'NV003', 'hungtran', 'j+NE/7ITxbv5hcu41ShyHze7rEOu+3rJVS/pCcDVyKs1xKlDgyPNxFmQhcC0iVZ3', 'Staff'),
       -- Nhân viên Lê Thị Mai
       ('TK004', 'NV004', 'maile', 'j+NE/7ITxbv5hcu41ShyHze7rEOu+3rJVS/pCcDVyKs1xKlDgyPNxFmQhcC0iVZ3', 'Staff'),
       -- Nhân viên Ngô Văn Long
       ('TK005', 'NV005', 'longngo', 'j+NE/7ITxbv5hcu41ShyHze7rEOu+3rJVS/pCcDVyKs1xKlDgyPNxFmQhcC0iVZ3', 'Staff');
INSERT INTO KhachHang (maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD, hangKhach, trangThai)
VALUES ('KH001', N'Nguyễn Thanh Tùng', '0912345678', 'tung.nguyen@company.com', N'Hà Nội', '1995-02-15',
        N'Khách doanh nghiệp, cần hóa đơn VAT', '012345678901', N'Khách doanh nghiệp',
        'Check-out'),                                                                           -- Hoàn thành booking PD001
       ('KH002', N'Trần Thị Mai Hương', '0987654321', 'huong.tran@email.com', N'TP.HCM', '1998-06-22',
        N'Khách du lịch nước ngoài (đặt phòng VIP)', '012345678902', N'Khách VIP',
        'Check-out'),                                                                           -- Hoàn thành booking PD002
       ('KH003', N'Lê Văn Chiến', '0934567890', 'chien.le@web.com', N'Đà Nẵng', '2000-09-10', N'Khách ở lần đầu',
        '012345678903', N'Khách thường', N'Đang lưu trú'),                                      -- Đang sử dụng booking PD006 (ưu tiên active)
       ('KH004', N'Phạm Thị Yến', '0945678901', 'yen.pham@private.com', N'Hải Phòng', '1992-03-20',
        N'Khách quen, thích phòng tầng cao', '012345678904', N'Khách quen',
        N'Đang lưu trú'),                                                                       -- Đang sử dụng booking PD007 (ưu tiên active)
       ('KH005', N'Hoàng Anh Dũng', '0956789012', 'dung.hoang@contact.com', N'Quảng Ninh', '1989-11-05',
        N'Thanh toán bằng thẻ tín dụng', '012345678905', N'Khách VIP', 'Check-out'),            -- Hoàn thành booking PD005
       ('KH006', N'Ngô Minh Quân', '0962345678', 'quan.ngo@email.com', N'Cần Thơ', '1996-08-14',
        N'Khách đặt qua ứng dụng', '012345678906', N'Khách thường', N'Đang lưu trú'),           -- Khách mới, chưa có booking
       ('KH007', N'Đặng Thảo Nhi', '0973456789', 'nhi.dang@company.com', N'Bình Dương', '1999-01-09',
        N'Khách trẻ, hay đi công tác', '012345678907', N'Khách doanh nghiệp',
        N'Đang lưu trú'),                                                                       -- Khách mới, chưa có booking
       ('KH008', N'Tạ Đức Long', '0984567890', 'long.ta@hotel.com', N'Huế', '1993-12-30', N'Khách VIP lâu năm',
        '012345678908', N'Khách VIP', N'Đang lưu trú'),                                         -- Khách tiềm năng, chưa có booking
       ('KH009', N'Vũ Quỳnh Anh', '0995678901', 'anh.vu@personal.com', N'Nha Trang', '1997-07-21',
        N'Khách nữ thân thiết, đặt phòng view biển', '012345678909', N'Khách quen', N'Đã Đặt'), -- Có thể đã đặt trước
       ('KH010', N'Trịnh Quốc Hưng', '0906789012', 'hung.trinh@email.com', N'Hà Nội', '1991-10-01',
        N'Khách lâu năm, yêu cầu dịch vụ cao cấp', '012345678910', N'Khách VIP',
        N'Đang lưu trú'); -- Khách VIP tiềm năng
GO

-------------------------------------------------------------------
-- 6. KhuyenMai (Mã khuyến mãi)
-------------------------------------------------------------------
PRINT N'-- 6. Đang thêm dữ liệu bảng KhuyenMai...';
INSERT INTO KhuyenMai (maKM, code, giamGia, kieuGiamGia, ngayBatDau, ngayKetThuc, tongTienToiThieu, moTa, trangThai,
                       maNV)
VALUES
    -- KM0001: Khuyến mãi cả năm (Active)
    -- Giảm 10% (PERCENT)
    ('KM0001', 'WELCOME2025', 10, 'PERCENT', '2025-01-01', '2025-12-31', 1500000,
     N'Giảm 10% cho khách hàng mới năm 2025', N'Đang diễn ra', 'NV001'),

    -- KM0002: Khuyến mãi Mùa Đông (Active)
    -- Giảm 500k tiền mặt (AMOUNT) - Đang trong thời gian áp dụng (1/10 - 31/12)
    ('KM0002', 'WINTER500', 500000, 'AMOUNT', '2025-10-01', '2025-12-31', 5000000,
     N'Ưu đãi mùa đông: Giảm 500k cho hóa đơn trên 5 triệu', N'Đang diễn ra', 'NV002'),

    -- KM0003: Khách VIP (Active)
    -- Giảm 15% (PERCENT) - Hiệu lực dài hạn
    ('KM0003', 'VIPGOLD', 15, 'PERCENT', '2025-01-01', '2026-01-01', 0,
     N'Đặc quyền VIP: Giảm 15% trên mọi hóa đơn', N'Đang sử dụng', 'NV001'),

    -- KM0004: Khuyến mãi Tháng 11 (Active)
    -- Giảm 200k tiền mặt (AMOUNT) - Chạy trong tháng 11 (Hiện tại là 22/11 -> OK)
    ('KM0004', 'NOV200', 200000, 'AMOUNT', '2025-11-01', '2025-11-30', 2000000,
     N'Chào tháng 11: Giảm 200k khi đặt phòng sớm', N'Đang sử dụng', 'NV004'),

    -- KM0005: Giáng Sinh/Năm mới (Upcoming)
    -- Giảm 30% (PERCENT) - Ngày bắt đầu là 20/12 (Tương lai) nên trạng thái là Chưa bắt đầu
    ('KM0005', 'XMAS30', 30, 'PERCENT', '2025-12-20', '2025-12-25', NULL,
     N'Giáng sinh an lành: Giảm 30% cho các cặp đôi', N'Chưa bắt đầu', 'NV002');
GO

-------------------------------------------------------------------
-- 7. DichVu (Dịch vụ)
-------------------------------------------------------------------
PRINT N'-- 7. Đang thêm dữ liệu bảng DichVu...';
INSERT INTO DichVu (maDV, tenDV, soLuong, donGia, moTa, conKinhDoanh)
VALUES ('DV001', N'Giặt ủi nhanh', 100, 60000, N'Dịch vụ giặt ủi nhanh (trong 4 giờ)', 1),
       ('DV002', N'Đưa đón sân bay (VIP)', 50, 350000, N'Xe 7 chỗ đưa đón cao cấp', 1),
       ('DV003', N'Massage Toàn Thân', 30, 600000, N'Gói Massage Thư giãn 60 phút', 1),
       ('DV004', N'Ăn sáng buffet', 200, 180000, N'Buffet sáng cao cấp tại nhà hàng', 1),
       ('DV005', N'Thức ăn phòng (Room Service)', 100, 150000, N'Dịch vụ gọi món ăn nhẹ tại phòng', 1);
GO

-------------------------------------------------------------------
-- 8. PhieuDatPhong (Phiếu đặt phòng)
-------------------------------------------------------------------
PRINT N'-- 8. Đang thêm dữ liệu bảng PhieuDatPhong...';
INSERT INTO PhieuDatPhong (maPhieu, ngayDat, ngayDen, ngayDi, trangThai, ghiChu, maKH, maNV, tienCoc)
VALUES
-- Nhóm Hoàn thành (Đã thanh toán)
('PD001', '2025-10-10', '2025-10-10', '2025-10-12', N'Hoàn thành', N'Đặt trực tiếp', 'KH001', 'NV002', 0.00),
('PD002', '2025-10-14', '2025-10-14', '2025-10-15', N'Hoàn thành', N'Đặt trực tiếp', 'KH002', 'NV004', 0.00),
('PD003', '2025-10-16', '2025-10-16', '2025-10-17', N'Hoàn thành', N'Đặt trực tiếp', 'KH003', 'NV002', 0.00),
('PD004', '2025-10-15', '2025-10-22', '2025-10-25', N'Hoàn thành', N'Đặt trước', 'KH004', 'NV003', 300000.00),
('PD005', '2025-10-18', '2025-10-24', '2025-10-26', N'Hoàn thành', N'Đặt trước', 'KH005', 'NV004', 200000.00),
-- Nhóm Đang sử dụng (Chưa thanh toán)
('PD006', '2025-12-20', '2025-12-20', '2025-12-22', N'Đang sử dụng', N'Đặt trực tiếp', 'KH002', 'NV001', 0.00),
('PD007', '2025-12-20', '2025-12-20', '2025-12-23', N'Đang sử dụng', N'Đặt trực tiếp', 'KH003', 'NV001', 0.00),
('PD008', '2025-12-15', '2025-12-19', '2025-12-24', N'Đang sử dụng', N'Đặt trước', 'KH004', 'NV003', 500000.00);
GO

-------------------------------------------------------------------
-- 9. HoaDon (Hóa đơn) - Tính toán theo logic Controller Java
-------------------------------------------------------------------
PRINT N'-- 9. Đang thêm dữ liệu bảng HoaDon...';
INSERT INTO HoaDon
(maHD, ngayLap, phuongThuc, trangThai, tongTien, PhatNhanPhongTre, PhatTraPhongSom, PhatTraPhongTre, GiamGiaMaGG,
 GiamGiaHangKH, TongVAT, ngayDi, maKH, maNV, maKM)
VALUES
-- HD001: P.Phòng 2.6M + DV 470k. KM giảm 260k. VAT 281k.
('HD001', '2025-10-10', N'Chuyển khoản', N'Đã thanh toán', 3091000.00, 0, 0, 0, 260000.00, 0, 281000.00, '2025-10-12',
 'KH001', 'NV002', 'KM0001'),
-- HD002: P.Phòng 2.2M + DV 780k. VIP giảm 10% (220k). VAT 276k.
('HD002', '2025-10-14', N'Tiền mặt', N'Đã thanh toán', 3036000.00, 0, 0, 0, 0, 220000.00, 276000.00, '2025-10-15',
 'KH002', 'NV004', NULL),
-- HD003: P.Phòng 850k + DV 360k. Phạt trả trễ 1 ngày (85k). VAT 129.5k.
('HD003', '2025-10-16', N'Ví điện tử', N'Đã thanh toán', 1424500.00, 0, 0, 85000.00, 0, 0, 129500.00, '2025-10-18',
 'KH003', 'NV002', NULL),
-- HD004: P.Phòng 1.5M. Phạt nhận trễ (250k). Giảm 5% (75k). Cọc 300k. VAT 167.5k.
('HD004', '2025-10-15', N'Tiền mặt', N'Đã thanh toán', 1542500.00, 250000.00, 0, 0, 0, 75000.00, 167500.00,
 '2025-10-25', 'KH004', 'NV003', NULL),
-- HD005: P.Phòng 1M. VIP giảm 10% (100k). Cọc 200k. VAT 90k.
('HD005', '2025-10-18', N'Chuyển khoản', N'Đã thanh toán', 790000.00, 0, 0, 0, 0, 100000.00, 90000.00, '2025-10-26',
 'KH005', 'NV004', NULL),
-- Nhóm chưa thanh toán (Tiền = 0, ngayDi = NULL)
('HD006', '2025-12-20', NULL, N'Chưa thanh toán', 0.00, 0, 0, 0, 0, 0, 0, NULL, 'KH002', 'NV001', NULL),
('HD007', '2025-12-20', NULL, N'Chưa thanh toán', 0.00, 0, 0, 0, 0, 0, 0, NULL, 'KH003', 'NV001', NULL),
('HD008', '2025-12-15', NULL, N'Chưa thanh toán', 0.00, 0, 0, 0, 0, 0, 0, NULL, 'KH004', 'NV002', NULL);
GO

-------------------------------------------------------------------
-- 10. CTHoaDonPhong (Chi tiết hóa đơn phòng)
-------------------------------------------------------------------
PRINT N'-- 10. Đang thêm dữ liệu bảng CTHoaDonPhong...';
INSERT INTO CTHoaDonPhong (maHD, maPhieu, maPhong, ngayDen, ngayDi, giaPhong)
VALUES ('HD001', 'PD001', 'P301', '2025-10-10', '2025-10-12', 1300000),
       ('HD002', 'PD002', 'P401', '2025-10-14', '2025-10-15', 2200000),
       ('HD003', 'PD003', 'P105', '2025-10-16', '2025-10-18', 850000), -- Trễ 1 ngày so với phiếu
       ('HD004', 'PD004', 'P101', '2025-10-23', '2025-10-25', 500000), -- Đến trễ 1 ngày so với phiếu
       ('HD005', 'PD005', 'P102', '2025-10-24', '2025-10-26', 500000),
       ('HD006', 'PD006', 'P102', '2025-12-20', '2025-12-22', 500000),
       ('HD007', 'PD007', 'P204', '2025-12-20', '2025-12-23', 850000),
       ('HD008', 'PD008', 'P205', '2025-12-19', '2025-12-24', 850000);
GO

-------------------------------------------------------------------
-- 11. PhieuDichVu (Phiếu dịch vụ)
-------------------------------------------------------------------
PRINT N'-- 11. Đang thêm dữ liệu bảng PhieuDichVu...';
INSERT INTO PhieuDichVu (maPhieuDV, maHD, ngayLap, maNV, ghiChu)
VALUES ('PDV001', 'HD001', '2025-10-11', 'NV003', N'Giặt ủi HD001'),
       ('PDV002', 'HD002', '2025-10-15', 'NV004', N'Ăn sáng HD002'),
       ('PDV003', 'HD003', '2025-10-17', 'NV002', N'Ăn tối HD003');
GO

-------------------------------------------------------------------
-- 12. CTHoaDonDichVu (Chi tiết hóa đơn dịch vụ)
-------------------------------------------------------------------
PRINT N'-- 12. Đang thêm dữ liệu bảng CTHoaDonDichVu...';
INSERT INTO CTHoaDonDichVu (maHD, maPhieuDV, maDV, soLuong, donGia)
VALUES ('HD001', 'PDV001', 'DV001', 2, 60000),
       ('HD001', 'PDV001', 'DV002', 1, 350000),
       ('HD002', 'PDV002', 'DV003', 1, 600000),
       ('HD002', 'PDV002', 'DV004', 1, 180000),
       ('HD003', 'PDV003', 'DV001', 3, 60000),
       ('HD003', 'PDV003', 'DV004', 1, 180000);
GO