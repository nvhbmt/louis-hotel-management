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
('P102', 1, N'Trống', N'Phòng đơn tầng 1, cửa sổ hướng đường lớn', 'LP001'),
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
        'Staff'), -- Mật khẩu giả định: 'staff123';
       -- Nhân viên Trần Văn Hùng
       ('TK003', 'NV003', 'hungtran', 'j+NE/7ITxbv5hcu41ShyHze7rEOu+3rJVS/pCcDVyKs1xKlDgyPNxFmQhcC0iVZ3', 'Staff'),
       -- Nhân viên Lê Thị Mai
       ('TK004', 'NV004', 'maile', 'j+NE/7ITxbv5hcu41ShyHze7rEOu+3rJVS/pCcDVyKs1xKlDgyPNxFmQhcC0iVZ3', 'Staff'),
       -- Nhân viên Ngô Văn Long
       ('TK005', 'NV005', 'longngo', 'j+NE/7ITxbv5hcu41ShyHze7rEOu+3rJVS/pCcDVyKs1xKlDgyPNxFmQhcC0iVZ3', 'Staff');
INSERT INTO KhachHang (maKH, hoTen, soDT, email, diaChi, ngaySinh, ghiChu, CCCD, hangKhach, trangThai)
VALUES ('KH001', N'Nguyễn Thanh Tùng', '0912345678', 'tung.nguyen@company.com', N'Hà Nội', '1995-02-15',
        N'Khách doanh nghiệp, cần hóa đơn VAT', '012345678901', N'Khách doanh nghiệp', 'DANG_LUU_TRU'),
       ('KH002', N'Trần Thị Mai Hương', '0987654321', 'huong.tran@email.com', N'TP.HCM', '1998-06-22',
        N'Khách du lịch nước ngoài (đặt phòng VIP)', '012345678902', N'Khách VIP', 'DA_DAT'),
       ('KH003', N'Lê Văn Chiến', '0934567890', 'chien.le@web.com', N'Đà Nẵng', '2000-09-10', N'Khách ở lần đầu',
        '012345678903', N'Khách thường', 'CHECK_OUT'),
       ('KH004', N'Phạm Thị Yến', '0945678901', 'yen.pham@private.com', N'Hải Phòng', '1992-03-20',
        N'Khách quen, thích phòng tầng cao', '012345678904', N'Khách quen', 'DANG_LUU_TRU'),
       ('KH005', N'Hoàng Anh Dũng', '0956789012', 'dung.hoang@contact.com', N'Quảng Ninh', '1989-11-05',
        N'Thanh toán bằng thẻ tín dụng', '012345678905', N'Khách VIP', 'DA_DAT'),
       ('KH006', N'Ngô Minh Quân', '0962345678', 'quan.ngo@email.com', N'Cần Thơ', '1996-08-14',
        N'Khách đặt qua ứng dụng', '012345678906', N'Khách thường', 'DANG_LUU_TRU'),
       ('KH007', N'Đặng Thảo Nhi', '0973456789', 'nhi.dang@company.com', N'Bình Dương', '1999-01-09',
        N'Khách trẻ, hay đi công tác', '012345678907', N'Khách doanh nghiệp', 'CHECK_OUT'),
       ('KH008', N'Tạ Đức Long', '0984567890', 'long.ta@hotel.com', N'Huế', '1993-12-30', N'Khách VIP lâu năm',
        '012345678908', N'Khách VIP', 'DANG_LUU_TRU'),
       ('KH009', N'Vũ Quỳnh Anh', '0995678901', 'anh.vu@personal.com', N'Nha Trang', '1997-07-21',
        N'Khách nữ thân thiết, đặt phòng view biển', '012345678909', N'Khách quen', 'DA_DAT'),
       ('KH010', N'Trịnh Quốc Hưng', '0906789012', 'hung.trinh@email.com', N'Hà Nội', '1991-10-01',
        N'Khách lâu năm, yêu cầu dịch vụ cao cấp', '012345678910', N'Khách VIP', 'CHECK_OUT');
GO

