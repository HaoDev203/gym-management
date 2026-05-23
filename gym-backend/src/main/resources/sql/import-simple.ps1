# Gym Management System - Database Import Script
# Database Configuration:
#   HOST: 127.0.0.1
#   PORT: 3306
#   USERNAME: root
#   PASSWORD: 123

$ErrorActionPreference = "Stop"

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Gym Management System - DB Import" -ForegroundColor Yellow
Write-Host "========================================`n" -ForegroundColor Cyan

# Database config
$mysqlHost = "127.0.0.1"
$mysqlPort = "3306"
$mysqlUser = "root"
$mysqlPassword = "123"
$databaseName = "gym_management"

# Get script directory
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$initSql = Join-Path $scriptDir "init.sql"
$testDataSql = Join-Path $scriptDir "test-data.sql"

Write-Host "Database:" -ForegroundColor White
Write-Host "  Host: $mysqlHost" -ForegroundColor Green
Write-Host "  Port: $mysqlPort" -ForegroundColor Green
Write-Host "  Database: $databaseName" -ForegroundColor Green
Write-Host "  Username: $mysqlUser`n" -ForegroundColor Green

# Check MySQL
try {
    $mysqlPath = Get-Command mysql -ErrorAction Stop
    Write-Host "[OK] MySQL found" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] MySQL not found" -ForegroundColor Red
    pause
    exit 1
}

# Check SQL files
if (-not (Test-Path $initSql)) {
    Write-Host "[ERROR] init.sql not found" -ForegroundColor Red
    pause
    exit 1
}

if (-not (Test-Path $testDataSql)) {
    Write-Host "[ERROR] test-data.sql not found" -ForegroundColor Red
    pause
    exit 1
}

Write-Host "[OK] SQL files found`n" -ForegroundColor Green

# Step 1: Drop old database
Write-Host "[Step 1/4] Dropping old database..." -ForegroundColor Cyan
$env:MYSQL_PWD = $mysqlPassword
mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser -e "DROP DATABASE IF EXISTS $databaseName;"
if ($LASTEXITCODE -eq 0) {
    Write-Host "  [OK] Done" -ForegroundColor Green
}

# Step 2: Create new database
Write-Host "`n[Step 2/4] Creating database..." -ForegroundColor Cyan
mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser -e "CREATE DATABASE IF NOT EXISTS $databaseName DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
if ($LASTEXITCODE -eq 0) {
    Write-Host "  [OK] Created" -ForegroundColor Green
} else {
    Write-Host "  [ERROR] Failed" -ForegroundColor Red
    pause
    exit 1
}

# Step 3: Create tables
Write-Host "`n[Step 3/4] Creating tables..." -ForegroundColor Cyan
Get-Content $initSql | mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser $databaseName
if ($LASTEXITCODE -eq 0) {
    Write-Host "  [OK] Tables created" -ForegroundColor Green
} else {
    Write-Host "  [ERROR] Failed" -ForegroundColor Red
    pause
    exit 1
}

# Step 4: Import test data
Write-Host "`n[Step 4/4] Importing test data..." -ForegroundColor Cyan
Get-Content $testDataSql | mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser $databaseName
if ($LASTEXITCODE -eq 0) {
    Write-Host "  [OK] Data imported" -ForegroundColor Green
} else {
    Write-Host "  [ERROR] Failed" -ForegroundColor Red
    pause
    exit 1
}

# Clear env
Remove-Item Env:\MYSQL_PWD -ErrorAction SilentlyContinue

# Verify
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Verification" -ForegroundColor Yellow
Write-Host "========================================`n" -ForegroundColor Cyan

$verifySql = "SELECT 'admin' as tbl, COUNT(*) as cnt FROM $databaseName.admin UNION ALL SELECT 'member', COUNT(*) FROM $databaseName.member UNION ALL SELECT 'coach', COUNT(*) FROM $databaseName.coach UNION ALL SELECT 'course', COUNT(*) FROM $databaseName.course UNION ALL SELECT 'equipment', COUNT(*) FROM $databaseName.equipment UNION ALL SELECT 'orders', COUNT(*) FROM $databaseName.orders;"

$verifySql | mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser

Write-Host "`n========================================" -ForegroundColor Green
Write-Host "  Import completed successfully!" -ForegroundColor Yellow
Write-Host "========================================`n" -ForegroundColor Green

Write-Host "Test accounts:" -ForegroundColor Cyan
Write-Host "  Admin: admin / admin123" -ForegroundColor White
Write-Host "  Member: member1 / m123456" -ForegroundColor White
Write-Host "  Coach: coach1 / c123456`n" -ForegroundColor White

pause
