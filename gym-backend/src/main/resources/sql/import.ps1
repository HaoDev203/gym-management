# ========================================
# 健身房管理系统 - 数据库一键导入 (PowerShell)
# ========================================
# 如果 MySQL 配置不同，只需修改下面 4 个变量
$mysqlHost   = "127.0.0.1"
$mysqlPort   = "3306"
$mysqlUser   = "root"
$mysqlPass   = "123"
$dbName      = "gym_management"
# ========================================

$ErrorActionPreference = "Stop"

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  健身房管理系统 - 数据库导入" -ForegroundColor Yellow
Write-Host "========================================`n" -ForegroundColor Cyan

Write-Host "数据库配置：" -ForegroundColor White
Write-Host "  Host: $mysqlHost" -ForegroundColor Green
Write-Host "  Port: $mysqlPort" -ForegroundColor Green
Write-Host "  User: $mysqlUser" -ForegroundColor Green
Write-Host "  Database: $dbName" -ForegroundColor Green
Write-Host "`n========================================`n" -ForegroundColor Cyan

try { $null = Get-Command mysql -ErrorAction Stop }
catch {
    Write-Host "[X] 未找到 MySQL 命令行工具" -ForegroundColor Red
    Write-Host "请将 MySQL bin 目录加入 PATH 环境变量" -ForegroundColor Yellow
    pause; exit 1
}
Write-Host "[OK] MySQL 已就绪" -ForegroundColor Green

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$schemaSql = Join-Path $scriptDir "schema.sql"
$dataSql   = Join-Path $scriptDir "data.sql"

if (-not (Test-Path $schemaSql)) { Write-Host "[X] 缺少 schema.sql" -ForegroundColor Red; pause; exit 1 }
if (-not (Test-Path $dataSql))   { Write-Host "[X] 缺少 data.sql"   -ForegroundColor Red; pause; exit 1 }

$env:MYSQL_PWD = $mysqlPass

Write-Host "`n[1/3] 创建数据库..." -ForegroundColor Cyan
mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser -e "CREATE DATABASE IF NOT EXISTS $dbName DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
if ($LASTEXITCODE -ne 0) { Write-Host "[X] 创建数据库失败" -ForegroundColor Red; pause; exit 1 }
Write-Host "  [OK] 数据库 $dbName 已就绪" -ForegroundColor Green

Write-Host "`n[2/3] 导入表结构（schema.sql）..." -ForegroundColor Cyan
Get-Content $schemaSql | mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser $dbName
if ($LASTEXITCODE -ne 0) { Write-Host "[X] 表结构导入失败" -ForegroundColor Red; pause; exit 1 }
Write-Host "  [OK] 表结构创建完成" -ForegroundColor Green

Write-Host "`n[3/3] 导入测试数据（data.sql）..." -ForegroundColor Cyan
Get-Content $dataSql | mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser $dbName
if ($LASTEXITCODE -ne 0) { Write-Host "[X] 测试数据导入失败" -ForegroundColor Red; pause; exit 1 }
Write-Host "  [OK] 测试数据导入完成" -ForegroundColor Green

Remove-Item Env:\MYSQL_PWD -ErrorAction SilentlyContinue

Write-Host "`n========================================" -ForegroundColor Green
Write-Host "  导入成功！" -ForegroundColor Yellow
Write-Host "========================================`n" -ForegroundColor Green
Write-Host "测试账户：" -ForegroundColor Cyan
Write-Host "  管理员  admin      / admin123" -ForegroundColor White
Write-Host "  会员    13800138001 / 123456" -ForegroundColor White
Write-Host "`n其他会员账号均为手机号，密码 123456`n"

$verifySql = "SELECT 'admin' tbl, COUNT(*) cnt FROM $dbName.admin UNION ALL SELECT 'member', COUNT(*) FROM $dbName.member UNION ALL SELECT 'coach', COUNT(*) FROM $dbName.coach UNION ALL SELECT 'course', COUNT(*) FROM $dbName.course;"
mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser $dbName -e $verifySql

pause