-------------------------------------------------------------------
-- 6. KhuyenMai (Mã khuyến mãi)
-------------------------------------------------------------------
PRINT N'-- 6. Đang thêm dữ liệu bảng KhuyenMai...';
INSERT INTO KhuyenMai (maKM, code, giamGia, kieuGiamGia, ngayBatDau, ngayKetThuc, tongTienToiThieu, moTa, trangThai, maNV)
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
     N'Đặc quyền VIP: Giảm 15% trên mọi hóa đơn', N'Đang diễn ra', 'NV001'),

    -- KM0004: Khuyến mãi Tháng 11 (Active)
    -- Giảm 200k tiền mặt (AMOUNT) - Chạy trong tháng 11 (Hiện tại là 22/11 -> OK)
    ('KM0004', 'NOV200', 200000, 'AMOUNT', '2025-11-01', '2025-11-30', 2000000,
     N'Chào tháng 11: Giảm 200k khi đặt phòng sớm', N'Đang diễn ra', 'NV004'),

    -- KM0005: Giáng Sinh/Năm mới (Upcoming)
    -- Giảm 30% (PERCENT) - Ngày bắt đầu là 20/12 (Tương lai) nên trạng thái là Chưa diễn ra
    ('KM0005', 'XMAS30', 30, 'PERCENT', '2025-12-20', '2025-12-25', NULL,
     N'Giáng sinh an lành: Giảm 30% cho các cặp đôi', N'Chưa diễn ra', 'NV002');
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
-- Đặt trực tiếp: tiền cọc = 0
('PD001', '2025-10-10', '2025-10-10', '2025-10-12', N'Hoàn thành', N'Đặt trực tiếp', 'KH001', 'NV002', 0.00),
('PD002', '2025-10-14', '2025-10-14', '2025-10-15', N'Hoàn thành', N'Đặt trực tiếp', 'KH002', 'NV004', 0.00),
('PD003', '2025-10-16', '2025-10-16', '2025-10-17', N'Hoàn thành', N'Đặt trực tiếp', 'KH003', 'NV002', 0.00),
-- Đặt trước: tiền cọc = 20% tổng chi phí phòng
('PD004', '2025-10-20', '2025-10-22', '2025-10-25', N'Hoàn thành', N'Đặt trước', 'KH004', 'NV003', 300000.00),
('PD005', '2025-10-21', '2025-10-24', '2025-10-26', N'Hoàn thành', N'Đặt trước', 'KH005', 'NV004', 200000.00),
-- Đã đặt (Đặt trực tiếp): tiền cọc = 0
('PD006', '2025-12-03', '2025-12-03', '2025-12-09', N'Đang sử dụng', N'Đặt trực tiếp', 'KH003', 'NV001', 0.00),
-- Đã đặt (Đặt trước): tiền cọc = 20% tổng chi phí phòng
('PD007', '2025-11-25', '2025-12-06', '2025-12-10', N'Đang sử dụng', N'Đặt trước', 'KH004', 'NV003', 510000.00);
GO

-------------------------------------------------------------------
-- 9. HoaDon (Hóa đơn)
-------------------------------------------------------------------
PRINT N'-- 9. Đang thêm dữ liệu bảng HoaDon (Sửa Tổng Tiền)...';
-- XÓA DỮ LIỆU CŨ CỦA HoaDon (Nếu bạn chạy script nhiều lần)
DELETE FROM HoaDon WHERE maHD IN ('HD001', 'HD002', 'HD003', 'HD004', 'HD005', 'HD006', 'HD007');

INSERT INTO HoaDon
(
    maHD, ngayLap, phuongThuc, trangThai, tongTien,
    PhatNhanPhongTre, PhatTraPhongSom, PhatTraPhongTre,
    GiamGiaMaGG, GiamGiaHangKH,
    TongVAT, ngayDi, maKH, maNV, maKM
)
VALUES
-- HD001: Áp dụng mã khuyến mãi KM0001 giảm 10% (307k), không phạt
('HD001', '2025-10-12', N'Chuyển khoản', N'Đã thanh toán', 2763000.00,
 0.00, 0.00, 0.00,
 307000.00, 0.00,
 276300.00, '2025-10-12', 'KH001', 'NV002', 'KM0001'),

-- HD002: Khách VIP - Giảm giá hạng khách 15% (447k), không phạt
('HD002', '2025-10-15', N'Tiền mặt', N'Đã thanh toán', 2980000.00,
 0.00, 0.00, 0.00,
 0.00, 447000.00,
 298000.00, '2025-10-15', 'KH002', 'NV004', NULL),

-- HD003: Áp dụng mã khuyến mãi KM0002 giảm 500k, không phạt
('HD003', '2025-10-17', N'Ví điện tử', N'Đã thanh toán', 710000.00,
 0.00, 0.00, 0.00,
 500000.00, 0.00,
 71000.00, '2025-10-17', 'KH003', 'NV002', 'KM0002'),

-- HD004: Khách quen - Giảm giá hạng khách 5% (75k), không phạt
('HD004', '2025-10-23', N'Tiền mặt', N'Đã thanh toán', 1575000.00,
 0.00, 0.00, 0.00,
 0.00, 75000.00,
 75000.00, '2025-10-25', 'KH004', 'NV003', NULL),

-- HD005: Khách VIP - Giảm giá hạng khách 15% (172.5k), không phạt
('HD005', '2025-10-24', N'Chuyển khoản', N'Đã thanh toán', 1150000.00,
 0.00, 0.00, 0.00,
 0.00, 172500.00,
 115000.00, '2025-10-26', 'KH005', 'NV004', NULL),

-- HD006: Chưa thanh toán, các giá trị tiền bằng 0
('HD006', '2025-12-03', NULL, N'Chưa thanh toán', 0.00,
 0.00, 0.00, 0.00,
 0.00, 0.00,
 0.00, NULL, 'KH003', 'NV001', NULL),

