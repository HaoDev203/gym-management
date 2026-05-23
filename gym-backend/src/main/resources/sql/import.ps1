# ========================================
# 健身房管理系统 - 数据库导入脚本 (PowerShell)
# ========================================
# 数据库配置：
#   Host: 127.0.0.1
#   Port: 3306
#   Database: gym_management
#   Username: root
#   Password: 123
# ========================================

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  健身房管理系统 - 数据库导入脚本" -ForegroundColor Yellow
Write-Host "========================================`n" -ForegroundColor Cyan

Write-Host "数据库配置：" -ForegroundColor White
Write-Host "  Host: 127.0.0.1" -ForegroundColor Green
Write-Host "  Port: 3306" -ForegroundColor Green
Write-Host "  Database: gym_management" -ForegroundColor Green
Write-Host "  Username: root" -ForegroundColor Green
Write-Host "  Password: 123" -ForegroundColor Green
Write-Host "`n========================================`n" -ForegroundColor Cyan

# 检查 MySQL 命令是否可用
try {
    $mysqlPath = Get-Command mysql -ErrorAction Stop
    Write-Host "[信息] 找到 MySQL 命令行工具：$($mysqlPath.Source)`n" -ForegroundColor Green
} catch {
    Write-Host "[错误] 未找到 MySQL 命令行工具" -ForegroundColor Red
    Write-Host "`n请确保：" -ForegroundColor Yellow
    Write-Host "  1. MySQL 已安装" -ForegroundColor White
    Write-Host "  2. MySQL 已添加到系统 PATH 环境变量" -ForegroundColor White
    Write-Host "  3. 或者使用 MySQL Workbench 手动导入 SQL 文件`n" -ForegroundColor White
    pause
    exit 1
}

Write-Host "[信息] 开始导入数据库..." -ForegroundColor Cyan
Write-Host ""

# 获取脚本所在目录
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$sqlFile = Join-Path $scriptDir "test-data.sql"

# 检查 SQL 文件是否存在
if (-not (Test-Path $sqlFile)) {
    Write-Host "[错误] 找不到 SQL 文件：$sqlFile" -ForegroundColor Red
    pause
    exit 1
}

Write-Host "[信息] SQL 文件：$sqlFile`n" -ForegroundColor Green

# 执行 SQL 文件
$env:MYSQL_PWD = "123"
mysql -h 127.0.0.1 -P 3306 -u root < $sqlFile

$exitCode = $LASTEXITCODE

# 清除环境变量
Remove-Item Env:\MYSQL_PWD -ErrorAction SilentlyContinue

if ($exitCode -eq 0) {
    Write-Host "`n========================================" -ForegroundColor Green
    Write-Host "  数据库导入成功！" -ForegroundColor Yellow
    Write-Host "========================================`n" -ForegroundColor Green
    
    Write-Host "测试账户：" -ForegroundColor Cyan
    Write-Host "  管理员：admin / admin123" -ForegroundColor White
    Write-Host "  会员：member1 / m123456" -ForegroundColor White
    Write-Host "  教练：coach1 / c123456`n" -ForegroundColor White
} else {
    Write-Host "`n========================================" -ForegroundColor Red
    Write-Host "  数据库导入失败！" -ForegroundColor Yellow
    Write-Host "========================================`n" -ForegroundColor Red
    
    Write-Host "请检查：" -ForegroundColor Cyan
    Write-Host "  1. MySQL 服务是否启动" -ForegroundColor White
    Write-Host "  2. 用户名密码是否正确" -ForegroundColor White
    Write-Host "  3. 是否有足够的权限`n" -ForegroundColor White
}

pause
