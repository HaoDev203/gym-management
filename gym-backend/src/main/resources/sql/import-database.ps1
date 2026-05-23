# ========================================
# 健身房管理系统 - 数据库一键导入脚本
# ========================================
# 数据库配置：
#   HOST: 127.0.0.1
#   PORT: 3306
#   DB_USERNAME: root
#   DB_PASSWORD: 123
# ========================================

$ErrorActionPreference = "Stop"

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  健身房管理系统 - 数据库导入" -ForegroundColor Yellow
Write-Host "========================================`n" -ForegroundColor Cyan

# 数据库配置
$mysqlHost = "127.0.0.1"
$mysqlPort = "3306"
$mysqlUser = "root"
$mysqlPassword = "123"
$databaseName = "gym_management"

# 获取脚本所在目录
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$initSql = Join-Path $scriptDir "init.sql"
$testDataSql = Join-Path $scriptDir "test-data.sql"

Write-Host "数据库配置：" -ForegroundColor White
Write-Host "  Host: $mysqlHost" -ForegroundColor Green
Write-Host "  Port: $mysqlPort" -ForegroundColor Green
Write-Host "  Database: $databaseName" -ForegroundColor Green
Write-Host "  Username: $mysqlUser" -ForegroundColor Green
Write-Host "`n========================================`n" -ForegroundColor Cyan

# 检查 MySQL 命令
try {
    $mysqlPath = Get-Command mysql -ErrorAction Stop
    Write-Host "[✓] MySQL 命令行工具已找到" -ForegroundColor Green
} catch {
    Write-Host "[✗] 未找到 MySQL 命令行工具" -ForegroundColor Red
    Write-Host "`n请确保 MySQL 已安装并添加到 PATH 环境变量" -ForegroundColor Yellow
    pause
    exit 1
}

# 检查 SQL 文件
if (-not (Test-Path $initSql)) {
    Write-Host "[✗] 找不到初始化 SQL 文件：$initSql" -ForegroundColor Red
    pause
    exit 1
}

if (-not (Test-Path $testDataSql)) {
    Write-Host "[] 找不到测试数据 SQL 文件：$testDataSql" -ForegroundColor Red
    pause
    exit 1
}

Write-Host "[✓] SQL 文件检查通过" -ForegroundColor Green
Write-Host ""

# 步骤 1: 删除旧数据库
Write-Host "[步骤 1/4] 删除旧数据库..." -ForegroundColor Cyan
$env:MYSQL_PWD = $mysqlPassword
mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser -e "DROP DATABASE IF EXISTS $databaseName;"
if ($LASTEXITCODE -eq 0) {
    Write-Host "  [✓] 数据库删除成功（或不存在）" -ForegroundColor Green
} else {
    Write-Host "  [] 数据库删除失败（可忽略）" -ForegroundColor Yellow
}

# 步骤 2: 创建新数据库
Write-Host "`n[步骤 2/4] 创建新数据库..." -ForegroundColor Cyan
mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser -e "CREATE DATABASE IF NOT EXISTS $databaseName DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
if ($LASTEXITCODE -eq 0) {
    Write-Host "  [✓] 数据库创建成功" -ForegroundColor Green
} else {
    Write-Host "  [✗] 数据库创建失败" -ForegroundColor Red
    pause
    exit 1
}

# 步骤 3: 执行初始化 SQL（创建表结构）
Write-Host "`n[步骤 3/4] 创建表结构..." -ForegroundColor Cyan
Write-Host "  文件：$initSql" -ForegroundColor Gray
Get-Content $initSql | mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser $databaseName
if ($LASTEXITCODE -eq 0) {
    Write-Host "  [✓] 表结构创建成功" -ForegroundColor Green
} else {
    Write-Host "  [✗] 表结构创建失败" -ForegroundColor Red
    pause
    exit 1
}

# 步骤 4: 导入测试数据
Write-Host "`n[步骤 4/4] 导入测试数据..." -ForegroundColor Cyan
Write-Host "  文件：$testDataSql" -ForegroundColor Gray
Get-Content $testDataSql | mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser $databaseName
if ($LASTEXITCODE -eq 0) {
    Write-Host "  [✓] 测试数据导入成功" -ForegroundColor Green
} else {
    Write-Host "  [✗] 测试数据导入失败" -ForegroundColor Red
    pause
    exit 1
}

# 清除环境变量
Remove-Item Env:\MYSQL_PWD -ErrorAction SilentlyContinue

# 验证导入
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  数据验证" -ForegroundColor Yellow
Write-Host "========================================`n" -ForegroundColor Cyan

$verifySql = @"
SELECT 
    'admin' as table_name, COUNT(*) as count FROM $databaseName.admin
UNION ALL SELECT 'member', COUNT(*) FROM $databaseName.member
UNION ALL SELECT 'coach', COUNT(*) FROM $databaseName.coach
UNION ALL SELECT 'course', COUNT(*) FROM $databaseName.course
UNION ALL SELECT 'equipment', COUNT(*) FROM $databaseName.equipment
UNION ALL SELECT 'orders', COUNT(*) FROM $databaseName.orders
UNION ALL SELECT 'notification', COUNT(*) FROM $databaseName.notification
UNION ALL SELECT 'waitlist', COUNT(*) FROM $databaseName.waitlist
UNION ALL SELECT 'attendance', COUNT(*) FROM $databaseName.attendance;
"@

Write-Host "数据量统计:" -ForegroundColor White
$verifySql | mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser

Write-Host "`n========================================" -ForegroundColor Green
Write-Host "  数据库导入完成！" -ForegroundColor Yellow
Write-Host "========================================`n" -ForegroundColor Green

Write-Host "测试账户：" -ForegroundColor Cyan
Write-Host "  管理员：admin / admin123" -ForegroundColor White
Write-Host "  会员：member1 / m123456" -ForegroundColor White
Write-Host "  教练：coach1 / c123456`n" -ForegroundColor White

Write-Host "提示：可以启动 Spring Boot 应用进行测试了！" -ForegroundColor Green
Write-Host ""

pause
