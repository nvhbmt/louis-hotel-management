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
('P101', 1, N'Trống', N'Phòng đơn tầng 1, cửa sổ hướng sân', 'LP001'),
('P102', 1, N'Trống', N'Phòng đơn tầng 1, cửa sổ hướng đường', 'LP001'),
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

-- Thêm một số phòng đang được sử dụng để test
UPDATE Phong SET trangThai = N'Đang sử dụng' WHERE maPhong IN ('P101', 'P204');
UPDATE Phong SET trangThai = N'Đã đặt' WHERE maPhong IN ('P102', 'P301');
UPDATE Phong SET trangThai = N'Bảo trì' WHERE maPhong = 'P103';