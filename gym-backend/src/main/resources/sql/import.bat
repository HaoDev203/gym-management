@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo ========================================
echo   健身房管理系统 - 数据库导入
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
echo   重要提示：
echo   使用前请根据实际情况修改脚本中的
echo   MySQL 用户名和密码配置
echo ========================================
echo.

where mysql >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [X] 未找到 MySQL 命令行工具
    echo 请将 MySQL bin 目录加入 PATH 环境变量
    pause
    exit /b 1
)
echo [OK] MySQL 已就绪
echo.

REM 获取脚本所在目录
set "SCRIPT_DIR=%~dp0"

if not exist "%SCRIPT_DIR%schema.sql" (
    echo [X] 缺少 schema.sql
    pause
    exit /b 1
)
if not exist "%SCRIPT_DIR%data.sql" (
    echo [X] 缺少 data.sql
    pause
    exit /b 1
)

echo [1/3] 创建数据库...
mysql -h 127.0.0.1 -P 3306 -u root -p123 -e "CREATE DATABASE IF NOT EXISTS gym_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
if %ERRORLEVEL% NEQ 0 ( echo [X] 创建数据库失败 & pause & exit /b 1 )
echo   [OK] 数据库 gym_management 已就绪
echo.

echo [2/3] 导入表结构（schema.sql）...
mysql -h 127.0.0.1 -P 3306 -u root -p123 gym_management < "%SCRIPT_DIR%schema.sql"
if %ERRORLEVEL% NEQ 0 ( echo [X] 表结构导入失败 & pause & exit /b 1 )
echo   [OK] 表结构创建完成
echo.

echo [3/3] 导入测试数据（data.sql）...
mysql -h 127.0.0.1 -P 3306 -u root -p123 gym_management < "%SCRIPT_DIR%data.sql"
if %ERRORLEVEL% NEQ 0 ( echo [X] 测试数据导入失败 & pause & exit /b 1 )
echo   [OK] 测试数据导入完成
echo.

echo ========================================
echo   导入成功！
echo ========================================
echo.
echo 测试账户：
echo   管理员  admin      / admin123
echo   会员    13800138001 / 123456
echo.
echo 其他会员账号均为手机号，密码 123456
echo.

pause
