@echo off
echo ========================================
echo    MULTITHREADING DEMO RUNNER
echo ========================================
echo.

cd src\main\java\projects

echo Compiling Prime Calculator Demo...
javac PrimeCalculatorDemo.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compiling Data Processor Demo...
javac DataProcessorDemo.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo    COMPILATION SUCCESSFUL!
echo ========================================
echo.
echo Choose which demo to run:
echo 1. Prime Calculator Demo
echo 2. Data Processor Demo
echo 3. Exit
echo.
set /p choice="Enter your choice (1-3): "

if "%choice%"=="1" (
    echo.
    echo Running Prime Calculator Demo...
    echo.
    java PrimeCalculatorDemo
) else if "%choice%"=="2" (
    echo.
    echo Running Data Processor Demo...
    echo.
    java DataProcessorDemo
) else if "%choice%"=="3" (
    echo Goodbye!
    exit /b 0
) else (
    echo Invalid choice!
)

echo.
pause
