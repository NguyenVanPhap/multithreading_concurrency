@echo off
echo ============================================
echo   Week 6 - Synchronizers Demo
echo ============================================
echo.

:menu
echo [1] CountDownLatchDemo
echo [2] CyclicBarrierDemo
echo [3] SemaphoreDemo
echo [4] PhaserDemo
echo [5] ParallelProcessor
echo [6] ResourcePool
echo [7] Run all
echo [0] Exit
echo.

set /p choice="Enter your choice: "

if "%choice%"=="1" mvn compile exec:java -Dexec.mainClass="exercises.CountDownLatchDemo" && goto menu
if "%choice%"=="2" mvn compile exec:java -Dexec.mainClass="exercises.CyclicBarrierDemo" && goto menu
if "%choice%"=="3" mvn compile exec:java -Dexec.mainClass="exercises.SemaphoreDemo" && goto menu
if "%choice%"=="4" mvn compile exec:java -Dexec.mainClass="exercises.PhaserDemo" && goto menu
if "%choice%"=="5" mvn compile exec:java -Dexec.mainClass="projects.ParallelProcessor" && goto menu
if "%choice%"=="6" mvn compile exec:java -Dexec.mainClass="projects.ResourcePool" && goto menu
if "%choice%"=="7" goto all
if "%choice%"=="0" goto end

echo Invalid choice!
goto menu

:all
mvn compile exec:java -Dexec.mainClass="exercises.CountDownLatchDemo"
mvn compile exec:java -Dexec.mainClass="exercises.CyclicBarrierDemo"
mvn compile exec:java -Dexec.mainClass="exercises.SemaphoreDemo"
mvn compile exec:java -Dexec.mainClass="exercises.PhaserDemo"
mvn compile exec:java -Dexec.mainClass="projects.ParallelProcessor"
mvn compile exec:java -Dexec.mainClass="projects.ResourcePool"
goto menu

:end
pause

