@echo off
echo ============================================
echo   Week 10 - Virtual Threads Demo
echo ============================================
echo.

:menu
echo [1] VirtualThreadDemo
echo [2] VirtualThreadExecutorDemo
echo [3] PerformanceDemo
echo [4] HighConcurrencyServer
echo [5] AsyncTaskProcessor
echo [6] Run all
echo [0] Exit
echo.

set /p choice="Enter your choice: "

if "%choice%"=="1" mvn compile exec:java -Dexec.mainClass="exercises.VirtualThreadDemo" && goto menu
if "%choice%"=="2" mvn compile exec:java -Dexec.mainClass="exercises.VirtualThreadExecutorDemo" && goto menu
if "%choice%"=="3" mvn compile exec:java -Dexec.mainClass="exercises.PerformanceDemo" && goto menu
if "%choice%"=="4" mvn compile exec:java -Dexec.mainClass="projects.HighConcurrencyServer" && goto menu
if "%choice%"=="5" mvn compile exec:java -Dexec.mainClass="projects.AsyncTaskProcessor" && goto menu
if "%choice%"=="6" goto all
if "%choice%"=="0" goto end

echo Invalid choice!
goto menu

:all
mvn compile exec:java -Dexec.mainClass="exercises.VirtualThreadDemo"
mvn compile exec:java -Dexec.mainClass="exercises.VirtualThreadExecutorDemo"
mvn compile exec:java -Dexec.mainClass="exercises.PerformanceDemo"
mvn compile exec:java -Dexec.mainClass="projects.HighConcurrencyServer"
mvn compile exec:java -Dexec.mainClass="projects.AsyncTaskProcessor"
goto menu

:end
pause

