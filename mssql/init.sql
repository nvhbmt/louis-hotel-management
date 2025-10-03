IF NOT EXISTS (
    SELECT name
    FROM sys.databases
    WHERE name = N'QuanLyKhachSan'
)
BEGIN
    CREATE DATABASE QuanLyKhachSan;
END
