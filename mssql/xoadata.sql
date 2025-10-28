-- 1. Chuyển về database 'master' để không bị khóa chính database cần xóa
USE master;
GO

-- 2. Đặt database về chế độ người dùng đơn và đóng mọi kết nối ngay lập tức
ALTER DATABASE QuanLyKhachSan
    SET SINGLE_USER
    WITH ROLLBACK IMMEDIATE;
GO

-- 3. Xóa database
-- 1. Chuyển vào database của bạn
USE QuanLyKhachSan;
GO

-- 2. XÓA user 'lynn' ra khỏi database này
-- (Nếu lỗi ở đây, không sao, cứ chạy tiếp lệnh 3)
DROP USER lynn;
GO

-- 3. TẠO LẠI user 'lynn' và liên kết nó với login 'lynn'
CREATE USER lynn FOR LOGIN lynn;
GO

-- 4. Thêm user 'lynn' vào vai trò 'db_owner' (quan trọng)
ALTER ROLE db_owner ADD MEMBER lynn;
GO

PRINT N'Đã sửa lỗi ánh xạ (mapping) và cấp quyền db_owner cho user ''lynn''.';
GO

-- Đặt schema mặc định cho user 'lynn' là 'dbo'
USE QuanLyKhachSan;
GO

-- Đặt schema mặc định cho user 'lynn' là 'dbo'
ALTER USER lynn WITH DEFAULT_SCHEMA = dbo;
GO