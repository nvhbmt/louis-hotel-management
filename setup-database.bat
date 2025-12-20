@echo off
echo Setting up database for Louis Hotel Management System
echo ====================================================

echo Step 1: Waiting for SQL Server to be ready...
timeout /t 30 /nobreak > nul

echo Step 2: Creating database and tables...
sqlcmd -S localhost,1433 -U sa -P "SApassword@123" -i "mssql\khoiTaoDataBase.sql"
if %errorlevel% neq 0 (
    echo ERROR: Failed to create database
    pause
    exit /b 1
)

echo Step 3: Importing sample data...
sqlcmd -S localhost,1433 -U sa -P "SApassword@123" -i "mssql\duLieuMau.sql"
if %errorlevel% neq 0 (
    echo ERROR: Failed to import sample data
    pause
    exit /b 1
)

echo Step 4: Creating stored procedures...
sqlcmd -S localhost,1433 -U sa -P "SApassword@123" -i "mssql\storedProceduresKhuyenMai.sql"
if %errorlevel% neq 0 (
    echo ERROR: Failed to create stored procedures
    pause
    exit /b 1
)
if %errorlevel% neq 0 (
    echo ERROR: Failed to create stored procedures
    pause
    exit /b 1
)

echo.
echo ====================================================
echo Database setup completed successfully!
echo You can now run the application.
echo ====================================================
pause
