-- Script tạo tài khoản mẫu cho Manager và Staff
-- Chạy script này sau khi đã tạo bảng TaiKhoan

-- Tạo tài khoản Manager  
INSERT INTO TaiKhoan (maTK, maNV, tenDangNhap, matKhauHash, quyen, trangThai)
VALUES ('TK002', 'NV002', 'manager', '4XKzp/qajqT93GnwSpzI3k00cLhgU/fEFLw/RW1tsqjv6NREO9+L+8ckMRGLxrxY', 'Manager', 1);

-- Tạo tài khoản Staff
INSERT INTO TaiKhoan (maTK, maNV, tenDangNhap, matKhauHash, quyen, trangThai)
VALUES ('TK003', 'NV003', 'staff', 'j+NE/7ITxbv5hcu41ShyHze7rEOu+3rJVS/pCcDVyKs1xKlDgyPNxFmQhcC0iVZ3', 'Staff', 1);

-- Lưu ý: 
-- 1. Mật khẩu đã được hash bằng PasswordUtil.hashPassword()
-- 2. Đảm bảo maNV tương ứng với mã nhân viên có trong bảng NhanVien
-- 3. Mật khẩu mặc định: manager123, staff123
-- 4. Manager có quyền quản lý tài khoản và xem thống kê
-- 5. Staff chỉ có quyền truy cập các chức năng cơ bản
-- 6. trangThai: 1 = Hoạt động, 0 = Khóa
