@echo off
chcp 65001 >nul
echo ========================================
echo   健身房管理系统 - 数据库导入脚本
echo ========================================
echo.
echo 数据库配置：
echo   Host: 127.0.0.1
echo   Port: 3306
echo   Database: gym_management
echo   Username: root
echo   Password: 123
echo.
echo ========================================
echo.

REM 检查 mysql 命令是否可用
where mysql >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [错误] 未找到 MySQL 命令行工具
    echo.
    echo 请确保：
    echo   1. MySQL 已安装
    echo   2. MySQL 已添加到系统 PATH 环境变量
    echo   3. 或者使用 MySQL Workbench 手动导入 SQL 文件
    echo.
    pause
    exit /b 1
)

echo [信息] 开始导入数据库...
echo.

REM 执行 SQL 文件
mysql -h 127.0.0.1 -P 3306 -u root -p123 < test-data.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo   数据库导入成功！
    echo ========================================
    echo.
    echo 测试账户：
    echo   管理员：admin / admin123
    echo   会员：member1 / m123456
    echo   教练：coach1 / c123456
    echo.
) else (
    echo.
    echo ========================================
    echo   数据库导入失败！
    echo ========================================
    echo.
    echo 请检查：
    echo   1. MySQL 服务是否启动
    echo   2. 用户名密码是否正确
    echo   3. 是否有足够的权限
    echo.
)

pause