-- HD007: Chưa thanh toán, các giá trị tiền bằng 0
('HD007', '2025-12-06', NULL, N'Chưa thanh toán', 0.00,
 0.00, 0.00, 0.00,
 0.00, 0.00,
 0.00, NULL, 'KH004', 'NV002', NULL);
GO
-------------------------------------------------------------------
-- 10. CTHoaDonPhong (Chi tiết hóa đơn phòng)
-------------------------------------------------------------------
-- LƯU Ý: Nếu cấu trúc CTHoaDonPhong của bạn thiếu cột thanhTien, hãy bổ sung nó.
-- (Tôi giả định cột thanhTien đã được bổ sung trong bảng CTHoaDonPhong)
PRINT N'-- 10. Đang thêm dữ liệu bảng CTHoaDonPhong (Đã sửa ThanhTien)...';
-- XÓA DỮ LIỆU CŨ CỦA CTHoaDonPhong (Nếu bạn chạy script nhiều lần)
DELETE FROM CTHoaDonPhong WHERE maHD IN ('HD001', 'HD002', 'HD003', 'HD004', 'HD005', 'HD006', 'HD007');

INSERT INTO CTHoaDonPhong (maHD, maPhieu, maPhong, ngayDen, ngayDi, giaPhong)
VALUES
-- HD001/PD001: P301 (1.3M/ngày) x 2 ngày
('HD001', 'PD001', 'P301', '2025-10-10', '2025-10-12', 1300000),
-- HD002/PD002: P401 (2.2M/ngày) x 1 ngày
('HD002', 'PD002', 'P401', '2025-10-14', '2025-10-15', 2200000),
-- HD003/PD003: P105 (850k/ngày) x 1 ngày
('HD003', 'PD003', 'P105', '2025-10-16', '2025-10-17', 850000),
-- HD004/PD004: P101 (500k/ngày) x 3 ngày
('HD004', 'PD004', 'P101', '2025-10-22', '2025-10-25', 500000),
-- HD005/PD005: P102 (500k/ngày) x 2 ngày
('HD005', 'PD005', 'P102', '2025-10-24', '2025-10-26', 500000),
-- HD006 (Đang sử dụng) - Tiền tạm tính: 850k x 2 ngày
('HD006', 'PD006', 'P204', '2025-12-03','2025-12-09' , 850000),
-- HD007 (Đang sử dụng) - Tiền tạm tính: 850k x 1 ngày
('HD007', 'PD007', 'P205', '2025-12-06', '2025-12-10', 850000);
GO

-------------------------------------------------------------------
-- 11. PhieuDichVu (Phiếu dịch vụ)
-------------------------------------------------------------------
PRINT N'-- 11. Đang thêm dữ liệu bảng PhieuDichVu...';
INSERT INTO PhieuDichVu (maPhieuDV, maHD, ngayLap, maNV, ghiChu)
VALUES ('PDV001', 'HD001', '2025-10-11', 'NV003', N'Giặt ủi và dọn phòng cho khách phòng P301'),
       ('PDV002', 'HD002', '2025-10-15', 'NV004', N'Đưa đón sân bay và phục vụ ăn sáng phòng P401'),
       ('PDV003', 'HD003', '2025-10-16', 'NV002', N'Phục vụ ăn tối cho khách phòng P105');
GO

-------------------------------------------------------------------
-- 12. CTHoaDonDichVu (Chi tiết hóa đơn dịch vụ)
-------------------------------------------------------------------
PRINT N'-- 12. Đang thêm dữ liệu bảng CTHoaDonDichVu (Sửa đơn giá)...';
-- XÓA DỮ LIỆU CŨ CỦA CTHoaDonDichVu (Nếu bạn chạy script nhiều lần)
DELETE FROM CTHoaDonDichVu WHERE maHD IN ('HD001', 'HD002', 'HD003');

INSERT INTO CTHoaDonDichVu (maHD, maPhieuDV, maDV, soLuong, donGia)
VALUES
-- HD001 (DV001: Giặt ủi 60k; DV002: Đưa đón 350k) -> Tổng 2*(60k) + 350k = 470k
('HD001', 'PDV001', 'DV001', 2, 60000),
('HD001', 'PDV001', 'DV002', 1, 350000),

-- HD002 (DV003: Massage 600k; DV004: Ăn sáng 180k) -> Tổng 600k + 180k = 780k
('HD002', 'PDV002', 'DV003', 1, 600000),
('HD002', 'PDV002', 'DV004', 1, 180000),

-- HD003 (DV001: Giặt ủi 60k; DV004: Ăn sáng 180k) -> Tổng 3*(60k) + 180k = 360k
('HD003', 'PDV003', 'DV001', 3, 60000),
('HD003', 'PDV003', 'DV004', 1, 180000);
GO